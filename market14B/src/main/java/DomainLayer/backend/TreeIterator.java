package DomainLayer.backend;

import java.util.Iterator;
import java.util.Stack;

public class TreeIterator implements Iterator<String>{
    private Stack<Node> stack;

    public TreeIterator(Node root) {
        stack = new Stack<>();
        if (root != null) {
            stack.push(root);
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public String next() {
        Node currentNode = stack.pop();
        for (Node child : currentNode.getChildren()) {
            stack.push(child);
        }
        return currentNode.getData().getUserName();
    }

}
