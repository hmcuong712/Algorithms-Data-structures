/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String text = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(text);

        int first = 0;
        int len = csa.length();
        StringBuilder t = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            int originI = csa.index(i);
            if (originI == 0) {
                first = i;
                t.append(text.charAt(len - 1));
            }
            else t.append(text.charAt(originI - 1));
        }

        BinaryStdOut.write(first, 32);
        BinaryStdOut.write(t.toString());
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        // int first = 3;
        // String t = "ARD!RCAAAABB";
        int R = 256;
        int N = t.length();

        // int[] next = new int[N];
        int[] aux = new int[N]; // char instead of int
        int[] count = new int[R + 1];

        for (int i = 0; i < N; i++) count[t.charAt(i) + 1]++;
        for (int r = 0; r < R; r++) count[r + 1] += count[r];
        // for (int i = 0; i < N; i++) aux[count[t.charAt(i)]++] = t.charAt(i);
        for (int i = 0; i < N; i++) aux[count[t.charAt(i)]++] = i;

        // char prev = aux[0];
        // int bin = 0;
        // for (int i = 0; i < N; i++) {
        //     KMP kmp = new KMP(Character.toString(aux[i]));
        //     if (aux[i] == prev && i != 0) {
        //         next[i] = kmp.search(t.substring(bin + 1)) + bin + 1;
        //     }
        //     else next[i] = kmp.search(t);
        //     prev = aux[i];
        //     bin = next[i];
        // }

        StringBuilder sb = new StringBuilder(N);
        while (N > 0) {
            // sb.append(aux[first]);
            // first = next[first];
            // N--;
            int ind = aux[first];
            sb.append(t.charAt(ind));
            first = ind;
            N--;
        }
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.close();
    }


    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}
