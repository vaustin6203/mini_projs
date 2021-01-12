package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF backwash;
    private int top;
    private int bottom;
    private boolean[] sites;
    private int openSites;
    private int colSize;

    /** A contructor that creates a N * N Percolation instance.
     *
     * @param N
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("The grid's "
                    + "dimensions must be grater than 0.");
        }
        top = N * N;
        bottom = top + 1;
        colSize = N;
        openSites = 0;
        grid = new WeightedQuickUnionUF(top + 2);
        backwash = new WeightedQuickUnionUF(top + 1);
        sites = new boolean[top + 1];
        openSites = 0;
        connectTopBottom();
    }

    private void connectTopBottom() {
        for (int i = 0; i < colSize; i += 1) {
            backwash.union(i, top);
        }
        for (int j = top - colSize; j < top; j += 1) {
            grid.union(j, bottom);
        }
    }

    /** A method that converts and row and column index to an index usable
     * for a WeightedQuickUnionUF instance.
     *
     * @param row
     * @param col
     * @return int
     */
    private int xyTo1D(int row, int col) {
        return row * colSize + col;
    }

    /** A method that checks if a row and column index are in range for this Percolation instance.
     *
     * @param row
     * @param col
     */
    private boolean inRange(int row, int col) {
        if (row >= colSize || col >= colSize) {
            return false;
        }
        return row >= 0 && col >= 0;
    }

    private int[] findNeighbors(int row, int col) {
        int[] neighbors = {-1, -1, -1, -1};
        int n = 0;
        if (openSites <= 1) {
            return neighbors;
        }
        if (inRange(row + 1, col) && sites[xyTo1D(row + 1, col)]) {
            neighbors[n] = xyTo1D(row + 1, col);
            n += 1;
        }
        if (inRange(row - 1, col) && sites[xyTo1D(row - 1, col)]) {
            neighbors[n] = xyTo1D(row - 1, col);
            n += 1;
        }
        if (inRange(row, col + 1) && sites[xyTo1D(row, col + 1)]) {
            neighbors[n] = xyTo1D(row, col + 1);
            n += 1;
        }
        if (inRange(row, col - 1) && sites[xyTo1D(row, col - 1)]) {
            neighbors[n] = xyTo1D(row, col - 1);
        }
        return neighbors;
    }

    private void unionNeighbors(int row, int col) {
        int[] neighbors = findNeighbors(row, col);
        int index = xyTo1D(row, col);
        for (int n : neighbors) {
            if (n >= 0) {
                grid.union(n, index);
                backwash.union(n, index);
                if (backwash.connected(top, n)) {
                    grid.union(top, index);
                    backwash.union(top, index);
                }
            }
        }
    }

    /** A method that opens a Site if it is not open already.
     *
     * @param row
     * @param col
     */
    public void open(int row, int col) {
        int upperI = top - 1;
        if (!inRange(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("Row" + row + "and column" + col
                    + "are not between 0 and" + upperI);
        }
        int index = xyTo1D(row, col);
        if (!sites[index]) {
            sites[index] = true;
            openSites += 1;
            unionNeighbors(row, col);
        }
    }

    /** A method that checks if a Site is open.
     *
     * @param row
     * @param col
     * @return boolean
     */
    public boolean isOpen(int row, int col) {
        int lastI = top - 1;
        if (!inRange(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("Row" + row + "and column" + col
                    + "are not between 0 and " + lastI);
        }
        int index = xyTo1D(row, col);
        return sites[index];
    }

    /** A method that checks if a Site is full.
     *
     * @param row
     * @param col
     * @return boolean
     */
    public boolean isFull(int row, int col) {
        int lastI = top - 1;
        if (!inRange(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("Row" + row + "and column" + col
                    + "are not between 0 and " + lastI);
        }
        int index = xyTo1D(row, col);
        if (sites[index]) {
            return backwash.connected(index, top);
        }
        return false;
    }

    /** A method that returns the number of open Sites in this Percolation instance.
     *
     * @return int
     */
    public int numberOfOpenSites() {
        return openSites;
    }

    /** A method that checks if a Percolation instance percolates.
     *
     * @return boolean
     */
    public boolean percolates() {
        if (colSize == 1) {
            return openSites == 1;
        }
        return grid.connected(top, bottom);
    }

    /** A main method used for junit testing. */
    public static void main(String[] args) {
        Percolation p = new Percolation(1);
        p.open(0, 0);
        p.numberOfOpenSites();
        p.percolates();
    }
}
