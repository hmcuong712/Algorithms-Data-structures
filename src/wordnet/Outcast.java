/* *****************************************************************************
 *  Name: Outcast.java
 *  Date: 22/08/20
 *  Description: Given a list of WordNet nouns x1, x2, ..., xn, which noun is the least related to the others?
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int champion = 0;
        int distance;
        String winner = nouns[0];
        for (String s : nouns) {
            distance = 0;
            for (String t : nouns) {
                distance += wordnet.distance(s, t);
            }
            // StdOut.println("distance is " + distance + " string is " + s + " champ = " + champion);
            if (distance > champion) {
                champion = distance;
                winner = s;
            }
        }
        return winner;
    }

    // testing client
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
