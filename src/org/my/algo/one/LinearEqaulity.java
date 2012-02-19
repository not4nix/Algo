package org.my.algo.one;

public class LinearEqaulity {
	 public static long countSolutions(int[] a, int b) {
		    long[] dp = new long[b + 1];
		    dp[0] = 1;
		    for (int i = 0; i < a.length; i++) {
		      for (int j = a[i]; j <= b; j++) {
		        dp[j] += dp[j - a[i]];
		      }
		    }
		    return dp[b];
		  }

		  public static void main(String[] args) {
		    System.out.println(5 == countSolutions(new int[] { 1, 2, 3 }, 5));
		  }
}
