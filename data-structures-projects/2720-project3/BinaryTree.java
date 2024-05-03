/**
 * Binary Search Tree class.
 */

public class BinaryTree<T extends Comparable<T>> {

    NodeType<T> root;
    boolean found = false;
    int leafCount = 0;
    NodeType<T> foundNode = null;

    public BinaryTree() {
        root = null;
    }

    public boolean isEmpty() {
        if(root == null) {
            return true;
        } else return false;
    }

    public T getMin() {
        if(isEmpty()) {
            return null;
        }
        return getMin(root);
    }

    T getMin(NodeType<T> node) {
        if (node.getLeftChild() != null) {
            return getMin(node.getLeftChild());
        }
        return node.getItem();
    }

    public NodeType<T> getMax() {
        if(isEmpty()) {
            return null;
        }
        return getMax(root);
    }

    NodeType<T> getMax(NodeType<T> node) {
        if (node.getRightChild() != null) {
            return getMax(node.getRightChild());
        }
        return node;
    }

    public void inOrder() {
        traverseInOrder(root);
        System.out.println("");
    }

    void traverseInOrder(NodeType<T> node) {
        if(node != null) {
            traverseInOrder(node.getLeftChild());
            System.out.print(node.getItem() + "  ");
            traverseInOrder(node.getRightChild());
        }
    }

    public void insert(T item) {
        root = insert(null, item, root);
    }

    private NodeType<T> insert(NodeType<T> parent, T item, NodeType<T> node) {
        if (node == null) {
            return new NodeType<T>(item, parent);
        }

        // item has less value than node
        if (item.compareTo(node.getItem()) < 0) {
            node.setLeftChild(insert(node, item, node.getLeftChild()));
        } else if (item.compareTo(node.getItem()) > 0) {   // item has more value than node
            node.setRightChild(insert(node, item, node.getRightChild()));
        } else if (item.compareTo(node.getItem()) == 0) {
            System.out.println("The item already exists in the tree.");
        }
        return node;
    }

    public void delete(T item) {
        if (search(item)) {
            delete(foundNode);
        }
    }

    private void delete(NodeType<T> node) {
        // Leaf Node
        if (node.getLeftChild() == null && node.getRightChild() == null) {
            if (node == root) {
                root = null;
            }
            if (node == node.getParent().getRightChild()) {
                node.getParent().setRightChild(null);
            } else {
                node.getParent().setLeftChild(null);
            }
        }
        if (node.getLeftChild() != null || node.getRightChild() != null) {
            //One Child
            if (node.getLeftChild() == null) {
                if (node == root) {
                    root = node.getRightChild();
                }
                node.getRightChild().setParent(node.getParent());
                if (node == node.getParent().getRightChild()) {
                    node.getParent().setRightChild(node.getRightChild());
                } else {
                    node.getParent().setLeftChild(node.getRightChild());
                }
            } else if (node.getRightChild() == null) {
                if (node == root) {
                    root = node.getLeftChild();
                }
                node.getLeftChild().setParent(node.getParent());
                if (node == node.getParent().getRightChild()) {
                    node.getParent().setRightChild(node.getLeftChild());
                } else {
                    node.getParent().setLeftChild(node.getLeftChild());
                }
            }
        }
        if (node.getLeftChild() != null && node.getRightChild() != null) {
            // Two Children (using predecessor)
            node.setItem(getMax(node.getLeftChild()).getItem());
            delete(getMax(node.getLeftChild()));
        }
    }

    public boolean search(T item) {
        found = false;
        foundNode = null;
        search(root, item);
        if (!found) {
            System.out.println("The item is not present in the tree.");
        }
        return found;
    }

    private void search(NodeType<T> node, T item) {
        if (node != null) {
            if (item.compareTo(node.getItem()) < 0) {
                search(node.getLeftChild(), item);
            } else if (item.compareTo(node.getItem()) > 0) {
                search(node.getRightChild(), item);
            } else if (item.compareTo(node.getItem()) == 0) {
                System.out.println("The item is present in the tree.");
                found = true;
                foundNode = node;
            }
        }
    }

    public void getSingleParents() {
        System.out.print("Single Parents: ");
        printSingleParents(root);
        System.out.println("");
    }

    private void printSingleParents(NodeType<T> node) {
        if (node.getLeftChild() == null) {
            // checking right subtree
            if (node.getRightChild() != null) {
                System.out.print(node.getItem() + "  ");
                printSingleParents(node.getRightChild());
            }
        } else {
            // checking left subtree
            printSingleParents(node.getLeftChild());
            if (node.getRightChild() == null) {
                System.out.print(node.getItem() + "  ");
            } else {
                printSingleParents(node.getRightChild());
            }
        }
    }

    public void getNumLeafNodes() {
        leafCount = 0;
        countLeafNodes(root);
        System.out.println("The number of leaf nodes are " + leafCount);
    }

    private void countLeafNodes(NodeType<T> node) {
        if (node.getLeftChild() == null && node.getRightChild() == null) {
            leafCount++;
        } else {
            if (node.getLeftChild() != null) {
                countLeafNodes(node.getLeftChild());
            }
            if (node.getRightChild() != null) {
                countLeafNodes(node.getRightChild());
            }
        }
    }

    public void getCousins(T item) {
        NodeType<T> cousin1 = null;
        NodeType<T> cousin2 = null;

        if (search(item)) {
            System.out.print("cousins of " + item + ": ");
            if (foundNode != root && foundNode.getParent().getParent() != null) {
                NodeType<T> grandparent = foundNode.getParent().getParent();

                // finds granparent than finds children of grandparent's
                // other child that is not nodes parent
                if (foundNode.getParent() == grandparent.getRightChild()) {
                    cousin1 = grandparent.getLeftChild().getLeftChild();
                    cousin2 = grandparent.getLeftChild().getRightChild();
                } else {
                    cousin1 = grandparent.getRightChild().getLeftChild();
                    cousin2 = grandparent.getRightChild().getRightChild();
                }
                if (cousin1 != null) {
                    System.out.print(cousin1.getItem() + "  ");
                }
                if (cousin2 != null) {
                    System.out.print(cousin2.getItem() + "  ");
                }
            }
            System.out.println("");
        }
    }
}
