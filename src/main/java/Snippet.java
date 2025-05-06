import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Snippet implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_CODE_LENGTH = 10000; // Maximum code length
    private static final int MIN_CODE_LENGTH = 1;     // Minimum code length
    private static final int MAX_TITLE_LENGTH = 100;  // Maximum title length
    
    private String title;
    private String language;
    private String code;
    private List<String> tags;
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private int version;

    public Snippet(String title, String language, String code) {
        validateTitle(title);
        validateLanguage(language);
        validateCode(code);
        
        this.title = title;
        this.language = language;
        this.code = code;
        this.tags = new ArrayList<>();
        this.id = generateId();
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.version = 1;
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Title exceeds maximum length of " + MAX_TITLE_LENGTH + " characters");
        }
    }

    private void validateLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw new IllegalArgumentException("Language cannot be empty");
        }
        // Add more language validation if needed
    }

    private void validateCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Code cannot be empty");
        }
        if (code.length() > MAX_CODE_LENGTH) {
            throw new IllegalArgumentException("Code exceeds maximum length of " + MAX_CODE_LENGTH + " characters");
        }
        if (code.length() < MIN_CODE_LENGTH) {
            throw new IllegalArgumentException("Code must be at least " + MIN_CODE_LENGTH + " character long");
        }
    }

    private String generateId() {
        return String.valueOf(System.currentTimeMillis());
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { 
        validateTitle(title);
        this.title = title;
        this.lastModified = LocalDateTime.now();
        this.version++;
    }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { 
        validateLanguage(language);
        this.language = language;
        this.lastModified = LocalDateTime.now();
        this.version++;
    }
    
    public String getCode() { return code; }
    public void setCode(String code) { 
        validateCode(code);
        this.code = code;
        this.lastModified = LocalDateTime.now();
        this.version++;
    }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { 
        this.tags = tags;
        this.lastModified = LocalDateTime.now();
        this.version++;
    }
    
    public String getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastModified() { return lastModified; }
    public int getVersion() { return version; }

    public void addTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be empty");
        }
        tags.add(tag.trim());
        this.lastModified = LocalDateTime.now();
        this.version++;
    }

    public void removeTag(String tag) {
        tags.remove(tag);
        this.lastModified = LocalDateTime.now();
        this.version++;
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    public boolean isDuplicate(Snippet other) {
        return this.code.equals(other.code);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(title).append("\n");
        sb.append("Language: ").append(language).append("\n");
        sb.append("Tags: ").append(String.join(", ", tags)).append("\n");
        sb.append("Created: ").append(createdAt).append("\n");
        sb.append("Last Updated: ").append(lastModified).append("\n");
        sb.append("Version: ").append(version).append("\n");
        sb.append("Code:\n").append(code);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Snippet snippet = (Snippet) o;
        return id.equals(snippet.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
} 