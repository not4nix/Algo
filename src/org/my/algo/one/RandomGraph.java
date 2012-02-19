package org.my.algo.one;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

public class RandomGraph {
	public static List<Integer>[] pruferCode2Tree(int[] a) {
	    int n = a.length + 2;
	    List<Integer>[] t = new List[n];
	    for (int i = 0; i < n; i++) {
	      t[i] = new ArrayList<Integer>();
	    }
	    int[] degree = new int[n];
	    for (int x : a) {
	      ++degree[x];
	    }
	    PriorityQueue<Long> q = new PriorityQueue<Long>();
	    for (int i = 0; i < n; i++) {
	      ++degree[i];
	      q.add(((long) degree[i] << 32) + i);
	    }
	    for (int x : a) {
	      int num = 0;
	      int deg = 0;
	      do {
	        long node = q.poll();
	        deg = (int) (node >>> 32);
	        num = (int) (node & 0xFFFFFFFF);
	      } while (deg != degree[num]);
	      t[x].add(num);
	      t[num].add(x);
	      --degree[x];
	      if (degree[x] >= 1) {
	        q.add(((long) degree[x] << 32) + x);
	      }
	    }
	    int u = (int) (q.poll() & 0xFFFFFFFF);
	    int v = (int) (q.poll() & 0xFFFFFFFF);
	    t[u].add(v);
	    t[v].add(u);
	    return t;
	  }

	  // precondition: n >= 2
	  public static List<Integer>[] getRandomTree(int V, Random rnd) {
	    int[] a = new int[V - 2];
	    for (int i = 0; i < a.length; i++) {
	      a[i] = rnd.nextInt(V);
	    }
	    return pruferCode2Tree(a);
	  }

	  // precondition: V >= 2, V-1 <= E <= V*(V-1)/2
	  public static List<Integer>[] getRandomUndirectedConnectedGraph(int V, int E, Random rnd) {
	    List<Integer>[] g = getRandomTree(V, rnd);
	    Set<Long> edgeSet = new LinkedHashSet<Long>();
	    for (int i = 0; i < V; i++) {
	      for (int j = i + 1; j < V; j++) {
	        edgeSet.add(((long) i << 32) + j);
	      }
	    }
	    for (int i = 0; i < V; i++) {
	      for (int j : g[i]) {
	        edgeSet.remove(((long) i << 32) + j);
	      }
	    }
	    List<Long> edges = new ArrayList<Long>(edgeSet);
	    for (int x : getRandomCombination(edges.size(), E - (V - 1), rnd)) {
	      long e = edges.get(x);
	      int u = (int) (e >>> 32);
	      int v = (int) (e & 0xFFFFFFFF);
	      g[u].add(v);
	      g[v].add(u);
	    }
	    for (int i = 0; i < V; i++)
	      Collections.sort(g[i]);
	    return g;
	  }

	  static int[] getRandomCombination(int n, int m, Random rnd) {
	    int[] res = new int[n];
	    for (int i = 0; i < n; i++) {
	      res[i] = i;
	    }
	    for (int i = 0; i < m; i++) {
	      int j = n - 1 - rnd.nextInt(n - i);
	      int t = res[i];
	      res[i] = res[j];
	      res[j] = t;
	    }
	    return Arrays.copyOf(res, m);
	  }

	  static void checkGraph(int V, int E, Random rnd) {
	    List<Integer>[] g = getRandomUndirectedConnectedGraph(V, E, rnd);
	    int n = g.length;
	    int[][] a = new int[n][n];
	    int edges = 0;
	    for (int i = 0; i < n; i++) {
	      for (int j : g[i]) {
	        ++a[i][j];
	        ++edges;
	      }
	    }
	    if (edges != 2 * E) {
	      throw new RuntimeException();
	    }
	    for (int i = 0; i < n; i++) {
	      if (a[i][i] != 0) {
	        throw new RuntimeException();
	      }
	      for (int j = 0; j < n; j++) {
	        if (a[i][j] != a[j][i] || a[i][j] != 0 && a[i][j] != 1) {
	          throw new RuntimeException();
	        }
	      }
	    }
	  }

	  public static void main(String[] args) {
	    System.out.println(Arrays.toString(pruferCode2Tree(new int[] { 3, 3, 3, 4 })));
	    System.out.println(Arrays.toString(pruferCode2Tree(new int[] { 0, 0 })));

	    Random rnd = new Random(1);
	    for (int step = 0; step < 1000; step++) {
	      int V = rnd.nextInt(50) + 2;
	      checkGraph(V, V - 1, rnd);
	      checkGraph(V, V * (V - 1) / 2, rnd);
	      checkGraph(V, rnd.nextInt(V * (V - 1) / 2 - (V - 1) + 1) + V - 1, rnd);
	    }
	  }
}
