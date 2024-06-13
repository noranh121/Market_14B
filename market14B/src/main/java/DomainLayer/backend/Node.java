package DomainLayer.backend;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Permission data;
    private List<Node> children;
    private Node parent;

    public Node(Permission data) {
        this.data = data;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public Permission getData() {
        return data;
    }

    public Node getParent() {
        return this.parent;
    }

    public void addChild(Node child) {
        children.add(child);
        child.parent = this;
    }

    public Node findNode(String userName) {
        if (data.getUserName().equals(userName)) {
            return this;
        }
        for (Node child : children) {
            Node found = child.findNode(userName);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public boolean isChild(String userName) {
        for (Node node : children) {
            if (node.getData().getUserName() == userName) {
                return true;
            }
        }
        return false;
    }


    public void edit(Permission per) {
        this.data = per;
    }

    public boolean deleteNode(String userName) {
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            if (child.data.getUserName().equals(userName)) {
                children.remove(i);
                return true;
            }
        }
        for (Node child : children) {
            if (child.deleteNode(userName)) {
                return true;
            }
        }
        return false;
    }

}
