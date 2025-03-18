import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

public class KdTreeST<Value> {
    /* @citation Adapted from: https://algs4.cs.princeton.edu/32bst/BST.java.html.
    Accessed 10/09/2021. */

    private static final double INF = Double.POSITIVE_INFINITY;
    private static final double NEG_INF = Double.NEGATIVE_INFINITY;
    private Node root; // root Node
    private int size; // number of keys in tree
    private Point2D champPoint; // closest point tracker for nearest()
    private double champDist; // closest distance tracker for nearest()


    private class Node {
        private Point2D p; // point associated with Node
        private Value val; // value of Node
        private Node left, right; // left and right references
        private boolean level; // vertical or horizontal line
        private RectHV rect; // rectangle for each Node bounds

        // Node constructor
        public Node(Point2D p, Value val, boolean level, RectHV rect) {
            this.p = p;
            this.val = val;
            this.level = level;
            this.rect = rect;
        }
    }

    // construct an empty symbol table of points
    public KdTreeST() {
        size = 0;
        // initializes champ point and distance for use in nearest()
        champPoint = new Point2D(0, 0);
        champDist = INF;
    }

    // checks if the symbol table is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points
    public int size() {
        return size;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null) throw new
                IllegalArgumentException("calls put() with null argument(s)");

        // infinite rectangle bounds for root node
        RectHV rect = new RectHV(NEG_INF, NEG_INF, INF, INF);

        // if tree is empty, constructs root node
        if (root == null) {
            // vertical level = false; horizontal = true;
            root = new Node(p, val, false, rect);
            size++;
        }

