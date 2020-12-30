/* *****************************************************************************
 *  Name: SAP.java
 *  Date: 20/08/20
 *  Description: Shortest acestral path
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.List;

public class SAP {

    private BreadthFirstDirectedPaths bfsV;
    private BreadthFirstDirectedPaths bfsW;
    private LinearProbingHashST<List<Integer>, List<Integer>> hashST;
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)

    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = G;
        hashST
                = new LinearProbingHashST<List<Integer>, List<Integer>>(); // <<v, w>, <ancestor, length>>
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0) throw new IllegalArgumentException();
        List<Integer> key = Arrays.asList(v, w);
        if (!hashST.contains(key)) cacheVW(v, w);
        List<Integer> a = hashST.get(key);
        return a.get(1);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0) throw new IllegalArgumentException();
        List<Integer> key = Arrays.asList(v, w);
        if (!hashST.contains(key)) cacheVW(v, w);
        List<Integer> a = hashST.get(key);
        return a.get(0);
    }

    private void cacheVW(int v, int w) {
        int champion = G.E();
        int length = -1;
        int ancestor = -1;
        List<Integer> key = Arrays.asList(v, w);
        List<Integer> keySymmetry = Arrays.asList(w, v);
        List<Integer> value = Arrays.asList(ancestor, length);
        bfsV = new BreadthFirstDirectedPaths(G, v);
        bfsW = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                length = bfsV.distTo(i) + bfsW.distTo(i);
                if (length <= champion) {
                    ancestor = i;
                    value = Arrays.asList(ancestor, length);
                    champion = length;
                }
            }
            hashST.put(key, value);
            hashST.put(keySymmetry, value);
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null && w == null) return -1;
        int length = iterableCacheVW(v, w).get(0);
        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null && w == null) return -1;
        int ancestor = iterableCacheVW(v, w).get(1);
        return ancestor;
    }

    private List<Integer> iterableCacheVW(Iterable<Integer> v, Iterable<Integer> w) {
        int champion = G.E();
        int length = -1;
        int ancestor = -1;
        bfsV = new BreadthFirstDirectedPaths(G, v);
        bfsW = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                length = bfsV.distTo(i) + bfsW.distTo(i);
                if (length <= champion) {
                    ancestor = i;
                    champion = length;
                }
            }
        }
        if (ancestor == -1) champion = -1;
        return Arrays.asList(champion, ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP s = new SAP(G);
        // StdOut.println("ancestor is " + s.ancestor(3, 11) + ", length is " + s.length(3, 11));

        // Test the generalized version
        List<Integer> v = Arrays.asList(13, 23, 24);
        List<Integer> w = Arrays.asList(6, 16, 17);
        StdOut.println("ancestor is " + s.ancestor(v, w) + ", length is " + s.length(v, w));

/*        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            // StdOut.println(9999);
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }*/
    }
}
