import java.util.List;

public class EditSnippetCommand implements Command {
    private List<CodeSnippet> snippets;
    private int index;
    private CodeSnippet oldSnippet;
    private CodeSnippet newSnippet;

    public EditSnippetCommand(List<CodeSnippet> snippets, int index, CodeSnippet oldSnippet, CodeSnippet newSnippet) {
        this.snippets = snippets;
        this.index = index;
        this.oldSnippet = oldSnippet;
        this.newSnippet = newSnippet;
    }

    @Override
    public void execute() {
        snippets.set(index, newSnippet);
    }

    @Override
    public void undo() {
        snippets.set(index, oldSnippet);
    }
} 