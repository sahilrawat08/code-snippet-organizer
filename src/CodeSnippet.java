import java.time.LocalDateTime;
import java.util.*;

public class CodeSnippet {
    private String title;
    private String description;
    private String code;
    private String language;
    private Set<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;

    public CodeSnippet(String title, String description, String code, String language, Set<String> tags) {
        this.title = title;
        this.description = description;
        this.code = code;
        this.language = language;
        this.tags = new HashSet<>(tags);
        this.createdAt = LocalDateTime.now();
        this.lastModified = this.createdAt;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCode() { return code; }
    public String getLanguage() { return language; }
    public Set<String> getTags() { return new HashSet<>(tags); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastModified() { return lastModified; }

    // Setters
    public void setTitle(String title) {
        this.title = title;
        this.lastModified = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.lastModified = LocalDateTime.now();
    }

    public void setCode(String code) {
        this.code = code;
        this.lastModified = LocalDateTime.now();
    }

    public void setLanguage(String language) {
        this.language = language;
        this.lastModified = LocalDateTime.now();
    }

    public void setTags(Set<String> tags) {
        this.tags = new HashSet<>(tags);
        this.lastModified = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeSnippet that = (CodeSnippet) o;
        return Objects.equals(title, that.title) &&
               Objects.equals(description, that.description) &&
               Objects.equals(code, that.code) &&
               Objects.equals(language, that.language) &&
               Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, code, language, tags);
    }

    @Override
    public String toString() {
        return String.format("Title: %s\nLanguage: %s\nTags: %s\nCreated: %s\nLast Modified: %s\n\nDescription:\n%s\n\nCode:\n%s",
            title, language, String.join(", ", tags), createdAt, lastModified, description, code);
    }
} 