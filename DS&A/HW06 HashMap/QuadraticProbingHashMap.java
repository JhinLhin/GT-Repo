import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;

/**
 * Your implementation of a QuadraticProbingHashMap.
 *
 * @author Jinlin Yang
 * @version 1.0
 */
public class QuadraticProbingHashMap<K, V> {

    /**
     * The initial capacity of the QuadraticProbingHashMap when created with the
     * default constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    /**
     * The max load factor of the QuadraticProbingHashMap
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    private static final double MAX_LOAD_FACTOR = 0.67;

    // Do not add new instance variables or modify existing ones.
    private QuadraticProbingMapEntry<K, V>[] table;
    private int size;

    /**
     * Constructs a new QuadraticProbingHashMap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     *
     * Use constructor chaining.
     */
    public QuadraticProbingHashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructs a new QuadraticProbingHashMap.
     *
     * The backing array should have an initial capacity of initialCapacity.
     *
     * You may assume initialCapacity will always be positive.
     *
     * @param initialCapacity the initial capacity of the backing array
     */
    //what is this?
    public QuadraticProbingHashMap(int initialCapacity) {
        table = new QuadraticProbingMapEntry[initialCapacity];
    }

    /**
     * Adds the given key-value pair to the map. If an entry in the map
     * already has this key, replace the entry's value with the new one
     * passed in.
     *
     * In the case of a collision, use quadratic probing as your resolution
     * strategy.
     *
     * Before actually adding any data to the HashMap, you should check to
     * see if the array would violate the max load factor if the data was
     * added. For example, let's say the array is of length 5 and the current
     * size is 3 (LF = 0.6). For this example, assume that no elements are
     * removed in between steps. If another entry is attempted to be added,
     * before doing anything else, you should check whether (3 + 1) / 5 = 0.8
     * is larger than the max LF. It is, so you would trigger a resize before
     * you even attempt to add the data or figure out if it's a duplicate. Be
     * careful to consider the differences between integer and double
     * division when calculating load factor.
     *
     * You must also resize when there are not any valid spots to add a
     * (key, value) pair in the HashMap after checking table.length spots.
     * There is more information regarding this case in the assignment PDF.
     *
     * When regrowing, resize the length of the backing table to
     * 2 * old length + 1. You must use the resizeBackingTable method to do so.
     *
     * Return null if the key was not already in the map. If it was in the map,
     * return the old value associated with it.
     *
     * @param key the key to add
     * @param value the value to add
     * @return null if the key was not already in the map. If it was in the
     * map, return the old value associated with it
     * @throws java.lang.IllegalArgumentException if key or value is null
     */

    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Cannot take in null!");
        }
        double loadFactor = (size + 1.0) / table.length;
        if (loadFactor > MAX_LOAD_FACTOR) {
            resizeBackingTable(table.length * 2 + 1);
        }
        int index = quadraticProbe(key);
        //if need to override it
        if (table[index] != null && !table[index].isRemoved()) {
            V oldVal = table[index].getValue();
            table[index].setValue(value);
            return oldVal;
        }
        //if it is null or del
        table[index] = new QuadraticProbingMapEntry<>(key, value);
        size++;
        return null;
    }

    /**Determine the inserting index
     *
     * @param key the key to probe
     * asda@param tableLength the current length of table
     * asd@param localTable the current table we are probing
     * @return inserting index we found
     */
    /*
    private int quadraticProbe(K key, int tableLength, QuadraticProbingMapEntry<K, V>[] localTable) {
        int index = Math.abs(key.hashCode() % tableLength);
        int probecount = 0;
        int probeindex = index;
        int h = 1;
        int delIndex = -1;

        while (probecount <= tableLength - 1 && localTable[probeindex] != null) {
            if (!localTable[probeindex].isRemoved() && localTable[probeindex].getKey().equals(key)) {
                return probeindex;
            }
            // Key is a del
            if (localTable[probeindex].isRemoved() && delIndex == -1) {
                delIndex = probeindex;
            }
            probecount++;
            probeindex = (h * h + index) % tableLength;
            h++;
        }
        if (probecount == tableLength && delIndex == -1 && localTable[probeindex] != null) {
            resizeBackingTable(2 * tableLength + 1);
            return quadraticProbe(key, table.length, table);
        }
        if (delIndex != -1) {
            return delIndex;
        }
        return probeindex;
    }
*/
    private int quadraticProbe(K key) {
        int index = Math.abs(key.hashCode() % table.length);
        int probecount = 0;
        int probeindex = index;
        int h = 1;
        int delIndex = -1;
        while (probecount <= table.length - 1 && table[probeindex] != null) {
            if (!table[probeindex].isRemoved() && table[probeindex].getKey().equals(key)) {
                return probeindex;
            }
            // Key is a del
            if (table[probeindex].isRemoved() && delIndex == -1) {
                delIndex = probeindex;
            }
            probecount++;
            probeindex = (h * h + index) % table.length;
            h++;
        }
        if (probecount == table.length && delIndex == -1 && table[probeindex] != null) {
            resizeBackingTable(2 * table.length + 1);
            return quadraticProbe(key);
        }
        if (delIndex != -1) {
            return delIndex;
        }
        return probeindex;
    }



    /**
     * Removes the entry with a matching key from map by marking the entry as
     * removed.
     *
     * @param key the key to remove
     * @return the value previously associated with the key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException if the key is not in the map
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null!");
        }
        //1. find the index
        int index = findindex(key);
        V oldVal = table[index].getValue();
        table[index].setRemoved(true); // that is how we remove?
        size--;
        return oldVal;

    }

    /**
     * find the index of the key
     *
     * @param key the key we are trying to find
     * @return  the index of the key
     */
    // case 1: find it
    // case 2: null or n times exception
    // case 3: hit del while probing exception
    private int findindex(K key) {
        //1. find the index

        int index = Math.abs(key.hashCode() % table.length);
        int probeindex = index;
        //2. check if key is valid index
        int probecount = 0;
        int h = 1;
        while (probecount < table.length && table[probeindex] != null) {
            if (!table[probeindex].isRemoved() && table[probeindex].getKey().equals(key)) {
                return probeindex;
            } else if (table[probeindex].getKey().equals(key) && table[probeindex].isRemoved()){
                break;
            }
            probeindex = Math.abs((h * h + index) % table.length);
            probecount++;
            h++;
        }
        //3. if the entry is null or entry is a del marker with matching key or probed n times still did not find the key
        throw new NoSuchElementException("This key does not exist!");

    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key the key to search for in the map
     * @return the value associated with the given key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException if the key is not in the map
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("the key does not exist!");
        }
        int index = findindex(key);
        return table[index].getValue();
    }

    /**
     * Returns whether or not the key is in the map.
     *
     * @param key the key to search for in the map
     * @return true if the key is contained within the map, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if key is null
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("the key does not exist!");
        }
        try {
            int index = findindex(key);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns a Set view of the keys contained in this map.
     *
     * Use java.util.HashSet.
     *
     * @return the set of keys in this map
     */
    public Set<K> keySet() {
        Set<K> key = new HashSet<K>();
        int counter = 0;
       for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isRemoved()) {
                if (counter == size) {
                    break;
                }
                key.add(table[i].getKey());
                counter++;
            }
        }
        return key;
    }

    /**
     * Returns a List view of the values contained in this map.
     *
     * Use java.util.ArrayList or java.util.LinkedList.
     *
     * You should iterate over the table in order of increasing index and add
     * entries to the List in the order in which they are traversed.
     *
     * @return list of values in this map
     */
    public List<V> values() {
        List<V> value = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isRemoved()) {
                if (counter == size) {
                    break;
                }
                value.add(table[i].getValue());
                counter++;
            }
        }
        return value;
    }

    /**
     * Resize the backing table to length.
     *
     * Disregard the load factor for this method. So, if the passed in length is
     * smaller than the current capacity, and this new length causes the table's
     * load factor to exceed MAX_LOAD_FACTOR, you should still resize the table
     * to the specified length and leave it at that capacity.
     *
     * Note: This method does not have to handle the case where the new length
     * results in collisions that cannot be resolved without resizing again. It
     * also does not have to handle the case where size = 0, and length = 0 is
     * passed into the function.
     *
     * You should iterate over the old table in order of increasing index and
     * add entries to the new table in the order in which they are traversed.
     *
     * Since resizing the backing table is working with the non-duplicate
     * data already in the table, you shouldn't explicitly check for
     * duplicates.
     *
     * Hint: You cannot just simply copy the entries over to the new array.
     *
     * @param length new length of the backing table
     * @throws java.lang.IllegalArgumentException if length is less than the
     * number of items in the hash map
     */
    public void resizeBackingTable(int length) {
        if (length < size) {
            throw new IllegalArgumentException("Cannot resize "
                    + "table to less than the current number of elements.");
        }
        //QuadraticProbingMapEntry<K, V>[] newTable = new QuadraticProbingMapEntry[length];
        QuadraticProbingMapEntry<K, V>[] oldTable = table;
        table = new QuadraticProbingMapEntry[length];
        int counter = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (counter == size) {
                break;
            }
            if (oldTable[i] != null && !oldTable[i].isRemoved()) {
                int index = quadraticProbe(oldTable[i].getKey());
                table[index] = oldTable[i];
                counter ++;
            }
        }
    }

    /**
     * Clears the map.
     *
     * Resets the table to a new array of the INITIAL_CAPACITY and resets the
     * size.
     *
     * Must be O(1).
     */
    public void clear() {
        table = new QuadraticProbingMapEntry[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the table of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the table of the map
     */
    public QuadraticProbingMapEntry<K, V>[] getTable() {
        // DO NOT MODIFY THIS METHOD!
        return table;
    }

    /**
     * Returns the size of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the map
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
