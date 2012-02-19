package org.my.algo.one;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CircleAlgo {
	static final double eps = 1e-9;

	  public static double sqr(double x) {
	    return x * x;
	  }

	  public static double quickHypot(double x, double y) {
	    return Math.sqrt(x * x + y * y);
	  }

	  public static class Point {
	    double x, y;

	    public Point(double x, double y) {
	      this.x = x;
	      this.y = y;
	    }
	  }

	  public static class Circle {
	    double r, x, y;

	    public Circle(double x, double y, double r) {
	      this.x = x;
	      this.y = y;
	      this.r = r;
	    }

	    public boolean contains(Point p) {
	      return quickHypot(p.x - x, p.y - y) < r + eps;
	    }
	  }

	  public static Circle getCircumCircle(Point a, Point b) {
	    double x = (a.x + b.x) / 2.;
	    double y = (a.y + b.y) / 2.;
	    double r = quickHypot(a.x - x, a.y - y);
	    return new Circle(x, y, r);
	  }

	  public static Circle getCircumCircle(Point a, Point b, Point c) {
	    double Bx = b.x - a.x;
	    double By = b.y - a.y;
	    double Cx = c.x - a.x;
	    double Cy = c.y - a.y;
	    double d = 2 * (Bx * Cy - By * Cx);
	    double z1 = Bx * Bx + By * By;
	    double z2 = Cx * Cx + Cy * Cy;
	    double cx = Cy * z1 - By * z2;
	    double cy = Bx * z2 - Cx * z1;
	    double x = (double) cx / d;
	    double y = (double) cy / d;
	    double r = quickHypot(x, y);
	    return new Circle(x + a.x, y + a.y, r);
	  }

	  public static class Line {
	    double a, b, c;

	    public Line(double a, double b, double c) {
	      this.a = a;
	      this.b = b;
	      this.c = c;
	    }

	    public Line(Point p1, Point p2) {
	      a = +(p1.y - p2.y);
	      b = -(p1.x - p2.x);
	      c = p1.x * p2.y - p2.x * p1.y;
	    }
	  }

	  // geometric solution
	  public static List<Point2D.Double> circleLineIntersection(Circle circle, Line line) {
	    double a = line.a;
	    double b = line.b;
	    double c = line.c + circle.x * a + circle.y * b;
	    double r = circle.r;
	    double aabb = a * a + b * b;
	    List<Point2D.Double> res = new ArrayList<Point2D.Double>();
	    double d = c * c / aabb - r * r;
	    if (d > eps)
	      return res;
	    double x0 = -a * c / aabb;
	    double y0 = -b * c / aabb;
	    if (d > -eps) {
	      res.add(new Point2D.Double(x0 + circle.x, y0 + circle.y));
	      return res;
	    }
	    d /= -aabb;
	    double k = Math.sqrt(d < 0 ? 0 : d);
	    res.add(new Point2D.Double(x0 + k * b + circle.x, y0 - k * a + circle.y));
	    res.add(new Point2D.Double(x0 - k * b + circle.x, y0 + k * a + circle.y));
	    return res;
	  }

	  // algebraic solution
	  public static List<Point2D.Double> circleLineIntersection2(Circle circle, Line line) {
	    double a = line.a;
	    double b = line.b;
	    double c = line.c;
	    double CX = circle.x, CY = circle.y;
	    double R = circle.r;
	    // ax+by+c=0
	    // (by+c+aCX)^2+(ay-aCY)^2=(aR)^2
	    boolean swap = false;
	    if (Math.abs(a) < Math.abs(b)) {
	      swap = true;
	      double t = a;
	      a = b;
	      b = t;
	      t = CX;
	      CX = CY;
	      CY = t;
	    }
	    List<Point2D.Double> res = new ArrayList<Point2D.Double>();
	    double A = a * a + b * b;
	    double B = 2.0 * b * (c + a * CX) - 2.0 * a * a * CY;
	    double C = sqr(c + a * CX) + a * a * (CY * CY - R * R);
	    double d = B * B - 4 * A * C;
	    if (d < -eps)
	      return res;
	    d = Math.sqrt(d < 0 ? 0 : d);
	    double y1 = (-B + d) / (2 * A);
	    double x1 = (-c - b * y1) / a;
	    double y2 = (-B - d) / (2 * A);
	    double x2 = (-c - b * y2) / a;
	    if (swap) {
	      double t = x1;
	      x1 = y1;
	      y1 = t;
	      t = x2;
	      x2 = y2;
	      y2 = t;
	    }
	    res.add(new Point2D.Double(x1, y1));
	    if (d > eps)
	      res.add(new Point2D.Double(x2, y2));
	    return res;
	  }

	  public static List<Point2D.Double> circleCircleIntersection(Circle c1, Circle c2) {
	    if (quickHypot(c1.x - c2.x, c1.y - c2.y) < eps) {
	      if (Math.abs(c1.r - c2.r) < eps) {
	        // infinity intersection points
	        return null;
	      }
	      return new ArrayList<Point2D.Double>();
	    }
	    c2.x -= c1.x;
	    c2.y -= c1.y;
	    double A = -2 * c2.x;
	    double B = -2 * c2.y;
	    double C = c2.x * c2.x + c2.y * c2.y + c1.r * c1.r - c2.r * c2.r;
	    List<Point2D.Double> res = circleLineIntersection(new Circle(0, 0, c1.r), new Line(A, B, C));
	    for (Point2D.Double point : res) {
	      point.x += c1.x;
	      point.y += c1.y;
	    }
	    return res;
	  }

	  public static double circleCircleIntersectionArea(Circle c1, Circle c2) {
	    double r = Math.min(c1.r, c2.r);
	    double R = Math.max(c1.r, c2.r);
	    double d = quickHypot(c1.x - c2.x, c1.y - c2.y);
	    if (d < R - r + eps) {
	      return Math.PI * r * r;
	    }
	    if (d > R + r - eps) {
	      return 0;
	    }
	    double area = r * r * Math.acos((d * d + r * r - R * R) / 2 / d / r) + R * R
	        * Math.acos((d * d + R * R - r * r) / 2 / d / R) - 0.5
	        * Math.sqrt((-d + r + R) * (d + r - R) * (d - r + R) * (d + r + R));
	    return area;
	  }

	  static Circle minEnclosingCircleWith2Points(List<Point> points, Point q1, Point q2) {
	    Circle circle = getCircumCircle(q1, q2);
	    for (int i = 0; i < points.size(); i++) {
	      if (!circle.contains(points.get(i))) {
	        circle = getCircumCircle(q1, q2, points.get(i));
	      }
	    }
	    return circle;
	  }

	  static Circle minEnclosingCircleWith1Point(List<Point> pointsList, Point q) {
	    List<Point> points = new ArrayList<Point>(pointsList);
	    Collections.shuffle(points);
	    Circle circle = getCircumCircle(points.get(0), q);
	    for (int i = 1; i < points.size(); i++) {
	      if (!circle.contains(points.get(i))) {
	        circle = minEnclosingCircleWith2Points(points.subList(0, i), points.get(i), q);
	      }
	    }
	    return circle;
	  }

	  public static Circle minEnclosingCircle(Point[] pointsArray) {
	    if (pointsArray.length == 0) {
	      return new Circle(0, 0, 0);
	    }
	    if (pointsArray.length == 1) {
	      return new Circle(pointsArray[0].x, pointsArray[0].y, 0);
	    }
	    List<Point> points = Arrays.asList(pointsArray);
	    Collections.shuffle(points);
	    Circle circle = getCircumCircle(points.get(0), points.get(1));
	    for (int i = 2; i < points.size(); i++) {
	      if (!circle.contains(points.get(i))) {
	        circle = minEnclosingCircleWith1Point(points.subList(0, i), points.get(i));
	      }
	    }
	    return circle;
	  }

	  // Usage example
	  public static void main(String[] args) {
	    for (int x1 = -4; x1 <= 4; x1++)
	      for (int y1 = -4; y1 <= 4; y1++)
	        for (int x2 = -4; x2 <= 4; x2++)
	          for (int y2 = -4; y2 <= 4; y2++)
	            for (int x3 = -4; x3 <= 4; x3++)
	              for (int y3 = -4; y3 <= 4; y3++) {
	                if (x1 == x2 && y1 == y2)
	                  continue;
	                Point p1 = new Point(x1, y1);
	                Point p2 = new Point(x2, y2);
	                Line line = new Line(p1, p2);
	                for (int r = 0; r <= 4; r++) {
	                  Circle circle = new Circle(x3, y3, r);
	                  List<Point2D.Double> res1 = circleLineIntersection(circle, line);
	                  List<Point2D.Double> res2 = circleLineIntersection2(circle, line);

	                  boolean eq = res1.size() == res2.size();
	                  if (eq) {
	                    int n = res1.size();
	                    boolean eq1 = true;
	                    for (int i = 0; i < n; i++) {
	                      eq1 &= Math.abs(res1.get(i).x - res2.get(i).x) < eps
	                          && Math.abs(res1.get(i).y - res2.get(i).y) < eps;
	                    }
	                    boolean eq2 = true;
	                    for (int i = 0; i < n; i++) {
	                      eq2 &= Math.abs(res1.get(i).x - res2.get(n - 1 - i).x) < eps
	                          && Math.abs(res1.get(i).y - res2.get(n - 1 - i).y) < eps;
	                    }
	                    eq = eq1 || eq2;
	                  }
	                  if (!eq) {
	                    System.out.println(res1);
	                    System.out.println(res2);
	                  }
	                }
	              }
	  }
}
