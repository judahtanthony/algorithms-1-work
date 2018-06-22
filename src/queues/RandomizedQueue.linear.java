/*----------------------------------------------------------------
 *  Author:        Judah Anthony
 *  Written:       1/19/2017
 *  Last updated:  1/19/2017
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
    private Node head = null;
    private int length = 0;
    
    private class Node {
        private Item item;
        private Node next;
    }
    
    private class NodeIterator implements Iterator<Item> {
        private Item[] items;
        private int n;
        public NodeIterator() {
            items = (Item[]) new Object[length];
            Node curr = head;
            n = 0;
            while (curr != null) {
                items[n++] = curr.item;
                curr = curr.next;
            }
            StdRandom.shuffle(items);
        }
        public boolean hasNext() {
            return n > 0;
        }
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Cannot call next() on an empty NodeIterator.");
            }
            return items[--n];
        }
        public void remove() {
            throw new UnsupportedOperationException("remove() is not allowed on Deque.");
        }
    }
    
    /**
     * construct an empty randomized queue
     */
    public RandomizedQueue() {
        
    }
    /**
     * is the queue empty?
     */
    public boolean isEmpty() {
        return head == null;
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
        Node n = new Node();
        n.item = item;
        if (head == null) {
            n.next = null;
            head = n;
        }
        else {
            int to = StdRandom.uniform(0, length + 1);
            if (to == 0) {
                n.next = head;
                head = n;
            }
            else {
                Node curr = head;
                for (int i = 1; i < to; ++i) {
                    curr = curr.next;
                }
                n.next = curr.next;
                curr.next = n;
            }
        }
        length += 1;
    }
    /**
     * remove and return a random item
     */
    public Item dequeue() {
        if (head == null) {
            throw new NoSuchElementException("Cannot removeFirst() on an empty Deque.");
        }
        Item item = head.item;
        head = head.next;
        length -= 1;
        return item;
    }
    /**
     * return (but do not remove) a random item
     */
    public Item sample() {
        if (head == null) {
            throw new NoSuchElementException("Cannot removeFirst() on an empty Deque.");
        }
        if (length == 1) {
            return head.item;
        }
        int to = StdRandom.uniform(0, length);
        if (to == 0) {
            return head.item;
        }
        else {
            Node curr = head;
            for (int i = 0; i < to; ++i) {
                curr = curr.next;
            }
            return curr.item;
        }
    }
    /**
     * return an independent iterator over items in random order
     */
    public Iterator<Item> iterator() {
        return new  NodeIterator();
    }
    /**
     * unit testing (optional)
     */
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        while (queue.size() > 0) {
            StdOut.print(queue.dequeue() + " ");
        }
//        for (String item : queue) {
//            StdOut.print(item);
//            StdOut.print(" ");
//        }
        StdOut.println("");
    }
}
