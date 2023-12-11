/**
 * Node class.
 */

public class NodeType<T extends Comparable<T>> {
    private T item;
    private NodeType<T> leftChild;
    private NodeType<T> rightChild;
    private NodeType<T> parent;

    private int height = 1;

    /** Creates a node with the given element. */
    public NodeType(T t, NodeType<T> node) {
        item = t;
        parent = node;
    }

    // Accessor methods
    /** Returns the item stored at the node. */
    public T getItem() {
        return item;
    }

    /** Returns the height of the node. */
    public int getHeight() {
        return height;
    }

    /** Returns the left child node (or null if no such node). */
    public NodeType<T> getLeftChild() {
        return leftChild;
    }

    /** Returns the right child node (or null if no such node). */
    public NodeType<T> getRightChild() {
        return rightChild;
    }

    /** Returns the parent node (or null if no such node). */
    public NodeType<T> getParent() {
        return parent;
    }

    // Modifier methods

    /** Set the item of the node. */
    public void setItem(T t) {
        item = t;
    }

    /** Set the item of the node. */
    public void setHeight(int h) {
        height = h;
    }

    /** Set the left child node. */
    public void setLeftChild(NodeType<T> node) {
        leftChild = node;
    }

    /** Set the right child node. */
    public void setRightChild(NodeType<T> node) {
        rightChild = node;
    }

    /** Set the parent node. */
    public void setParent(NodeType<T> node) {
        parent = node;
    }
}
