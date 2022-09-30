package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;
    HashMap<T, Integer> itemMap;

    public ArrayHeapMinPQ() {
        // ArrayList representation of heap data structure
        items = new ArrayList<>();
        items.add(null);
        // stores items for quick access
        itemMap = new HashMap<>();

    }
    /*
    public List<String> getList() {
        List<String> result = new ArrayList<>();
        for (int i = START_INDEX; i < items.size(); i++) {
            result.add((String) items.get(i).getItem());
        }
        return result;
    }
    */
    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.

    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        // swap indices in itemMap
        itemMap.put(items.get(a).getItem(), b);
        itemMap.put(items.get(b).getItem(), a);
        // swap node data
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }

    @Override
    public void add(T item, double priority) {
        // return if item already exists
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        // append item to end of heap
        items.add(new PriorityNode<>(item, priority));
        itemMap.put(item, items.size() - 1);
        // percolate up and add item to HashMap
        percolateUp(itemMap.get(item));
    }

    @Override
    public boolean contains(T item) {
        return itemMap.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (items.size() == START_INDEX) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    @Override
    public T removeMin() {
        // no items
        if (items.size() == START_INDEX) {
            throw new NoSuchElementException();
        }
        // only one item
        if (items.size() == 1 + START_INDEX) {
            itemMap.clear();
            return items.remove(START_INDEX).getItem();
        }
        // swap with last item
        swap(START_INDEX, items.size() - 1);
        T item = items.remove(items.size() - 1).getItem();
        // update itemMap and percolate down
        itemMap.remove(item);
        percolateDown(START_INDEX);
        return item;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        items.set(itemMap.get(item), new PriorityNode<>(item, priority));
        // retrieve indices
        percolateUp(itemMap.get(item));
        percolateDown(itemMap.get(item));
    }

    @Override
    public int size() {
        return items.size() - START_INDEX;
    }

    private void percolateUp(int currIndex) {
        if (currIndex == START_INDEX) {
            return;
        }
        int pareIndex = (currIndex - 1 + START_INDEX) / 2;
        double currPriority = items.get(currIndex).getPriority();
        double parePriority = items.get(pareIndex).getPriority();
        // swap w/ parent node if necessary
        while (currPriority < parePriority) {
            // swap nodes
            swap(currIndex, pareIndex);
            currIndex = pareIndex;
            if (currIndex == START_INDEX) {
                break;
            }
            pareIndex = (currIndex - 1 + START_INDEX) / 2;
            // update nodes
            currPriority = items.get(currIndex).getPriority();
            parePriority = items.get(pareIndex).getPriority();
        }
    }

    private void percolateDown(int currIndex) {
        // children indices
        int leftIndex = currIndex * 2 + 1 - START_INDEX;
        int rightIndex = currIndex * 2 + 2 - START_INDEX;
        int childIndex;
        double childPriority;
        double currPriority;
        // if both children exists
        if (rightIndex < items.size()) {
            if (items.get(leftIndex).getPriority() < items.get(rightIndex).getPriority()) {
                childPriority = items.get(leftIndex).getPriority();
                childIndex = leftIndex;
            } else {
                childPriority = items.get(rightIndex).getPriority();
                childIndex = rightIndex;
            }
        }
        // if left child exists
        else if (leftIndex < items.size()) {
            childPriority = items.get(leftIndex).getPriority();
            childIndex = leftIndex;
        }
        // node has no children
        else {
            return;
        }
        // node priorities
        currPriority = items.get(currIndex).getPriority();
        // if current node is greater than the smallest child
        if (childPriority < currPriority) {
            swap(currIndex, childIndex);
            percolateDown(childIndex);
        }
    }
}
