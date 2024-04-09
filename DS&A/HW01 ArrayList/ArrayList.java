import java.util.NoSuchElementException;

/**
 * Your implementation of an ArrayList.
 *
 * @author Jinlin Yang
 * @version 1.0
 */
public class ArrayList<T> {

    /**
     * The initial capacity of the ArrayList.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 9;

    // Do not add new instance variables or modify existing ones.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new ArrayList.
     *
     * Java does not allow for regular generic array creation, so you will have
     * to cast an Object[] to a T[] to get the generic typing.
     */

    public ArrayList() {
        this.backingArray = (T[]) new Object[INITIAL_CAPACITY];
    }

    /**
     * Adds the element to the specified index.
     *
     * Remember that this add may require elements to be shifted.
     *
     * Must be amortized O(1) for index size and O(n) for all other cases.
     *
     * @param index the index at which to add the new element
     * @param data  the data to add at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index > size
     * @throws java.lang.IllegalArgumentException  if data is null
     */

    //just need to memorize this method only and take in 0 or size - 1 for other 2 cases
    public void addAtIndex(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index is outside of the acceptable range!");
        }
        if (data == null) {
            throw new IllegalArgumentException("Cannot insert null data into data structure.");
        }
        if (index == size) {
            addToBack(data);
        } else if (index == 0) {
            addToFront(data);
        } else {
            if (size == backingArray.length) {
                T[] temp = (T[]) new Object[size * 2];

                //this loop cover index 0 until index -1 into temp
                for (int i = 0; i <= index - 1; i++) {
                    temp[i] = backingArray[i];
                }
                temp[index] = data;
                //copy the rest of original element into temp, starting at index!
                //but since temp already has new added element at index position
                //we use index + 1 for temp
                //j will still traverse the rest of the original loop starting at index
                for (int j = index; j <= size - 1; j++) {
                    temp[j+1] = backingArray[j];
                }
                backingArray = temp;
                size++;
            } else {
                //How we shift?
                //We shift everything after adding index 1 position backward to the right
                //we start at the last element of the backingArray and shift it to the right
                //then we go from right to left to shift everything
                //do not go from left to right because it will copy the same element
                for (int i = size - 1; i >= index; i--) {
                    backingArray[i + 1] = backingArray[i];
                }
                backingArray[index] = data;
                size++;
            }
        }
    }

    /**
     * Adds the element to the front of the list.
     *
     * Remember that this add may require elements to be shifted.
     *
     * Must be O(n).
     *
     * @param data the data to add to the front of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */


    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot insert null data into data structure.");
        } else {
            if (size == backingArray.length) {
                T[] temp = (T[]) new Object[size * 2];
                temp[0] = data;
                for (int i = 0; i <= size - 1; i++) {
                    temp[i + 1] = backingArray[i];
                }
                backingArray = temp;
                size++;
            } else {
                for (int i = size - 1; i >= 0; i--) {
                    backingArray[i + 1] = backingArray[i];
                }
                backingArray[0] = data;
                size++;
            }

        }
    }




    /**
     * Adds the element to the back of the list.
     *
     * Must be amortized O(1).
     *
     * @param data the data to add to the back of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */

    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot insert null data into data structure.");
        }
        if (size == backingArray.length) {
            T[] temp = (T[]) new Object[size * 2];
            for (int i = 0; i <= size - 1; i++) {
                temp[i] = backingArray[i];
            }
            temp[size] = data;
            backingArray = temp;
            size++;
        } else {
            backingArray[size] = data;
            size++;
        }
    }

    /**
     * Removes and returns the element at the specified index.
     *
     * Remember that this remove may require elements to be shifted.
     *
     * Must be O(1) for index size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to remove
     * @return the data formerly located at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */


    //just need to memorize this method only and take in 0 or size - 1 for other 2 cases
    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is outside of the acceptable range!");
        }
        if (index == 0) {
            return removeFromFront();
            
        } else if (index == (size - 1)) {
            return removeFromBack();
        }
        T removedElement = backingArray[index];
        for (int i = index + 1; i <= size - 1; i++) {
            backingArray[i - 1] = backingArray[i];
        }
        backingArray[size - 1] = null;
        size--;
        return removedElement;
    }





    /**
     * Removes and returns the first element of the list.
     *
     * Remember that this remove may require elements to be shifted.
     *
     * Must be O(n).
     *
     * @return the data formerly located at the front of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */

    public T removeFromFront() {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is currently empty!");
        }
        T removedElement = backingArray[0];
        for (int i = 1; i <= size - 1; i++) {
            backingArray[i - 1] = backingArray[i];
        }
        backingArray[size - 1] = null;
        size--;
        return removedElement;
    }

    /**
     * Removes and returns the last element of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the back of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */

    public T removeFromBack() {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is currently empty!");
        }
        T removedElement = backingArray[size - 1];
        backingArray[size - 1] = null;
        size--;
        return removedElement;
    }

    /**
     * Returns the element at the specified index.
     *
     * Must be O(1).
     *
     * @param index the index of the element to get
     * @return the data stored at the index in the list
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is outside of the acceptable range!");
        }
        return backingArray[index];
    }

    /**
     * Returns whether or not the list is empty.
     *
     * Must be O(1).
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Clears the list.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     *
     * Must be O(1).
     */

    public void clear() {
        this.backingArray = (T[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the backing array of the list.
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
     * Returns the size of the list.
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
