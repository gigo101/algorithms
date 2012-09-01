package org.giccon.algorithms.challenges;

/* Solution: number theory, modular arithmetic */

import java.util.Scanner;

public class Ones {
    public static void main(String[] args) {
        Ones.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();

        while (sc.hasNextInt()) {
            int a = sc.nextInt();

            int x = findSmallestMultipleAsOne(a);
            sb.append(x).append("\n");
        }

        System.out.print(sb);
    }

    private static int findSmallestMultipleAsOne(int a) {
        /*
         * e.g., 111 = (1 * 10 + 1) * 10 + 1
         * 111 mod 3 = ((((1 mod 3) * 10 + 1) mod 3) * 10 + 1) mod 3
         * 1 mod 3 = 1;
         * 1 * 10 + 1 = 11;
         * 11 mod 3 = 2;
         * 2 * 10 + 1 = 21;
         * 21 mod 3 = 0;
         * is similar to:
         * 1 = 1;
         * 11 = 1 * 10 + 1 = 3 * 3 + 2;
         * 111 = 11 * 10 + 1 = (3 * 3 + 2) * 10 + 1 = 30 * 3 + 20 + 1 = 30 * 3 + 21
         * 111 mod 3 = (30 * 3 + 21) mod 3 = ((30 * 3 mod 3) + (21 mod 3)) mod 3 = 0
         * As one can see, when we mod m in each step, we only keep the remainder.
         * There is no need to take over the value when mod m gives 0.
         * e.g., (3 * 3) mod 3 = 0, (30 * 3) mod 3 = 0 as well.
         */
        int s = 1;
        int n = 1;
        while (s % a != 0) {
            s = (s % a) * 10 + 1;
            n++;
        }
        return n;
    }
}
