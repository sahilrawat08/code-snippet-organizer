import java.util.ArrayList;
import java.util.List;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toLowerCase().toCharArray()) {
            current.getChildren().putIfAbsent(c, new TrieNode());
            current = current.getChildren().get(c);
        }
        current.setEndOfWord(true);
        current.setWord(word);
    }

    public List<String> searchPrefix(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode node = findNode(prefix.toLowerCase());
        if (node != null) {
            findAllWords(node, results);
        }
        return results;
    }

    private TrieNode findNode(String prefix) {
        TrieNode current = root;
        for (char c : prefix.toCharArray()) {
            if (!current.getChildren().containsKey(c)) {
                return null;
            }
            current = current.getChildren().get(c);
        }
        return current;
    }

    private void findAllWords(TrieNode node, List<String> results) {
        if (node.isEndOfWord()) {
            results.add(node.getWord());
        }
        for (TrieNode child : node.getChildren().values()) {
            findAllWords(child, results);
        }
    }

    public boolean contains(String word) {
        TrieNode node = findNode(word.toLowerCase());
        return node != null && node.isEndOfWord();
    }
} 