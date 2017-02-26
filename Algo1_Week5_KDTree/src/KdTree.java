import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;


public class KdTree {
    private Node root;
    // construct an empty set of points
    public KdTree()
    {
        root = null;
    }

    // is the set empty?
    public  boolean isEmpty()
    {
        return root == null;
    }

    // number of points in the set
    public int size()
    {
        return size(root);
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        if (p == null) {
            throw new NullPointerException("argument is null");
        }
        if (!contains(p))
        {
            root = put(root, p, 1,  new RectHV(0, 0, 1, 1));
        }
    }


    private Node put(Node node, Point2D point, int orientation, RectHV rect)
    {
        if (node == null) return new Node(point, 1, rect);
        int cmp;
        RectHV rectLB = null;
        RectHV rectRT = null;
        if (orientation > 0)
        {
            cmp = Double.compare(point.x(), node.p.x());
        }
        else
        {
            cmp = Double.compare(point.y(), node.p.y());

        }

        if (cmp < 0)
        {
            if (node.lb == null)
            {
                rectLB  = orientation > 0 ?
                    new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax()) :
                    new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y());
            }
            node.lb  = put(node.lb,  point, (-1)*orientation, rectLB);
        }
        else
        {
            if (node.rt == null) {
                rectRT = orientation > 0 ?
                        new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax()) :
                        new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax());
            }
            node.rt  = put(node.rt,  point, (-1)*orientation, rectRT);
        }

        node.size = 1 + size(node.lb) + size(node.rt);

//        if (orientation > 0)
//        {
//            if (node.lb != null && node.lb.rect == null)
//            {
//                node.lb.rect = new RectHV(0, 0, node.lb.p.x(), 1);
//            }
//            if (node.rt != null && node.rt.rect == null)
//            {
//                node.rt.rect = new RectHV(node.rt.p.x(), 0, 1, 1);
//            }
//        }
//        else
//        {
//            if (node.lb != null && node.lb.rect != null)
//            {
//                node.lb.rect = new RectHV(0, 0, 1, node.lb.p.y());
//            }
//            if (node.rt != null && node.rt.rect != null)
//            {
//                node.rt.rect = new RectHV(0, node.rt.p.y(), 1, 1);
//            }
//        }

        return node;
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    // does the set contain point p?
    public boolean contains(Point2D p)
    {
        if (p == null) {
            throw new NullPointerException("argument is null");
        }
        return contains(p, root, 1);
    }

    private boolean contains(Point2D p, Node node, int orientation)
    {

        if (node == null)
        {
            return false;
        }

        if (node.p.equals(p))
        {
            return true;
        }
        int cmp;
        if (orientation > 0)
        {
            cmp = Double.compare(p.x(), node.p.x());
        }
        else
        {
            cmp = Double.compare(p.y(), node.p.y());
        }
        if      (cmp < 0) return contains(p, node.lb, (-1)*orientation);
        else return contains(p, node.rt, (-1)*orientation);


    }

    // draw all points to standard draw
    public void draw()
    {
        draw(root, 1, 0, 1);
    }

    private void draw(Node node, int orientation, double prev0, double prev1)
    {
        if (node == null)
        {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.03);


        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.01);

        // node.rect.draw();

        StdDraw.setPenRadius();
        if (orientation > 0)
        {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.03);
            node.p.draw();
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.001);
            StdDraw.line(node.p.x(), prev0, node.p.x(), prev1);
            draw(node.lb, (-1)*orientation, 0, node.p.x());
            draw(node.rt, (-1)*orientation, node.p.x(), 1);
        }
        else
        {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.03);
            node.p.draw();
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.001);
            StdDraw.line(prev0, node.p.y(), prev1, node.p.y());
            draw(node.lb, (-1)*orientation, 0, node.p.y());
            draw(node.rt, (-1)*orientation, node.p.y(), 1);
        }


    }


    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect)
    {
        ArrayList<Point2D> result = new ArrayList<>();
        if (rect == null) {
            throw new NullPointerException("argument is null");
        }
        return range(rect, root, result);
    }

    private Iterable<Point2D> range(RectHV rect, Node node, ArrayList<Point2D> result)
    {
        if (node == null)
        {
            return result;
        }
        if (rect.contains(node.p))
        {
            result.add(node.p);
        }
        if (node.lb != null && node.lb.rect.intersects(rect))
        {
            range(rect, node.lb, result);
        }

        if (node.rt != null && node.rt.rect.intersects(rect))
        {
            range(rect, node.rt, result);
        }
        return result;
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)
    {
        if (p == null) {
            throw new NullPointerException("argument is null");
        }
        if (isEmpty())
        {
            return null;
        }

        return nearest(p, root, root.p, root.p.distanceSquaredTo(p), 1);
    }

    private Point2D nearest(Point2D p, Node node, Point2D nearestPoint, double nearestDistance, int orientation)
    {
        // StdOut.println("Nearest visit level "+orientation+" node "+node.p);
        if (node == null)
        {
            return nearestPoint;
        }

        double distance = node.p.distanceSquaredTo(p);
        if (distance < nearestPoint.distanceSquaredTo(p))
        {
            nearestPoint = node.p;
            nearestDistance = distance;
        }
        Point2D prevNearestPoint = nearestPoint;


        double cmp = orientation > 0 ? Double.compare(p.x(), node.p.x()) : Double.compare(p.y(), node.p.y());

        if (cmp < 0)
        {
            nearestPoint = searchNearest(p, node.lb, nearestPoint, nearestDistance, orientation);
            if (!prevNearestPoint.equals(nearestPoint))
            {
                nearestDistance = nearestPoint.distanceSquaredTo(p);
            }
            nearestPoint = searchNearest(p, node.rt, nearestPoint, nearestDistance, orientation);
        }
        else
        {
            nearestPoint = searchNearest(p, node.rt, nearestPoint, nearestDistance, orientation);
            if (!prevNearestPoint.equals(nearestPoint))
            {
                nearestDistance = nearestPoint.distanceSquaredTo(p);
            }
            nearestPoint = searchNearest(p, node.lb, nearestPoint, nearestDistance, orientation);
        }

        return nearestPoint;

    }

    private Point2D searchNearest(Point2D p, Node node, Point2D nearestPoint, double nearestDistance, int orientation) {
        if (node != null && node.rect.distanceSquaredTo(p) <= nearestDistance)
        {
            nearestPoint = nearest(p, node, nearestPoint, nearestDistance, (-1)*(orientation+1));
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        KdTree kdTree = new KdTree();
        int i = 0;
        while (!in.isEmpty())
        {
            double pointX = in.readDouble();
            double pointY = in.readDouble();
            Point2D point = new Point2D(pointX, pointY);

            kdTree.insert(point);

            StdDraw.text(pointX+0.02, pointY+0.02, ""+Integer.toString(i));

        }
        kdTree.draw();
        Point2D neareastTest = new Point2D(0.81, 0.30);
        StdOut.println(kdTree.nearest(neareastTest));

    }


    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int size;       // size of node

        public Node(Point2D point, int size, RectHV rect)
        {
            this.p = point;
            this.size = size;
            this.rect = rect;
        }
    }

}
