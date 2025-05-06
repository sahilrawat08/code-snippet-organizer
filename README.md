# Code Snippet Organizer

A powerful command-line application for organizing and managing code snippets with advanced search capabilities and tag management.

## Features

- **Snippet Management**
  - Add, edit, and delete code snippets
  - Support for multiple programming languages
  - Tag-based organization
  - Version tracking for snippets

- **Advanced Search**
  - Search by title, content, language, or tags
  - Keyword-based search with suggestions
  - Date range search
  - Advanced search across all fields

- **Tag Management**
  - Add and remove tags
  - Tag-based filtering
  - Automatic tag suggestions

- **Data Management**
  - Import/Export functionality
  - Automatic backup system
  - Undo/Redo operations

## Requirements

- Java 8 or higher
- Maven (for building)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/code-snippet-organizer.git
cd code-snippet-organizer
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
java -jar target/code-snippet-organizer.jar
```

## Usage

### Adding a Snippet
1. Select option 1 from the main menu
2. Enter the snippet title
3. Specify the programming language
4. Enter your code (type 'END' on a new line to finish)
5. Add tags (optional)
6. Review and confirm

### Searching Snippets
1. Select option 2 from the main menu
2. Choose search type:
   - By title
   - By tag
   - By keyword
   - By language
   - By content
   - By date range
   - Advanced search
3. Enter your search query

### Editing a Snippet
1. Select option 3 from the main menu
2. Enter the title of the snippet to edit
3. Modify the desired fields
4. Review and confirm changes

### Managing Tags
- Add tags during snippet creation or editing
- Remove tags as needed
- Use tags for organization and quick access

## Project Structure

```
code-snippet-organizer/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── CodeSnippetOrganizer.java
│   │       ├── Snippet.java
│   │       ├── SnippetManager.java
│   │       ├── Operation.java
│   │       ├── OperationType.java
│   │       ├── Trie.java
│   │       └── TrieNode.java
│   └── test/
│       └── java/
├── pom.xml
├── README.md
└── .gitignore
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Thanks to all contributors who have helped shape this project
- Inspired by the need for better code snippet organization 