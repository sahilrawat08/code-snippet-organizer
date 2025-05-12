package com.codesnippetmanager;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Snippet implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String code;
    private String language;
    private Set<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Snippet(String title, String code, String language) {
        this.id = generateId();
        this.title = title;
        this.code = code;
        this.language = language;
        this.tags = new HashSet<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private String generateId() {
        return String.valueOf(System.currentTimeMillis());
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { 
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }
    public String getCode() { return code; }
    public void setCode(String code) { 
        this.code = code;
        this.updatedAt = LocalDateTime.now();
    }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { 
        this.language = language;
        this.updatedAt = LocalDateTime.now();
    }
    public Set<String> getTags() { return tags; }
    public void addTag(String tag) { 
        this.tags.add(tag);
        this.updatedAt = LocalDateTime.now();
    }
    public void removeTag(String tag) { 
        this.tags.remove(tag);
        this.updatedAt = LocalDateTime.now();
    }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public String toString() {
        return String.format(
            "ID: %s\n" +
            "Title: %s\n" +
            "Language: %s\n" +
            "Tags: %s\n" +
            "Created: %s\n" +
            "Last Updated: %s\n" +
            "Code:\n%s",
            id, title, language, tags, createdAt, updatedAt, code);
    }
} 