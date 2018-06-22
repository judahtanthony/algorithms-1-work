/*----------------------------------------------------------------
 *  Author:        Judah Anthony
 *  Written:       1/19/2017
 *  Last updated:  1/21/2017
 *
 *  Compilation:   javac-algs4 RandomizedQueue.java
 *  Execution:     cat samples.txt |  java-algs4 RandomizedQueue
 *  
 *  The is a basic implementation of a randomized bag.
 *
 *  $ cat samples.txt | java-algs4 Deque
 *  C D A G E I F B H
 *
 *----------------------------------------------------------------*/
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items = null;
    private int length = 0;
    private int size = 2;
    
    private class NodeIterator implements Iterator<Item> {
        private Item[] newitems;
        private int n;
        public NodeIterator() {
            newitems = (Item[]) new Object[length];
            n = length;
            for (int i = 0; i < n; ++i) {
                newitems[i] = items[i];
            }
            StdRandom.shuffle(newitems);
        }
        public boolean hasNext() {
            return n > 0;
        }
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Cannot call next() on an empty NodeIterator.");
            }
            return newitems[--n];
        }
        public void remove() {
            throw new UnsupportedOperationException("remove() is not allowed on Deque.");
        }
    }
    
    /**
     * construct an empty randomized queue
     */
    public RandomizedQueue() {
        items = (Item[]) new Object[size];
    }
    /**
     * is the queue empty?
     */
    public boolean isEmpty() {
        return length == 0;
    }
    /**
     * return the number of items on the queue
     */
    public int size() {
        return length;
    }
    /**
     * add the item
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException("item must not be null.");
        }
        // Put the new item on the end.
        items[length] = item;
        // If we have more than one item, randomly shuffle it in.
        if (length > 0) {
            int i = StdRandom.uniform(0, length + 1);
            // If we are to swapt the last with another index, swap them.
            if (i < length) {
                Item ex = items[i];
                items[i] = items[length];
                items[length] = ex;
            }
        }
        length += 1;
        maintenance();
    }
    /**
     * remove and return a random item
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot removeFirst() on an empty Deque.");
        }
        Item byebye = items[--length];
        maintenance();
        return byebye;
    }
    /**
     * return (but do not remove) a random item
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot removeFirst() on an empty Deque.");
        }
        if (length == 1) {
            return items[0];
        }
        int i = StdRandom.uniform(0, length);
        return items[i];
    }
    /**
     * return an independent iterator over items in random order
     */
    public Iterator<Item> iterator() {
        return new  NodeIterator();
    }
    
    
    private void maintenance() {
        if (length >= size) {
            size *= 2;
            Item[] newitems = (Item[]) new Object[size];
            for (int i = 0; i < length; ++i) {
                newitems[i] = items[i];
            }
            items = newitems;
        }
        else if (length <= size / 4) {
            size /= 2;
            Item[] newitems = (Item[]) new Object[size];
            for (int i = 0; i < length; ++i) {
                newitems[i] = items[i];
            }
            items = newitems;
        }
    }
    
    
    /**
     * unit testing (optional)
     */
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        StdOut.print("iterator: ");
        for (String item : queue) {
            StdOut.print(item + " ");
        }
        StdOut.println("");
        StdOut.print("dequeue: ");
        while (queue.size() > 0) {
            StdOut.print(queue.dequeue() + " ");
        }
        StdOut.println("");
    }
}
