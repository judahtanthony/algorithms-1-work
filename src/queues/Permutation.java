/*----------------------------------------------------------------
 *  Author:        Judah Anthony
 *  Written:       1/20/2017
 *  Last updated:  1/20/2017
 *
 *  Compilation:   javac-algs4 Permutation.java
 *  Execution:     cat samples.txt |  java-algs4 Permutation 3
 *  
 *  Output a subset of strings.
 *
 *  $ cat samples.txt |  java-algs4 Permutation 3
 *  BB
 *  HH
 *  DD
 *
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        int i = 0;
        int to = Integer.parseInt(args[0]);
        for (String item : queue) {
            if (++i > to) {
                break;
            }
            StdOut.println(item);
        }
    }
}