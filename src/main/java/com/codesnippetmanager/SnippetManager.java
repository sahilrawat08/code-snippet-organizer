package com.codesnippetmanager;

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;

public class SnippetManager {
    private Map<String, Snippet> snippets;
    private UndoManager undoManager;
    private static final String SAVE_FILE = "snippets.txt";

    public SnippetManager() {
        this.snippets = new HashMap<>();
        this.undoManager = new UndoManager();
        loadSnippets();
    }

    /**
     * Adds a new snippet to the manager
     * Time Complexity: O(1) - HashMap insertion is constant time
     * Space Complexity: O(1) - Space for one snippet object
     */
    public void addSnippet(String title, String code, String language) {
        Snippet snippet = new Snippet(title, code, language);
        snippets.put(snippet.getId(), snippet);
        undoManager.addAction(new UndoManager.Action("ADD", snippet));
        saveSnippets();
    }

    /**
     * Deletes a snippet by its ID
     * Time Complexity: O(1) - HashMap removal is constant time
     * Space Complexity: O(1) - No additional space needed
     */
    public void deleteSnippet(String id) {
        Snippet snippet = snippets.remove(id);
        if (snippet != null) {
            undoManager.addAction(new UndoManager.Action("DELETE", snippet));
            saveSnippets();
        }
    }

    /**
     * Edits an existing snippet
     * Time Complexity: O(1) - HashMap update is constant time
     * Space Complexity: O(1) - Space for temporary snippet object
     */
    public void editSnippet(String id, String title, String code, String language) {
        Snippet oldSnippet = snippets.get(id);
        if (oldSnippet != null) {
            Snippet newSnippet = new Snippet(title, code, language);
            snippets.put(id, newSnippet);
            undoManager.addAction(new UndoManager.Action("EDIT", newSnippet, oldSnippet));
            saveSnippets();
        }
    }

    /**
     * Retrieves a snippet by its ID
     * Time Complexity: O(1) - HashMap lookup is constant time
     * Space Complexity: O(1) - No additional space needed
     */
    public Snippet getSnippet(String id) {
        return snippets.get(id);
    }

