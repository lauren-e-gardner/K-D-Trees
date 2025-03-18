import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

public class PointST<Value> {

    private RedBlackBST<Point2D, Value> tree; // empty BST

    // construct an empty symbol table of points
    public PointST() {
        tree = new RedBlackBST<Point2D, Value>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return tree.size() == 0;
    }

    // number of points
    public int size() {
        return tree.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null) throw new IllegalArgumentException();
        tree.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!tree.contains(p)) return null;

        return tree.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return tree.contains(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return tree.keys();
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle is null");

        // queue for points inside rectangle
        Queue<Point2D> iterable = new Queue<>();

        // iterates through points and enqueues if inside rectangle
        for (Point2D p : tree.keys()) {
            if (rect.contains(p)) iterable.enqueue(p);
        }
        // returns queue
        return iterable;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point is null");

        if (tree.isEmpty()) return null;

        // iterates through points, and updates champ distance and point
        // if current distance is closer to search point than champ distance
        double distance;
        double champ = Double.POSITIVE_INFINITY;
        Point2D champPoint = new Point2D(0, 0);
        for (Point2D point : tree.keys()) {
            distance = point.distanceSquaredTo(p);
            if (distance <= champ) {
                champ = distance;
                champPoint = point;
            }
        }
        return champPoint;
    }

    // unit testing (required)
    public static void main(String[] args) {

        // UNIT TESTING ALL METHODS
        PointST<String> test = new PointST<String>();
        StdOut.println("Is BST Empty?: " + test.isEmpty());
        Point2D test1 = new Point2D(0, 0);
        Point2D test2 = new Point2D(0, 4);
        Point2D test3 = new Point2D(4, 4);
        Point2D test4 = new Point2D(1, 0);
        test.put(test2, "hello");
        test.put(test3, "solute");
        test.put(test4, "guten tag");
        StdOut.println("Size: " + test.size());
        StdOut.println("Nearest point to (0,0): " + test.nearest(test1));
        StdOut.println("Get (0, 4): " + test.get(test2));
        StdOut.println("Size: " + test.size());
        StdOut.println("Contains (0, 4)?: " + test.contains(test2));
        StdOut.println("All points: " + test.points());
        RectHV testRect = new RectHV(0.0, 0.0, 2.0, 2.0);
        StdOut.println("Points in range: " + test.range(testRect));

        // TIMING ANALYSIS
        // read in points from file
        String filename = args[0];
        In in = new In(filename);
        KdTreeST<Integer> kdtree = new KdTreeST<Integer>();

        // create k-d tree
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();

            Point2D p = new Point2D(x, y);
            kdtree.put(p, i);
        }

        // call nearest() m times and record elapsed seconds
        int m = 1000;
        Stopwatch time = new Stopwatch();
        for (int i = 0; i < m; i++) {
            kdtree.nearest(new Point2D(StdRandom.uniform(0.0, 1.0),
                                       StdRandom.uniform(0.0, 1.0)));
        }
        double t = time.elapsedTime();
        StdOut.println("seconds: " + t + " calls/second: " + m / t);
    }
}
