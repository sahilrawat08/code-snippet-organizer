import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CodeSnippetOrganizer {
    private static SnippetManager manager;
    private static Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    // ANSI color codes
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public static void main(String[] args) {
        manager = new SnippetManager();
        scanner = new Scanner(System.in);
        
        // Display welcome message
        displayWelcomeMessage();
        
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
                        System.out.println(ANSI_GREEN + "\nThank you for using Code Snippet Organizer!" + ANSI_RESET);
                        return;
                    default:
                        System.out.println(ANSI_RED + "Invalid choice. Please try again." + ANSI_RESET);
                }
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
                System.out.println("Please try again.");
            }
        }
    }

    private static void displayWelcomeMessage() {
        System.out.println(ANSI_CYAN + 
            "╔══════════════════════════════════════════════════════════╗\n" +
            "║                                                          ║\n" +
            "║              Welcome to Code Snippet Organizer!          ║\n" +
            "║                                                          ║\n" +
            "║     Your personal code snippet management assistant      ║\n" +
            "║                                                          ║\n" +
            "╚══════════════════════════════════════════════════════════╝" + 
            ANSI_RESET);
    }

    private static void displayMenu() {
        System.out.println(ANSI_CYAN + "\n=== " + ANSI_BOLD + "Code Snippet Organizer" + ANSI_RESET + ANSI_CYAN + " ===" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "1. " + ANSI_RESET + "Add new snippet");
        System.out.println(ANSI_YELLOW + "2. " + ANSI_RESET + "Search snippets");
        System.out.println(ANSI_YELLOW + "3. " + ANSI_RESET + "Edit snippet");
        System.out.println(ANSI_YELLOW + "4. " + ANSI_RESET + "Delete snippet");
        System.out.println(ANSI_YELLOW + "5. " + ANSI_RESET + "List all snippets");
        System.out.println(ANSI_YELLOW + "6. " + ANSI_RESET + "Undo last operation");
        System.out.println(ANSI_YELLOW + "7. " + ANSI_RESET + "Redo last operation");
        System.out.println(ANSI_YELLOW + "8. " + ANSI_RESET + "Export snippets");
        System.out.println(ANSI_YELLOW + "9. " + ANSI_RESET + "Import snippets");
        System.out.println(ANSI_YELLOW + "10. " + ANSI_RESET + "Exit");
    }

    private static void addSnippet() {
        System.out.println(ANSI_CYAN + "\n=== " + ANSI_BOLD + "Add New Snippet" + ANSI_RESET + ANSI_CYAN + " ===" + ANSI_RESET);
        
        // Get title with validation
        String title;
        while (true) {
            title = getStringInput(ANSI_YELLOW + "📝 Enter snippet title: " + ANSI_RESET).trim();
            if (!title.isEmpty()) {
                break;
            }
            System.out.println(ANSI_RED + "Title cannot be empty. Please try again." + ANSI_RESET);
        }

        // Get language with validation
        String language;
        while (true) {
            language = getStringInput(ANSI_YELLOW + "💻 Enter programming language: " + ANSI_RESET).trim();
            if (!language.isEmpty()) {
                break;
            }
            System.out.println(ANSI_RED + "Language cannot be empty. Please try again." + ANSI_RESET);
        }

        // Get code with validation
        System.out.println(ANSI_CYAN + "\n📝 Enter code (type 'END' on a new line to finish):" + ANSI_RESET);
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
            System.out.println(ANSI_RED + "Code cannot be empty. Please try again." + ANSI_RESET);
            return;
        }

        // Create the snippet
        List<String> tags = new ArrayList<>();
        
        // Add tags
        System.out.println(ANSI_CYAN + "\n🏷️  Would you like to add tags? (yes/no)" + ANSI_RESET);
        String addTags = getStringInput("Enter your choice: ").toLowerCase();
        
        if (addTags.equals("yes") || addTags.equals("y")) {
            System.out.println(ANSI_CYAN + "\nEnter tags (one per line, type 'DONE' to finish):" + ANSI_RESET);
            while (true) {
                String tag = getStringInput(ANSI_YELLOW + "Tag: " + ANSI_RESET).trim();
                if (tag.equals("DONE")) {
                    break;
                }
                if (!tag.isEmpty()) {
                    tags.add(tag);
                }
            }
        }

        // Confirm before adding
        System.out.println(ANSI_CYAN + "\n📋 Please review your snippet:" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Title: " + ANSI_RESET + title);
        System.out.println(ANSI_YELLOW + "Language: " + ANSI_RESET + language);
        System.out.println(ANSI_YELLOW + "Tags: " + ANSI_RESET + String.join(", ", tags));
        System.out.println(ANSI_YELLOW + "Code:\n" + ANSI_RESET + code);
        
        System.out.println(ANSI_CYAN + "\n💾 Do you want to save this snippet? (yes/no)" + ANSI_RESET);
        String confirm = getStringInput("Enter your choice: ").toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            try {
                // Show saving animation
                System.out.print(ANSI_CYAN + "\n💾 Saving");
                for (int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(300);
                        System.out.print(".");
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println(ANSI_RESET);
                
                manager.addSnippet(title, language, code.toString(), tags);
                System.out.println(ANSI_GREEN + "\n✨ Snippet added successfully!" + ANSI_RESET);
            } catch (IllegalArgumentException e) {
                System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_YELLOW + "\nSnippet creation cancelled." + ANSI_RESET);
        }
    }

    private static void searchSnippets() {
        System.out.println(ANSI_CYAN + "\n=== " + ANSI_BOLD + "Search Snippets" + ANSI_RESET + ANSI_CYAN + " ===" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "1. " + ANSI_RESET + "Search by title");
        System.out.println(ANSI_YELLOW + "2. " + ANSI_RESET + "Search by tag");
        System.out.println(ANSI_YELLOW + "3. " + ANSI_RESET + "Search by keyword");
        System.out.println(ANSI_YELLOW + "4. " + ANSI_RESET + "Search by language");
        System.out.println(ANSI_YELLOW + "5. " + ANSI_RESET + "Search by content");
        System.out.println(ANSI_YELLOW + "6. " + ANSI_RESET + "Search by date range");
        System.out.println(ANSI_YELLOW + "7. " + ANSI_RESET + "Advanced search (searches everywhere)");
        
        int choice = getIntInput("Enter your choice: ");
        String query = getStringInput("Enter search query: ").trim();
        
        if (query.isEmpty()) {
            System.out.println(ANSI_RED + "Search query cannot be empty." + ANSI_RESET);
            return;
        }
        
        // Show searching animation
        System.out.print(ANSI_CYAN + "\n🔍 Searching");
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(300);
                System.out.print(".");
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(ANSI_RESET);
        
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
                        System.out.println(ANSI_GREEN + "\n✨ Suggested keywords: " + ANSI_RESET + 
                            String.join(", ", keywords));
                        System.out.println(ANSI_CYAN + "\n📚 Snippets containing these keywords:" + ANSI_RESET);
                        results = manager.advancedSearch(query);
                    } else {
                        System.out.println(ANSI_YELLOW + "\nNo matching keywords found." + ANSI_RESET);
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
                    System.out.println(ANSI_CYAN + "\nEnter date range (format: yyyy-MM-dd HH:mm)" + ANSI_RESET);
                    String startDate = getStringInput("Start date: ");
                    String endDate = getStringInput("End date: ");
                    try {
                        LocalDateTime start = LocalDateTime.parse(startDate, DATE_FORMATTER);
                        LocalDateTime end = LocalDateTime.parse(endDate, DATE_FORMATTER);
                        results = manager.searchByDate(start, end);
                    } catch (DateTimeParseException e) {
                        System.out.println(ANSI_RED + "Invalid date format. Please use yyyy-MM-dd HH:mm" + ANSI_RESET);
                        return;
                    }
                    break;
                case 7:
                    results = manager.advancedSearch(query);
                    break;
                default:
                    System.out.println(ANSI_RED + "Invalid choice." + ANSI_RESET);
                    return;
            }
            
            if (results.isEmpty()) {
                System.out.println(ANSI_YELLOW + "\nNo snippets found matching your search criteria." + ANSI_RESET);
                System.out.println(ANSI_CYAN + "\nSuggestions:" + ANSI_RESET);
                System.out.println("1. Try using different keywords");
                System.out.println("2. Use the advanced search option");
                System.out.println("3. Check for typos in your search query");
                return;
            }
            
            // Group results by language
            Map<String, List<Snippet>> resultsByLanguage = results.stream()
                .collect(Collectors.groupingBy(Snippet::getLanguage));
            
            System.out.println(ANSI_GREEN + "\n✨ Found " + results.size() + " matching snippet(s):" + ANSI_RESET);
            
            // Display results grouped by language
            for (Map.Entry<String, List<Snippet>> entry : resultsByLanguage.entrySet()) {
                String language = entry.getKey();
                List<Snippet> languageSnippets = entry.getValue();
                
                System.out.println(ANSI_GREEN + "\n📚 " + ANSI_BOLD + language.toUpperCase() + ANSI_RESET + 
                                 ANSI_GREEN + " (" + languageSnippets.size() + " snippets)" + ANSI_RESET);
                System.out.println(ANSI_CYAN + "═".repeat(50) + ANSI_RESET);
                
                for (Snippet snippet : languageSnippets) {
                    System.out.println(ANSI_YELLOW + "\n📝 " + ANSI_BOLD + snippet.getTitle() + ANSI_RESET);
                    System.out.println(ANSI_PURPLE + "   Tags: " + ANSI_RESET + String.join(", ", snippet.getTags()));
                    System.out.println(ANSI_BLUE + "   Created: " + ANSI_RESET + 
                        snippet.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    System.out.println(ANSI_BLUE + "   Last Updated: " + ANSI_RESET + 
                        snippet.getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    System.out.println(ANSI_CYAN + "   " + "─".repeat(40) + ANSI_RESET);
                }
            }
            
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error during search: " + e.getMessage() + ANSI_RESET);
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
        System.out.println(ANSI_CYAN + "\n=== " + ANSI_BOLD + "All Snippets" + ANSI_RESET + ANSI_CYAN + " ===" + ANSI_RESET);
        List<Snippet> snippets = manager.getAllSnippets();
        
        if (snippets.isEmpty()) {
            System.out.println(ANSI_YELLOW + "\nNo snippets found. Add some snippets to get started!" + ANSI_RESET);
            return;
        }

        // Group snippets by language
        Map<String, List<Snippet>> snippetsByLanguage = snippets.stream()
            .collect(Collectors.groupingBy(Snippet::getLanguage));

        // Display snippets grouped by language
        for (Map.Entry<String, List<Snippet>> entry : snippetsByLanguage.entrySet()) {
            String language = entry.getKey();
            List<Snippet> languageSnippets = entry.getValue();

            System.out.println(ANSI_GREEN + "\n📚 " + ANSI_BOLD + language.toUpperCase() + ANSI_RESET + 
                             ANSI_GREEN + " (" + languageSnippets.size() + " snippets)" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "═".repeat(50) + ANSI_RESET);

            for (Snippet snippet : languageSnippets) {
                System.out.println(ANSI_YELLOW + "\n📝 " + ANSI_BOLD + snippet.getTitle() + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "   Tags: " + ANSI_RESET + String.join(", ", snippet.getTags()));
                System.out.println(ANSI_BLUE + "   Created: " + ANSI_RESET + 
                    snippet.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                System.out.println(ANSI_BLUE + "   Last Updated: " + ANSI_RESET + 
                    snippet.getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                System.out.println(ANSI_CYAN + "   " + "─".repeat(40) + ANSI_RESET);
            }
        }
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
        
        // Group snippets by language
        Map<String, List<Snippet>> snippetsByLanguage = snippets.stream()
            .collect(Collectors.groupingBy(Snippet::getLanguage));
        
        System.out.println(ANSI_GREEN + "\n✨ Found " + snippets.size() + " matching snippet(s):" + ANSI_RESET);
        
        // Display snippets grouped by language
        for (Map.Entry<String, List<Snippet>> entry : snippetsByLanguage.entrySet()) {
            String language = entry.getKey();
            List<Snippet> languageSnippets = entry.getValue();
            
            System.out.println(ANSI_GREEN + "\n📚 " + ANSI_BOLD + language.toUpperCase() + ANSI_RESET + 
                             ANSI_GREEN + " (" + languageSnippets.size() + " snippets)" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "═".repeat(50) + ANSI_RESET);
            
            for (Snippet snippet : languageSnippets) {
                System.out.println(ANSI_YELLOW + "\n📝 " + ANSI_BOLD + snippet.getTitle() + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "   Tags: " + ANSI_RESET + String.join(", ", snippet.getTags()));
                System.out.println(ANSI_BLUE + "   Created: " + ANSI_RESET + 
                    snippet.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                System.out.println(ANSI_BLUE + "   Last Updated: " + ANSI_RESET + 
                    snippet.getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                System.out.println(ANSI_CYAN + "   " + "─".repeat(40) + ANSI_RESET);
            }
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