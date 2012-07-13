package org.giccon.algorithms.challenges;

import java.util.Scanner;

public class PrimaryArithmetic {
    public static void main(String[] args) {
        PrimaryArithmetic.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);

        StringBuilder sb = new StringBuilder();
        while (sc.hasNextInt()) {
            int n1 = sc.nextInt();
            int n2 = sc.nextInt();

            if (n1 == 0 && n2 == 0) {
                break;
            }

            sb.append(getCarryInfo(n1, n2)).append("\n");
        }

        System.out.print(sb);
    }

    private static String getCarryInfo(int n1, int n2) {
        int carry = 0;
        int noCarries = 0;
        /* Count the number of carry operations by performing
         * digit by digit addition and check from right to left.
         */
        while (n1 > 0 || n2 > 0) {
            int r = n1 % 10 + n2 % 10 + carry;
            if (r > 9) {
                carry = 1;
                noCarries++;
            } else {
                carry = 0;
            }

            n1 /= 10;
            n2 /= 10;
        }

        switch (noCarries) {
            case 0:
                return "No carry operation.";
            case 1:
                return "1 carry operation.";
            default:
                return noCarries + " carry operations.";
        }
    }
}
