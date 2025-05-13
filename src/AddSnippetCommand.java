import java.util.List;

public class AddSnippetCommand implements Command {
    private List<CodeSnippet> snippets;
    private CodeSnippet snippet;

    public AddSnippetCommand(List<CodeSnippet> snippets, CodeSnippet snippet) {
        this.snippets = snippets;
        this.snippet = snippet;
    }

    @Override
    public void execute() {
        snippets.add(snippet);
    }

    @Override
    public void undo() {
        snippets.remove(snippet);
    }
} 