        // if tree isn't empty, recursively calls put() helper
        // in order to put new node at correct position
        else put(root, p, val, root.level, rect);
    }

    // private helper method to enable put() recursion
    private Node put(Node x, Point2D p, Value val, boolean level,
                     RectHV rect) {

        // base case when recursion reaches end of tree
        // adds new Node with correct arguments
        if (x == null) {
            Node newNode = new Node(p, val, level, rect);
            size++;
            return newNode;
        }

        // compares x & y values of point, for later use depending on level
        int cmpX = Double.compare(p.x(), x.p.x());
        int cmpY = Double.compare(p.y(), x.p.y());

        // vertical level
        if (!level) {
            // if equal point, replace old value with new
            if (x.p.equals(p)) {
                x.p = p;
                x.val = val;
            }
            // if x val of point is less than x val of node,
            // revise rectangle bounds and recurse left
            else if (cmpX < 0) {
                rect = new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax());
                x.left = put(x.left, p, val, true, rect);
            }
            // if x val of point is greater than x val of node,
            // revise rectangle bounds and recurse right
            else {
                rect = new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                x.right = put(x.right, p, val, true, rect);
            }
        }

        // horizontal level
        else {
            // if equal point, replace old value with new
            if (x.p.equals(p)) {
                x.p = p;
                x.val = val;
            }
            // if y val of point is less than y val of node,
            // revise rectangle bounds and recurse left
            else if (cmpY < 0) {
                rect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y());
                x.left = put(x.left, p, val, false, rect);
            }
            // if y val of point is greater than y val of node,
            // revise rectangle bounds and recurse right
            else {
                rect = new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax());
                x.right = put(x.right, p, val, false, rect);
            }
        }
        return x;
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) throw new
                IllegalArgumentException("calls get() with null point");

        // recursively calls get() helper until point is found or end of tree
        // starts at root, vertical level
        return get(root, p, false);
    }

    // private helper method to enable get() recursion
    private Value get(Node x, Point2D p, boolean level) {

        // returns null if end of tree is reached and point wasn't found
        if (x == null) return null;

        // compares x & y values of point, for later use depending on level
        int cmpX = Double.compare(p.x(), x.p.x());
        int cmpY = Double.compare(p.y(), x.p.y());

        // vertical level
        if (!level) {
            // if point found, return value
            if (x.p.equals(p)) return x.val;
                // if x val of point is less than x val of node, search left subtree
            else if (cmpX < 0) return get(x.left, p, true);
                // if x val of point is greater than x val of node,
                // search right subtree
            else return get(x.right, p, true);
        }

        // horizontal level
        else {
            // if point found, return value
            if (x.p.equals(p)) return x.val;
                // if y val of point is less than y val of node, search left subtree
            else if (cmpY < 0) return get(x.left, p, false);
                // if y val of point is greater than y val of node,
                // search right subtree
            else return get(x.right, p, false);
        }
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new
                IllegalArgumentException("call contains() with null point");
        // searches for p, and returns whether or not p was found
        return get(p) != null;
    }

    // all points in the symbol table; level order traversal
    public Iterable<Point2D> points() {
        /* @citation Copied from: https://algs4.cs.princeton.edu/32bst/BST.java.html.
        Accessed 10/10/2021. */
        // queue for points
        Queue<Point2D> points = new Queue<Point2D>();
        // queue for nodes
        Queue<Node> queue = new Queue<Node>();
        // starts at root
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            // examines current node
            Node x = queue.dequeue();
            // restarts loop if reaches null node
            if (x == null) continue;
            // adds current point to queue of points
            points.enqueue(x.p);
            // will check left sub-node, then right sub-node
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        // returns queue of points (not nodes)
        return points;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("rect is null!");

        // initializes queue to hold points inside rectangle
        Queue<Point2D> points = new Queue<Point2D>();
        // recursively searches inside rectangle for points
        return range(rect, root, points);
    }

    // private helper method to enable range recursion
    private Iterable<Point2D> range(RectHV rect, Node x, Queue<Point2D> points) {

        // returns queue of points if reaches end of tree
        if (x == null) return points;

        // returns queue of points if point being examined
        // is no longer in rectangle range
        if (!rect.intersects(x.rect)) return points;

        // if point is in rectangle, add to queue of points and search
        // left and right subtrees recursively
        if (rect.contains(x.p)) points.enqueue(x.p);
        range(rect, x.left, points);
        range(rect, x.right, points);
        return points;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument");
        if (isEmpty()) return null;

        // initializes champPoint to random point (which will be immediately rewritten)
        // initializes champDist to infinite distance
        champPoint = new Point2D(0, 0);
        champDist = INF;

        // recursively searches for nearest point, starting at root of tree
        return nearest(root, p);
    }

    // private helper method to implement nearest() recursion
    private Point2D nearest(Node x, Point2D p) {
        // when end of tree reached, return running champion point
        if (x == null)
            return champPoint;

        // distance from node's bounded box to search point
        double boxDist = x.rect.distanceSquaredTo(p);

        // prunes subtrees if point is not inside node's box
        // and node's point cannot possibly be closer than running champion
        if (!((p.x() > x.rect.xmin() && p.x() < x.rect.xmax()) && (p.y() >
                x.rect.ymin() && p.y() < x.rect.ymax()))) {
            if (boxDist >= champDist) return champPoint;
        }


        // distance from node's point to search point
        double tempDist = p.distanceSquaredTo(x.p);

        // if node is closer than running champion, updates champion distance
        // and point
        if (tempDist < champDist) {
            champDist = tempDist;
            champPoint = x.p;
        }

        // vertical level
        if (!x.level) {
            // if point's x val is left of node's x val, search left subtree first
            if (x.p.x() > p.x()) {
                nearest(x.left, p);
                nearest(x.right, p);

            }
            // if point's x val is equal to or right of node's x val,
            // search right subtree first
            else {
                nearest(x.right, p);
                nearest(x.left, p);
            }
        }
        // horizontal level
        else {
            // if point's y val is above node's y val, search left subtree first
            if (x.p.y() > p.y()) {
                nearest(x.left, p);
                nearest(x.right, p);

            }
            // if point's y val is equal to or below node's y val,
            // search right subtree first
            else {
                nearest(x.right, p);
                nearest(x.left, p);
            }
        }
        // return running champ point at end of recursive calls
        return champPoint;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // UNIT TESTING ALL METHODS
        KdTreeST<Integer> tree = new KdTreeST<>();
        StdOut.println("size of tree: " + tree.size());
        StdOut.println("the tree is empty? " + tree.isEmpty());
        Point2D point1 = new Point2D(0, 4);
        Point2D point6 = new Point2D(-5, 5);
        Point2D point2 = new Point2D(0, 1);
        Point2D point3 = new Point2D(0, 3);
        Point2D point4 = new Point2D(1, 3);
        Point2D point5 = new Point2D(3, 3);
        RectHV rect = new RectHV(0, 0, 2, 2);
        tree.put(point1, 1);
        tree.put(point2, 2);
        tree.put(point3, 3);
        tree.put(point4, 4);
        tree.put(point5, 5);
        tree.put(point6, 6);
        StdOut.println("all points: " + tree.points());
        StdOut.println("contains (0, 2)? " + tree.contains(new Point2D(0, 2)));
        StdOut.println("point 2: " + tree.get(point2));
        StdOut.println("nearest to (-5, 5): " + tree.nearest(new Point2D(-5, 5)));
        StdOut.println("all points inside rectangle: " + tree.range(rect));
        StdOut.println("size of tree: " + tree.size());
        StdOut.println("the tree is empty? " + tree.isEmpty());

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
