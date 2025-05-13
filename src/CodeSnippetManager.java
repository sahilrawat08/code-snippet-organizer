import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

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
        System.out.println("\n=== Code Snippet Manager ===");
        System.out.println("1. Add a new snippet");
        System.out.println("2. View all snippets");
        System.out.println("3. Edit a snippet");
        System.out.println("4. Delete a snippet");
        System.out.println("5. Search snippets");
        System.out.println("6. Sort snippets");
        System.out.println("7. Undo last operation");
        System.out.println("8. Redo last operation");
        System.out.println("9. Export snippet to file");
        System.out.println("10. View recent snippets");
        System.out.println("11. View statistics");
        System.out.println("12. Exit");
    }

    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static void addSnippet() {
        String title = getStringInput("Enter snippet title: ");
        String description = getStringInput("Enter snippet description: ");
        System.out.println("Enter code (type 'END' on a new line to finish):");
        StringBuilder code = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("END")) {
            code.append(line).append("\n");
        }
        String language = getStringInput("Enter programming language: ");
        System.out.println("Enter tags (comma-separated):");
        Set<String> tags = new HashSet<>(List.of(getStringInput("").split(",")));
        tags = tags.stream().map(String::trim).collect(Collectors.toSet());

        CodeSnippet snippet = new CodeSnippet(title, description, code.toString(), language, tags);
        manager.addSnippet(snippet);
        System.out.println("Snippet added successfully!");
    }

    private static void viewAllSnippets() {
        List<CodeSnippet> snippets = manager.getAllSnippets();
        if (snippets.isEmpty()) {
            System.out.println("No snippets found.");
            return;
        }
        for (int i = 0; i < snippets.size(); i++) {
            System.out.println("\n=== Snippet " + (i + 1) + " ===");
            System.out.println(snippets.get(i));
        }
    }

    private static void editSnippet() {
        List<CodeSnippet> snippets = manager.getAllSnippets();
        if (snippets.isEmpty()) {
            System.out.println("No snippets to edit.");
            return;
        }
        viewAllSnippets();
        int index = getIntInput("Enter snippet number to edit (1-" + snippets.size() + "): ") - 1;
        if (index < 0 || index >= snippets.size()) {
            System.out.println("Invalid snippet number.");
            return;
        }

        CodeSnippet oldSnippet = snippets.get(index);
        String title = getStringInput("Enter new title [" + oldSnippet.getTitle() + "]: ");
        if (title.isEmpty()) title = oldSnippet.getTitle();

        String description = getStringInput("Enter new description [" + oldSnippet.getDescription() + "]: ");
        if (description.isEmpty()) description = oldSnippet.getDescription();

        System.out.println("Enter new code (type 'END' on a new line to finish, or press Enter to keep existing):");
        String line = scanner.nextLine();
        String code = line.isEmpty() ? oldSnippet.getCode() : "";
        if (!line.isEmpty()) {
            StringBuilder codeBuilder = new StringBuilder(line + "\n");
            while (!(line = scanner.nextLine()).equals("END")) {
                codeBuilder.append(line).append("\n");
            }
            code = codeBuilder.toString();
        }

        String language = getStringInput("Enter new language [" + oldSnippet.getLanguage() + "]: ");
        if (language.isEmpty()) language = oldSnippet.getLanguage();

        System.out.println("Enter new tags (comma-separated) [" + String.join(", ", oldSnippet.getTags()) + "]:");
        String tagsInput = getStringInput("");
        Set<String> tags = tagsInput.isEmpty() ? oldSnippet.getTags() :
            new HashSet<>(List.of(tagsInput.split(","))).stream().map(String::trim).collect(Collectors.toSet());

        CodeSnippet newSnippet = new CodeSnippet(title, description, code, language, tags);
        manager.editSnippet(index, newSnippet);
        System.out.println("Snippet updated successfully!");
    }

    private static void deleteSnippet() {
        List<CodeSnippet> snippets = manager.getAllSnippets();
        if (snippets.isEmpty()) {
            System.out.println("No snippets to delete.");
            return;
        }
        viewAllSnippets();
        int index = getIntInput("Enter snippet number to delete (1-" + snippets.size() + "): ") - 1;
        if (index < 0 || index >= snippets.size()) {
            System.out.println("Invalid snippet number.");
            return;
        }
        manager.deleteSnippet(index);
        System.out.println("Snippet deleted successfully!");
    }

    private static void searchSnippet() {
        String query = getStringInput("Enter search query: ");
        List<CodeSnippet> results = manager.searchSnippets(query);
        if (results.isEmpty()) {
            System.out.println("No matching snippets found.");
            return;
        }
        System.out.println("\n=== Search Results ===");
        for (int i = 0; i < results.size(); i++) {
            System.out.println("\n=== Result " + (i + 1) + " ===");
            System.out.println(results.get(i));
        }
    }

    private static void sortSnippets() {
        System.out.println("\nSort by:");
        System.out.println("1. Title");
        System.out.println("2. Language");
        System.out.println("3. Creation Date");
        System.out.println("4. Last Modified");
        int choice = getIntInput("Enter your choice: ");

        List<CodeSnippet> snippets = manager.getAllSnippets();
        switch (choice) {
            case 1:
                snippets.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
                break;
            case 2:
                snippets.sort((a, b) -> a.getLanguage().compareToIgnoreCase(b.getLanguage()));
                break;
            case 3:
                snippets.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
                break;
            case 4:
                snippets.sort((a, b) -> a.getLastModified().compareTo(b.getLastModified()));
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        System.out.println("\n=== Sorted Snippets ===");
        for (int i = 0; i < snippets.size(); i++) {
            System.out.println("\n=== Snippet " + (i + 1) + " ===");
            System.out.println(snippets.get(i));
        }
    }

    private static void exportSnippet() {
        List<CodeSnippet> snippets = manager.getAllSnippets();
        if (snippets.isEmpty()) {
            System.out.println("No snippets to export.");
            return;
        }
        viewAllSnippets();
        int index = getIntInput("Enter snippet number to export (1-" + snippets.size() + "): ") - 1;
        if (index < 0 || index >= snippets.size()) {
            System.out.println("Invalid snippet number.");
            return;
        }

        CodeSnippet snippet = snippets.get(index);
        String filename = getStringInput("Enter filename to save (e.g., snippet.txt): ");
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(snippet.toString());
            System.out.println("Snippet exported successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error exporting snippet: " + e.getMessage());
        }
    }

    private static void viewRecentSnippets() {
        List<CodeSnippet> recentSnippets = manager.getRecentSnippets();
        if (recentSnippets.isEmpty()) {
            System.out.println("No recent snippets.");
            return;
        }
        System.out.println("\n=== Recent Snippets ===");
        for (int i = 0; i < recentSnippets.size(); i++) {
            System.out.println("\n=== Recent Snippet " + (i + 1) + " ===");
            System.out.println(recentSnippets.get(i));
        }
    }

    private static void viewStatistics() {
        Map<String, Integer> languageStats = manager.getLanguageStats();
        Map<String, Integer> tagStats = manager.getTagStats();

        System.out.println("\n=== Language Statistics ===");
        if (languageStats.isEmpty()) {
            System.out.println("No language statistics available.");
        } else {
            languageStats.forEach((lang, count) -> 
                System.out.printf("%s: %d snippet(s)\n", lang, count));
        }

        System.out.println("\n=== Tag Statistics ===");
        if (tagStats.isEmpty()) {
            System.out.println("No tag statistics available.");
        } else {
            tagStats.forEach((tag, count) -> 
                System.out.printf("%s: %d snippet(s)\n", tag, count));
        }
    }
} 