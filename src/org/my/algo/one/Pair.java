package org.my.algo.one;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Pair implements Comparable<Pair>{
	long u;
	  long v;

	  public Pair(long u, long v) {
	    this.u = u;
	    this.v = v;
	  }

	  public int hashCode() {
	    int hu = (int) (u ^ (u >>> 32));
	    int hv = (int) (v ^ (v >>> 32));
	    return (int) (hu * 31 + hv);
	  }

	  public boolean equals(Object o) {
	    Pair other = (Pair) o;
	    return u == other.u && v == other.v;
	  }

	  public int compareTo(Pair other) {
	    if (u != other.u)
	      return u < other.u ? -1 : 1;

	    return v < other.v ? -1 : v > other.v ? 1 : 0;
	  }

	  public String toString() {
	    return "[u=" + u + ", v=" + v + "]";
	  }

	  // Usage example
	  public static void main(String[] args) {
	    Set<Pair> set1 = new TreeSet<Pair>();
	    Set<Pair> set2 = new HashSet<Pair>();
	    for (int i = 0; i < 20; i++) {
	      Pair p = new Pair(i % 5, i % 10);
	      set1.add(p);
	      set2.add(p);
	    }
	    System.out.println(true == (set1.size() == set2.size()));
	    System.out.println(set1);
	    System.out.println(set2);
	  }
}
