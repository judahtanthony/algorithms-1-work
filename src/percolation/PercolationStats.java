/*----------------------------------------------------------------
 *  Author:        Judah Anthony
 *  Written:       1/14/2017
 *  Last updated:  1/14/2017
 *
 *  Compilation:   javac-algs4 PercolationStats.java
 *  Execution:     java-algs4 PercolationStats 200 100
 *  
 *  Prints the mean, standard deviation and 95% confidence range
 *  of the Monte Carlo simulation n-by-n grid over T experiments
 *  using random data.
 *
 *  $ java-algs4 PercolationStats 200 100
 *  mean                    = 0.5929934999999997
 *  stddev                  = 0.00876990421552567
 *  95% confidence interval = 0.5912745987737567, 0.5947124012262428
 *
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
// import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private double[] results;
    private double resultsMean;
    private double resultsStdDev;
    private double resultsConfidenceLo;
    private double resultsConfidenceHi;
    
   /**
    * perform trials independent experiments on an n-by-n grid
    * 
    * @param int n
    * @param int trials
    */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            String error = "Both `n` (" + n + ") and `trials` (" + trials + ") must be greater than 0.";
            throw new IllegalArgumentException(error);
        }
        
        results = new double[trials];
        for (int i = 0; i < trials; ++i) {
            results[i] = testPercolation(n);
        }
        
        resultsMean         = StdStats.mean(results);
        resultsStdDev       = StdStats.stddev(results);
        double confRange    = 1.96 * resultsStdDev / Math.sqrt(trials);
        resultsConfidenceLo = resultsMean - confRange;
        resultsConfidenceHi = resultsMean + confRange;
    }
    /**
     * sample mean of percolation threshold
     * 
     * @return double
     */
    public double mean() {
        return resultsMean;
    }
    /**
     * sample standard deviation of percolation threshold
     * 
     * @return double
     */
    public double stddev() {
        return resultsStdDev;
    }
    /**
     * low  endpoint of 95% confidence interval
     * 
     * @return double
     */
    public double confidenceLo() {
        return resultsConfidenceLo;
    }
    /**
     * high endpoint of 95% confidence interval
     * 
     * @return double
     */
    public double confidenceHi() {
        return resultsConfidenceHi;
    }
    
    private double testPercolation(int n) {
        Percolation tester = new Percolation(n);
        // Initialize sites.
        int[] sites = new int[n * n];
        for (int i = 0; i < sites.length; ++i) {
            sites[i] = i;
        }
        // Randomize sires.
        StdRandom.shuffle(sites);
        // Test when it percolates.
        for (int i = 0; i < sites.length; ++i) {
            int r = sites[i] / n;
            int c = sites[i] % n;
            tester.open(r + 1, c + 1);
            
            if (tester.percolates()) {
                return (double) tester.numberOfOpenSites() / (double) (n * n);
            }
        }
        
        // Something went totally amiss, but I don't know if I'm allowed to throw
        // an exception.
        return n;
    }
    
    /**
     * test client (described below)
     * 
     * @param String[] args
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            StdOut.println("You must pass two intergers as arguements");
            return;
        }
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        if (n <= 0 || t <= 0) {
            StdOut.println("Both arguements must be greater than 0");
            return;
        }
        
        // Stopwatch timer = new Stopwatch();
        PercolationStats stats = new PercolationStats(n, t);
        StdOut.printf("mean                    = %f\n", stats.mean());
        StdOut.printf("stddev                  = %f\n", stats.stddev());
        StdOut.printf("95%% confidence interval = %f, %f\n", stats.confidenceLo(), stats.confidenceHi());
        // StdOut.printf("elapsedTime()           = %f\n", timer.elapsedTime());
    }
}
