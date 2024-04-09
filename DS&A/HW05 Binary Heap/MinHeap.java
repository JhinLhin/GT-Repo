import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a MinHeap.
 *
 * @author Jinlin Yang
 * @version 1.0
 */
public class MinHeap<T extends Comparable<? super T>> {

    /**
     * The initial capacity of the MinHeap when created with the default
     * constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    // Do not add new instance variables or modify existing ones.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new MinHeap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     * To initialize the backing array, create a Comparable array and then cast
     * it to a T array.
     */
    public MinHeap() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
    }

    /**
     * Creates a properly ordered heap from a set of initial values.
     *
     * You must use the BuildHeap algorithm that was taught in lecture! Simply
     * adding the data one by one using the add method will not get any credit.
     * As a reminder, this is the algorithm that involves building the heap
     * from the bottom up by repeated use of downHeap operations.
     *
     * Before doing the algorithm, first copy over the data from the
     * ArrayList to the backingArray (leaving index 0 of the backingArray
     * empty). The data in the backingArray should be in the same order as it
     * appears in the passed in ArrayList before you start the BuildHeap
     * algorithm.
     *
     * The backingArray should have capacity 2n + 1 where n is the
     * size of the passed in ArrayList (not INITIAL_CAPACITY). Index 0 should
     * remain empty, indices 1 to n should contain the data in proper order, and
     * the rest of the indices should be empty.
     *
     * @param data a list of data to initialize the heap with
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */

    public MinHeap(ArrayList<T> data) {
        if (data == null) {
            throw  new IllegalArgumentException("The arraylist cannot be null!");
        }

        backingArray = (T[]) new Comparable[data.size() * 2 + 1];
        for (int i = 0; i <= data.size() - 1; i++) {
            if (data.get(i) == null) {
                throw new IllegalArgumentException("cannot have null data");
            }
            backingArray[i + 1] = data.get(i);
            size++;
        }
        for (int i = size / 2; i >= 1; i--) {
            down(i);
        }

    }

    /**
     * down heap method
     *
     * @param parent node that needs to be down heaped
     */
    private void down(int parent) {
        int left = parent * 2;
        int right = parent * 2 + 1;
        int min = parent;
        //valid index
        //to find minimum of the two/ use size do not use array.length to avoid null pointer
        if (left <= size  && backingArray[left].compareTo(backingArray[min]) < 0) {
            min = left;
        }
        if (right <= size && backingArray[right].compareTo(backingArray[min]) < 0) {
            min = right;
        }
        //if equal to parent then either no kids or order is correct
        //if not equal then found a smaller kid
        //min is the index
        if (min != parent) {
            swap(min, parent);
            //new index of parent after swap
            down(min);
        }

    }

    /**
     * swap nodes position method
     *
     * @param i index of the first node
     * @param j index of the second node
     */
    private void swap(int i, int j) {
        T temp = backingArray[i];
        backingArray[i] = backingArray[j];
        backingArray[j] = temp;
    }

    /**
     * Adds an item to the heap. If the backing array is full (except for
     * index 0) and you're trying to add a new item, then double its capacity.
     * The order property of the heap must be maintained after adding. You can
     * assume that no duplicate data will be passed in.
     * 
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add null!");
        } //leaving the index 0 empty

        //resize case, remember size gives you the index of last element
        if (size + 1 == (backingArray.length)) {
            T[] temp = (T[]) new Comparable[backingArray.length * 2];
            //size gives you last element index
            for (int i = 1; i <= size; i++) {
                temp[i] = backingArray[i];
            }
            backingArray = temp;
        }


        int child = size + 1;
        int parent = (child) / 2;

        while ((child > 1) && data.compareTo(backingArray[parent]) < 0) {
            //only down heap each parent
            backingArray[child] = backingArray[parent];
            child = parent;
            parent = child / 2;

        }
       backingArray[child] = data;
        size++;

    }


    /**
     * Removes and returns the min item of the heap. As usual for array-backed
     * structures, be sure to null out spots as you remove. Do not decrease the
     * capacity of the backing array.
     * The order property of the heap must be maintained after removing.
     *
     * @return the data that was removed
     * @throws java.util.NoSuchElementException if the heap is empty
     */

    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty and cannot remove!");
        }

        //swap and remove root
        T removedElement = backingArray[1];
        swap(1, size);
        backingArray[size] = null;
        size--;
        // down heap
        down(1);
        return removedElement;

    }




    /**
     * Returns the minimum element in the heap.
     *
     * @return the minimum element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T getMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("back array is empty!");
        }
        return backingArray[1];
    }

    /**
     * Returns whether or not the heap is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the heap.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     */
    public void clear() {
        this.backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the backing array of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the backing array of the list
     */
    public T[] getBackingArray() {
        // DO NOT MODIFY THIS METHOD!
        return backingArray;
    }

    /**
     * Returns the size of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
