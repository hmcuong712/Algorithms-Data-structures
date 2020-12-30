/* *****************************************************************************
 *  Name: WordNet.java
 *  Date: 21/08/2020
 *  Description: The hardest part
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    private LinearProbingHashST<Integer, String> st;
    private LinearProbingHashST<String, Queue<Integer>> ts;
    private Digraph G;
    private SAP sap;
    private int countV = 0;
    private boolean oneRoot = true;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        // File fileSynsets = new File(synsets);
        // File fileHypernyms = new File(hypernyms);
        readSynsets(synsets);
        readHypernyms(hypernyms);

        // check if G is rooted DAG, if so throw Illegal
        DirectedCycle DC = new DirectedCycle(G);
        if (!oneRoot || DC.hasCycle()) throw new IllegalArgumentException(
                "input to the constructor does not correspond to a rooted DAG");
        sap = new SAP(G);
    }

    // private helper method for the constructor: reading synsets from file
    private void readSynsets(String synsets) {
        if (synsets == null) throw new IllegalArgumentException();
        st = new LinearProbingHashST<Integer, String>();
        ts = new LinearProbingHashST<String, Queue<Integer>>();
        In in = new In(synsets);
        while (!in.isEmpty()) {
            // First ST: Integer as key and string as val
            String[] a = in.readLine().split(",");
            int key = Integer.parseInt(a[0]);
            String val = a[1];
            if (!st.contains(key)) {
                st.put(key, val);
                countV++;
            }

            // Second ST: String as key and Queue of integers as val
            String[] b = val.split(" ");
            for (String s : b) {
                if (!ts.contains(s)) ts.put(s, new Queue<Integer>());
                ts.get(s).enqueue(key);
            }
        }
    }

    // private helper method for the constructor: reading hypernyms from file
    private void readHypernyms(String hypernyms) {
        if (hypernyms == null) throw new IllegalArgumentException();
        int nRoot = 0;
        In in = new In(hypernyms);
        G = new Digraph(countV);
        while (!in.isEmpty()) {
            String[] a = in.readLine().split(",");
            // if (a.length == 1) nRoot++;
            int v = Integer.parseInt(a[0]);
            for (int i = 1; i < a.length; i++) {
                G.addEdge(v, Integer.parseInt(a[i]));
            }
            // StdOut.println("Out degree is " + G.outdegree(v) + " vertex is " + v);
            // if (G.outdegree(v) == 0) nRoot++;
        }
        for (int i = 0; i < G.V(); i++)
            if (G.outdegree(i) == 0) nRoot++;
        if (nRoot > 1) oneRoot = false;
        // StdOut.println("Number of vertices is " + G.V());
        // StdOut.println("Number of edges is " + G.E());
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return ts.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return ts.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Queue<Integer> a = ts.get(nounA);
        Queue<Integer> b = ts.get(nounB);
        int length = sap.length(a, b);
        return length;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Queue<Integer> a = ts.get(nounA);
        Queue<Integer> b = ts.get(nounB);
        int ancestor = sap.ancestor(a, b);
        return st.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // WordNet wn = new WordNet("synsets100-subgraph.txt", "hypernyms100-subgraph.txt");
        // WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        WordNet wn = new WordNet("synsets6.txt", "hypernyms6InvalidTwoRoots.txt");
        StdOut.println(wn.sap(args[0], args[1]));
        StdOut.println(wn.distance(args[0], args[1]));
    }
}