    /**
     * Searches snippets by title
     * Time Complexity: O(n) - Linear scan through all snippets
     * Space Complexity: O(k) - Where k is the number of matching results
     */
    public List<Snippet> searchByTitle(String title) {
        return snippets.values().stream()
                .filter(s -> s.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }

    /**
     * Searches snippets by tag
     * Time Complexity: O(n) - Linear scan through all snippets
     * Space Complexity: O(k) - Where k is the number of matching results
     */
    public List<Snippet> searchByTag(String tag) {
        return snippets.values().stream()
                .filter(s -> s.getTags().contains(tag))
                .toList();
    }

    /**
     * Searches snippets by language
     * Time Complexity: O(n) - Linear scan through all snippets
     * Space Complexity: O(k) - Where k is the number of matching results
     */
    public List<Snippet> searchByLanguage(String language) {
        return snippets.values().stream()
                .filter(s -> s.getLanguage().equalsIgnoreCase(language))
                .toList();
    }

    /**
     * Gets all snippets
     * Time Complexity: O(n) - Where n is the number of snippets
     * Space Complexity: O(n) - Space for the new ArrayList
     */
    public List<Snippet> getAllSnippets() {
        return new ArrayList<>(snippets.values());
    }

    /**
     * Sorts snippets by title
     * Time Complexity: O(n log n) - Using Collections.sort
     * Space Complexity: O(n) - Space for temporary list
     */
    public void sortSnippetsByTitle() {
        List<Snippet> sortedList = new ArrayList<>(snippets.values());
        Collections.sort(sortedList, Comparator.comparing(Snippet::getTitle));
        snippets.clear();
        for (Snippet snippet : sortedList) {
            snippets.put(snippet.getId(), snippet);
        }
    }

    /**
     * Sorts snippets by creation date
     * Time Complexity: O(n log n) - Using Collections.sort
     * Space Complexity: O(n) - Space for temporary list
     */
    public void sortSnippetsByDate() {
        List<Snippet> sortedList = new ArrayList<>(snippets.values());
        Collections.sort(sortedList, Comparator.comparing(Snippet::getCreatedAt));
        snippets.clear();
        for (Snippet snippet : sortedList) {
            snippets.put(snippet.getId(), snippet);
        }
    }

    /**
     * Undoes the last operation
     * Time Complexity: O(1) - Stack operations are constant time
     * Space Complexity: O(1) - No additional space needed
     */
    public void undo() {
        UndoManager.Action action = undoManager.undo();
        if (action != null) {
            switch (action.getType()) {
                case "ADD":
                    snippets.remove(action.getSnippet().getId());
                    break;
                case "DELETE":
                    snippets.put(action.getSnippet().getId(), action.getSnippet());
                    break;
                case "EDIT":
                    snippets.put(action.getOldSnippet().getId(), action.getOldSnippet());
                    break;
            }
            saveSnippets();
        }
    }

    /**
     * Redoes the last undone operation
     * Time Complexity: O(1) - Stack operations are constant time
     * Space Complexity: O(1) - No additional space needed
     */
    public void redo() {
        UndoManager.Action action = undoManager.redo();
        if (action != null) {
            switch (action.getType()) {
                case "ADD":
                    snippets.put(action.getSnippet().getId(), action.getSnippet());
                    break;
                case "DELETE":
                    snippets.remove(action.getSnippet().getId());
                    break;
                case "EDIT":
                    snippets.put(action.getSnippet().getId(), action.getSnippet());
                    break;
            }
            saveSnippets();
        }
    }

    /**
     * Saves all snippets to file
     * Time Complexity: O(n) - Where n is the total number of snippets
     * Space Complexity: O(n) - Space for serializing all snippets
     */
    private void saveSnippets() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(new ArrayList<>(snippets.values()));
        } catch (IOException e) {
            System.err.println("Error saving snippets: " + e.getMessage());
        }
    }

    /**
     * Loads snippets from file
     * Time Complexity: O(n) - Where n is the total number of snippets
     * Space Complexity: O(n) - Space for deserializing all snippets
     */
    @SuppressWarnings("unchecked")
    private void loadSnippets() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<Snippet> loadedSnippets = (List<Snippet>) ois.readObject();
                for (Snippet snippet : loadedSnippets) {
                    snippets.put(snippet.getId(), snippet);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading snippets: " + e.getMessage());
            }
        }
    }

    /**
     * Exports a snippet to HTML format
     * Time Complexity: O(1) - Simple string formatting
     * Space Complexity: O(1) - Space for the formatted string
     */
    public String exportToHtml(Snippet snippet) {
        return String.format(
            "<div class=\"code-snippet\">\n" +
            "    <h3>%s</h3>\n" +
            "    <p><strong>Language:</strong> %s</p>\n" +
            "    <p><strong>Tags:</strong> %s</p>\n" +
            "    <p><strong>Created:</strong> %s</p>\n" +
            "    <pre><code class=\"language-%s\">%s</code></pre>\n" +
            "</div>",
            snippet.getTitle(),
            snippet.getLanguage(),
            String.join(", ", snippet.getTags()),
            snippet.getCreatedAt(),
            snippet.getLanguage().toLowerCase(),
            snippet.getCode());
    }

    /**
     * Exports a snippet to Markdown format
     * Time Complexity: O(1) - Simple string formatting
     * Space Complexity: O(1) - Space for the formatted string
     */
    public String exportToMarkdown(Snippet snippet) {
        return String.format(
            "# %s\n\n" +
            "**Language:** %s  \n" +
            "**Tags:** %s  \n" +
            "**Created:** %s\n\n" +
            "```%s\n" +
            "%s\n" +
            "```",
            snippet.getTitle(),
            snippet.getLanguage(),
            String.join(", ", snippet.getTags()),
            snippet.getCreatedAt(),
            snippet.getLanguage().toLowerCase(),
            snippet.getCode());
    }

    /**
     * Gets snippets created in the last 24 hours
     * Time Complexity: O(n) - Linear scan through snippets
     * Space Complexity: O(k) - Where k is the number of recent snippets
     */
    public List<Snippet> getRecentSnippets() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return snippets.values().stream()
                .filter(s -> s.getCreatedAt().isAfter(yesterday))
                .toList();
    }

    /**
     * Gets the most used programming languages
     * Time Complexity: O(n) - Linear scan through snippets
     * Space Complexity: O(k) - Where k is the number of unique languages
     */
    public Map<String, Integer> getLanguageStats() {
        Map<String, Integer> stats = new HashMap<>();
        for (Snippet snippet : snippets.values()) {
            stats.merge(snippet.getLanguage(), 1, Integer::sum);
        }
        return stats;
    }

    /**
     * Gets the most used tags
     * Time Complexity: O(n) - Linear scan through snippets
     * Space Complexity: O(k) - Where k is the number of unique tags
     */
    public Map<String, Integer> getTagStats() {
        Map<String, Integer> stats = new HashMap<>();
        for (Snippet snippet : snippets.values()) {
            for (String tag : snippet.getTags()) {
                stats.merge(tag, 1, Integer::sum);
            }
        }
        return stats;
    }
} 