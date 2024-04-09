import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.LinkedList;

/**
 * Your implementation of various sorting algorithms.
 *
 * @author Jinlin Yang
 * @version 1.0
 */
public class Sorting {

    /**
     * Implement insertion sort.
     *
     * It should be:
     * in-place
     * stable
     * adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n)
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void insertionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException("Cannot take in Null!");
        }
/*
        for (int low = 1; low <= arr.length - 1; low++) {
            T t = arr[low]; //inserting value
            int i = low - 1; //inserting index
            while (i >= 0 && comparator.compare(t, arr[i]) < 0) {
                arr[i + 1] = arr[i];
                i--;
            }
            if (i != low - 1) {
                arr[i + 1] = t;
            }
        }
*/
        //this is easier to understand
        for (int i = 1; i <= arr.length - 1; i++) {
            int j = i;
            while (j > 0 && comparator.compare(arr[j - 1], arr[j]) > 0) {
                swap(arr, j, j - 1);
                j--;
            }
        }
    }
    /**
     * Implement cocktail sort.
     *
     * It should be:
     * in-place
     * stable
     * adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n)
     *
     * NOTE: See pdf for last swapped optimization for cocktail sort. You
     * MUST implement cocktail sort with this optimization
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void cocktailSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException("Cannot not take in Null!");
        }
        int lowerBound = 0;
        int upperBound = arr.length - 1;
        int lastSwapIndex = -1;
        boolean swapMade = true;
        while (swapMade) {
            swapMade = false;
            for (int i = lowerBound; i <= upperBound - 1; i++) { // -1 because no need to compare the last element
                if (comparator.compare(arr[i], arr[i + 1]) > 0) {
                    T temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    swapMade = true;
                    lastSwapIndex = i;
                }
            }
            if (lastSwapIndex != -1) {
                upperBound = lastSwapIndex;
            }
            if (swapMade) {
                swapMade = false;
                for (int j = upperBound; j >= lowerBound + 1; j--) { // +1 because no need to compare the first element
                    if (comparator.compare(arr[j], arr[j - 1]) < 0) {
                        T temp = arr[j];
                        arr[j] = arr[j - 1];
                        arr[j - 1] = temp;
                        swapMade = true;
                        lastSwapIndex = j;
                    }
                }
                lowerBound = lastSwapIndex;
            }
        }
    }
    /**
     * Implement merge sort.
     *
     * It should be:
     * out-of-place
     * stable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n log n)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * You can create more arrays to run merge sort, but at the end, everything
     * should be merged back into the original T[] which was passed in.
     *
     * When splitting the array, if there is an odd number of elements, put the
     * extra data on the right side.
     *
     * Hint: If two data are equal when merging, think about which subarray
     * you should pull from first
     *
     * @param <T>        data type to sort
     * @param arr        the array to be sorted
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void mergeSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException("Cannot take in null!");
        }
            if (arr.length <= 1){
                return;
            }
            T[] firstHalf = (T[]) new Object[arr.length / 2];
            for (int i = 0; i <= firstHalf.length - 1; i++) {
                firstHalf[i] = arr[i];
            }
            mergeSort(firstHalf, comparator);
            T[] secondHalf = (T[]) new Object[arr.length - arr.length / 2];
            for (int i = 0; i <= secondHalf.length - 1; i++) {
                secondHalf[i] = arr[arr.length / 2 + i];
            }
            mergeSort(secondHalf, comparator);
            merge(firstHalf, secondHalf, arr, comparator);
    }
    private static <T> void merge(T[] firstHalf, T[] secondHalf, T[] temp, Comparator<T> comparator){
        int current1 = 0; // current index in first half
        int current2 = 0; // current index in second half
        int current3 = 0; // current index in temp
        while (current1 < firstHalf.length && current2 < secondHalf.length) {
            if (comparator.compare(firstHalf[current1], secondHalf[current2]) <= 0) {
                temp[current3++] = firstHalf[current1++];
            } else {
                temp[current3++] = secondHalf[current2++];
            }
        }
        while (current1 < firstHalf.length) {
            temp[current3++] = firstHalf[current1++];
        }
        while (current2 < secondHalf.length) {
            temp[current3++] = secondHalf[current2++];
        }
    }

    /**
     * Implement quick sort.
     *
     * Use the provided random object to select your pivots. For example if you
     * need a pivot between a (inclusive) and b (exclusive) where b > a, use
     * the following code:
     *
     * int pivotIndex = rand.nextInt(b - a) + a;
     *
     * If your recursion uses an inclusive b instead of an exclusive one,
     * the formula changes by adding 1 to the nextInt() call:
     *
     * int pivotIndex = rand.nextInt(b - a + 1) + a;
     *
     * It should be:
     * in-place
     * unstable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not receive
     * credit if you do not use the one we have taught you!
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @param rand       the Random object used to select pivots
     * @throws java.lang.IllegalArgumentException if the array or comparator or
     *                                            rand is null
     */

    public static <T> void quickSort(T[] arr, Comparator<T> comparator,
                                     Random rand) {
        if (arr == null || comparator == null || rand == null) {
            throw new IllegalArgumentException("Cannot take in null!");
        }
        if (arr.length == 1) {
            return;
        }
        helper(arr, 0, arr.length - 1, rand, comparator);
    }

    /**
     * the helper method for quick sort
     *
     * @param arr the current array
     * @param start the index of the lower bound of the array
     * @param end   the index of the upper bound of the array
     * @param rand  random value
     * @param comparator the Comparator used to compare the data in arr
     * @param <T>   data type to sort
     */
    private static <T> void helper(T[] arr, int start, int end, Random rand, Comparator<T> comparator) {
        if ((end - start) < 1) {
            return;
        }
        int pivotIndex = rand.nextInt(end - start + 1) + start;
        swap(arr, pivotIndex, start);
        int i = start + 1;
        int j = end;
        while (!(i > j)) {
            while (!(i > j) && comparator.compare(arr[i], arr[start]) <= 0) {
                i++;
            }
            while (!(i > j) && comparator.compare(arr[start], arr[j]) <= 0) {
                j--;
            }
            if (!(i > j)) {
                swap(arr, i, j);
                i++;
                j--;
            }
        }
        swap(arr, start, j);
        helper(arr, start, j - 1, rand, comparator);
        helper(arr, j + 1, end, rand, comparator);

    }

    /**
     * swap data in array
     *
     * @param arr current array
     * @param i the first index
     * @param j the second index
     * @param <T>   the data type to compare
     */
    private static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }



    /**
     * Implement LSD (least significant digit) radix sort.
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not get full
     * credit if you do not implement the one we have taught you!
     *
     * Remember you CANNOT convert the ints to strings at any point in your
     * code! Doing so may result in a 0 for the implementation.
     *
     * It should be:
     * out-of-place
     * stable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(kn)
     *
     * And a best case running time of:
     * O(kn)
     *
     * You are allowed to make an initial O(n) passthrough of the array to
     * determine the number of iterations you need. The number of iterations
     * can be determined using the number with the largest magnitude.
     *
     * At no point should you find yourself needing a way to exponentiate a
     * number; any such method would be non-O(1). Think about how how you can
     * get each power of BASE naturally and efficiently as the algorithm
     * progresses through each digit.
     *
     * Refer to the PDF for more information on LSD Radix Sort.
     *
     * You may use ArrayList or LinkedList if you wish, but it may only be
     * used inside radix sort and any radix sort helpers. Do NOT use these
     * classes with other sorts. However, be sure the List implementation you
     * choose allows for stability while being as efficient as possible.
     *
     * Do NOT use anything from the Math class except Math.abs().
     *
     * @param arr the array to be sorted
     * @throws java.lang.IllegalArgumentException if the array is null
     */

    public static void lsdRadixSort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Cannot not take in null!");
        }
        int largestValue = 0;
        for (int i = 0; i <= arr.length - 1; i++) {
            //edge case int.min
            if (arr[i] == Integer.MIN_VALUE) {
                if (largestValue < Integer.MAX_VALUE) {
                    largestValue = Integer.MAX_VALUE;
                }
            }
            if (largestValue < Math.abs(arr[i])) {
                largestValue = Math.abs(arr[i]);
            }
        }
        int maxDigits = 1;
        while (largestValue >= 10) {
            maxDigits++;
            largestValue /= 10;
        }
        LinkedList<Integer>[] backingArray = new LinkedList[19];//make the backing array ready
        for (int i = 0; i <= backingArray.length - 1; i++) {
            backingArray[i] = new LinkedList<>();
        }
        for (int i = 0, x = 1; i <= maxDigits - 1; i++, x *= 10) {// sorting total times is max digits
            for (int j = 0; j <= arr.length - 1; j++) { // //each element in the original array, put them into backing array according to the value of the current digit
                int currentDigitValue = ((arr[j]) / x) % 10 + 9;
                backingArray[currentDigitValue].add(arr[j]);
            }
            int index = 0;// overwrite the original array pointer
            for (int y = 0; y <= backingArray.length - 1; y++) {// traverse backing Array overwrite and empty
                for (Integer element: backingArray[y]) {
                    arr[index++] = element;
                }
                backingArray[y].clear();
            }
        }
    }



    /**
     * Implement heap sort.
     *
     * It should be:
     * out-of-place
     * unstable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n log n)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * Use java.util.PriorityQueue as the heap. Note that in this
     * PriorityQueue implementation, elements are removed from smallest
     * element to largest element.
     *
     * Initialize the PriorityQueue using its build heap constructor (look at
     * the different constructors of java.util.PriorityQueue).
     *
     * Return an int array with a capacity equal to the size of the list. The
     * returned array should have the elements in the list in sorted order.
     *
     * @param data the data to sort
     * @return the array with length equal to the size of the input list that
     * holds the elements from the list is sorted order
     * @throws java.lang.IllegalArgumentException if the data is null
     */
    public static int[] heapSort(List<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot take in Null!");
        }
        PriorityQueue<Integer> heap = new PriorityQueue<>(data);
        int[] sorted = new int[data.size()];
        for (int i = 0; i <= data.size() - 1; i++) {
            sorted[i] = heap.poll();
        }
        return sorted;
    }
}
