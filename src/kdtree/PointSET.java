/**************************************************************
 * Date: 30/07/20
 * Description: Brute-force solution
 * ************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {

    private SET<Point2D> pSet;
    private int count;

    public PointSET() {                   // construct an empty set of points
        pSet = new SET<Point2D>();
        count = 0;
    }

    public boolean isEmpty() {                     // is the set empty?
        return size() == 0;
    }

    public int size() {                     // number of points in the set
        return count;
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) {
            pSet.add(p);
            count++;
        }
    }

    public boolean contains(Point2D p) {           // does the set contain point p?
        if (p == null) throw new IllegalArgumentException();
        return pSet.contains(p);
    }

    public void draw() {                      // draw all points to standard draw
        for (Point2D p : pSet)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {          // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> q = new Stack<Point2D>();
        for (Point2D p : pSet)
            if (rect.contains(p))
                q.push(p);
        return q;
    }

    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        double r = 2;
        Point2D near = p;
        for (Point2D neighbor : pSet)
            if (p.distanceSquaredTo(neighbor) < r) {
                r = p.distanceSquaredTo(neighbor);
                near = neighbor;
            }
        return near;
    }

    public static void main(String[] args) {                // unit testing of the methods (optional)
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        RectHV rect = new RectHV(0.75, 0.25, 1.00, 0.75);
        for (Point2D i : brute.range(rect))
            StdOut.println(i);
        Point2D p = new Point2D(0.9, 0.5);
        StdOut.println("The nearest point is " + brute.nearest(p));
    }
}
