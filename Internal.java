/*  Name: Ryan Hayes
 *  COSC 311  FA19
 *  pp2
 *  URL:  <https://github.com/rybo-hub/COSC311/>
 */
package mergesort;

import java.util.Random;

public class Internal {
    static int[] a;
    static Random r = new Random(97);
    
    /* Internal merge sort implementation */
    static void merge(int[] a, int[] L, int left, int[] R, int right) {
        /* Merge temp arrays; index i for L; j for R; k for new */
        int i = 0, j = 0;
        int k = 0;
        
        while(i < left && j < right) {
            if(L[i] <= R[j]) {
                a[k] = L[i];
                i++;
            }else {
                a[k] = R[j];
                j++;
            }
            k++;
        }
        
        /* Copy remains */
        while(i < left) {
            a[k] = L[i];
            k++;
            i++;
        }
        while(j < right) {
            a[k] = R[j];
            k++;
            j++;
        }
    }
    
    /* Sorting function using merge() */
    static void sort(int[] a, int size) {
        if(size == 0 || size == 1) return;
        /* Create temp arrays with midpoints */
        int midL = size/2;
        int midR = size-midL;
        int[] L = new int[midL];
        int[] R = new int[midR];
        
        for(int i=0; i < midL; i++) {
            L[i] = a[i];
        }
        for(int j=midL; j < size; j++) {
            R[j-midL] = a[j];
        }
        
        /* Sort halves */
        sort(L, midL);
        sort(R, midR);
        
        /* Merge sorted halves */
        merge(a, L, midL, R, midR);
    }
    
    static void createArray(int size) {
        a = new int[size];
        for(int i=0; i < a.length; i++) {
            a[i] = r.nextInt(50);
        }
    }
    
    static void print() {
        for(int i=0; i < a.length; i++) {
            System.out.println("index #" + i + " has " + a[i]);
        }
    }
    
    public static void main(String[] args) {
        int size = 100; // Modify size here
        createArray(size);
        
        long startTime = System.nanoTime();
        sort(a, size);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
       
        print();
        System.out.println("Duration (ns): " + duration);
    }
    
}
