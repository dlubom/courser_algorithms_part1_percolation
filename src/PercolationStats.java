import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final int trials;
    private final double[] xt;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.trials = trials;
        this.xt = new double[trials];

        for (int t = 0; t < trials; t++) {
            Percolation p = new Percolation(n);
            int row;
            int col;
            for (int c = 0; c < n * n; c++) {
                do {
                    row = StdRandom.uniform(n) + 1;
                    col = StdRandom.uniform(n) + 1;
                } while (p.isOpen(row, col));
                p.open(row, col);
                if (p.percolates()) {
                    this.xt[t] = (double) c / (n * n);
                    break;
                }
            }
        }
    }

    /*
     test client (described below)
     Also, include a main() method that takes two command-line arguments n and T, performs T independent computational
     experiments (discussed above) on an n-by-n grid, and prints the sample mean, sample standard deviation,
     and the 95% confidence interval for the percolation threshold. Use StdRandom to generate random numbers;
     use StdStats to compute the sample mean and sample standard deviation.
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("mean\t\t\t\t\t= " + ps.mean());
        System.out.println("stddev\t\t\t\t\t= " + ps.stddev());
        System.out.println("95% confidence interval\t= [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }




    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.xt);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (this.trials == 1)
            return Double.NaN;
        return StdStats.stddev(this.xt);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * Math.sqrt(stddev())) / Math.sqrt(this.trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * Math.sqrt(stddev())) / Math.sqrt(this.trials);
    }
}