package bearmaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private ArrayList<HeapNode<T>> heap;
    private HashMap<T, Integer> itemIndexMap;

    private class HeapNode<T> {
        private T item;
        private double priority;

        HeapNode(T i, double p) {
            item = i;
            priority = p;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double p) {
            priority = p;
        }
    }

    public ArrayHeapMinPQ() {
        heap = new ArrayList<>();
        itemIndexMap = new HashMap<>();
    }


    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentException if item is already present.
     * You may assume that item is never null. */
    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Item already exists in the priority queue.");
        }
        heap.add(new HeapNode<>(item, priority));
        itemIndexMap.put(item, size() - 1);
        swimUp(size() - 1);
    }

    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item) {
        return itemIndexMap.containsKey(item);
    }

    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T getSmallest() {
        if (isEmpty()) {
            throw new NoSuchElementException("The priority queue is empty.");
        }
        return heap.get(0).getItem();
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T removeSmallest() {
        if (isEmpty()) {
            throw new NoSuchElementException("The priority queue is empty.");
        }
        T itemRemoved = getSmallest();
        heap.set(0, heap.get(size() - 1));
        itemIndexMap.put(heap.get(0).getItem(), 0);
        heap.remove(size() - 1);
        itemIndexMap.remove(itemRemoved);
        if (!isEmpty()) {
            sinkDown(0);
        }
        return itemRemoved;
    }

    /* Returns the number of items in the PQ. */
    @Override
    public int size() {
        return heap.size();
    }

    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("Item does not exist.");
        }
        int index = itemIndexMap.get(item);

        if (priority < heap.get(index).getPriority()) {
            heap.get(index).setPriority(priority);
            swimUp(index);
        } else {
            heap.get(index).setPriority(priority);
            sinkDown(index);
        }
    }

    private boolean isEmpty() {
        return size() == 0;
    }

    private void swap(int index1, int index2) {
        HeapNode<T> node1 = heap.get(index1);
        HeapNode<T> node2 = heap.get(index2);
        itemIndexMap.put(node1.getItem(), index2);
        itemIndexMap.put(node2.getItem(), index1);
        heap.set(index1, node2);
        heap.set(index2, node1);
    }

    private void swimUp(int index) {
        int parentIndex = parent(index);
        if (heap.get(index).getPriority() < heap.get(parentIndex).getPriority()) {
            swap(parentIndex, index);
            swimUp(parentIndex);
        }
    }

    private void sinkDown(int index) {
        int smallerChild = smallerChild(index);
        while (heap.get(index).getPriority() > heap.get(smallerChild).getPriority()) {
            swap(index, smallerChild);
            index = smallerChild;
            smallerChild = smallerChild(index);
        }
    }

    /* Recursive implementation of sinkDown(). This may cause StackOverflowError when calling too many times.
       int smallerChild = smallerChild(index);
       if (heap.get(index).getPriority() > heap.get(smallerChild).getPriority()) {
            swap(index, smallerChild);
          swimDown(smallerChild);
        }
    } */

    private int parent(int index) {
        if (index == 0) {
            return 0;
        }
        return (index - 1) / 2;
    }

    private int smallerChild(int index) {
        int leftChild = index * 2 + 1;
        leftChild = leftChild < size() ? leftChild : index;
        int rightChild = index * 2 + 2;
        rightChild = rightChild < size() ? rightChild : leftChild;
        return heap.get(leftChild).getPriority() < heap.get(rightChild).getPriority() ? leftChild : rightChild;
    }
}