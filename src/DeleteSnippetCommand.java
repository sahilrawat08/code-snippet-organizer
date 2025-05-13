import java.util.List;

public class DeleteSnippetCommand implements Command {
    private List<CodeSnippet> snippets;
    private int index;
    private CodeSnippet snippet;

    public DeleteSnippetCommand(List<CodeSnippet> snippets, int index, CodeSnippet snippet) {
        this.snippets = snippets;
        this.index = index;
        this.snippet = snippet;
    }

    @Override
    public void execute() {
        snippets.remove(index);
    }

    @Override
    public void undo() {
        snippets.add(index, snippet);
    }
} 