import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {
  private SearchNode root;
  private int length = 0;

  private class SearchNode {
    Point2D point;
    boolean useX;
    SearchNode left;
    SearchNode right;

    private class FromPoint {
      Point2D point;
      double dest;
      public FromPoint(Point2D from, Point2D to) {
        point = to;
        dest = from.distanceSquaredTo(to);
      }
    }

    public SearchNode(Point2D p, boolean x) {
      point = p;
      useX = x;
    }
    void draw(RectHV rect) {
      if (left != null) {
        left.draw(new RectHV(
          rect.xmin(),
          rect.ymin(),
          useX ? point.x() : rect.xmax(),
          useX ? rect.ymax() : point.y()));
      }
      StdDraw.setPenRadius(0.01);
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.point(point.x(), point.y());
      StdDraw.setPenRadius();
      StdDraw.setPenColor(useX ? StdDraw.RED : StdDraw.BLUE);
      StdDraw.line(
        useX ? point.x()   : rect.xmin(),
        useX ? rect.ymin() : point.y(),
        useX ? point.x()   : rect.xmax(),
        useX ? rect.ymax() : point.y());

      if (right != null) {
        right.draw(new RectHV(
          useX ? point.x() : rect.xmin(),
          useX ? rect.ymin() : point.y(),
          rect.xmax(),
          rect.ymax()));
      }
    }
    void in(RectHV rect, SET<Point2D> inRange) {
      if (rect.contains(point)) {
        inRange.add(point);
      }
      if (useX) {
        if (left != null && rect.xmin() <= point.x()) {
          left.in(rect, inRange);
        }
        if (right != null && rect.xmax() >= point.x()) {
          right.in(rect, inRange);
        }
      }
      else {
        if (left != null && rect.ymin() <= point.y()) {
          left.in(rect, inRange);
        }
        if (right != null && rect.ymax() >= point.y()) {
          right.in(rect, inRange);
        }
      }
    }
    int compare(Point2D p) {
      if (point.equals(p)) {
        return 0;
      }
      if (useX) {
        return (p.x() < point.x()) ? -1 : 1;
      }
      else {
        return (p.y() < point.y()) ? -1 : 1;
      }
    }
    Point2D closest(Point2D p) {
      FromPoint min = new FromPoint(p, point);
      int cmp = compare(p);
      if (cmp == 0) {
        return min.point;
      }
      SearchNode first  = cmp == -1 ? left : right;
      SearchNode second = cmp == -1 ? right : left;
      if (first != null) {
        min = first.closest(p, min);
      }
      if (second != null) {
        double dest = Math.abs(useX ? point.x() - p.x() : point.y() - p.y());
        if (min.dest >= dest * dest) {
          min = second.closest(p, min);
        }
      }
      return min.point;
    }
    FromPoint closest(Point2D p, FromPoint min) {
      FromPoint newMin = new FromPoint(p, point);
      if (newMin.dest < min.dest) {
        min = newMin;
      }
      int cmp = compare(p);
      if (cmp == 0) {
        return min;
      }
      SearchNode first  = cmp == -1 ? left : right;
      SearchNode second = cmp == -1 ? right : left;
      if (first != null) {
        min = first.closest(p, min);
      }
      if (second != null) {
        double dest = Math.abs(useX ? point.x() - p.x() : point.y() - p.y());
        if (min.dest >= dest * dest) {
          min = second.closest(p, min);
        }
      }
      return min;
    }
  }
  /**
   * construct an empty set of points
   */
  public KdTree() {}
  /**
   * is the set empty?
   */
  public boolean isEmpty() {
    return root == null;
  }
  /**
   * number of points in the set
   */
  public int size() {
    return length;
  }
  /**
   * add the point to the set (if it is not already in the set)
   */
  public void insert(Point2D p) {
    root = addTo(root, p, true);
  }
  /**
   * does the set contain point p?
   */
  public boolean contains(Point2D p) {
    return has(root, p) != null;
  }
  /**
   * draw all points to standard draw
   */
  public void draw() {
    if (root != null) {
      root.draw(new RectHV(0, 0, 1, 1));
    }
  }
  /**
   * all points that are inside the rectangle
   */
  public Iterable<Point2D> range(RectHV rect) {
    SET<Point2D> inRange = new SET<Point2D>();
    if (root != null) {
      root.in(rect, inRange);
    }
    return inRange;
  }
  /**
   * a nearest neighbor in the set to point p; null if the set is empty
   */
  public Point2D nearest(Point2D p) {
    Point2D min = null;
    if (root != null) {
      min = root.closest(p);
    }
    return min;
  }

  private SearchNode has(SearchNode node, Point2D point) {
    if (node == null || node.point.equals(point)) {
      return node;
    }
    return (node.compare(point) == -1) ? has(node.left, point) : has(node.right, point);
  }

  private SearchNode addTo(SearchNode node, Point2D point, boolean useX) {
    if (node == null) {
      length += 1;
      return new SearchNode(point, useX);
    }

    switch(node.compare(point)) {
      case -1:
        node.left = addTo(node.left, point, !useX);
        break;
      case 1:
        node.right = addTo(node.right, point, !useX);
        break;
      default: //case 0:
        break;
    }

    return node;
  }

  /**
   * unit testing of the methods (optional)
   */
   public static void main(String[] args) {
     RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
     StdDraw.enableDoubleBuffering();
     PointSET brute = new PointSET();
     KdTree kdtree = new KdTree();
     while (true) {
        StdDraw.clear();

        // the location (x, y) of the mouse
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        Point2D query = new Point2D(x, y);
        if (StdDraw.mousePressed()) {
           StdOut.printf("%8.6f %8.6f\n", x, y);
           if (rect.contains(query)) {
               StdOut.printf("%8.6f %8.6f\n", x, y);
               brute.insert(query);
               kdtree.insert(query);
           }
        }

        if (brute.size() == 0) {
          continue;
        }

        // draw all of the points
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        //brute.draw();
        kdtree.draw();

        // draw in red the nearest neighbor (using brute-force algorithm)
        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.RED);
        brute.nearest(query).draw();
        StdDraw.setPenRadius(0.02);

        // draw in blue the nearest neighbor (using kd-tree algorithm)
        StdDraw.setPenColor(StdDraw.BLUE);
        kdtree.nearest(query).draw();
        StdDraw.show();
        StdDraw.pause(50);
      }

   }
}
