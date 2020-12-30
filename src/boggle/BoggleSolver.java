/* *****************************************************************************
 *  Name:
 *  Date: 07/09/20
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class BoggleSolver {

    private BoggleBoard board;
    private final TrieSET26 dictSET;
    private Node[][] balls;
    private TrieSET26 validWords;

    private class Node {
        private final String c;
        private final int i;
        private final int j;
        private Iterable<Node> adj;

        public Node(int i, int j) {
            this.i = i;
            this.j = j;

            Character l = board.getLetter(i, j);
            // if (l == 'Q') c = new String("QU");
            if (l == 'Q') c = "QU";
            else c = l.toString();
            adj = new Queue<Node>();
            // getAdjacent();
        }

        public Iterable<Node> adjacent() {
            return adj;
        }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException();
        dictSET = new TrieSET26();

        for (String key : dictionary) {
            dictSET.add(key);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException();
        int r = board.rows();
        int c = board.cols();

        validWords = new TrieSET26();
        this.board = board;
        balls = new Node[r][c];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                Node ball = new Node(i, j);
                balls[i][j] = ball;
            }
        }

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                balls[i][j].adj = getAdjacent(board, balls[i][j]);
            }
        } // can we merge this loop into the above one?

        boolean[][] marked;
        int[][] edgeTo;
        StringBuilder str;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                marked = new boolean[r][c];
                str = new StringBuilder();

                str.append(balls[i][j].c);
                dfs(balls[i][j], marked, str);
            }
        }
        return validWords;
    }

    private Iterable<Node> getAdjacent(BoggleBoard board, Node node) {
        Queue<Node> next = new Queue<Node>();
        int[] a = new int[] { 0, -1, 1 };

        for (int m : a) {
            for (int n : a) {
                if (node.i + m >= 0 && node.i + m < board.rows() && node.j + n >= 0
                        && node.j + n < board.cols()) {
                    next.enqueue(balls[node.i + m][node.j + n]);
                }
            }
        }
        next.dequeue();
        return next;
    }

    private void dfs(Node ball, boolean[][] marked, StringBuilder str) {
        marked[ball.i][ball.j] = true;
        String candidate = str.toString(); // bottleneck?
        if (isWord(candidate) && !validWords.contains(candidate) && candidate.length() >= 3) {
            validWords.add(candidate);
        }
        for (Node w : ball.adj)
            if (!marked[w.i][w.j]) {
                // edgeTo[w.i][w.j] = s;
                str.append(w.c);
                candidate = str.toString();
                // if (size(dictSET.keysWithPrefix(candidate)) != 0) { // THIS IS A HUGE BOTTLENECK!
                if (dictSET.containsPrefix(candidate)) { // bottleneck?
                    dfs(w, marked, str);
                }
                // else str.deleteCharAt(str.length() - 1);
                else deleteLastChar(str);
            }
        marked[ball.i][ball.j] = false; // test to see if removing the mark does help
        // str.deleteCharAt(str.length() - 1);
        deleteLastChar(str);
    }

    private boolean isWord(String candidate) {
        return dictSET.contains(candidate);
    }

    private void deleteLastChar(StringBuilder s) {
        int l = s.length();
        if (l >= 2) {
            Character penultimate = s.charAt(l - 2);
            if (penultimate.equals('Q')) {
                s.deleteCharAt(s.length() - 1);
                s.deleteCharAt(s.length() - 1);
            }
            else s.deleteCharAt(s.length() - 1);
        }
        else s.deleteCharAt(s.length() - 1);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException();
        if (!dictSET.contains(word)) return 0;
        int l = word.length();
        if (l == 3 || l == 4) return 1;
        else if (l == 5) return 2;
        else if (l == 6) return 3;
        else if (l == 7) return 5;
        else if (l >= 8) return 11;
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        Stopwatch sw = new Stopwatch();
        BoggleSolver solver = new BoggleSolver(dictionary);
        StdOut.println("Elapsed time: " + sw.elapsedTime() + " seconds.");

        // BoggleBoard board = new BoggleBoard(args[1]);
        // int score = 0;
        // for (String word : solver.getAllValidWords(board)) {
        //     StdOut.println(word);
        //     score += solver.scoreOf(word);
        // }
        // StdOut.println("Score = " + score);
    }
}
