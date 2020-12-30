/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int EXTENDED_ASCII = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] a = new char[EXTENDED_ASCII];
        for (int i = 0; i < EXTENDED_ASCII; i++)
            a[i] = (char) i;

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            for (int i = 0; i < EXTENDED_ASCII; i++) {
                if (a[i] == c) {
                    BinaryStdOut.write(i, 8);
                    System.arraycopy(a, 0, a, 1, i);
                    a[0] = c;
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] a = new char[EXTENDED_ASCII];
        for (int i = 0; i < EXTENDED_ASCII; i++)
            a[i] = (char) i;

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(a[c]);
            char temp = a[c];
            System.arraycopy(a, 0, a, 1, (int) c);
            a[0] = temp;
        }
        BinaryStdOut.close();
    }

/*    private void moveFirst(char[] a, char c, int pos) {
        char[] aux = new char[a.length];
        aux[0] = c;
        System.arraycopy(a, 0, aux, 1, pos);
        System.arraycopy(a, pos + 1, aux, pos + 1, a.length - pos);
        a = aux;
    }*/

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }

}
