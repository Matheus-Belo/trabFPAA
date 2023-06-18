import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ConvexHullGrahamScan {

        public static List<Point> convexHull(List<Point> points) {
            if (points.size() <= 3) {
                return points;
            }

            int n = points.size();
            int leftmost = 0;
            for (int i = 1; i < n; i++) {
                if (points.get(i).x < points.get(leftmost).x) {
                    leftmost = i;
                }
            }

            List<Point> hull = new ArrayList<>();
            int p = leftmost;
            int q;
            do {
                hull.add(points.get(p));
                q = (p + 1) % n;

                for (int i = 0; i < n; i++) {
                    if (orientation(points.get(p), points.get(i), points.get(q)) == 2) {
                        q = i;
                    }
                }

                p = q;
            } while (p != leftmost);

            return hull;
        }

        private static int orientation(Point p, Point q, Point r) {
            int value = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

            if (value == 0) {
                return 0;  // Colinear
            }

            return (value > 0) ? 1 : 2; // 1 para horário, 2 para anti-horário
        }

    public static void main(String[] args) {

        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(0, 4));
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 1));
        points.add(new Point(4, 4));
        points.add(new Point(4, 0));


        List<Point> p1 = new ArrayList<>();
        p1.add (new Point(1, 2));
        p1.add (new Point(4, 5));
        p1.add (new Point(3, 16));
        p1.add (new Point(2, 7));
        p1.add (new Point(7, 10));
        p1.add (new Point(5, 8));
        p1.add (new Point(5, 5));
        p1.add (new Point(5, -3));
        p1.add (new Point(-4, 5));



        List<Point> convexHull = convexHull(points);
        for (Point point : convexHull) {
            System.out.println("(" + point.x + ", " + point.y + ")");
        }
    }
}