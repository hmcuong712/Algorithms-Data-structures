/* *****************************************************************************
 *  Name: Data type Circular Suffix Array
 *  Date: 14/09/2020
 *  Description: Implementation of CircularSuffixArray, based on class SuffixArray in java.util.Arrays
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final CircularSuffix[] suffixes;

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final String text; // reference to the text itself
        private final int ind; // reference to the first index

        public CircularSuffix(String text, int ind) {
            this.text = text;
            this.ind = ind;
        }

        public int compareTo(CircularSuffix cs) {
            if (cs == this) return 0;
            for (int i = 0; i < length(); i++) {
                if (this.charAt(i) < cs.charAt(i)) return -1;
                if (this.charAt(i) > cs.charAt(i)) return 1;
            }
            return 0;
        }

        private char charAt(int i) {
            int newInd = i + ind;
            if (newInd > length() - 1) return text.charAt(newInd - length());
            return text.charAt(newInd);
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        int len = s.length();
        // String[] CircularSuffix = new String[len];
        this.suffixes = new CircularSuffix[len];
        for (int i = 0; i < len; i++) {
            suffixes[i] = new CircularSuffix(s, i);
        }
        Arrays.sort(suffixes); // could improve, currently worst case performance is n^2 log n
    }

    // length of s
    public int length() {
        return suffixes.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) throw new IllegalArgumentException();
        return suffixes[i].ind;
    }

    // unit testing: required
    public static void main(String[] args) {
        CircularSuffixArray a = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < a.length(); i++) {
            StdOut.println(a.index(i));
        }
    }
}
