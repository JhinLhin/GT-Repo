import java.util.NoSuchElementException;

/**
 * Your implementation of a non-circular DoublyLinkedList with a tail pointer.
 *
 * @author Jinlin Yang
 */
public class DoublyLinkedList<T> {

    // Do not add new instance variables or modify existing ones.
    private DoublyLinkedListNode<T> head;
    private DoublyLinkedListNode<T> tail;
    private int size;

    // Do not add a constructor.

    /**
     * Adds the element to the specified index. Don't forget to consider whether
     * traversing the list from the head or tail is more efficient!
     *
     * Must be O(1) for indices 0 and size and O(n) for all other cases.
     *
     * @param index the index at which to add the new element
     * @param data  the data to add at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index > size
     * @throws java.lang.IllegalArgumentException  if data is null
     */
    public void addAtIndex(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index is outside of the acceptable range!");
        }
        if (data == null) {
            throw new IllegalArgumentException("Cannot insert null data into data structure.");
        }
        if (size == 0) {
            addToFront(data);
        } else if (index == 0) {
            addToFront(data);
        } else if (size == index) {
            addToBack(data);
        } else {
            DoublyLinkedListNode<T> newNode = new DoublyLinkedListNode<>(data);
            if (index <= (size / 2)) {
                DoublyLinkedListNode<T> current = head;
                for (int i = 0; i <= index - 2; i++) { // stop at 1 position before the index
                    current = current.getNext();
                }
                newNode.setPrevious(current);
                newNode.setNext(current.getNext());
                current.getNext().setPrevious(newNode);
                current.setNext(newNode);
                size++;
            } else {
                DoublyLinkedListNode<T> current = tail;
                for (int i = (size - 1); i > index; i--) { // stop at the index
                    current = current.getPrevious();
                }
                newNode.setPrevious(current.getPrevious());
                newNode.setNext(current);
                current.getPrevious().setNext(newNode);
                current.setPrevious(newNode);
                size++;
            }
        }
    }

    /**
     * Adds the element to the front of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the front of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data added cannot be null!");
        }
        DoublyLinkedListNode<T> newNode = new DoublyLinkedListNode<>(data);
        if (size == 0) {
            head = newNode;
            tail = newNode;
        } else {
            head.setPrevious(newNode);
            newNode.setNext(head);
            head = newNode;
        }
        size++;
    }

    /**
     * Adds the element to the back of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the back of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data added cannot be null!");
        }
        DoublyLinkedListNode<T> newNode = new DoublyLinkedListNode<>(data);
        if (size == 0) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
            tail = newNode;
        }
        size++;

    }

    /**
     * Removes and returns the element at the specified index. Don't forget to
     * consider whether traversing the list from the head or tail is more
     * efficient!
     *
     * Must be O(1) for indices 0 and size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to remove
     * @return the data formerly located at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is outside of the acceptable range!");
        }
        if (index == 0) {
            return removeFromFront();
        }
        if (index == (size - 1)) {
            return removeFromBack();
        }
        DoublyLinkedListNode<T> current;
        if (index <= (size / 2)) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrevious();
            }
        }
        DoublyLinkedListNode<T> removedNode = current;
        current.getPrevious().setNext(current.getNext());
        current.getNext().setPrevious(current.getPrevious());
        size--;
        return removedNode.getData();


    }

    /**
     * Removes and returns the first element of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the front of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromFront() {
        if (isEmpty()) {
            throw new NoSuchElementException("There is no element in the list!");
        }
        DoublyLinkedListNode<T> removedNode = head;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.getNext();
            head.setPrevious(null);
        }
        size--;
        return removedNode.getData();
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
            throw new NoSuchElementException("There is no element in the list!");
        }
        DoublyLinkedListNode<T> removedNode = tail;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.getPrevious();
            tail.setNext(null);
        }
        size--;
        return removedNode.getData();
    }

    /**
     * Returns the element at the specified index. Don't forget to consider
     * whether traversing the list from the head or tail is more efficient!
     *
     * Must be O(1) for indices 0 and size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to get
     * @return the data stored at the index in the list
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is outside of the acceptable range!");
        }
        if (index == 0) {
            return (T) head.getData();
        }
        if (index == (size - 1)) {
            return (T) tail.getData();
        }
        DoublyLinkedListNode<T> current;
        if (index <= (size / 2)) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrevious();
            }
        }
        return current.getData();
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
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        size = 0;
        head = null;
        tail = null;
    }

    /**
     * Removes and returns the last copy of the given data from the list.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the list.
     *
     * Must be O(1) if data is in the tail and O(n) for all other cases.
     *
     * @param data the data to be removed from the list
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if data is not found
     */
    public T removeLastOccurrence(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data intended for removal cannot be null");
        }
        if (isEmpty()) {
            throw new NoSuchElementException("There is no element in the list!");
        }
        DoublyLinkedListNode<T> current = tail;
        while (current != null && !current.getData().equals(data)) {
            current = current.getPrevious();
        }
        if (current == null) {
            throw new NoSuchElementException("The element is not found in the list");
        }
        DoublyLinkedListNode<T> removedNode = current;
        if (current == head) {
            return removeFromFront();
        } else if (current == tail) {
            return removeFromBack();
        }
        current.getPrevious().setNext(current.getNext());
        current.getNext().setPrevious(current.getPrevious());
        size--;
        return removedNode.getData();

    }

    /**
     * Returns an array representation of the linked list. If the list is
     * size 0, return an empty array.
     *
     * Must be O(n) for all cases.
     *
     * @return an array of length size holding all of the objects in the
     * list in the same order
     */
    public Object[] toArray() {
        if (isEmpty()) {
            return new Object[0];
        }
        DoublyLinkedListNode<T> current = head;
        Object[] arr = new Object[size()];
        int i = 0;
        while (current != null) {
            arr[i] = current.getData();
            current = current.getNext();
            i++;
        }
        return arr;
    }

    /**
     * Returns the head node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the head of the list
     */
    public DoublyLinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    /**
     * Returns the tail node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the tail of the list
     */
    public DoublyLinkedListNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
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
        // DO NOT MODIFY!
        return size;
    }
}
