import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/***************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    java BruteCollinearPoints
 *  Dependencies: Point.java, LineSegment.java
 ***************************************************/

public class BruteCollinearPoints {
    private int count = 0;
    private Point min;
    private Point max;
    private LineSegment[] ls = new LineSegment[1];

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null)
            throw new IllegalArgumentException("You must present values to the constructor");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("Points contain null value(s)");
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("Points must not contain duplicates");
                for (int k = j + 1; k < points.length; k++)
                    for (int m = k + 1; m < points.length; m++) {
                        if (areCollinear(points[i], points[j], points[k], points[m])) {
                            min = findMin(points[i], points[j], points[k], points[m]);
                            max = findMax(points[i], points[j], points[k], points[m]);
                            ls[count] = new LineSegment(min, max);
                            count++;
                            if (count == ls.length)
                                resize(2 * ls.length);
                        }
                    }
            }
        }
    }

    public int numberOfSegments() {    // the number of line segments
        return count;
    }

    public LineSegment[] segments() { // this means returning the line segments
        resize(count);
        LineSegment[] lsCopy = ls.clone();
        return lsCopy;
    }

    private boolean less(Point i, Point j) { // return true if i < j
        return i.compareTo(j) < 0;
    }

    private static boolean areCollinear(Point p, Point q, Point r, Point s) {
        if (p.slopeTo(q) == p.slopeTo(r) && p.slopeTo(q) ==
                p.slopeTo(s)) return true;
        return false;
    }

    private Point findMin(Point p, Point q) {
        if (less(p, q)) return p;
        else return q;
    }

    private Point findMin(Point p, Point q, Point r, Point s) {
        Point p1 = findMin(p, q);
        Point p2 = findMin(r, s);
        return findMin(p1, p2);
    }

    private Point findMax(Point p, Point q) {
        if (less(p, q)) return q;
        else return p;
    }

    private Point findMax(Point p, Point q, Point r, Point s) {
        Point p1 = findMax(p, q);
        Point p2 = findMax(r, s);
        return findMax(p1, p2);
    }

    private void resize(int capacity) {
        assert capacity >= count;
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < count; i++)
            copy[i] = ls[i];
        ls = copy;
    }

    /* private void validate(Point p, Point q, Point r, Point s) {
        if (p == q || p == r || p == s || q == r || q == s || r == s)
            throw new IllegalArgumentException("Points must not contain duplicates");
    } */ // Maybe leave this out because i and j can already traverse through the whole array of points.

    // Testing client
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
