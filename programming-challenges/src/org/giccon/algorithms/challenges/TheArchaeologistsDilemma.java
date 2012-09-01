package org.giccon.algorithms.challenges;

/* Solution: logarithm theory, brute force */

import java.util.Scanner;

/**
 * The solution is as follows:
 * We define the following formula:
 * We know that: 1. P * 10^T <= 2^N < (P+1) * 10^T
 * 2. log2(P) + T * log2(10) <= N < log2(P+1) + T * log2(10) => derived from 1.
 * 3. We also know: (P < 10^(T-1)) <-> (log10(P) < T-1) <-> T > log10(P) + 1
 * We can now brute force on T until we know that the
 * ceil(lower part / left part of N) = floor(upper part / right part of N)
 */
public class TheArchaeologistsDilemma {
    private static final int MAX_TIME = 5000; // try for no more than 5 seconds.
    private static final double R = Math.log(10) / Math.log(2);

    public static void main(String[] args) {
        TheArchaeologistsDilemma.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        while (sc.hasNextInt()) {
            int prefix = sc.nextInt();
            sb.append(findSmallestExp(prefix)).append("\n");
        }

        System.out.print(sb);
    }

    private static String findSmallestExp(int prefix) {
        double lower = Math.log(prefix) / Math.log(2);
        double upper = Math.log(prefix + 1) / Math.log(2);
        /*
         * Use 0.5, otherwise if prefix is e.g., 10 we get
         * Math.ceil(Math.log10(10) + 1) = 2. Now, P < 10^(T-1) is not
         * correct as 10 < 10.
         */
        double t = Math.ceil(Math.log10(prefix + 0.5) + 1);

        double startTime = System.currentTimeMillis();
        boolean isFound = true;
        while ((int) Math.ceil(lower + t * R) != (int) Math.floor(upper + t * R)) {
            t++;
            if (System.currentTimeMillis() - startTime > MAX_TIME) {
                isFound = false;
                break;
            }
        }

        if (isFound) {
            return String.valueOf((int) Math.ceil(lower + t * R));
        }

        return "no power of 2";
    }
}
