# Smart Code Snippet Organizer

A console-based application for managing and organizing code snippets with advanced features like undo/redo, search, and sorting capabilities.

## Features

- Add, edit, delete, and view code snippets
- Search snippets by title, tag, or programming language
- Sort snippets alphabetically or by date
- Undo/redo functionality for all operations
- Persistent storage of snippets
- Tag-based organization
- Multi-line code input support

## Data Structures Used

- HashMap: For efficient storage and retrieval of snippets
- Stack: For implementing undo/redo functionality
- TreeSet: For maintaining sorted collections
- LinkedList: For maintaining insertion order

## Time and Space Complexity Analysis

### Core Operations

1. **Add Snippet**
   - Time Complexity: O(1) - Constant time for HashMap insertion
   - Space Complexity: O(1) - Space for one snippet object

2. **Delete Snippet**
   - Time Complexity: O(1) - Constant time for HashMap removal
   - Space Complexity: O(1) - No additional space needed

3. **Edit Snippet**
   - Time Complexity: O(1) - Constant time for HashMap update
   - Space Complexity: O(1) - Space for temporary snippet object

4. **Search Operations**
   - Search by Title: O(n) - Linear scan through all snippets
   - Search by Tag: O(n) - Linear scan through all snippets
   - Search by Language: O(n) - Linear scan through all snippets
   - Space Complexity: O(k) - Where k is the number of matching results

5. **Sort Operations**
   - Sort by Title: O(n log n) - Using Collections.sort
   - Sort by Date: O(n log n) - Using Collections.sort
   - Space Complexity: O(n) - Additional space for temporary list

6. **Undo/Redo Operations**
   - Time Complexity: O(1) - Constant time for stack operations
   - Space Complexity: O(n) - Where n is the number of operations in history

7. **File Operations**
   - Save: O(n) - Where n is the total number of snippets
   - Load: O(n) - Where n is the total number of snippets
   - Space Complexity: O(n) - For storing all snippets in memory

### Overall Space Complexity
- Total Space: O(n + m) - Where n is the number of snippets and m is the number of operations in history

## How to Run

1. Make sure you have Java installed on your system
2. Compile all Java files:
   ```
   javac *.java
   ```
3. Run the application:
   ```
   java CodeSnippetManager
   ```

## Usage

1. **Add Snippet**
   - Enter a title for your code snippet
   - Enter the code (type 'END' on a new line to finish)
   - Specify the programming language

2. **View All Snippets**
   - Displays all saved snippets with their details

3. **Edit Snippet**
   - Enter the snippet ID
   - Modify title, code, or language
   - Press Enter to keep current values

4. **Delete Snippet**
   - Enter the snippet ID to delete

5. **Search Snippet**
   - Search by title, tag, or language
   - View matching results

6. **Sort Snippets**
   - Sort alphabetically by title
   - Sort by creation date

7. **Undo/Redo**
   - Undo last operation
   - Redo previously undone operation

## File Storage

Snippets are automatically saved to `snippets.txt` in the application directory. The file is updated after each operation.

## Requirements

- Java 8 or higher
- No external dependencies required

## Project Structure

- `CodeSnippetManager.java`: Main application class with user interface
- `SnippetManager.java`: Core functionality implementation
- `Snippet.java`: Snippet data model
- `UndoManager.java`: Undo/redo functionality 