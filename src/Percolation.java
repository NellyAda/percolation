import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int size;
    private boolean[] opened;
    private int count;
    private final WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Argument " + n + " is not greater than 0");
        }
        size = n;
        int dimension = n * n;
        opened = new boolean[dimension];
        uf = new WeightedQuickUnionUF(dimension + 2);
    }

    private void isOutOfRange(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("Arguments out of boundaries");
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        isOutOfRange(row, col);
        if (!isOpen(row, col)) {
            opened[(row - 1) * size + col - 1] = true;
            if (row == 1) {
                uf.union(size * size, (row - 1) * size + col - 1);
            }
            if (row == size) {
                uf.union(size * size + 1, (row - 1) * size + col - 1);
            }

            count++;
            if (row != 1) {
                // Look for upper adjacent
                connect(row, col, row - 1, col);
            }
            if (row != size) {
                // Look for lower adjacent
                connect(row, col, row + 1, col);
            }
            if (col != 1) {
                // Look for left adjacent
                connect(row, col, row, col - 1);
            }
            if (col != size) {
                // Look for right adjacent
                connect(row, col, row, col + 1);
            }
        }
    }

    private void connect(int row1, int col1, int row2, int col2) {
        if (isOpen(row2, col2)) {
            int p = (row1 - 1) * size + col1 - 1;
            int q = (row2 - 1) * size + col2 - 1;
            uf.union(p, q);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        isOutOfRange(row, col);
        return opened[(row - 1) * size + col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        isOutOfRange(row, col);
        boolean connected = uf.find((row - 1) * size + col - 1) == uf.find(size * size);
        return isOpen(row, col) && connected;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(size * size) == uf.find(size * size + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(3);

        StdOut.printf("Open sites: %d\n", p.numberOfOpenSites());
        StdOut.printf("Is Open (1, 1): %s\n", p.isOpen(1, 1));
        StdOut.printf("Open: 1, 1\n");
        p.open(1, 1);
        StdOut.printf("Opened: 1, 1\n");
        StdOut.printf("Is full (1, 1): %s\n", p.isFull(1, 1));
        StdOut.printf("Percolates?: %s\n", p.percolates());

        StdOut.printf("Open: 1, 2\n");
        p.open(1, 2);
        StdOut.printf("Opened: 1, 2\n");
        StdOut.printf("Open: 2, 2\n");
        p.open(2, 2);
        StdOut.printf("Opened: 2, 2\n");
        StdOut.printf("Is full (2, 2): %s\n", p.isFull(2, 2));
        StdOut.printf("Percolates?: %s\n", p.percolates());

        StdOut.printf("Open: 3, 2\n");
        p.open(3, 2);
        StdOut.printf("Opened: 3, 2\n");
        StdOut.printf("Is full (3, 2): %s\n", p.isFull(3, 2));
        StdOut.printf("Percolates?: %s\n", p.percolates());

        StdOut.printf("Open sites: %d\n", p.numberOfOpenSites());

    }
}