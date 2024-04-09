import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a BST.
 *
 * @author Jinlin Yang
 * @version 1.0
 */
public class BST<T extends Comparable<? super T>> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private BSTNode<T> root;
    private int size;

    /**
     * Constructs a new BST.
     * <p>
     * This constructor should initialize an empty BST.
     * <p>
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new BST.
     * <p>
     * This constructor should initialize the BST with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     * <p>
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data entered cannot be null!");
        }
        for (T element : data) {
            if (element == null) {
                throw new IllegalArgumentException("Data entered cannot be null!");
            }
            add(element);
        }
    }

    /**
     * Adds the data to the tree.
     * <p>
     * This must be done recursively.
     * <p>
     * The data becomes a leaf in the tree.
     * <p>
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate
     * shouldn't get added, and size should not be incremented).
     * <p>
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data entered cannot be null!");
        }
        root = addH(root, data);
    }

    /**
     * helper method for add
     *
     * @param curr the root of a tree
     * @param data the data to be added
     * @return root node that now links to updated BST
     */
    private BSTNode<T> addH(BSTNode<T> curr, T data) {
        //the first part add a new node as a leaf, also if root == null
        if (curr == null) {
            //found the adding spot
            //Make a new node, don't return current directly here
            BSTNode<T> node = new BSTNode<>(data);
            size++;
            return node;
            // the second part recursively locating the adding spot
            //perform comparison on each node
        } else if (data.compareTo(curr.getData()) > 0) {
            //we update the structure first then return
            curr.setRight(addH(curr.getRight(), data));
        } else if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(addH(curr.getLeft(), data));
        }
        // the third part return each waiting node
        //handled the case when already exist
        return curr;
    }

    /**
     * Removes and returns the data from the tree matching the given parameter.
     * <p>
     * This must be done recursively.
     * <p>
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data. You MUST use recursion to find and remove the
     * successor (you will likely need an additional helper method to
     * handle this case efficiently).
     * <p>
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     * <p>
     * Hint: Should you use value equality or reference equality?
     * <p>
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */


    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data entered cannot be null!");
        }
        BSTNode<T> removed = new BSTNode<>(null);
        root = removeH(root, removed, data);
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
    private BSTNode<T> removeH(BSTNode<T> curr, BSTNode<T> removed, T data) {
        if (curr == null) {
            //not found or root is null
            throw new NoSuchElementException("Data is not in the tree!");
        }
        if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(removeH(curr.getRight(), removed, data));
        } else if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(removeH(curr.getLeft(), removed, data));
        } else {
            removed.setData(curr.getData());
            size--;
            if (curr.getLeft() == null && curr.getRight() == null) {
                return null;
            }
            if ((curr.getLeft() == null) && (curr.getRight() != null)) {
                return curr.getRight();
            }
            if ((curr.getLeft() != null) && (curr.getRight() == null)) {
                return curr.getLeft();
            }
            if (curr.getLeft() != null && curr.getRight() != null) {
                //hold successor's data
                BSTNode<T> hold = new BSTNode<>(null);

                curr.setRight(removeSuccessor(curr.getRight(), hold));

                curr.setData(hold.getData());
            }
        }
        return curr;
    }

    /**
     * Helper method for remove()
     * Finds predecessor node
     *
     * @param curr the current node
     * @param hold the node that hold the successor data
     * @return data from the successor node
     */
    private BSTNode<T> removeSuccessor(BSTNode<T> curr, BSTNode<T> hold) {
        if (curr.getLeft() == null) {
            hold.setData(curr.getData());
            //either null or kid
            return curr.getRight();
        }
        curr.setLeft(removeSuccessor(curr.getLeft(), hold));
        //update each subtree
        return curr;
    }


    /**
     * Returns the data from the tree matching the given parameter.
     * <p>
     * This must be done recursively.
     * <p>
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     * <p>
     * Hint: Should you use value equality or reference equality?
     * <p>
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data entered cannot be null!");
        }
        return getH(root, data);
    }

    /**
     * helper method for get
     *
     * @param node check to see if data exist
     * @param data data to look for
     * @return data in the BST
     */
    private T getH(BSTNode<T> node, T data) {
        if (node == null) {
            throw new NoSuchElementException("Data is not in the tree!");
        }
        if (data.compareTo(node.getData()) < 0) {
            return getH(node.getLeft(), data);
        }
        if (data.compareTo(node.getData()) > 0) {
            //first return to the method call then the return of the method return it again
            return getH(node.getRight(), data);
        }
        return node.getData();
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     * <p>
     * This must be done recursively.
     * <p>
     * Hint: Should you use value equality or reference equality?
     * <p>
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
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
     * @param node the node to check
     * @param data  data to look for
     * @return  boolean stating if data is in BST
     */
    private boolean containsH(BSTNode<T> node, T data) {
        if (node == null) {
            return false;
        }
        if (data.compareTo(node.getData()) < 0) {
            return containsH(node.getLeft(), data);
        }
        if (data.compareTo(node.getData()) > 0) {
            return containsH(node.getRight(), data);
        }
        return true;
    }

    /**
     * Generate a pre-order traversal of the tree.
     * <p>
     * This must be done recursively.
     * <p>
     * Must be O(n).
     *
     * @return the preorder traversal of the tree
     */
    public List<T> preorder() {
        List<T> temp = new ArrayList<>();
        return preorderH(root, temp);
    }

    /**
     * helper method for pre order traversal
     *
     * @param node node to check
     * @param list list to hold the data
     * @return return the list
     */
    private List<T> preorderH(BSTNode<T> node, List<T> list) {
        if (node == null) {
            return list;
        }
        list.add(node.getData());
        preorderH(node.getLeft(), list);
        preorderH(node.getRight(), list);
        return list;
    }

    /**
     * Generate an in-order traversal of the tree.
     * <p>
     * This must be done recursively.
     * <p>
     * Must be O(n).
     *
     * @return the inorder traversal of the tree
     */

    public List<T> inorder() {
        List<T> temp = new ArrayList<>();
        return inorderH(root, temp);
    }

    /**
     * helper method for in order traversal\
     *
     * @param node the node to walk through
     * @param list list to hold the data
     * @return return the list
     */
    private List<T> inorderH(BSTNode<T> node, List<T> list) {
        if (node == null) {
            return list;
        }
        inorderH(node.getLeft(), list);
        list.add(node.getData());
        inorderH(node.getRight(), list);
        return list;
    }


    /**
     * Generate a post-order traversal of the tree.
     * <p>
     * This must be done recursively.
     * <p>
     * Must be O(n).
     *
     * @return the postorder traversal of the tree
     */

    public List<T> postorder() {
        List<T> temp = new ArrayList<>();
        return postorderH(root, temp);
    }

    /**
     * helper method for post order traversal
     *
     * @param node node to walk through the BST
     * @param list list to hold data
     * @return return the list
     */
    private List<T> postorderH(BSTNode<T> node, List<T> list) {
        if (node == null) {
            return list;
        }
        postorderH(node.getLeft(), list);
        postorderH(node.getRight(), list);
        list.add(node.getData());

        return list;
    }


    /**
     * Generate a level-order traversal of the tree.
     * <p>
     * This does not need to be done recursively.
     * <p>
     * Hint: You will need to use a queue of nodes. Think about what initial
     * node you should add to the queue and what loop / loop conditions you
     * should use.
     * <p>
     * Must be O(n).
     *
     * @return the level order traversal of the tree
     */

    public List<T> levelorder() {
        List<T> actualList = new ArrayList<>();
        Queue<BSTNode<T>> queue = new LinkedList<>();
        if (root != null) {
            queue.add(root);
        }
        while (!queue.isEmpty()) {
            BSTNode<T> curr = queue.remove();
            actualList.add(curr.getData());
            if (curr.getLeft() != null) {
                queue.add(curr.getLeft());
            }
            if (curr.getRight() != null) {
                queue.add(curr.getRight());
            }
        }
        return actualList;
    }
    /**
     * Returns the height of the root of the tree.
     * <p>
     * This must be done recursively.
     * <p>
     * A node's height is defined as max(left.height, right.height) + 1. A
     * leaf node has a height of 0 and a null child has a height of -1.
     * <p>
     * Must be O(n).
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
    private int heightH(BSTNode<T> curr) {
        if (curr == null) {
            return -1;
        }
        return (Math.max(heightH(curr.getLeft()), heightH(curr.getRight())) + 1);
    }


    /**
     * Clears the tree.
     * <p>
     * Clears all data and resets the size.
     * <p>
     * Must be O(1).
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Finds the path between two elements in the tree, specifically the path
     * from data1 to data2, inclusive of both.
     * <p>
     * This must be done recursively.
     * <p>
     * A good way to start is by finding the deepest common ancestor (DCA) of both data
     * and add it to the list. You will most likely have to split off and
     * traverse the tree for each piece of data adding to the list in such a
     * way that it will return the path in the correct order without requiring any
     * list manipulation later. One way to accomplish this (after adding the DCA
     * to the list) is to then traverse to data1 while adding its ancestors
     * to the front of the list. Finally, traverse to data2 while adding its
     * ancestors to the back of the list.
     * <p>
     * Please note that there is no relationship between the data parameters
     * in that they may not belong to the same branch.
     * <p>
     * You may only use 1 list instance to complete this method. Think about
     * what type of list to use considering the Big-O efficiency of the list
     * operations.
     * <p>
     * This method only needs to traverse to the deepest common ancestor once.
     * From that node, go to each data in one traversal each. Failure to do
     * so will result in a penalty.
     * <p>
     * If both data1 and data2 are equal and in the tree, the list should be
     * of size 1 and contain the element from the tree equal to data1 and data2.
     * <p>
     * Ex:
     * Given the following BST composed of Integers
     *      50
     * /        \
     *  25         75
     * /    \
     * 12    37
     * /  \    \
     * 11   15   40
     * /
     * 10
     * findPathBetween(10, 40) should return the list [10, 11, 12, 25, 37, 40]
     * findPathBetween(50, 37) should return the list [50, 25, 37]
     * findPathBetween(75, 75) should return the list [75]
     * <p>
     * Must be O(log n) for a balanced tree and O(n) for worst case.
     *
     * @param data1 the data to start the path from
     * @param data2 the data to end the path on
     * @return the unique path between the two elements
     * @throws java.lang.IllegalArgumentException if either data1 or data2 is
     *                                            null
     * @throws java.util.NoSuchElementException   if data1 or data2 is not in
     *                                            the tree
     */

    public List<T> findPathBetween(T data1, T data2) {
        if (data1 == null || data2 == null) {
            throw new IllegalArgumentException("Data entered cannot be null!");
        }

        LinkedList<T> actualList = new LinkedList<>();
        BSTNode<T> DCA = findDCA(root, data1, data2);

        addToFrontofList(DCA, data1, actualList);
        actualList.remove(actualList.size()-1);
        addToBackofList(DCA,data2,actualList);


        return actualList;
    }

    /**
     * helper method to find the Deepest common ancestor
     *
     * @param curr curr to walk through the tree
     * @param data1 data1 is the data we want to use to find DCA
     * @param data2 data2 is the data we want to use to find DCA
     * @param //actualList list hold the path
     * @return return the DCA node
     */
    /*
    private BSTNode<T> findDCA(BSTNode<T> curr, T data1, T data2, LinkedList<T> actualList) {
        if (curr == null) {
            throw new NoSuchElementException("DCA does not exist!");
        }
        int cmp1 = data1.compareTo(curr.getData());
        int cmp2 = data2.compareTo(curr.getData());
        if (cmp1 < 0 && cmp2 < 0) {
            return findDCA(curr.getLeft(), data1, data2, actualList);
        } else if (cmp1 > 0 && cmp2 > 0) {
            return findDCA(curr.getRight(), data1, data2, actualList);
        }
        return curr;
    }

     */
    private BSTNode<T> findDCA(BSTNode<T> curr, T data1, T data2) {
        if (curr == null) {
            throw new NoSuchElementException("DCA does not exist!");
        }
        int cmp1 = data1.compareTo(curr.getData());
        int cmp2 = data2.compareTo(curr.getData());
        if (cmp1 < 0 && cmp2 < 0) {
            return findDCA(curr.getLeft(), data1, data2);
        } else if (cmp1 > 0 && cmp2 > 0) {
            return findDCA(curr.getRight(), data1, data2);
        }
        return curr;
    }

    /**
     * helper method to add the path from data1 to DCA
     *
     * @param curr curr to walk through the tree
     * @param data1 data1 is the data we want to use to find DCA
     * @param actualList list contains the path
     */
    private void addToFrontofList (BSTNode<T> curr, T data1, LinkedList<T> actualList) {
        if (curr == null) {
            throw new NoSuchElementException("Data 1 is not in the tree");
        }
        if (curr.getData().equals(data1)) {
            actualList.addFirst(curr.getData());
        }
        if (data1.compareTo(curr.getData()) < 0) {
            actualList.addFirst(curr.getData());
            addToFrontofList(curr.getLeft(), data1, actualList);
        }
        if (data1.compareTo(curr.getData()) > 0) {
            actualList.addFirst(curr.getData());
            addToFrontofList(curr.getRight(), data1, actualList);
        }
    }

    /**
     * helper method to add the path from data2 to DCA
     *
     * @param curr curr to walk through the tree
     * @param data2 data2 is the data we want to use to find DCA
     * @param actualList list contains the path
     */
    private void addToBackofList (BSTNode<T> curr, T data2, LinkedList<T> actualList) {
        if (curr == null) {
            throw new NoSuchElementException("Data 2 is not in the tree");
        }
        if (curr.getData().equals(data2)) {
            actualList.addLast(curr.getData());
        }
        if (data2.compareTo(curr.getData()) < 0) {
            actualList.addLast(curr.getData());
            addToBackofList(curr.getLeft(), data2, actualList);
        }
        if (data2.compareTo(curr.getData()) > 0) {
            actualList.addLast(curr.getData());
            addToBackofList(curr.getRight(), data2, actualList);
        }
    }








    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
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
