import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SnippetManager {
    private Map<String, Snippet> snippets;
    private Map<String, List<Snippet>> tagMap;
    private Stack<Operation> undoStack;
    private Stack<Operation> redoStack;
    private Trie keywordTrie;
    private static final String SAVE_FILE = "snippets.dat";
    private static final String BACKUP_FILE = "snippets_backup.dat";
    private static final int MAX_SNIPPETS = 10000;
    private static final Set<String> SUPPORTED_LANGUAGES = new HashSet<>(Arrays.asList(
        "java", "python", "javascript", "cpp", "c", "csharp", "ruby", "php", "swift", "kotlin"
    ));

    public SnippetManager() {
        snippets = new HashMap<>();
        tagMap = new HashMap<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        keywordTrie = new Trie();
        loadSnippets();
    }

    public void addSnippet(String title, String language, String code, List<String> tags) {
        // Validate input
        if (snippets.size() >= MAX_SNIPPETS) {
            throw new IllegalStateException("Maximum number of snippets reached");
        }

        if (snippets.containsKey(title)) {
            throw new IllegalArgumentException("A snippet with this title already exists");
        }

        // Validate language
        if (!SUPPORTED_LANGUAGES.contains(language.toLowerCase())) {
            throw new IllegalArgumentException("Unsupported programming language: " + language);
        }

        // Check for duplicate code
        for (Snippet existing : snippets.values()) {
            if (existing.getCode().equals(code)) {
                throw new IllegalArgumentException("A snippet with this code already exists");
            }
        }

        Snippet snippet = new Snippet(title, language, code);
        for (String tag : tags) {
            snippet.addTag(tag);
            tagMap.computeIfAbsent(tag, k -> new ArrayList<>()).add(snippet);
        }
        
        snippets.put(title, snippet);
        
        // Add to undo stack
        undoStack.push(new Operation(OperationType.ADD, snippet));
        redoStack.clear(); // Clear redo stack when new operation is performed
        
        // Extract and add keywords to trie
        String[] keywords = extractKeywords(code);
        for (String keyword : keywords) {
            keywordTrie.insert(keyword);
        }
        
        saveSnippets();
    }

    public void addSnippet(String title, String language, String code) {
        addSnippet(title, language, code, new ArrayList<>());
    }

    public void editSnippet(String title, String newLanguage, String newCode, List<String> newTags) {
        Snippet oldSnippet = snippets.get(title);
        if (oldSnippet == null) {
            throw new IllegalArgumentException("Snippet not found");
        }

        // Validate language
        if (!SUPPORTED_LANGUAGES.contains(newLanguage.toLowerCase())) {
            throw new IllegalArgumentException("Unsupported programming language: " + newLanguage);
        }

        // Check if the new code is different
        if (oldSnippet.getCode().equals(newCode) && 
            oldSnippet.getLanguage().equals(newLanguage) && 
            oldSnippet.getTags().equals(new HashSet<>(newTags))) {
            throw new IllegalArgumentException("No changes detected");
        }

        // Check for duplicate code with other snippets
        for (Snippet existing : snippets.values()) {
            if (!existing.getTitle().equals(title) && existing.getCode().equals(newCode)) {
                throw new IllegalArgumentException("A snippet with this code already exists");
            }
        }

        Snippet newSnippet = new Snippet(title, newLanguage, newCode);
        for (String tag : newTags) {
            newSnippet.addTag(tag);
        }
        
        snippets.put(title, newSnippet);
        
        // Update tag map
        updateTagMap(oldSnippet, newSnippet);
        
        // Add to undo stack
        undoStack.push(new Operation(OperationType.EDIT, newSnippet, oldSnippet));
        redoStack.clear();
        
        // Update keywords
        String[] keywords = extractKeywords(newCode);
        for (String keyword : keywords) {
            keywordTrie.insert(keyword);
        }
        
        saveSnippets();
    }

    private void updateTagMap(Snippet oldSnippet, Snippet newSnippet) {
        // Remove old tags
        for (String tag : oldSnippet.getTags()) {
            List<Snippet> taggedSnippets = tagMap.get(tag);
            if (taggedSnippets != null) {
                taggedSnippets.remove(oldSnippet);
                if (taggedSnippets.isEmpty()) {
                    tagMap.remove(tag);
                }
            }
        }
        
        // Add new tags
        for (String tag : newSnippet.getTags()) {
            tagMap.computeIfAbsent(tag, k -> new ArrayList<>()).add(newSnippet);
        }
    }

    public void deleteSnippet(String title) {
        Snippet snippet = snippets.remove(title);
        if (snippet == null) {
            throw new IllegalArgumentException("Snippet not found");
        }

        // Add to undo stack
        undoStack.push(new Operation(OperationType.DELETE, snippet));
        redoStack.clear();
        
        // Remove from tag map
        for (String tag : snippet.getTags()) {
            List<Snippet> taggedSnippets = tagMap.get(tag);
            if (taggedSnippets != null) {
                taggedSnippets.remove(snippet);
                if (taggedSnippets.isEmpty()) {
                    tagMap.remove(tag);
                }
            }
        }
        
        saveSnippets();
    }

    public Snippet getSnippet(String title) {
        return snippets.get(title);
    }

    public List<Snippet> searchByTag(String tag) {
        return tagMap.getOrDefault(tag.toLowerCase(), new ArrayList<>());
    }

    public List<String> searchKeywords(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return keywordTrie.searchPrefix(prefix.toLowerCase());
    }

    public List<Snippet> searchByTitle(String query) {
        final String searchQuery = query.toLowerCase();
        return snippets.values().stream()
                .filter(s -> s.getTitle().toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());
    }

    public List<Snippet> searchByLanguage(String language) {
        final String searchLanguage = language.toLowerCase();
        return snippets.values().stream()
                .filter(s -> s.getLanguage().toLowerCase().equals(searchLanguage))
                .collect(Collectors.toList());
    }

    public List<Snippet> searchByContent(String query) {
        final String searchQuery = query.toLowerCase();
        return snippets.values().stream()
                .filter(s -> s.getCode().toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());
    }

    public List<Snippet> searchByDate(LocalDateTime start, LocalDateTime end) {
        return snippets.values().stream()
                .filter(s -> !s.getCreatedAt().isBefore(start) && !s.getCreatedAt().isAfter(end))
                .collect(Collectors.toList());
    }

    public List<Snippet> advancedSearch(String query) {
        query = query.toLowerCase();
        Set<Snippet> results = new HashSet<>();
        
        // Search in titles
        results.addAll(searchByTitle(query));
        
        // Search in content
        results.addAll(searchByContent(query));
        
        // Search in tags
        results.addAll(searchByTag(query));
        
        // Search in language
        results.addAll(searchByLanguage(query));
        
        return new ArrayList<>(results);
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            throw new IllegalStateException("No operations to undo");
        }

        Operation operation = undoStack.pop();
        redoStack.push(operation);
        
        switch (operation.getType()) {
            case ADD:
                snippets.remove(operation.getSnippet().getTitle());
                break;
            case DELETE:
                snippets.put(operation.getSnippet().getTitle(), operation.getSnippet());
                break;
            case EDIT:
                snippets.put(operation.getOldSnippet().getTitle(), operation.getOldSnippet());
                break;
        }
        
        saveSnippets();
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            throw new IllegalStateException("No operations to redo");
        }

        Operation operation = redoStack.pop();
        undoStack.push(operation);
        
        switch (operation.getType()) {
            case ADD:
                snippets.put(operation.getSnippet().getTitle(), operation.getSnippet());
                break;
            case DELETE:
                snippets.remove(operation.getSnippet().getTitle());
                break;
            case EDIT:
                snippets.put(operation.getSnippet().getTitle(), operation.getSnippet());
                break;
        }
        
        saveSnippets();
    }

    private String[] extractKeywords(String code) {
        // Simple keyword extraction - can be enhanced with more sophisticated NLP
        return code.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s]", " ")
                .split("\\s+");
    }

    private void saveSnippets() {
        // Create backup before saving
        File currentFile = new File(SAVE_FILE);
        if (currentFile.exists()) {
            try {
                Files.copy(currentFile.toPath(), new File(BACKUP_FILE).toPath(), 
                    StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.err.println("Error creating backup: " + e.getMessage());
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(snippets);
        } catch (IOException e) {
            System.err.println("Error saving snippets: " + e.getMessage());
            // Restore from backup if save fails
            restoreFromBackup();
        }
    }

    private void restoreFromBackup() {
        File backupFile = new File(BACKUP_FILE);
        if (backupFile.exists()) {
            try {
                Files.copy(backupFile.toPath(), new File(SAVE_FILE).toPath(), 
                    StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.err.println("Error restoring from backup: " + e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadSnippets() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            snippets = (Map<String, Snippet>) ois.readObject();
            
            // Rebuild tag map and keyword trie
            for (Snippet snippet : snippets.values()) {
                for (String tag : snippet.getTags()) {
                    tagMap.computeIfAbsent(tag, k -> new ArrayList<>()).add(snippet);
                }
                String[] keywords = extractKeywords(snippet.getCode());
                for (String keyword : keywords) {
                    keywordTrie.insert(keyword);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading snippets: " + e.getMessage());
            restoreFromBackup();
        }
    }

    public List<Snippet> getAllSnippets() {
        return new ArrayList<>(snippets.values());
    }

    public List<Snippet> getSnippetsByLanguage(String language) {
        return snippets.values().stream()
                .filter(s -> s.getLanguage().equalsIgnoreCase(language))
                .collect(Collectors.toList());
    }

    public List<Snippet> getSnippetsByDateRange(LocalDateTime start, LocalDateTime end) {
        return snippets.values().stream()
                .filter(s -> !s.getCreatedAt().isBefore(start) && !s.getCreatedAt().isAfter(end))
                .collect(Collectors.toList());
    }

    public void exportToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Snippet snippet : snippets.values()) {
                writer.println("=== Snippet ===");
                writer.println(snippet);
                writer.println("==============");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error exporting snippets: " + e.getMessage());
        }
    }

    public void importFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder currentSnippet = new StringBuilder();
            String line;
            String title = null;
            String language = null;
            String code = null;
            List<String> tags = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("=== Snippet ===")) {
                    // Process previous snippet if exists
                    if (title != null && language != null && code != null) {
                        addSnippet(title, language, code, tags);
                    }
                    // Reset for new snippet
                    title = null;
                    language = null;
                    code = null;
                    tags.clear();
                    currentSnippet = new StringBuilder();
                } else if (line.startsWith("Title: ")) {
                    title = line.substring(7);
                } else if (line.startsWith("Language: ")) {
                    language = line.substring(10);
                } else if (line.startsWith("Tags: ")) {
                    String tagsStr = line.substring(6);
                    if (!tagsStr.isEmpty()) {
                        tags.addAll(Arrays.asList(tagsStr.split(", ")));
                    }
                } else if (line.startsWith("Code:")) {
                    // Start collecting code
                    currentSnippet = new StringBuilder();
                } else if (!line.equals("==============")) {
                    currentSnippet.append(line).append("\n");
                }
            }

            // Process last snippet
            if (title != null && language != null && code != null) {
                addSnippet(title, language, code, tags);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error importing snippets: " + e.getMessage());
        }
    }
} 