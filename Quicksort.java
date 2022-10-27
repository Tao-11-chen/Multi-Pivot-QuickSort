import java.util.Arrays;
import java.util.Random;

/**
 * Based on the paper: "Multi-Pivot Quicksort: Theory and Experiments"
 * downloaded from:
 * http://epubs.siam.org/doi/pdf/10.1137/1.9781611973198.6
 * on Oct.24th, 2022
 * @author YuantaoChen ChangHan
 */

public class Quicksort {
    private static void insertionsort(int[] a, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            for (int j = i; j > left && a[j] < a[j - 1]; j--) {
                swap(a, j, j - 1);
            }
        }
    }

    public static void quicksort_1pivot(int[] A, int left, int right) {
        int i = left;
        int j = right;
        if (left < right) {
            int temp = A[i];
            while (i < j) {
                while (i < j && temp <= A[j])
                    j--;
                A[i] = A[j];
                while (i < j && temp >= A[i])
                    i++;
                A[j] = A[i];
                A[i] = temp;
            }
            quicksort_1pivot(A, left, i - 1);
            quicksort_1pivot(A, i + 1, right);
        }
    }

    /**
     * Naive choice of pivots: p,q,r as A[left], A[midpoint], A[right] elements.
     */
    public static void quicksort_3Pivot(int[] A, int left, int right) {
        int length = right - left + 1;
        if (length < 10) {
            if (length > 1)
                insertionsort(A, left, right);
            return;
        }

        // get the midpoint of the array
        int midpoint = (left + right) >>> 1;
        // sort the three pivots and put them to left, left+1, right
        if (A[midpoint] < A[left]) { int t = A[midpoint]; A[midpoint] = A[left]; A[left] = t; }
        if (A[right] < A[midpoint]) { int t = A[right]; A[right] = A[midpoint]; A[midpoint] = t;
            if (t < A[left]) { A[midpoint] = A[left]; A[left] = t; }
        }
        swap(A, left+1, midpoint);

        // follow the paper's description
        int a = left + 2;
        int b = left + 2;
        int c = right - 1;
        int d = right - 1;

        int p = A[left];
        int q = A[left+1];
        int r = A[right];

        while (b <= c) {
            while (A[b] < q && b <= c) {
                if (A[b] < p) {
                    swap(A, a, b);
                    a++;
                }
                b++;
            }
            while (A[c] > q && b <= c) {
                if (A[c] > r) {
                    swap(A, c, d);
                    d--;
                }
                c--;
            }
            if (b <= c) {
                if (A[b] > r) {
                    if (A[c] < p) {
                        swap(A, b, a); swap(A, a, c);
                        a++;
                    } else {
                        swap(A, b, c);
                    }
                    swap(A, c, d);
                    b++; c--; d--;
                } else {
                    if (A[c] < p) {
                        swap(A, b, a); swap(A, a, c);
                        a++;
                    } else {
                        swap(A, b, c);
                    }
                    b++; c--;
                }
            }
        }
        a--; b--; c++; d++;
        swap(A, left + 1, a); swap(A, a, b);
        a--;
        swap(A, left, a);
        swap(A, right, d);

        quicksort_3Pivot(A, left, a-1);
        quicksort_3Pivot(A, a+1, b-1);
        quicksort_3Pivot(A, b+1, d-1);
        quicksort_3Pivot(A, d+1, right);
    }

    /**
     * Choose pivots p,q,r as the 2nd, 4th, & 6th of 7 elements.
     */
    public static void quicksort_3pivot_op(int[] A, int left, int right) {
        int length = right - left + 1;
        if (length < 10) {
            if (length > 1)
                insertionsort(A, left, right);
            return;
        }

        // approximation of length / 7
        int seventh = (length >> 3) + (length >> 6) + 1;
        // get 7 numbers hashed in the list as much as possible,
        // sort them and choose 2th, 4th, 6th in the seven numbers as pivot1,2,3
        int e4 = (left + right) >>> 1;
        int e3 = e4 - seventh;
        int e2 = e3 - seventh;
        int e1 = left;
        int e5 = e4 + seventh;
        int e6 = e5 + seventh;
        int e7 = right;

        // Sort using insertion sort
        if (A[e2] < A[e1]) { int t = A[e2]; A[e2] = A[e1]; A[e1] = t; }

        if (A[e3] < A[e2]) { int t = A[e3]; A[e3] = A[e2]; A[e2] = t;
            if (t < A[e1]) { A[e2] = A[e1]; A[e1] = t; }
        }
        if (A[e4] < A[e3]) { int t = A[e4]; A[e4] = A[e3]; A[e3] = t;
            if (t < A[e2]) { A[e3] = A[e2]; A[e2] = t;
                if (t < A[e1]) { A[e2] = A[e1]; A[e1] = t; }
            }
        }
        if (A[e5] < A[e4]) { int t = A[e5]; A[e5] = A[e4]; A[e4] = t;
            if (t < A[e3]) { A[e4] = A[e3]; A[e3] = t;
                if (t < A[e2]) { A[e3] = A[e2]; A[e2] = t;
                    if (t < A[e1]) { A[e2] = A[e1]; A[e1] = t; }
                }
            }
        }
        if (A[e6] < A[e5]) { int t = A[e6]; A[e6] = A[e5]; A[e5] = t;
            if (t < A[e4]) { A[e5] = A[e4]; A[e4] = t;
                if (t < A[e3]) { A[e4] = A[e3]; A[e3] = t;
                    if (t < A[e2]) { A[e3] = A[e2]; A[e2] = t;
                        if (t < A[e1]) { A[e2] = A[e1]; A[e1] = t; }
                    }
                }
            }
        }
        if (A[e7] < A[e6]) { int t = A[e7]; A[e7] = A[e6]; A[e6] = t;
            if (t < A[e5]) { A[e6] = A[e5]; A[e5] = t;
                if (t < A[e4]) { A[e5] = A[e4]; A[e4] = t;
                    if (t < A[e3]) { A[e4] = A[e3]; A[e3] = t;
                        if (t < A[e2]) { A[e3] = A[e2]; A[e2] = t;
                            if (t < A[e1]) { A[e2] = A[e1]; A[e1] = t; }
                        }
                    }
                }
            }
        }
        swap(A, left, e2);
        swap(A, left+1, e4);
        swap(A, right, e6);

        // Follow the paper's description
        int a = left + 2;
        int b = left + 2;
        int c = right - 1;
        int d = right - 1;

        int p = A[left];
        int q = A[left+1];
        int r = A[right];
        while (b <= c) {
            while (A[b] < q && b <= c) {
                if (A[b] < p) {
                    swap(A, a, b);
                    a++;
                }
                b++;
            }
            while (A[c] > q && b <= c) {
                if (A[c] > r) {
                    swap(A, c, d);
                    d--;
                }
                c--;
            }
            if (b <= c) {
                if (A[b] > r) {
                    if (A[c] < p) {
                        swap(A, b, a); swap(A, a, c);
                        a++;
                    } else {
                        swap(A, b, c);
                    }
                    swap(A, c, d);
                    b++; c--; d--;
                } else {
                    if (A[c] < p) {
                        swap(A, b, a); swap(A, a, c);
                        a++;
                    } else {
                        swap(A, b, c);
                    }
                    b++; c--;
                }
            }
        }
        // swap the pivots to their correct positions
        a--; b--; c++; d++;
        swap(A, left + 1, a); swap(A, a, b);
        a--;
        swap(A, left, a);
        swap(A, right, d);

        quicksort_3pivot_op(A, left, a-1);
        quicksort_3pivot_op(A, a+1, b-1);
        quicksort_3pivot_op(A, b+1, d-1);
        quicksort_3pivot_op(A, d+1, right);
    }

    private static final void swap(int[] A, int x, int y) {
        int temp = A[x];
        A[x] = A[y];
        A[y] = temp;
    }

    // test if the list is sorted
    private static boolean isSorted(int[] A) {
        int lo = 0;
        int hi = A.length - 1;
        for (int i = lo + 1; i <= hi; i++) {
            if (A[i] < A[i - 1])
                return false;
        }
        return true;
    }

    // test all the algorithms
    public static void main(String[] args) {
        Random r = new Random();
        r.setSeed(0L);
        int[] test_list = new int[10000000];
        for (int i = 0; i < 10000000; i++) {
            test_list[i] = r.nextInt();
        }
        int[] test_list_1 = test_list.clone();
        int[] test_list_2 = test_list.clone();
        int[] test_list_3 = test_list.clone();
        int[] test_list_4 = test_list.clone();

        long start_1 = System.currentTimeMillis();
        quicksort_1pivot(test_list_1, 0, test_list_1.length - 1);
        long stop_1 = System.currentTimeMillis();
        long start_2 = System.currentTimeMillis();
        quicksort_3Pivot(test_list_2, 0, test_list_2.length - 1);
        long stop_2 = System.currentTimeMillis();
        long start_3 = System.currentTimeMillis();
        quicksort_3pivot_op(test_list_3, 0, test_list_3.length - 1);
        long stop_3 = System.currentTimeMillis();
        long start_4 = System.currentTimeMillis();
        Arrays.sort(test_list_4);
        long stop_4 = System.currentTimeMillis();
        if (isSorted(test_list_1))
            System.out.println((stop_1 - start_1) + "ms to sort " + test_list_1.length + " elements using 1 Pivot QuickSort");
        if (isSorted(test_list_2))
            System.out.println((stop_2 - start_2) + "ms to sort " + test_list_2.length + " elements using 3 Pivot QuickSort without optimization");
        if (isSorted(test_list_3))
            System.out.println((stop_3 - start_3) + "ms to sort " + test_list_3.length + " elements using optimized 3 Pivot QuickSort");
        if (isSorted(test_list_4))
            System.out.println((stop_4 - start_4) + "ms to sort " + test_list_4.length + " elements using JDK15's DualPivot QuickSort");
    }
}
