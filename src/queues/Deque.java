/*----------------------------------------------------------------
 *  Author:        Judah Anthony
 *  Written:       1/18/2017
 *  Last updated:  1/18/2017
 *
 *  Compilation:   javac-algs4 Deque.java
 *  Execution:     cat samples.txt |  java-algs4 Deque
 *  
 *  The is a basic implementation of a double-ended queue.
 *
 *  $ cat samples.txt | java-algs4 Deque
 *  A B C D E F G H I
 *
 *----------------------------------------------------------------*/
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Node head = null;
    private Node tail = null;
    private int length = 0;
    
    private class Node {
        private Item item;
        private Node prev;
        private Node next;
    }
    
    private class NodeIterator implements Iterator<Item> {
        private Node current;
        public NodeIterator() {
            current = head;
        }
        public boolean hasNext() {
            return current != null;
        }
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("Cannot call next() on an empty NodeIterator.");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
        public void remove() {
            throw new UnsupportedOperationException("remove() is not allowed on Deque.");
        }
    }
    
    /**
     * construct an empty deque
     */
    public Deque() {
    
    }
    /**
     * is the deque empty?
     */
    public boolean isEmpty() {
        return head == null;
    }
    /**
     * return the number of items on the deque
     */
    public int size() {
        return length;
    }
    /**
     * add the item to the front
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException("item must not be null.");
        }
        Node n = new Node();
        n.prev = null;
        n.next = head;
        n.item = item;
        if (head != null) {
            head.prev = n;
        }
        head = n;
        if (tail == null) {
            tail = n;
        }
        length += 1;
    }
    /**
     * add the item to the end
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException("item must not be null.");
        }
        Node n = new Node();
        n.prev = tail;
        n.next = null;
        n.item = item;
        if (tail != null) {
            tail.next = n;
        }
        tail = n;
        if (head == null) {
            head = n;
        }
        length += 1;
    }
    /**
     * remove and return the item from the front
     */
    public Item removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("Cannot removeFirst() on an empty Deque.");
        }
        Item item = head.item;
        head = head.next;
        if (head != null) {
            head.prev = null;
        }
        else {
            tail = null;
        }
        length -= 1;
        return item;
    }
    /**
     * remove and return the item from the end
     */
    public Item removeLast() {
        if (tail == null) {
            throw new NoSuchElementException("Cannot removeLast() on an empty Deque.");
        }
        Item item = tail.item;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        }
        else {
            head = null;
        }
        length -= 1;
        return item;
    }
    /**
     * return an iterator over items in order from front to end
     */
    public Iterator<Item> iterator() {
        return new  NodeIterator();
    }
    /**
     * unit testing (optional)
     */
    public static void main(String[] args) {
        Deque<String> queue = new Deque<String>();
        while (!StdIn.isEmpty()) {
            queue.addLast(StdIn.readString());
        }
        while (true) {
            StdOut.print(queue.removeFirst());
            if (queue.size() > 0) {
                StdOut.print(" ");
            }
            else {
                StdOut.println("");
                break;
            }
        }
    }
}