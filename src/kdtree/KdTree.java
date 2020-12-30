/**************************************************************
 * Date: 01/08/20
 * Description: Kd-Tree implementation
 * Compile: javac-algs4 KdTree.java
 * Run: java-algs4 KdTree filename.txt
 * ************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class KdTree {

    private Node root;
    private int count;
    private int step;
    private RectHV rectNode; // not sure
    private int cmp;
    private Stack<Node> r;
    private Point2D champion;
    private Point2D prev;

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb; // left/bottom
        private Node rt; // right/top
        private int stepNode;
    }

    public KdTree() {
        rectNode = new RectHV(0.0, 0.0, 1.0, 1.0);
    }

    public boolean isEmpty() {              // is the set empty?
//        return size() == 0;
        return root == null;
    }

    public int size() {                       // number of points in the set
        return count;
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) {
            step = 1;
            root = put(root, p);
            count++;
        }
    }

    private Node put(Node x, Point2D p) {
        if (x == null) {
            Node add = new Node();
            add.p = p;
            add.rect = getRectNode(p);
            add.stepNode = step;
            return add;
        }
        if (x.stepNode == 1) {
            cmp = compareX(x, p);
        } else if (x.stepNode == 2) {
            cmp = compareY(x, p);
        }
        rectNode = x.rect;
        prev = x.p;
        if (cmp < 0) {
            if (x.stepNode == 1) step = 2;
            else step = 1;
            x.lb = put(x.lb, p);
        } else {
            if (x.stepNode == 1) step = 2;
            else step = 1;
            x.rt = put(x.rt, p);
        }
        return x;
    }

    private int compareX(Node x, Point2D p) { // compare inserting point to the node above
        if (p.x() < x.p.x()) return -1;
        else if (p.x() > x.p.x()) return 1;
        else return 0;
    }

    private int compareY(Node x, Point2D p) { // compare inserting point to the node above
        if (p.y() < x.p.y()) return -1;
        else if (p.y() > x.p.y()) return 1;
        else return 0;
    }

    private RectHV getRectNode(Point2D p) {
        if (isEmpty()) return rectNode;
        if (step == 2 && cmp < 0) return new RectHV(rectNode.xmin(), rectNode.ymin(), prev.x(), rectNode.ymax());
        else if (step == 1 && cmp < 0) return new RectHV(rectNode.xmin(), rectNode.ymin(), rectNode.xmax(), prev.y());
        else if (step == 2 && cmp >= 0) return new RectHV(prev.x(), rectNode.ymin(), rectNode.xmax(), rectNode.ymax());
        else return new RectHV(rectNode.xmin(), prev.y(), rectNode.xmax(), rectNode.ymax());
    }

    public boolean contains(Point2D p) {           // does the set contain point p? Should be similar to get() in BST
        if (p == null) throw new IllegalArgumentException();
        step = 1;
        return get(root, p);
    }

    private boolean get(Node x, Point2D p) {
        if (x == null) return false;
        int cmpX;
        int cmpY;
//        int cmp2 = 0;
        if (step == 1) {
            cmpX = compareX(x, p);
            cmp = cmpX;
            if (cmpX == 0) {
                cmpY = compareY(x, p);
                if (cmpY == 0) return true;
            }
            step++;
        } else if (step == 2) {
            cmpY = compareY(x, p);
            cmp = cmpY;
            if (cmpY == 0) {
                cmpX = compareX(x, p);
                if (cmpX == 0) return true;
            }
            step--;
        }
//        if (cmpX == 0 || cmpY == 0) return true;
//        if (cmp < 0) return get(x.lb, p);
        if (cmp >= 0) return get(x.rt, p);
        else return get(x.lb, p);
        // return false;
    }

    public void draw() {                        // draw all points to standard draw
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) return;
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        x.p.draw();
        StdDraw.setPenRadius();
        if (x.stepNode == 1) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        draw(x.lb);
        draw(x.rt);
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle (or on the boundary)
        // check if rectangle intersects
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> s = new Stack<Point2D>();
        // recursively search for points
        // does the rectangle contains points?
        // if yes, add to the stack
        r = new Stack<Node>();
        rangeSearch(rect, root);
        for (Node n : r)
            if (rect.contains(n.p)) s.push(n.p);
        return s;
    }

    private void rangeSearch(RectHV rect, Node x) {
        if (x == null) return;
        else {
            if (rect.intersects(x.rect)) {
                r.push(x);
                rangeSearch(rect, x.lb);
                rangeSearch(rect, x.rt);
            }
            return;
        }
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        champion = root.p;
        nearestSearch(root, p);
        return champion;
    }

    private void nearestSearch(Node x, Point2D p) {
        if (x == null || p.distanceSquaredTo(champion) < x.rect.distanceSquaredTo(p))
            return; // was rect.distanceSquaredTo champion previously
        else {
            if (p.distanceSquaredTo(x.p) <= p.distanceSquaredTo(champion)) champion = x.p;
            // now choose the subtree that's on the same side with the query
            if ((x.stepNode == 1 && compareX(x, p) < 0) || (x.stepNode == 2 && compareY(x, p) < 0)) {
                nearestSearch(x.lb, p);
//                StdOut.println("Second search, point being search is " + x.rt.p + ", distance = " + p.distanceSquaredTo(champion) + " distance to rect = " + x.rt.rect.distanceSquaredTo(p));
                nearestSearch(x.rt, p);
            } else {
                nearestSearch(x.rt, p);
                nearestSearch(x.lb, p);
            }
        }
        return;
    }

    public static void main(String[] args) {            // unit testing of the methods (optional)
        String filename = args[0];
        In in = new In(filename);
        KdTree tree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }
        Point2D p = new Point2D(0.693, 0.659);
//        if (tree.contains(p)) StdOut.println("Found the node " + p);
        RectHV rect = new RectHV(0.10, 0.0, 0.69, 0.95);
        for (Point2D i : tree.range(rect))
            StdOut.println(i);
        StdOut.println("The nearest point is " + tree.nearest(p));
        tree.draw();
        StdDraw.setPenRadius(0.03);
        p.draw();
    }
}
