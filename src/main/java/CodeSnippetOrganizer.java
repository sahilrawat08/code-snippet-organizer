import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CodeSnippetOrganizer {
    private static SnippetManager manager;
    private static Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        manager = new SnippetManager();
        scanner = new Scanner(System.in);
        
        while (true) {
            try {
                displayMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        addSnippet();
                        break;
                    case 2:
                        searchSnippets();
                        break;
                    case 3:
                        editSnippet();
                        break;
                    case 4:
                        deleteSnippet();
                        break;
                    case 5:
                        listAllSnippets();
                        break;
                    case 6:
                        undoLastOperation();
                        break;
                    case 7:
                        redoLastOperation();
                        break;
                    case 8:
                        exportSnippets();
                        break;
                    case 9:
                        importSnippets();
                        break;
                    case 10:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== Code Snippet Organizer ===");
        System.out.println("1. Add new snippet");
        System.out.println("2. Search snippets");
        System.out.println("3. Edit snippet");
        System.out.println("4. Delete snippet");
        System.out.println("5. List all snippets");
        System.out.println("6. Undo last operation");
        System.out.println("7. Redo last operation");
        System.out.println("8. Export snippets");
        System.out.println("9. Import snippets");
        System.out.println("10. Exit");
    }

    private static void addSnippet() {
        System.out.println("\n=== Add New Snippet ===");
        
        // Get title with validation
        String title;
        while (true) {
            title = getStringInput("Enter snippet title: ").trim();
            if (!title.isEmpty()) {
                break;
            }
            System.out.println("Title cannot be empty. Please try again.");
        }

        // Get language with validation
        String language;
        while (true) {
            language = getStringInput("Enter programming language: ").trim();
            if (!language.isEmpty()) {
                break;
            }
            System.out.println("Language cannot be empty. Please try again.");
        }

        // Get code with validation
        System.out.println("Enter code (type 'END' on a new line to finish):");
        StringBuilder code = new StringBuilder();
        String line;
        boolean hasContent = false;
        
        while (!(line = scanner.nextLine()).equals("END")) {
            if (!line.trim().isEmpty()) {
                hasContent = true;
            }
            code.append(line).append("\n");
        }

        if (!hasContent) {
            System.out.println("Code cannot be empty. Please try again.");
            return;
        }

        // Create the snippet
        List<String> tags = new ArrayList<>();
        
        // Add tags
        System.out.println("\nWould you like to add tags? (yes/no)");
        String addTags = getStringInput("Enter your choice: ").toLowerCase();
        
        if (addTags.equals("yes") || addTags.equals("y")) {
            while (true) {
                System.out.println("\nCurrent tags: " + String.join(", ", tags));
                System.out.println("1. Add a tag");
                System.out.println("2. Remove a tag");
                System.out.println("3. Finish adding tags");
                
                int tagChoice = getIntInput("Enter your choice: ");
                
                switch (tagChoice) {
                    case 1:
                        String newTag = getStringInput("Enter tag to add: ").trim();
                        if (!newTag.isEmpty()) {
                            tags.add(newTag);
                            System.out.println("Tag added successfully!");
                        } else {
                            System.out.println("Tag cannot be empty!");
                        }
                        break;
                    case 2:
                        if (!tags.isEmpty()) {
                            String tagToRemove = getStringInput("Enter tag to remove: ").trim();
                            if (tags.contains(tagToRemove)) {
                                tags.remove(tagToRemove);
                                System.out.println("Tag removed successfully!");
                            } else {
                                System.out.println("Tag not found!");
                            }
                        } else {
                            System.out.println("No tags to remove!");
                        }
                        break;
                    case 3:
                        System.out.println("Finished adding tags.");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        continue;
                }
                
                if (tagChoice == 3) {
                    break;
                }
            }
        }

        // Confirm before adding
        System.out.println("\nPlease review your snippet:");
        System.out.println("Title: " + title);
        System.out.println("Language: " + language);
        System.out.println("Tags: " + String.join(", ", tags));
        System.out.println("Code:\n" + code);
        
        System.out.println("\nDo you want to save this snippet? (yes/no)");
        String confirm = getStringInput("Enter your choice: ").toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            try {
                manager.addSnippet(title, language, code.toString(), tags);
                System.out.println("Snippet added successfully!");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Snippet creation cancelled.");
        }
    }

    private static void searchSnippets() {
        System.out.println("\n=== Search Snippets ===");
        System.out.println("1. Search by title");
        System.out.println("2. Search by tag");
        System.out.println("3. Search by keyword");
        System.out.println("4. Search by language");
        System.out.println("5. Search by content");
        System.out.println("6. Search by date range");
        System.out.println("7. Advanced search (searches everywhere)");
        
        int choice = getIntInput("Enter your choice: ");
        String query = getStringInput("Enter search query: ").trim();
        
        if (query.isEmpty()) {
            System.out.println("Search query cannot be empty.");
            return;
        }
        
        List<Snippet> results;
        try {
            switch (choice) {
                case 1:
                    results = manager.searchByTitle(query);
                    break;
                case 2:
                    results = manager.searchByTag(query);
                    break;
                case 3:
                    List<String> keywords = manager.searchKeywords(query);
                    if (!keywords.isEmpty()) {
                        System.out.println("Suggested keywords: " + String.join(", ", keywords));
                        System.out.println("\nSnippets containing these keywords:");
                        results = manager.advancedSearch(query);
                    } else {
                        System.out.println("No matching keywords found.");
                        return;
                    }
                    break;
                case 4:
                    results = manager.searchByLanguage(query);
                    break;
                case 5:
                    results = manager.searchByContent(query);
                    break;
                case 6:
                    System.out.println("Enter date range (format: yyyy-MM-dd HH:mm)");
                    String startDate = getStringInput("Start date: ");
                    String endDate = getStringInput("End date: ");
                    try {
                        LocalDateTime start = LocalDateTime.parse(startDate, DATE_FORMATTER);
                        LocalDateTime end = LocalDateTime.parse(endDate, DATE_FORMATTER);
                        results = manager.searchByDate(start, end);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm");
                        return;
                    }
                    break;
                case 7:
                    results = manager.advancedSearch(query);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            
            displayResults(results);
            
            if (results.isEmpty()) {
                System.out.println("\nNo snippets found matching your search criteria.");
                System.out.println("Suggestions:");
                System.out.println("1. Try using different keywords");
                System.out.println("2. Use the advanced search option");
                System.out.println("3. Check for typos in your search query");
            }
        } catch (Exception e) {
            System.out.println("Error during search: " + e.getMessage());
        }
    }

    private static void editSnippet() {
        System.out.println("\n=== Edit Snippet ===");
        String title = getStringInput("Enter snippet title to edit: ");
        
        Snippet existingSnippet = manager.getSnippet(title);
        if (existingSnippet == null) {
            System.out.println("Snippet not found.");
            return;
        }

        System.out.println("\nCurrent snippet:");
        System.out.println(existingSnippet);
        
        System.out.println("\nEnter new values (press Enter to keep current value):");
        
        String newTitle = getStringInput("New title [" + existingSnippet.getTitle() + "]: ").trim();
        if (newTitle.isEmpty()) {
            newTitle = existingSnippet.getTitle();
        }
        
        String newLanguage = getStringInput("New language [" + existingSnippet.getLanguage() + "]: ").trim();
        if (newLanguage.isEmpty()) {
            newLanguage = existingSnippet.getLanguage();
        }
        
        System.out.println("Enter new code (type 'END' on a new line to finish, or press Enter to keep current code):");
        String line = scanner.nextLine();
        String newCode;
        if (line.isEmpty()) {
            newCode = existingSnippet.getCode();
        } else {
            StringBuilder codeBuilder = new StringBuilder(line + "\n");
            while (!(line = scanner.nextLine()).equals("END")) {
                codeBuilder.append(line).append("\n");
            }
            newCode = codeBuilder.toString();
        }

        // Handle tags
        List<String> newTags = new ArrayList<>(existingSnippet.getTags());
        System.out.println("\nCurrent tags: " + String.join(", ", newTags));
        System.out.println("Would you like to modify tags? (yes/no)");
        String modifyTags = getStringInput("Enter your choice: ").toLowerCase();
        
        if (modifyTags.equals("yes") || modifyTags.equals("y")) {
            while (true) {
                System.out.println("\nCurrent tags: " + String.join(", ", newTags));
                System.out.println("1. Add a tag");
                System.out.println("2. Remove a tag");
                System.out.println("3. Finish modifying tags");
                
                int tagChoice = getIntInput("Enter your choice: ");
                
                switch (tagChoice) {
                    case 1:
                        String newTag = getStringInput("Enter tag to add: ").trim();
                        if (!newTag.isEmpty()) {
                            newTags.add(newTag);
                            System.out.println("Tag added successfully!");
                        } else {
                            System.out.println("Tag cannot be empty!");
                        }
                        break;
                    case 2:
                        if (!newTags.isEmpty()) {
                            String tagToRemove = getStringInput("Enter tag to remove: ").trim();
                            if (newTags.contains(tagToRemove)) {
                                newTags.remove(tagToRemove);
                                System.out.println("Tag removed successfully!");
                            } else {
                                System.out.println("Tag not found!");
                            }
                        } else {
                            System.out.println("No tags to remove!");
                        }
                        break;
                    case 3:
                        System.out.println("Finished modifying tags.");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        continue;
                }
                
                if (tagChoice == 3) {
                    break;
                }
            }
        }

        // Confirm before saving
        System.out.println("\nPlease review your changes:");
        System.out.println("Title: " + newTitle);
        System.out.println("Language: " + newLanguage);
        System.out.println("Tags: " + String.join(", ", newTags));
        System.out.println("Code:\n" + newCode);
        
        System.out.println("\nDo you want to save these changes? (yes/no)");
        String confirm = getStringInput("Enter your choice: ").toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            try {
                manager.editSnippet(title, newLanguage, newCode, newTags);
                System.out.println("Snippet updated successfully!");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Edit cancelled.");
        }
    }

    private static void deleteSnippet() {
        System.out.println("\n=== Delete Snippet ===");
        String title = getStringInput("Enter snippet title to delete: ");
        
        Snippet snippet = manager.getSnippet(title);
        if (snippet == null) {
            System.out.println("Snippet not found.");
            return;
        }

        System.out.println("\nAre you sure you want to delete this snippet?");
        System.out.println(snippet);
        
        System.out.println("\nType 'DELETE' to confirm, or anything else to cancel:");
        String confirm = getStringInput("Enter your choice: ");
        
        if (confirm.equals("DELETE")) {
            try {
                manager.deleteSnippet(title);
                System.out.println("Snippet deleted successfully!");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void listAllSnippets() {
        System.out.println("\n=== All Snippets ===");
        List<Snippet> snippets = manager.getAllSnippets();
        displayResults(snippets);
    }

    private static void undoLastOperation() {
        try {
            manager.undo();
            System.out.println("Last operation undone successfully!");
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void redoLastOperation() {
        try {
            manager.redo();
            System.out.println("Last operation redone successfully!");
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void exportSnippets() {
        System.out.println("\n=== Export Snippets ===");
        String filename = getStringInput("Enter filename to export to: ");
        try {
            manager.exportToFile(filename);
            System.out.println("Snippets exported successfully!");
        } catch (Exception e) {
            System.out.println("Error exporting snippets: " + e.getMessage());
        }
    }

    private static void importSnippets() {
        System.out.println("\n=== Import Snippets ===");
        String filename = getStringInput("Enter filename to import from: ");
        try {
            manager.importFromFile(filename);
            System.out.println("Snippets imported successfully!");
        } catch (Exception e) {
            System.out.println("Error importing snippets: " + e.getMessage());
        }
    }

    private static void displayResults(List<Snippet> snippets) {
        if (snippets.isEmpty()) {
            return;
        }
        
        System.out.println("\nFound " + snippets.size() + " matching snippet(s):");
        for (int i = 0; i < snippets.size(); i++) {
            Snippet snippet = snippets.get(i);
            System.out.println("\n--- Snippet " + (i + 1) + " ---");
            System.out.println(snippet);
            System.out.println("-------------------");
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
} 