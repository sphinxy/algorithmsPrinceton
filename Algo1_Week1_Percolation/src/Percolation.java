import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private static final byte FAKE_SITES = 2;
    private static final byte FAKE_SITE_UP = 0;
    private int fakeSiteDown = 0;
    private short[][] grid;
    private short gridSize = 0;
    private WeightedQuickUnionUF unionData;
    private WeightedQuickUnionUF unionDataDown;
    // private int fakeSiteUpGroup;
    // private boolean fakeSiteUpChanged = false;
    private boolean downNotConnected = true;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        setGridSize((short)n);

	//not connected to fake down node
        unionData = new WeightedQuickUnionUF(gridSize*gridSize+ FAKE_SITES);
	//connected to fake down node
        unionDataDown = new WeightedQuickUnionUF(gridSize*gridSize+ FAKE_SITES);
        fakeSiteDown = gridSize*gridSize + FAKE_SITES - 1;

        grid = new short[gridSize + 1][gridSize + 1];
        for (short row = 1; row <= gridSize; row++)
        {
            for (short col = 1; col <= gridSize; col++)
            {
                // attach first and last grid row to a fake sites
                if (row == 1) {
                    unionData.union(xyTo1D(row, col), FAKE_SITE_UP);
                    unionDataDown.union(xyTo1D(row, col), FAKE_SITE_UP);
                }

                grid[row][col] = 1;

            }
        }

            for (short col = 1; col <= gridSize; col++) {
                unionDataDown.union(xyTo1D(gridSize, col), fakeSiteDown);
            }
            downNotConnected = false;
    }
    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        short prow = (short)i;
        short pcol = (short)j;
        indexCheck(prow, pcol);
        grid[prow][pcol] = 0;

        // left
        unionNeighbor(prow, pcol, prow,  (short) (pcol+1));
        // right
        unionNeighbor(prow, pcol, prow, (short) (pcol-1));
        // up
        unionNeighbor(prow, pcol, (short) (prow-1), pcol);
        // down
        unionNeighbor(prow, pcol, (short) (prow+1), pcol);
     }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        indexCheck(i, j);
        return grid[i][j] == 0;
    }
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        indexCheck(i, j);
        return isOpen(i, j) && unionData.connected(xyTo1D((short)i, (short)j), FAKE_SITE_UP);
    }
    // does the system percolate (will use special UF object connected to fake down node)?
    public boolean percolates() {
        if (gridSize>1)
        {
            return unionDataDown.connected(FAKE_SITE_UP, fakeSiteDown);
        }
        else
        {
            return isOpen(1,1);
        }
    }
    // test client (optional)
    public static void main(String[] args) {
        System.out.println("Percolation runs");
        try {
            new Percolation(0);
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Constructor with n=0 exception ok: " +
                    ex.getMessage());
        }
        Percolation percolation = new Percolation(1);
        System.out.println(percolation.percolates());
    }

    private int xyTo1D(short i, short j) {
        indexCheck(i, j);
        return (i-1)*gridSize + j;
    }

    private void setGridSize(short n) {
        if (n <= 0) throw new IllegalArgumentException("n must be above zero");
        gridSize = n;
    }

    private void indexCheck(int i, int j) {
        if (i <= 0 || i > gridSize)
            throw new IndexOutOfBoundsException("index i must be in " +
                    gridSize + " but " + i);
        if (j <= 0  || j > gridSize)
            throw new IndexOutOfBoundsException("index j must be in " +
                    gridSize + " but " + j);
    }

    private boolean neighborExists(int row, int col) {
        if (col <= 0 || col > gridSize) {
            return  false;
        }
        if (row <= 0 || row > gridSize) {
            return  false;
        }
        return true;
    }

    private void unionNeighbor(short prow, short pcol, short qrow, short qcol) {
        if (neighborExists(qrow, qcol) && isOpen(qrow, qcol)) {
            unionData.union(xyTo1D(prow, pcol), xyTo1D(qrow, qcol));
            unionDataDown.union(xyTo1D(prow, pcol), xyTo1D(qrow, qcol));
        }
    }
}