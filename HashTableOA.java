package hashtableoa;

import java.util.Random;

public class HashTableOA {
    
    public static int[] hashTable = new int[11];

    public int insert(int key) {
        int index = h1(key);
        for (int i = 0; i < 11; i++) {
            if(hashTable[index] != 0) {
                index = doubleHash(key, i);
            }
        }
        hashTable[index] = key;
        return index;
    }
    
    public int h1(int key) {
        return key % 11;
    }
    
    public int h2(int key) {
        return 7 - (key % 7);
    }
    
    public int doubleHash(int key, int i) {
        return (h1(key) + i * h2(key)) % 11;
    }
    
    public static void print() {
        System.out.println('\n' + "*************************");
        System.out.println('\n' + "Final contents: " + '\n');
        for (int i = 0; i < 11; i++) {
            if(hashTable[i] == 0) {
                System.out.println("Slot #" + i + " is empty");
            } else {
                System.out.println("Slot #" + i + " has value " + hashTable[i]);
            }
        }
    }
    
    public static void main(String[] args) {
        Random r = new Random(97);
        HashTableOA table = new HashTableOA();
        int key, location;
                
        for (int i = 0; i < 8; i++) {
            key = r.nextInt(100);
            location = table.insert(key);
            System.out.println(key + " inserted at slot #" + location);
        }
        
        table.print();
    }
}
