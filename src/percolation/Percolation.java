import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/* */
public class Percolation {
    // private int[] id;
    private int[] idOpen;
    private int openCount = 0;
    private int count;
    private WeightedQuickUnionUF uf; // because of n^2 tiles in total

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // constructor
        count = n;
        uf = new WeightedQuickUnionUF(count * count + 2);
        idOpen = new int[n * n + 2];
        for (int j = 0; j < (n * n + 2); j++) {
            idOpen[j] = 0;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // validate the indices of the site
        validate(row, col);
        int k = mapping(row, col);
        if (idOpen[k] == 0) {
            idOpen[k] = 1;
            openCount++;
            if (row == count) //&& uf.find(k) == uf.find(count * count))
                uf.union(count * count + 1, k);
            if (row == 1)
                uf.union(count * count, k);
        }

        // perform sequence of union-find operations that links the site in question to its open neighbors (multiple UF can happen in one call)
        if (idOpen[moveDown(row, col)] == 1)
            uf.union(k, moveDown(row, col));
        if (idOpen[moveUp(row, col)] == 1)
            uf.union(k, moveUp(row, col));
        if (idOpen[moveRi(row, col)] == 1)
            uf.union(k, moveRi(row, col));
        if (idOpen[moveLe(row, col)] == 1)
            uf.union(k, moveLe(row, col));
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        int k = mapping(row, col);
        return idOpen[k] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        int k = mapping(row, col);
        return uf.find(count * count) == uf.find(k);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return (openCount);
    }

    // does the system percolate?
    public boolean percolates() {
        return (uf.find(count * count) == uf.find(count * count + 1));
    }

    // unit testing (required)
    public static void main(String[] args) {
        Percolation pc = new Percolation(2);
        pc.open(0, 1);
        pc.open(1, 1);
        boolean check = pc.percolates();
        StdOut.println(check);
    }

    // mapping 2D coordinates into 1D coordinates
    private int mapping(int p, int q) {
        if (p < 1 || q < 1)
            throw new IllegalArgumentException("either p or q or both are below 1");
//            return -1;
        int k = count * (p - 1) + q - 1;
        return k;
    }

    // validating
    /* private void validate(int p) {
        int n = idOpen.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 1 and " + (n - 1));
        }
    } */

    // validating
    private void validate(int row, int col) {
        if (row < 1 || col < 1 || row > count || col > count) {
            throw new IllegalArgumentException("Row and column's index cannot go beyond 1 and " + count);
        }
    }

    // move up
    private int moveDown(int row, int col) {
        if (row + 1 > count)
            return mapping(row, col);
        else
            return mapping(row + 1, col);
    }

    // move down
    private int moveUp(int row, int col) {
        if (row - 1 == 0)
            return mapping(row, col);
        else
            return mapping(row - 1, col);
    }

    // move right
    private int moveRi(int row, int col) {
        if (col + 1 > count)
            return mapping(row, col);
        else
            return mapping(row, col + 1);
    }

    // move left
    private int moveLe(int row, int col) {
        if (col - 1 == 0)
            return mapping(row, col);
        else
            return mapping(row, col - 1);
    }
}
