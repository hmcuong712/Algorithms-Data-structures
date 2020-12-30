import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

/***************************************************
 *  Compilation:  javac-algs4 FastCollinearPoints.java
 *  Execution:    java-algs4 FastCollinearPoints
 *  Dependencies: Point.java, LineSegment.java
 ***************************************************/

public class FastCollinearPoints {
    private int count = 0;
    private int n;
    private LineSegment[] ls = new LineSegment[1];
    private Point[] copy;
    private Point[] tempPoint;
    private Point temp;

    public FastCollinearPoints(Point[] points) {   // finds all line segments containing 4 or more points

        // validation
        if (points == null)
            throw new IllegalArgumentException("You must present values to the constructor");

        // Copy the points array to a new one
        copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            copy[i] = points[i];
            if (points[i] == null) throw new IllegalArgumentException("Points contain null value(s)");
        }

        for (int i = 0; i < points.length; i++) {
            Comparator<Point> cmp = points[i].slopeOrder();
          /*  if (temp != null && temp.compareTo(points[i]) == 0)
                throw new IllegalArgumentException("Detect duplicate points");*/
            temp = points[i];
            Arrays.sort(copy);
            Arrays.sort(copy, cmp);
            getLineSegment(copy);
        }
    }

    public int numberOfSegments() { // the number of line segments
        return count;
    }

    public LineSegment[] segments() { // the line segments
        resize(count);
        LineSegment[] lsCopy = ls.clone();
        return lsCopy;
    }

    private void getLineSegment(Point[] points) {
        n = 1;
        tempPoint = new Point[points.length];
        tempPoint[0] = points[0]; // origin point
        if (points.length == 1)
            return; // do nothing
        tempPoint[1] = points[1]; // first point is always saved
        if (temp.slopeTo(points[1]) == Double.NEGATIVE_INFINITY)
            throw new IllegalArgumentException("Detect duplicate points");
        for (int i = 2; i < points.length; i++) {
            if (temp.slopeTo(points[i]) == Double.NEGATIVE_INFINITY)
                throw new IllegalArgumentException("Detect duplicate points");
            if (temp.slopeTo(points[i]) != temp.slopeTo(tempPoint[1])) {
                if (n < 3) { // less than 3 points
                    tempPoint[1] = points[i];
                    tempPoint[2] = null;
                } else { // we found 3 or more points
                    appendLS();
                    tempPoint[1] = points[i];
                }
                n = 1;
            } else if (temp.slopeTo(points[i]) == temp.slopeTo(tempPoint[1])) {
//                StdOut.println("With " + tempPoint[0] + " reference point is " + tempPoint[1] + " point being compared " + points[i]);
                n++;
                tempPoint[n] = points[i];
            }
        }
        if (n >= 3) {
            appendLS();
        }
    }

    private void appendLS() {
        Point min = tempPoint[1];
        Point max = tempPoint[n];
        if (min.compareTo(tempPoint[0]) > 0) {
            min = tempPoint[0];
            ls[count] = new LineSegment(min, max);
            count++;
            // Resize the ls array
            if (count == ls.length)
                resize(2 * ls.length);
        }
    }

    private void resize(int capacity) {
        assert capacity >= count;
        LineSegment[] copyLS = new LineSegment[capacity];
        for (int i = 0; i < count; i++)
            copyLS[i] = ls[i];
        ls = copyLS;
    }

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
