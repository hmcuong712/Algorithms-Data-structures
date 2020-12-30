/* *****************************************************************************
 *  Name:    PercolationStats
 *  NetID:   aturing
 *  Precept: P00
 *
 *  Description: To perform the Monte Carlo simulation.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    private int count;
    private int trialNum;
    private double[] samples;
    //    private Percolation pcl;
    private Stopwatch time;
    private double conFi95 = 1.96;

    /**
     * In order to get the relevant statistics, in each
     * experiment, we first perform the percolation() method
     * and get the total number of open sites. Then, x_t is the fraction of
     * open sites in experiment t.
     *
     * @param n:      The experiment is performed on an NxN grid
     * @param trials: Number of performed trials for the Monte Carlo simulation
     */
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validate(n, trials);
        count = n;
        samples = new double[trials];
        trialNum = trials;
        time = new Stopwatch();

        for (int j = 0; j < trials; j++) {
            Percolation pcl = new Percolation(n);
            // generate random numbers => open sites until percolation
            while (!pcl.percolates()) {
                int x = StdRandom.uniform(1, n + 1);
                int y = StdRandom.uniform(1, n + 1);
                pcl.open(x, y);
            }
            count = pcl.numberOfOpenSites();
            samples[j] = ((double) count) / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        double u = StdStats.mean(samples);
        return u;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double s = StdStats.stddev(samples);
        return s;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double low = mean() - (conFi95 * stddev()) / Math.sqrt(trialNum);
        return low;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double high = mean() + (conFi95 * stddev()) / Math.sqrt(trialNum);
        return high;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats pcs = new PercolationStats(n, T);
        StdOut.println("mean            = " + pcs.mean());
        StdOut.println("stddev          = " + pcs.stddev());
        StdOut.println("95% confidence interval = [" + pcs.confidenceLo()
                + ", " + pcs.confidenceHi() + "]");
    }

    // validating
    private void validate(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(
                    "index " + n + "and" + trials + "is not larger than 0");
        }
    }
}
