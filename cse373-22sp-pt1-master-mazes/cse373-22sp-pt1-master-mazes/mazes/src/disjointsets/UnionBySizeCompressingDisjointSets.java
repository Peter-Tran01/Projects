package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A quick-union-by-size data structure with path compression.
 *
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    Map<T, Integer> items; // Item, Index
    int size = 0;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        items = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        pointers.add(-1);
        items.put(item, size);
        size++;
    }

    @Override
    public int findSet(T item) {
        if (items.containsKey(item)) {
            int newParent = pointers.get(items.get(item));
            int newParentIndex = items.get(item);
            // path compress if needed
            if (pointers.get(items.get(item)) >= 0) {
                while (newParent >= 0) {
                    // Finding the index of item1's root
                    if (pointers.get(newParent) < 0) {
                        newParentIndex = newParent;
                    }
                    newParent = pointers.get(newParent);
                }
                pointers.set(items.get(item), newParentIndex);
            }
            return newParentIndex;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean union(T item1, T item2) {
        if (items.containsKey(item1) && items.containsKey(item2)) {
            int set1 = findSet(item1);
            int set2 = findSet(item2);
            int item1RootValue = pointers.get(items.get(item1));
            int item2RootValue = pointers.get(items.get(item2));
            int item1RootIndex = items.get(item1);
            int item2RootIndex = items.get(item2);
            if (set1 == set2) {
                return false;
            }
            // Finding the size of item1's set
            while (item1RootValue >= 0) {
                // Finding the index of item1's root
                if (pointers.get(item1RootValue) < 0) {
                    item1RootIndex = item1RootValue;
                }
                item1RootValue = pointers.get(item1RootValue);
            }
            // Finding the size of item2's set
            while (item2RootValue >= 0) {
                // Finding the index of item2's root
                if (pointers.get(item2RootValue) < 0) {
                    item2RootIndex = item2RootValue;
                }
                item2RootValue = pointers.get(item2RootValue);
            }
            // Updating the set of item2's set and the size
            if (item1RootValue <= item2RootValue) {
                // Update size
                pointers.set(item1RootIndex, item1RootValue + item2RootValue);
                // Update parent node
                pointers.set(item2RootIndex, item1RootIndex);
                // Updating the set of item1's set and the size
            } else {
                // Update size
                pointers.set(item2RootIndex, item1RootValue + item2RootValue);
                // Update parent node
                pointers.set(item1RootIndex, item2RootIndex);
            }
            return true;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
