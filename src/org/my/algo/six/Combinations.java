package org.my.algo.six;

import java.util.Arrays;

public class Combinations {
	public static long[][] binomialTable(int maxn) {
	    long[][] c = new long[maxn + 1][maxn + 1];
	    for (int i = 0; i <= maxn; i++)
	      for (int j = 0; j <= i; j++)
	        c[i][j] = (j == 0) ? 1 : c[i - 1][j - 1] + c[i - 1][j];
	    return c;
	  }

	  static long gcd(long a, long b) {
	    return b == 0 ? Math.abs(a) : gcd(b, a % b);
	  }

	  public static long binomial(long n, long m) {
	    if (n < m || n < 0 || m < 0) {
	      return 0;
	    }
	    long res = 1;
	    for (long i = 0; i < Math.min(m, n - m); i++) {
	      res = res / gcd(res, i + 1) * ((n - i) / ((i + 1) / gcd(res, i + 1)));
	      // res = res * (n - i) / (i + 1);
	    }
	    return res;
	  }

	  public static boolean nextCombination(int[] p, int n) {
	    int m = p.length;
	    for (int i = m - 1; i >= 0; i--) {
	      if (p[i] < n + i - m) {
	        ++p[i];
	        while (++i < m) {
	          p[i] = p[i - 1] + 1;
	        }
	        return true;
	      }
	    }
	    return false;
	  }

	  public static boolean nextCombinationWithRepeats(int[] p, int n) {
	    int m = p.length;
	    int[] f = new int[n];
	    for (int i = 0; i < m; i++) {
	      ++f[p[i]];
	    }
	    int[] b = new int[n + m - 1];
	    for (int i = 0, sum = -1; i < n - 1; i++) {
	      sum += f[i] + 1;
	      b[sum] = 1;
	    }
	    if (!nextPermutation(b)) {
	      return false;
	    }
	    for (int i = 0, j = 0, k = 0; i < b.length; i++) {
	      if (b[i] == 0) {
	        p[j++] = k;
	      } else {
	        ++k;
	      }
	    }
	    return true;
	  }

	  // auxiliary
	  static boolean nextPermutation(int[] p) {
	    for (int a = p.length - 2; a >= 0; --a)
	      if (p[a] < p[a + 1])
	        for (int b = p.length - 1;; --b)
	          if (p[b] > p[a]) {
	            int t = p[a];
	            p[a] = p[b];
	            p[b] = t;
	            for (++a, b = p.length - 1; a < b; ++a, --b) {
	              t = p[a];
	              p[a] = p[b];
	              p[b] = t;
	            }
	            return true;
	          }
	    return false;
	  }

	  // Usage example
	  public static void main(String[] args) {
	    int[] p = { 0, 1 };
	    boolean hasNext = nextCombination(p, 2);
	    System.out.println(false == hasNext);
	    System.out.println(true == Arrays.equals(new int[] { 0, 1 }, p));

	    p = new int[] { 0, 0 };
	    hasNext = nextCombinationWithRepeats(p, 2);
	    System.out.println(true == hasNext);
	    System.out.println(true == Arrays.equals(new int[] { 0, 1 }, p));

	    hasNext = nextCombinationWithRepeats(p, 2);
	    System.out.println(true == hasNext);
	    System.out.println(true == Arrays.equals(new int[] { 1, 1 }, p));

	    hasNext = nextCombinationWithRepeats(p, 2);
	    System.out.println(false == hasNext);
	  }
}
