/*  Name: Ryan Hayes
 *  COSC 311  FA19
 *  pp2
 *  URL:  <https://github.com/rybo-hub/COSC311/>
 */
package mergesort;

import java.io.IOException;
import java.util.Random;
import java.io.RandomAccessFile;

public class External {
    static Random r = new Random();
    static RandomAccessFile r1;
    static RandomAccessFile r2;
    
    /* External merge sort implementation */
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
    
    static void createArray(int size) throws IOException {
        r1.seek(0);
        for(int i=0; i < size; i++) {
            int fp = r.nextInt(50);
            r1.writeInt(fp);
        }
    }
    
    static void print(int size) throws IOException {
        r2.seek(0);
        for(int i=0; i < size; i++) {
           int fp = r2.readInt();
           System.out.println("index #" + i + " has " + fp);
        }
    }
    
    static void exe(int size, int ram) throws IOException {
        /* Temp array created in r1 */
        r1.seek(0);
        int[] exe = new int[ram];
        for(int i=0; i < ram; i++) {
            int fp = r1.readInt();
            exe[i] = fp;
        }

        /* Sort array */
        sort(exe, ram);

        /* New array created in r2 */
        for(int j=0; j < ram; j++) {
            r2.writeInt(exe[j]);
        } 
    }
    
    public static void main(String[] args) throws IOException {
        int size = 100; // Modify size here
        int ram = size;
        
        try {
            r1 = new RandomAccessFile("test6.raf", "rw");
        } catch (IOException error) {System.out.println(error);}
        
        try {
            r2 = new RandomAccessFile("test66.raf", "rw");
        } catch (IOException error) {System.out.println(error);}
        
        createArray(size);
        
        long startTime = System.nanoTime();
        exe(size, ram);
        r1.close();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
       
        print(size);
        r2.close();
        System.out.println("Duration (ns): " + duration);
    }
}
