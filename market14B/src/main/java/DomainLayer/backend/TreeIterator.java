package DomainLayer.backend;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TreeIterator implements Iterator<String>{
    private ConcurrentLinkedDeque<Node> stack;


    public TreeIterator(Node root) {
        stack =  new ConcurrentLinkedDeque<>();
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
