public class Operation {
    private OperationType type;
    private Snippet snippet;
    private Snippet oldSnippet; // For edit operations

    public Operation(OperationType type, Snippet snippet) {
        this.type = type;
        this.snippet = snippet;
    }

    public Operation(OperationType type, Snippet snippet, Snippet oldSnippet) {
        this.type = type;
        this.snippet = snippet;
        this.oldSnippet = oldSnippet;
    }

    public OperationType getType() {
        return type;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public Snippet getOldSnippet() {
        return oldSnippet;
    }
} 