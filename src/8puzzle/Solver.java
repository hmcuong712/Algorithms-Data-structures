import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/******************************************************************
 * Date: 19/07/20
 * Compile: java-algs4 Board.java
 * Description: 8-puzzle solver
 *****************************************************************/

public class Solver {
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode prev;
        private int stepNode;
        private int priority;

        // constructor
        public SearchNode(Board board, SearchNode prev, int stepNode) {
            this.board = board;
            this.stepNode = stepNode;
            this.prev = prev;
            this.priority = priority(board);
        }

        public int compareTo(SearchNode that) {
            if (this.priority == that.priority) return 0;
            else if (this.priority < that.priority || (this.priority == that.priority && this.board.manhattan() < that.board.manhattan()))
                return -1;
            else return 1;
        }

        // return the priority
        public int priority(Board board) {
            int priority = stepNode + board.manhattan();
            return priority;
        }
    }

    private SearchNode firstNode;
    private SearchNode firstNodeTwin;
    private SearchNode currentMin;
    private SearchNode currentMinTwin;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();
        firstNode = new SearchNode(initial, null, 0);
        firstNodeTwin = new SearchNode(initial.twin(), null, 0);

//        currentMin = firstNode;
        pq.insert(firstNode);
        pqTwin.insert(firstNodeTwin);
        // while (true) {
        while (true) {
            currentMin = pq.delMin(); 
            currentMinTwin = pqTwin.delMin();
            Board prev = currentMin.board;
            Board prevTwin = currentMinTwin.board;

            if (prev.isGoal()) {
                solvable = true;
                break;
            } else if (prevTwin.isGoal()) {
                solvable = false;
                break;
            }

            for (Board neighbor : prev.neighbors()) {
                SearchNode nextNode = new SearchNode(neighbor, currentMin, currentMin.stepNode + 1);
                if (nextNode.stepNode >= 2) {
                    if (neighbor.equals(currentMin.prev.board) == false) // critical optimization
                        pq.insert(nextNode);
                } else
                    pq.insert(nextNode);
            }

            for (Board neighborTwin : prevTwin.neighbors()) {
                SearchNode nextNodeTwin = new SearchNode(neighborTwin, currentMinTwin, currentMinTwin.stepNode + 1);
                if (nextNodeTwin.stepNode >= 2) {
                    if (neighborTwin.equals(currentMinTwin.prev.board) == false) // critical optimization
                        pqTwin.insert(nextNodeTwin);
                } else
                    pqTwin.insert(nextNodeTwin);
            }
        }
//        solvable = false;
//        StdOut.print("step = " + step);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        boolean b = solvable;
        return b;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        int moves = -1;
        if (isSolvable())
            moves = currentMin.stepNode;
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Queue<Board> q2 = new LinkedList<Board>();
        Stack<Board> q = new Stack<Board>();
        int step = moves();
        if (isSolvable()) {
            q.push(currentMin.board);
            for (int i = 0; i < step - 1; i++) {
                currentMin = currentMin.prev;
                q.push(currentMin.board);
            }
            while (!q.isEmpty())
                q2.add(q.pop());
        }
        return q2;
    }

    // test client (see below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // return the solution
        StdOut.println("The shortest solution is " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
}
