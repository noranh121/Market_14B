package org.market.DomainLayer.backend;

import java.util.Iterator;

public class Tree  implements Iterable<String> {
    private Node root;

    public Node getRoot() {
        return root;
    }

    public Tree(Permission rootData) {
        this.root = new Node(rootData);
    }

    public Node findNode(String userName) {
        return root.findNode(userName);
    }

    public boolean deleteNode(String userName) {
        if (root.getData().getUserName().equals(userName)) {
            root = null; // If root matches, delete the whole tree
            return true;
        }
        return root.deleteNode(userName);
    }

    public boolean isRoot(Node node) {
        return node.getParent() == null;
    }

    @Override
    public Iterator<String> iterator() {
        return new TreeIterator(root);
    }

    public Iterator<String> subtreeIterator(String userName) {
        Node subtreeRoot = findNode(userName);
        if (subtreeRoot != null) {
            return new TreeIterator(subtreeRoot);
        }
        return null;
    }

}
