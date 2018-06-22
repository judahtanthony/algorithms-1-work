/*----------------------------------------------------------------
 *  Author:        Judah Anthony
 *  Written:       1/13/2017
 *  Last updated:  1/23/2017
 *
 *  Compilation:   javac-algs4 Percolation.java
 *  Execution:     cat samples.txt |  java-algs4 Percolation
 *  
 *  Prints "YES" if the data set percolates.  Print "No" otherwise.
 *
 *  $ cat samples.txt | java-algs4 Percolation
 *  YES
 *
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
/*/
import edu.princeton.cs.algs4.QuickFindUF;
/*/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
//*/
public class Percolation {
    private static final int IS_OPEN = 0b1;
    private static final int IS_CONNECTED_TO_TOP = 0b10;
    private static final int IS_CONNECTED_TO_BOTTOM = 0b100;
    private static final int IS_CONNECTED_TO_BOTH = 0b110;
    
    private int size;
    private int[] sites;
    private int openCount = 0;
    private boolean hasPercolated = false;
    /*/
    private QuickFindUF unionFind;
    /*/
    private WeightedQuickUnionUF unionFind;
    //*/
    
    /**
     * create n-by-n grid, with all sites blocked
     * 
     * @param int n
     * @throws java.lang.IllegalArgumentException
     */
    public Percolation(int n) {
        if (n <= 0) {
            String error = "`n` (" + n + ") cannot be less than zero(0).";
            throw new IllegalArgumentException(error);
        }
        
        size      = n;
        sites     = new int[size * size];
        /*/
        unionFind = new QuickFindUF(size * size);
        /*/
        unionFind = new WeightedQuickUnionUF(size * size);
        //*/
    }
    /**
     * open site (row, col) if it is not open already
     * 
     * @param int row
     * @param int col
     * @return void
     * @throws java.lang.IndexOutOfBoundsException
     */
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            openCount += 1;
            sites[toOffset(row, col)] |= IS_OPEN;
            connectSites(row, col);
        }
    }
    /**
     * is site (row, col) open?
     * 
     * @param int row
     * @param int col
     * @return boolean
     * @throws java.lang.IndexOutOfBoundsException
     */
    public boolean isOpen(int row, int col) {
        return sites[toOffset(row, col)] > 0;
    }
    /**
     * is site (row, col) full?
     * 
     * @param int row
     * @param int col
     * @return boolean
     * @throws java.lang.IndexOutOfBoundsException
     */
    public boolean isFull(int row, int col) {
        return (compInfo(row, col) & IS_CONNECTED_TO_TOP) > 0;
    }
    /**
     * number of open sites
     * 
     * @return int
     */
    public int numberOfOpenSites() {
        return openCount;
    }
    /**
     * does the system percolate?
     * 
     * @return boolean
     */
    public boolean percolates() {
        return hasPercolated;
    }
    
    private void connectSites(int row, int col) {
        int info = 0;
        
        // Set top and buttom info;
        if (row == 1) {
            info |= IS_CONNECTED_TO_TOP;
        }
        if (row == size) {
            info |= IS_CONNECTED_TO_BOTTOM;
        }
        
        // Gather info from neighbors.
        // Up
        if (row > 1 && isOpen(row - 1, col)) {
            info |= compInfo(row - 1, col);
        }
        // Right
        if (col < size && isOpen(row, col + 1)) {
            info |= compInfo(row, col + 1);
        }
        // Down
        if (row < size && isOpen(row + 1, col)) {
            info |= compInfo(row + 1, col);
        }
        // Left
        if (col > 1 && isOpen(row, col - 1)) {
            info |= compInfo(row, col - 1);
        }
        
        // Now connect them.
        // Up
        if (row > 1 && isOpen(row - 1, col)) {
            connect(row, col, row - 1, col);
        }
        // Right
        if (col < size && isOpen(row, col + 1)) {
            connect(row, col, row, col + 1);
        }
        // Down
        if (row < size && isOpen(row + 1, col)) {
            connect(row, col, row + 1, col);
        }
        // Left
        if (col > 1 && isOpen(row, col - 1)) {
            connect(row, col, row, col - 1);
        }
        
        // Now that everything is connected, the component might have changed,
        // so let's merge in the info into the original.
        info = mergeCompInfo(info, row, col);
        
        if ((info & IS_CONNECTED_TO_BOTH) == IS_CONNECTED_TO_BOTH) {
            hasPercolated = true;
        }
    }
    private void connect(int row, int col, int row2, int col2) {
        unionFind.union(
            toOffset(row, col),
            toOffset(row2, col2));
    }
    private int compInfo(int row, int col) {
        return sites[unionFind.find(toOffset(row, col))];
    }
    private int mergeCompInfo(int info, int row, int col) {
        int comp = unionFind.find(toOffset(row, col));
        sites[comp] |= info;
        return sites[comp];
    }
    // Translate the grid coordinates to the list offset
    private int toOffset(int row, int col) {
        if (row < 1 || row > size) {
            String error = "`row` (" + row + ") must be between 1 and " + size + " (inclusive).";
            throw new IndexOutOfBoundsException(error);
        }
        if (col < 1 || col > size) {
            String error = "`col` (" + col + ") must be between 1 and " + size + " (inclusive).";
            throw new IndexOutOfBoundsException(error);
        }
        return ((row - 1) * size + (col - 1));
    }
    
    
    /**
     * test client (optional)
     * 
     * @param String[] args
     */
    public static void main(String[] args) {
        if (StdIn.isEmpty()) {
            StdOut.println("No input.");
            return;
        }
        int size = StdIn.readInt();
        if (size < 1) {
            StdOut.println("The size must be at least 1.");
            return;
        }
        
        Percolation tester = new Percolation(size);
        while (StdIn.hasNextLine()) {
            if (StdIn.isEmpty()) {
                break;
            }
            int r = StdIn.readInt();
            if (StdIn.isEmpty()) {
                break;
            }
            int c = StdIn.readInt();
            
            tester.open(r, c);
        }
        
        if (tester.percolates()) {
            StdOut.println("YES");
        }
        else {
            StdOut.println("NO");
        }
    }
}
