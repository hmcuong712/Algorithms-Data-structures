import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/******************************************************************
 * Date: 18/07/20
 * Compile: java-algs4 Board.java
 * Description: 8-puzzle solver
 *****************************************************************/

public class Board {

    private int[][] tiles;
    private int[][] aCopy;
    private int n;
    private int rowZ;
    private int colZ;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
//        this.tiles = new int[n][n];
        this.tiles = tiles;
        n = dimension();
        locBlank(this.tiles);
//        String nice = toString();
//        StdOut.println(nice);
    }

    // string representation of this board: code adapted from checklist
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        int n = tiles.length;
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hDistance = 0;
        int comp;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1)
                    comp = this.tiles[n - 1][n - 1]; // to handle the 0 case (which appears last in the goal board)
                else
                    comp = i * n + (j + 1);
                if (this.tiles[i][j] - comp != 0)
                    hDistance++;
            }
        }
        int out = hDistance;
        return out;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int a; // row of the goal board
        int b; // column of the goal board
        int mDistance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // handle the last column (n-1)
                if (tiles[i][j] % n == 0) {
                    a = tiles[i][j] / n - 1;
                    b = n - 1;
                    if (a < 0) {
                        a = i;
                        b = j;
                    } // handle the 0 case
                } else {
                    a = tiles[i][j] / n;
                    b = (tiles[i][j] % n) - 1;
//                    StdOut.println("element = " + tiles[i][j] + " a = " + a + " b = " + b);
//                    StdOut.println(tiles[i][j] % n - 1);
                }
                mDistance = mDistance + Math.abs(i - a) + Math.abs(j - b);
            }
        }
        return mDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (manhattan() == 0 || hamming() == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if ((Board) y != null) {
            Board that = (Board) y;
            if (that.n == this.n && Arrays.deepEquals(this.tiles, that.tiles)) return true;
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() { // how to implement this?
//        int nNeighbors = numNeighbors(this.tiles);
        aCopy = new int[n][n];

        Queue<Board> q = new LinkedList<Board>();
//        StdOut.println("Row = " + rowZ + " Col = " + colZ + " n = " + n);
        if (colZ - 1 >= 0) q.add(moveLeft());
        if (colZ + 1 <= n - 1) q.add(moveRight());
        if (rowZ - 1 >= 0) q.add(moveUp());
        if (rowZ + 1 <= n - 1) q.add(moveDown());
        return q;
    }

    private void locBlank(int[][] tiles) {
        int i = 0;
        int j = 0;
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    rowZ = i;
                    colZ = j;
                    break;
                }
            }
        }
    }

    private Board moveLeft() {
        aCopy = cloneArray();
        exchange(aCopy, rowZ, colZ, rowZ, colZ - 1);
        return new Board(aCopy);
    }

    private Board moveRight() {
        aCopy = cloneArray();
        exchange(aCopy, rowZ, colZ, rowZ, colZ + 1);
        return new Board(aCopy);
    }

    private Board moveDown() {
        aCopy = cloneArray();
        exchange(aCopy, rowZ, colZ, rowZ + 1, colZ);
        return new Board(aCopy);
    }

    private Board moveUp() {
        aCopy = cloneArray();
        exchange(aCopy, rowZ, colZ, rowZ - 1, colZ);
        return new Board(aCopy);
    }

    private void exchange(int[][] tiles, int row1, int col1, int row2, int col2) {
        int temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
    }

    private int[][] cloneArray() {
        int[][] newCopy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                newCopy[i][j] = this.tiles[i][j];
        }
        return newCopy;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        aCopy = cloneArray();
//        while (true) {
//            int row1 = StdRandom.uniform(n);
//            int col1 = StdRandom.uniform(n);
//            int row2 = StdRandom.uniform(n);
//            int col2 = StdRandom.uniform(n);
//            if (row1 != rowZ && row2 != rowZ || col1 != colZ && col2 != colZ) {
//                if (row1 != row2 || col1 != col2) {
//                    exchange(aCopy, row1, col1, row2, col2);
//                    break;
//                }
//            }
//        }
        if (colZ - 1 >= 0 && colZ + 1 <= n - 1) exchange(aCopy, rowZ, colZ - 1, rowZ, colZ + 1);
        if (rowZ - 1 >= 0 && rowZ + 1 <= n - 1) exchange(aCopy, rowZ - 1, colZ, rowZ + 1, colZ);
        if (rowZ == 0) exchange(aCopy, n - 1, 0, n - 1, n - 1);
        if (rowZ == n - 1) exchange(aCopy, 0, 0, 0, n - 1);
        Board twinBoard = new Board(aCopy);
        return twinBoard;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial);
        StdOut.println(initial.twin());
        StdOut.println(initial.twin().twin());

//        StdOut.println(initial.equals("LALALA"));
//        StdOut.println(initial.manhattan());
////        StdOut.println(twin);
//
//        for (Board board : initial.neighbors()) {
//            StdOut.print(board);
//            StdOut.println(board.manhattan());
//        }
    }
}

