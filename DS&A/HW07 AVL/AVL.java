import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL.
 *
 * @author Jinlin Yang
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private AVLNode<T> root;
    private int size;

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * updates a node's height and balance factor
     *
     * @param curr takes in a node
     */
    private void update(AVLNode<T> curr) {
        int left;
        int right;
        if (curr.getLeft() == null) {
            left = -1;
        } else {
            left = curr.getLeft().getHeight();
        }
        if (curr.getRight() == null) {
            right = -1;
        } else {
            right = curr.getRight().getHeight();
        }
        curr.setHeight(Math.max(left, right) + 1);
        curr.setBalanceFactor(left - right);
    }

    /**
     * single right rotation on a node
     *
     * @param curr currently unbalanced node
     * @return  return the new root of the subtree
     */
    private AVLNode<T> rightRotate(AVLNode<T> curr) {
        AVLNode<T> leftKidC = curr.getLeft();
        curr.setLeft(leftKidC.getRight());
        leftKidC.setRight(curr);
        update(curr);
        update(leftKidC);
        return leftKidC;
    }

    /**
     * single left rotation on a node
     *
     * @param curr currently unbalanced node
     * @return return the new root of the subtree
     */
    private AVLNode<T> leftRotate(AVLNode<T> curr) {
        AVLNode<T> rightKidC = curr.getRight();
        curr.setRight(rightKidC.getLeft());
        rightKidC.setLeft(curr);
        update(curr);
        update(rightKidC);
        return rightKidC;
    }
    /**
     * Left rotate the left kid then right rotate the current node
     *
     * @param curr currently unbalanced node
     * @return return the new root of the subtree
     */
    private AVLNode<T> leftRightRotate(AVLNode<T> curr) {
        curr.setLeft(leftRotate(curr.getLeft()));
        return rightRotate(curr);
    }

    /**
     * right rotate the right kid and left rotate the current node
     *
     * @param curr currently unbalanced node
     * @return return the new root of the subtree
     */
    private AVLNode<T> rightLeftRotate(AVLNode<T> curr) {
        curr.setRight(rightRotate(curr.getRight()));
        return leftRotate(curr);
    }

    /**
     * balance the current node
     *
     * @param curr check whether current node is balanced
     * @return  1. If balanced, return the original node
     *          2. If unbalanced, return the fixed root
     */
    private AVLNode<T> balance(AVLNode<T> curr) {
        if (curr.getBalanceFactor() > 1 && curr.getLeft().getBalanceFactor() >= 0) { // LL
            return rightRotate(curr);
        } else if (curr.getBalanceFactor() > 1 && curr.getLeft().getBalanceFactor() < 0) { //LR
            return leftRightRotate(curr);
        } else if (curr.getBalanceFactor() < -1 && curr.getRight().getBalanceFactor() > 0) { //RL
            return rightLeftRotate(curr);
        } else if (curr.getBalanceFactor() < -1 && curr.getRight().getBalanceFactor() <= 0) { //RR
            return leftRotate(curr);
        }
        return curr; // when curr == null and when no need to fix
    }

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize the AVL with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot take in null data!");
        }
        for (T element: data) {
            if (element == null) {
                throw new IllegalArgumentException("Cannot take in null element!");
            }
            add(element);
        }
    }

    /**
     * Adds the element to the tree.
     *
     * Start by adding it as a leaf like in a regular BST and then rotate the
     * tree as necessary.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after adding the element, making sure to rebalance if
     * necessary.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to add
     * @throws IllegalArgumentException if data is null
     */
    //same as BST only add to leaf
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add null data!");
        }
        root = addH(root, data);
    }
    /**
     * helper method for add
     *
     * @param curr the root of a tree
     * @param data the data to be added
     * @return root node that now links to updated AVL
     */
    private AVLNode<T> addH(AVLNode<T> curr, T data) {
        if (curr == null) {
            AVLNode<T> newNode = new AVLNode<>(data);
            size++;
            return newNode;
        } else if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(addH(curr.getLeft(), data));
        } else if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(addH(curr.getRight(), data));
        }
        update(curr);
        return balance(curr);
    }
    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the predecessor to
     * replace the data, NOT successor. As a reminder, rotations can occur
     * after removing the predecessor node.
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after removing the element, making sure to rebalance if
     * necessary.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws IllegalArgumentException if data is null
     * @throws NoSuchElementException   if the data is not found
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot remove null!");
        }
        AVLNode<T> removed = new AVLNode<>(null); // we create object not just T removed because object can hold data.
        root = removeH(root, data, removed);
        return removed.getData();
    }
    /**
     * Helper method for remove(T data)
     *
     * @param curr the current node
     * @param removed the node to be removed
     * @param data the data to remove from the tree
     * @return node containing data
     */
    private AVLNode<T> removeH(AVLNode<T> curr, T data, AVLNode<T> removed) {
        if (curr == null) {
            throw new NoSuchElementException("Data is not in the Tree!");
        } else if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(removeH(curr.getLeft(), data, removed));
        } else if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(removeH(curr.getRight(), data, removed));
        } else {
            size--;
            removed.setData(curr.getData());
            if (curr.getLeft() == null && curr.getRight() == null) {
                return null;
            } else if (curr.getLeft() != null && curr.getRight() == null) {
                return curr.getLeft();
            } else if (curr.getLeft() == null && curr.getRight() != null) {
                return curr.getRight();
            } else {
                AVLNode<T> hold = new AVLNode<>(null);
                curr.setLeft(removePredecessor(curr.getLeft(), hold));
                curr.setData(hold.getData());
            }
        }
        update(curr);
        return balance(curr);
    }
    /**
     * Helper method for remove()
     * Finds predecessor node
     *
     * @param curr the current node
     * @param hold the node that hold the predecessor data
     * @return node after removed predecessor
     */
    private AVLNode<T> removePredecessor(AVLNode<T> curr, AVLNode<T> hold) {
        if (curr.getRight() == null) {
            hold.setData(curr.getData());
            return curr.getLeft();
        }
        curr.setRight(removePredecessor(curr.getRight(), hold));
        update(curr);
        return balance(curr);
    }

    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws IllegalArgumentException if data is null
     * @throws NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("cannot get null data");
        }
        return getH(root, data);
    }
    /**
     * helper method for get
     *
     * @param curr check to see if data exist
     * @param data data to look for
     * @return data in the AVL
     */
    private T getH(AVLNode<T> curr, T data) {
        if (curr == null) {
            throw new NoSuchElementException("Data does not exist!");
        }
        if (data.compareTo(curr.getData()) < 0) {
            return getH(curr.getLeft(), data);
        }
        if (data.compareTo(curr.getData()) > 0) {
            return getH(curr.getRight(), data);
        }
        return curr.getData();
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree.
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data entered cannot be null");
        }
        return containsH(root, data);
    }
    /**
     * helper method for contains
     *
     * @param curr the node to check
     * @param data  data to look for
     * @return  boolean stating if data is in AVL
     */
    private boolean containsH(AVLNode<T> curr, T data) {
        if (curr == null) {
            return false;
        }
        if (data.compareTo(curr.getData()) < 0) {
            return containsH(curr.getLeft(), data);
        }
        if (data.compareTo(curr.getData()) > 0) {
            return containsH(curr.getRight(), data);
        }
        return true;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Should be O(1).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return heightH(root);
    }

    /**
     * height recursive helper
     *
     * @param curr curr to walk through the tree
     * @return return the height of the root
     */
    private int heightH(AVLNode<T> curr) {
        if (curr == null) {
            return -1;
        }
        return curr.getHeight();
    }
    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * The predecessor is the largest node that is smaller than the current data.
     *
     * Should be recursive.
     *
     * This method should retrieve (but not remove) the predecessor of the data
     * passed in. There are 2 cases to consider:
     * 1: The left subtree is non-empty. In this case, the predecessor is the
     * rightmost node of the left subtree.
     * 2: The left subtree is empty. In this case, the predecessor is the lowest
     * ancestor of the node containing data whose right child is also
     * an ancestor of data.
     *
     * This should NOT be used in the remove method.
     *
     * Ex:
     * Given the following AVL composed of Integers
     *     76
     *   /    \
     * 34      90
     *  \    /
     *  40  81
     * predecessor(76) should return 40
     * predecessor(81) should return 76
     *
     * @param data the data to find the predecessor of
     * @return the predecessor of data. If there is no smaller data than the
     * one given, return null.
     * @throws IllegalArgumentException if the data is null
     * @throws NoSuchElementException   if the data is not in the tree
     */
    public T predecessor(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot find the predecessor for null!");
        }
        AVLNode<T> pre = new AVLNode<>(null);
        AVLNode<T> curr = findCurr(root, data, pre);
        if (curr.getLeft() == null) {
            return pre.getData();
        }
        return findPredecessor(curr.getLeft()).getData();
    }

    /**
     * locate the current node
     *
     * @param curr takes in a root and recursively locating the current node
     * @param data is the value, we are searching for
     * @param pre  always track the closest older predecessor
     * @return  the current node
     */
    private AVLNode<T> findCurr(AVLNode<T> curr, T data, AVLNode<T> pre) {
        if (curr == null) {
            throw new NoSuchElementException("The given data does not exist in the tree!");
        }
        if (data.compareTo(curr.getData()) < 0) {
            return findCurr(curr.getLeft(), data, pre);
        }
        if (data.compareTo(curr.getData()) > 0) {
            pre.setData(curr.getData());
            return findCurr(curr.getRight(), data, pre);
        }
        return curr;
    }

    /**
     * Handles the case when current node has left kid
     *
     * @param curr takes in current node's left kid
     * @return  the predecessor node
     */
    private AVLNode<T> findPredecessor(AVLNode<T> curr) {
        if (curr.getRight() == null) {
            return curr;
        }
        return findPredecessor(curr.getRight());
    }

    /**
     * Returns the data in the deepest node. If there is more than one node
     * with the same deepest depth, return the rightmost (i.e. largest) node with
     * the deepest depth.
     *
     * Should be recursive.
     *
     * Must run in O(log n) for all cases.
     *
     * Example
     * Tree:
     *           2
     *        /    \
     *       0      3
     *        \
     *         1
     * Max Deepest Node:
     * 1 because it is the deepest node
     *
     * Example
     * Tree:
     *           2
     *        /    \
     *       0      4
     *        \    /
     *         1  3
     * Max Deepest Node:
     * 3 because it is the maximum deepest node (1 has the same depth but 3 > 1)
     *
     * @return the data in the maximum deepest node or null if the tree is empty
     */
    //>= go right
    public T maxDeepestNode() {
        if (size == 0) {
            return null;
        }
        return maxDeepHelper(root).getData();
    }

    /**
     * Max Deepest Node helper method
     *
     * @param curr takes in root and recursively locating MDN
     * @return the node we found
     */
    private AVLNode<T> maxDeepHelper(AVLNode<T> curr) {
        if (curr.getLeft() == null && curr.getRight() == null) {
            return curr;
        }

        if (curr.getLeft() != null && curr.getRight() == null) {
            return maxDeepHelper(curr.getLeft());
        }
        if (curr.getLeft() == null && curr.getRight() != null) {
            return maxDeepHelper(curr.getRight());
        }
        int leftHeight = curr.getLeft().getHeight();
        int rightHeight = curr.getRight().getHeight();
        if (leftHeight > rightHeight) {
            return maxDeepHelper(curr.getLeft());
        }
        return maxDeepHelper(curr.getRight());
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
