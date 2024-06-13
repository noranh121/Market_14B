package DomainLayer.backend;

public class Tree {
    private Node root;

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

}
