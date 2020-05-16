import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE = 1.96;

    private final double[] thresholds;
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Argument n: " + n + " or trials: " + trials + " is not greater than 0");
        }
        thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            double threshold = percolate(percolation, n);
            thresholds[i] = threshold;
        }

        runStats(trials);
    }

    private void runStats(int trials) {
        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
        confidenceLo = mean - (CONFIDENCE * stddev / Math.sqrt(trials));
        confidenceHi = mean + (CONFIDENCE * stddev / Math.sqrt(trials));
    }

    private double percolate(Percolation percolation, int n) {

        int dimension = n * n;
        // Generate random value between 1 and n*n
        int[] rand = StdRandom.permutation(dimension);
        int i = 0;
        while (!percolation.percolates()) {
            int row = rand[i] / n + 1;
            int col = rand[i] % n + 1;

            percolation.open(row, col);
            i++;
        }
        return ((double) percolation.numberOfOpenSites()) / dimension;
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, t);

        StdOut.printf("mean \t\t\t\t = %s\n", ps.mean());
        StdOut.printf("stddev \t\t\t\t = %s\n", ps.stddev());
        StdOut.printf("95%% confidence interval\t= [%s, %s]\n", ps.confidenceLo(), ps.confidenceHi());

    }

}