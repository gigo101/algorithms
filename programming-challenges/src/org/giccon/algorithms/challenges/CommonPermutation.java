package org.giccon.algorithms.challenges;

/* Simple algorithm */

import java.util.Arrays;
import java.util.Scanner;

public class CommonPermutation {
    public static void main(String[] args) {
        CommonPermutation.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String w1 = sc.nextLine();
            String w2 = sc.nextLine();

            char[] ar1 = w1.toCharArray();
            char[] ar2 = w2.toCharArray();

            // Sort for each comparison.
            Arrays.sort(ar1);
            Arrays.sort(ar2);

            int i = 0;
            int j = 0;
            StringBuilder output = new StringBuilder();
            while (i < ar1.length && j < ar2.length) {
                if (ar1[i] < ar2[j]) {
                    i++;
                } else if (ar1[i] > ar2[j]) {
                    j++;
                } else {
                    output.append(ar1[i]);
                    i++;
                    j++;
                }
            }

            System.out.println(output);
        }
    }
}
