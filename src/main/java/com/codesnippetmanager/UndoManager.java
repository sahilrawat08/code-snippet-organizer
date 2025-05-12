package com.codesnippetmanager;

import java.util.Stack;

public class UndoManager {
    private Stack<Action> undoStack;
    private Stack<Action> redoStack;

    /**
     * Constructor for UndoManager
     * Time Complexity: O(1)
     * Space Complexity: O(1) - Initializes empty stacks
     */
    public UndoManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * Adds a new action to the undo stack
     * Time Complexity: O(1) - Stack push operation
     * Space Complexity: O(1) - Space for one action object
     */
    public void addAction(Action action) {
        undoStack.push(action);
        redoStack.clear(); // Clear redo stack when new action is performed
    }

    /**
     * Checks if undo operation is possible
     * Time Complexity: O(1) - Stack isEmpty check
     * Space Complexity: O(1) - No additional space needed
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Checks if redo operation is possible
     * Time Complexity: O(1) - Stack isEmpty check
     * Space Complexity: O(1) - No additional space needed
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * Performs undo operation
     * Time Complexity: O(1) - Stack pop and push operations
     * Space Complexity: O(1) - No additional space needed
     */
    public Action undo() {
        if (!canUndo()) {
            return null;
        }
        Action action = undoStack.pop();
        redoStack.push(action);
        return action;
    }

    /**
     * Performs redo operation
     * Time Complexity: O(1) - Stack pop and push operations
     * Space Complexity: O(1) - No additional space needed
     */
    public Action redo() {
        if (!canRedo()) {
            return null;
        }
        Action action = redoStack.pop();
        undoStack.push(action);
        return action;
    }

    // Inner class to represent an action
    public static class Action {
        private String type; // "ADD", "DELETE", "EDIT"
        private Snippet snippet;
        private Snippet oldSnippet; // For edit operations

        /**
         * Constructor for basic actions (ADD, DELETE)
         * Time Complexity: O(1)
         * Space Complexity: O(1) - Space for action object and references
         */
        public Action(String type, Snippet snippet) {
            this.type = type;
            this.snippet = snippet;
        }

        /**
         * Constructor for edit actions
         * Time Complexity: O(1)
         * Space Complexity: O(1) - Space for action object and references
         */
        public Action(String type, Snippet snippet, Snippet oldSnippet) {
            this.type = type;
            this.snippet = snippet;
            this.oldSnippet = oldSnippet;
        }

        public String getType() { return type; }
        public Snippet getSnippet() { return snippet; }
        public Snippet getOldSnippet() { return oldSnippet; }
    }
} 