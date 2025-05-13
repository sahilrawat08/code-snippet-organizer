import java.util.*;
import java.time.LocalDateTime;

public class SnippetManager {
    private List<CodeSnippet> snippets;
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;
    private Map<String, Integer> languageStats;
    private Map<String, Integer> tagStats;
    private List<CodeSnippet> recentSnippets;
    private static final int MAX_RECENT_SNIPPETS = 5;

    public SnippetManager() {
        snippets = new ArrayList<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        languageStats = new HashMap<>();
        tagStats = new HashMap<>();
        recentSnippets = new ArrayList<>();
    }

    public void addSnippet(CodeSnippet snippet) {
        Command command = new AddSnippetCommand(snippets, snippet);
        command.execute();
        undoStack.push(command);
        redoStack.clear();
        updateStats(snippet);
        addToRecent(snippet);
    }

    public void editSnippet(int index, CodeSnippet newSnippet) {
        if (index >= 0 && index < snippets.size()) {
            CodeSnippet oldSnippet = snippets.get(index);
            Command command = new EditSnippetCommand(snippets, index, oldSnippet, newSnippet);
            command.execute();
            undoStack.push(command);
            redoStack.clear();
            updateStats(newSnippet);
            addToRecent(newSnippet);
        }
    }

    public void deleteSnippet(int index) {
        if (index >= 0 && index < snippets.size()) {
            CodeSnippet snippet = snippets.get(index);
            Command command = new DeleteSnippetCommand(snippets, index, snippet);
            command.execute();
            undoStack.push(command);
            redoStack.clear();
            removeFromStats(snippet);
        }
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }

    public List<CodeSnippet> getAllSnippets() {
        return new ArrayList<>(snippets);
    }

    public List<CodeSnippet> searchSnippets(String query) {
        List<CodeSnippet> results = new ArrayList<>();
        query = query.toLowerCase();
        final String queryFinal = query;
        
        for (CodeSnippet snippet : snippets) {
            if (snippet.getTitle().toLowerCase().contains(queryFinal) ||
                snippet.getDescription().toLowerCase().contains(queryFinal) ||
                snippet.getCode().toLowerCase().contains(queryFinal) ||
                snippet.getLanguage().toLowerCase().contains(queryFinal) ||
                snippet.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(queryFinal))) {
                results.add(snippet);
            }
        }
        return results;
    }

    public List<CodeSnippet> getRecentSnippets() {
        return new ArrayList<>(recentSnippets);
    }

    public Map<String, Integer> getLanguageStats() {
        return new HashMap<>(languageStats);
    }

    public Map<String, Integer> getTagStats() {
        return new HashMap<>(tagStats);
    }

    private void updateStats(CodeSnippet snippet) {
        // Update language statistics
        languageStats.merge(snippet.getLanguage(), 1, Integer::sum);
        
        // Update tag statistics
        for (String tag : snippet.getTags()) {
            tagStats.merge(tag, 1, Integer::sum);
        }
    }

    private void removeFromStats(CodeSnippet snippet) {
        // Remove from language statistics
        languageStats.merge(snippet.getLanguage(), -1, Integer::sum);
        if (languageStats.get(snippet.getLanguage()) <= 0) {
            languageStats.remove(snippet.getLanguage());
        }
        
        // Remove from tag statistics
        for (String tag : snippet.getTags()) {
            tagStats.merge(tag, -1, Integer::sum);
            if (tagStats.get(tag) <= 0) {
                tagStats.remove(tag);
            }
        }
    }

    private void addToRecent(CodeSnippet snippet) {
        recentSnippets.remove(snippet); // Remove if already exists
        recentSnippets.add(0, snippet); // Add to beginning
        if (recentSnippets.size() > MAX_RECENT_SNIPPETS) {
            recentSnippets.remove(recentSnippets.size() - 1);
        }
    }
} 