package org.my.algo.one;

import java.util.Random;

public class KdTreeRectQuery {
	public static class Point {
	    int x, y;

	    public Point(int x, int y) {
	      this.x = x;
	      this.y = y;
	    }
	  }

	  int[] tx;
	  int[] ty;
	  int[] minx, miny, maxx, maxy;
	  int[] count;

	  public KdTreeRectQuery(Point[] points) {
	    int n = points.length;
	    tx = new int[n];
	    ty = new int[n];
	    minx = new int[n];
	    miny = new int[n];
	    maxx = new int[n];
	    maxy = new int[n];
	    count = new int[n];
	    build(0, n, true, points);
	  }

	  void build(int low, int high, boolean divX, Point[] points) {
	    if (low >= high)
	      return;
	    int mid = (low + high) >> 1;
	    nth_element(points, low, high, mid - low, divX);

	    tx[mid] = points[mid].x;
	    ty[mid] = points[mid].y;
	    count[mid] = high - low;

	    minx[mid] = Integer.MAX_VALUE;
	    miny[mid] = Integer.MAX_VALUE;
	    maxx[mid] = Integer.MIN_VALUE;
	    maxy[mid] = Integer.MIN_VALUE;
	    for (int i = low; i < high; i++) {
	      minx[mid] = Math.min(minx[mid], points[i].x);
	      miny[mid] = Math.min(miny[mid], points[i].y);
	      maxx[mid] = Math.max(maxx[mid], points[i].x);
	      maxy[mid] = Math.max(maxy[mid], points[i].y);
	    }

	    build(low, mid, !divX, points);
	    build(mid + 1, high, !divX, points);
	  }

	  static int nth_element(Point[] a, int low, int high, int n, boolean divX) {
	    if (low == high - 1)
	      return low;
	    int q = randomizedPartition(a, low, high, divX);
	    int k = q - low;
	    if (n < k)
	      return nth_element(a, low, q, n, divX);
	    if (n > k)
	      return nth_element(a, q + 1, high, n - k - 1, divX);
	    return q;
	  }

	  static final Random rnd = new Random(1);

	  static int randomizedPartition(Point[] a, int low, int high, boolean divX) {
	    swap(a, low + rnd.nextInt(high - low), high - 1);
	    int v = divX ? a[high - 1].x : a[high - 1].y;
	    int i = low - 1;
	    for (int j = low; j < high; j++) {
	      if (divX && a[j].x <= v || !divX && a[j].y <= v) {
	        ++i;
	        swap(a, i, j);
	      }
	    }
	    return i;
	  }

	  static void swap(Point[] a, int i, int j) {
	    Point t = a[i];
	    a[i] = a[j];
	    a[j] = t;
	  }

	  // number of points in [x1,x2] x [y1,y2]
	  public int count(int x1, int y1, int x2, int y2) {
	    return count(0, tx.length, x1, y1, x2, y2);
	  }

	  int count(int low, int high, int x1, int y1, int x2, int y2) {
	    if (low >= high)
	      return 0;
	    int mid = (low + high) >> 1;

	    int ax = minx[mid];
	    int ay = miny[mid];
	    int bx = maxx[mid];
	    int by = maxy[mid];

	    if (ax > x2 || x1 > bx || ay > y2 || y1 > by)
	      return 0;
	    if (x1 <= ax && bx <= x2 && y1 <= ay && by <= y2)
	      return count[mid];

	    int res = 0;
	    res += count(low, mid, x1, y1, x2, y2);
	    res += count(mid + 1, high, x1, y1, x2, y2);
	    if (x1 <= tx[mid] && tx[mid] <= x2 && y1 <= ty[mid] && ty[mid] <= y2)
	      ++res;
	    return res;
	  }

	  // Usage example
	  public static void main(String[] args) {
	    int[] x = { 0, 10, 0, 10 };
	    int[] y = { 0, 10, 10, 0 };
	    
	    Point[] points = new Point[x.length];
	    for (int i = 0; i < points.length; i++)
	      points[i] = new Point(x[i], y[i]);

	    KdTreeRectQuery kdTree = new KdTreeRectQuery(points);
	    int count = kdTree.count(0, 0, 10, 10);
	    System.out.println(4 == count);
	  }
}
