/*  Name: Ryan Hayes
 *  COSC 311  FA19
 *  hw1114
 *  URL:  <https://github.com/rybo-hub/skooldump/blob/master/HashTable2.java>
 */
package hashtable2;

import java.io.IOException;
import java.util.Random;
import java.io.RandomAccessFile;

public class HashTable2 {

    Random r = new Random(97);

    public void initialize(RandomAccessFile raf) throws IOException{
        for (int i = 0; i < 11; i++) {
            raf.writeInt(-1);
        }
    }
    
    public int generate() {
        return r.nextInt(100);
    }
    
    public int h1(int key) {
        return key % 11;
    }
    
    public void print(RandomAccessFile raf) throws IOException{
        raf.seek(0);
        for (int i = 0; i < raf.length() / 4; i++) {
            System.out.println("byte offset: " + i * 4 + " | int index: " + 
                                i + " | int value: " + raf.readInt());
        }
    }
    
    public static void main(String[] args){
        HashTable2 table = new HashTable2();
        RandomAccessFile fp;
        int key, index;
        
        try {
            fp = new RandomAccessFile("HashTable2Data.raf", "rw");
            table.initialize(fp);

            for (int i = 0; i < 8; i++) {
                key = table.generate();
                index = table.h1(key);
                fp.seek(index * 4);
                if(fp.readInt() == -1) {
                    fp.seek(index * 4);
                    fp.writeInt(key);
                }
            }

            table.print(fp);
            fp.close();
            
        } catch (IOException error) {System.out.println(error);}
    }
}