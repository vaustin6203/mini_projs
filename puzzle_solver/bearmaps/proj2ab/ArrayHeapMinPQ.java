package bearmaps.proj2ab;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.HashMap;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private ArrayList<PQNode> arr;
    private HashMap<T, PQNode>  hash;
    private int size;
    private PQNode first;

    private class PQNode {
        T item;
        double priority;
        int index;

        private PQNode(T t, double p, int i) {
            item = t;
            priority = p;
            index = i;
        }
    }

    /** ArrayHeapMinPQ constructor. */
    public ArrayHeapMinPQ() {
        arr = new ArrayList<>();
        hash = new HashMap<>();
        size = 0;
        first = null;
    }

    /** Returns a given node's left child node. If the given node does
     * not have a left child node, returns null.
     *
     * @param node
     * @return left child node
     *
     * @source
     * https://cs.stackexchange.com/questions/87154/why-does-the-formula-
     * 2n-1-find-the-child-node-in-a-binary-heap
     * Used source to find the index of a node's left child.
     */
    private PQNode leftChild(PQNode node) {
        int child = (node.index * 2) + 1;
        if (child >= size) {
            return null;
        }
        return arr.get(child);
    }

    /** Returns a given node's right child node. If the given node does
     * not have a right child node, returns null.
     *
     * @param node
     * @return right child node
     *
     * @source
     * https://cs.stackexchange.com/questions/87154/why-does-the-formula-
     * 2n-1-find-the-child-node-in-a-binary-heap
     * Used source to find the index of a node's right child.
     */
    private PQNode rightChild(PQNode node) {
        int child = (node.index * 2) + 2;
        if (child >= size) {
            return null;
        }
        return arr.get(child);
    }

    /** Returns the parent node of the given node. If the given node is in
     * the first position of this ArrayHeapMinPQ, returns null.
     *
     * @param node
     * @return parent node
     */
    private PQNode parent(PQNode node) {
        if (node.index == 0) {
            return null;
        }
        return arr.get((node.index - 1) / 2);
    }

    /** Swaps the positions of the two given nodes in this ArrayHeapMinPQ.
     *
     * @param a
     * @param b
     */
    private PQNode swap(PQNode a, PQNode b) {
        int aI = a.index;
        int bI = b.index;
        a = new PQNode(a.item, a.priority, bI);
        b = new PQNode(b.item, b.priority, aI);
        if (aI == 0) {
            first = b;
        } else if (bI == 0) {
            first = a;
        }
        arr.set(aI, b);
        arr.set(bI, a);
        hash.replace(a.item, a);
        hash.replace(b.item, b);
        return a;
    }

    /** Moves a given node's position down in this ArrayHeapMinPQ until both
     * it's child node's priority is greater than or equal to its priority.
     * When choosing which child node to swap positions with, if both child
     * nodes have equal priority, the given node will arbitrarily swap with
     * the right.
     *
     * @param node
     */
    private void sink(PQNode node) {
        PQNode right = rightChild(node);
        PQNode left = leftChild(node);
        if (right == null && left != null) {
            if (node.priority > left.priority) {
                PQNode update = swap(node, left);
                sink(update);
            }
        } else if (left == null && right != null) {
            if (node.priority > right.priority) {
                PQNode update = swap(node, right);
                sink(update);
            }
        } else if (right != null && left != null) {
            double rDiff = node.priority - right.priority;
            double lDiff = node.priority - left.priority;
            if (rDiff >= lDiff && rDiff > 0) {
                PQNode update = swap(node, right);
                sink(update);
            } else if (lDiff >= rDiff && lDiff > 0) {
                PQNode update = swap(node, left);
                sink(update);
            }
        }
    }

    /** Moves a given node's position up in this ArrayHeapMinPQ until
     * its parent node's priority is less than or equal to its priority.
     * If the given node has the lowest priority in this ArrayHeapMinPQ,
     * the node will be set to the first position.
     *
     * @param node
     */
    private void swim(PQNode node) {
        PQNode parent = parent(node);
        if (parent == null) {
            first = node;
        } else if (node.priority < parent.priority) {
            PQNode update = swap(node, parent);
            swim(update);
        }
    }

    /** Returns the number of items in this ArrayHeapMinPQ.
     *
     * @return size
     */
    @Override
    public int size() {
        return size;
    }

    /** Returns a boolean indicating whether a given item is in
     * this ArrayHeapMinPQ.
     *
     * @param item
     * @return boolean
     */
    @Override
    public boolean contains(T item) {
        return hash.containsKey(item);
    }

    /** Adds an item with a given priority into this ArrayHeapMinPQ.
     * If the item is already present, throws an IllegalArgumentException.
     *
     * @param item
     * @param priority
     */
    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Item is already in this ArrayHeapMinPQ");
        }
        PQNode node = new PQNode(item, priority, size);
        arr.add(size, node);
        hash.put(item, node);
        size += 1;
        swim(node);
    }

    /** Returns the lowest priority item and throws NoSuchElementException if
     * the ArrayHeapMinPQ is empty.
     *
     * @return smallest item
     */
    @Override
    public T getSmallest() {
        if (size == 0) {
            throw new NoSuchElementException("This ArrayHeapMinPQ is empty");
        }
        return first.item;
    }

    /** Removes and returns the lowest priority item. If the ArrayHeapMinPQ is
     * empty, throws NoSuchElementException.
     *
     * @return smallest item
     */
    @Override
    public T removeSmallest() {
        if (size == 0) {
            throw new NoSuchElementException("This ArrayHeapMinPQ is empty");
        }
        T min = swap(first, arr.get(size - 1)).item;
        hash.remove(min);
        arr.remove(size - 1);
        size -= 1;
        sink(first);
        return min;
    }

    /** Changes the priority of the given item. Throws NoSuchElementException if
     * the item doesn't exist.
     *
     * @param item
     * @param priority
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("Item is not in this ArrayHeapMinPQ");
        }
        PQNode oldN = hash.get(item);
        double diff = oldN.priority - priority;
        PQNode newN = new PQNode(item, priority, oldN.index);
        hash.replace(item, newN);
        arr.set(oldN.index, newN);
        if (newN.index == 0) {
            first = newN;
        }
        if (diff > 0) {
            swim(newN);
        } else if (diff < 0) {
            sink(newN);
        }
    }
}

