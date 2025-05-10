import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CodeSnippetManager {
    private static SnippetManager manager;
    private static Scanner scanner;

    public static void main(String[] args) {
        manager = new SnippetManager();
        scanner = new Scanner(System.in);
        
        while (true) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addSnippet();
                    break;
                case 2:
                    viewAllSnippets();
                    break;
                case 3:
                    editSnippet();
                    break;
                case 4:
                    deleteSnippet();
                    break;
                case 5:
                    searchSnippet();
                    break;
                case 6:
                    sortSnippets();
                    break;
                case 7:
                    manager.undo();
                    System.out.println("Undo operation completed.");
                    break;
                case 8:
                    manager.redo();
                    System.out.println("Redo operation completed.");
                    break;
                case 9:
                    exportSnippet();
                    break;
                case 10:
                    viewRecentSnippets();
                    break;
                case 11:
                    viewStatistics();
                    break;
                case 12:
                    System.out.println("Thank you for using Code Snippet Manager!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\nWelcome to Code Snippet Manager");
        System.out.println("1. Add Snippet");
        System.out.println("2. View All Snippets");
        System.out.println("3. Edit Snippet");
        System.out.println("4. Delete Snippet");
        System.out.println("5. Search Snippet");
        System.out.println("6. Sort Snippets");
        System.out.println("7. Undo");
        System.out.println("8. Redo");
        System.out.println("9. Export Snippet");
        System.out.println("10. View Recent Snippets");
        System.out.println("11. View Statistics");
        System.out.println("12. Exit");
    }

    private static void addSnippet() {
        System.out.println("\nAdd New Snippet");
        String title = getStringInput("Enter title: ");
        System.out.println("Enter code (type 'END' on a new line to finish):");
        StringBuilder code = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("END")) {
            code.append(line).append("\n");
        }
        String language = getStringInput("Enter programming language: ");
        
        manager.addSnippet(title, code.toString(), language);
        System.out.println("Snippet added successfully!");
    }

    private static void viewAllSnippets() {
        List<Snippet> snippets = manager.getAllSnippets();
        if (snippets.isEmpty()) {
            System.out.println("No snippets found.");
            return;
        }
        
        System.out.println("\nAll Snippets:");
        for (Snippet snippet : snippets) {
            System.out.println(snippet);
            System.out.println("-------------------");
        }
    }

    private static void editSnippet() {
        String id = getStringInput("Enter snippet ID to edit: ");
        Snippet snippet = manager.getSnippet(id);
        if (snippet == null) {
            System.out.println("Snippet not found.");
            return;
        }

        System.out.println("Current snippet:");
        System.out.println(snippet);
        
        String title = getStringInput("Enter new title (or press Enter to keep current): ");
        if (title.isEmpty()) title = snippet.getTitle();
        
        System.out.println("Enter new code (type 'END' on a new line to finish, or press Enter to keep current):");
        String line = scanner.nextLine();
        String code;
        if (line.isEmpty()) {
            code = snippet.getCode();
        } else {
            StringBuilder newCode = new StringBuilder(line + "\n");
            while (!(line = scanner.nextLine()).equals("END")) {
                newCode.append(line).append("\n");
            }
            code = newCode.toString();
        }
        
        String language = getStringInput("Enter new language (or press Enter to keep current): ");
        if (language.isEmpty()) language = snippet.getLanguage();
        
        manager.editSnippet(id, title, code, language);
        System.out.println("Snippet updated successfully!");
    }

    private static void deleteSnippet() {
        String id = getStringInput("Enter snippet ID to delete: ");
        manager.deleteSnippet(id);
        System.out.println("Snippet deleted successfully!");
    }

    private static void searchSnippet() {
        System.out.println("\nSearch Options:");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Tag");
        System.out.println("3. Search by Language");
        
        int choice = getIntInput("Enter your choice: ");
        List<Snippet> results;
        
        switch (choice) {
            case 1:
                String title = getStringInput("Enter title to search: ");
                results = manager.searchByTitle(title);
                break;
            case 2:
                String tag = getStringInput("Enter tag to search: ");
                results = manager.searchByTag(tag);
                break;
            case 3:
                String language = getStringInput("Enter language to search: ");
                results = manager.searchByLanguage(language);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("No snippets found.");
            return;
        }
        
        System.out.println("\nSearch Results:");
        for (Snippet snippet : results) {
            System.out.println(snippet);
            System.out.println("-------------------");
        }
    }

    private static void sortSnippets() {
        System.out.println("\nSort Options:");
        System.out.println("1. Sort by Title");
        System.out.println("2. Sort by Date");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                manager.sortSnippetsByTitle();
                System.out.println("Snippets sorted by title.");
                break;
            case 2:
                manager.sortSnippetsByDate();
                System.out.println("Snippets sorted by date.");
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        viewAllSnippets();
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

    private static void exportSnippet() {
        String id = getStringInput("Enter snippet ID to export: ");
        Snippet snippet = manager.getSnippet(id);
        if (snippet == null) {
            System.out.println("Snippet not found.");
            return;
        }

        System.out.println("\nExport Options:");
        System.out.println("1. Export as HTML");
        System.out.println("2. Export as Markdown");
        
        int choice = getIntInput("Enter your choice: ");
        String export;
        
        switch (choice) {
            case 1:
                export = manager.exportToHtml(snippet);
                break;
            case 2:
                export = manager.exportToMarkdown(snippet);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        System.out.println("\nExported Snippet:");
        System.out.println(export);
        
        String save = getStringInput("Save to file? (y/n): ");
        if (save.equalsIgnoreCase("y")) {
            String filename = getStringInput("Enter filename: ");
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(export);
                System.out.println("Snippet exported successfully to " + filename);
            } catch (IOException e) {
                System.out.println("Error saving file: " + e.getMessage());
            }
        }
    }

    private static void viewRecentSnippets() {
        List<Snippet> recentSnippets = manager.getRecentSnippets();
        if (recentSnippets.isEmpty()) {
            System.out.println("No snippets created in the last 24 hours.");
            return;
        }
        
        System.out.println("\nRecent Snippets (Last 24 Hours):");
        for (Snippet snippet : recentSnippets) {
            System.out.println(snippet);
            System.out.println("-------------------");
        }
    }

    private static void viewStatistics() {
        System.out.println("\nSnippet Statistics");
        System.out.println("-----------------");
        
        Map<String, Integer> languageStats = manager.getLanguageStats();
        System.out.println("\nProgramming Languages:");
        languageStats.forEach((lang, count) -> 
            System.out.printf("%s: %d snippets%n", lang, count));
        
        Map<String, Integer> tagStats = manager.getTagStats();
        System.out.println("\nMost Used Tags:");
        tagStats.forEach((tag, count) -> 
            System.out.printf("%s: %d snippets%n", tag, count));
        
        System.out.printf("%nTotal Snippets: %d%n", manager.getAllSnippets().size());
    }
} 