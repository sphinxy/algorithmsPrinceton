import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] trialResult;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <= 0) {
            throw new IllegalArgumentException("n must be above zero");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("trials must be above zero");
        }

        int maxSites = n*n;
        int[] randomSites = new int[maxSites];
        for (int i = 0; i < n*n; i++) {
            randomSites[i] = i+1;
        }

        trialResult = new double[trials];
        for (int t = 0; t < trials; t++)
        {
            StdRandom.shuffle(randomSites);
            Percolation percolationExperiment = new Percolation(n);
            for (int i = 0; i < maxSites; i++) {
                int[] rowCol = toXY(n, randomSites[i]);
                // System.out.println("Opens sites" + randomSites[i]);
                percolationExperiment.open(rowCol[0], rowCol[1]);
                if (percolationExperiment.percolates()) {
                    trialResult[t] = (double) (i+1) /(double) maxSites;

                    break;
                }
            }
        }
    }
    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialResult);
    }
        // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialResult);
    }
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean()-(1.96*stddev() / Math.sqrt(trialResult.length));
    }
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean()+(1.96*stddev() / Math.sqrt(trialResult.length));
    }
    // test client (described below)
    public static void main(String[] args)
    {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        new PercolationStats(n, trials);
    }

    private static int[]  toXY(int gridSize, int value) {
        int[] result = new int[] {0, 0};
        result[0] = ((value-1) / gridSize) + 1;
        result[1] = value - ((result[0]-1)*gridSize);

        return  result;
    }
}
