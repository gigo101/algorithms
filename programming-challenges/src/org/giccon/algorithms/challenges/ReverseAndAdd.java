package org.giccon.algorithms.challenges;

import java.util.Scanner;

public class ReverseAndAdd {
    public static void main(String[] args) {
        ReverseAndAdd.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int noTestcases = Integer.parseInt(sc.nextLine());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < noTestcases; i++) {
            sb.append(getPalindromeInfo(Long.parseLong(sc.nextLine()))).append("\n");
        }
        System.out.print(sb);
    }

    private static String getPalindromeInfo(long n) {
        boolean isNegative = n < 0;
        n = isNegative ? -n : n;
        long rev = Long.valueOf(new StringBuilder().append(n).reverse().toString());
        int noItrs = 0;
        while (n != rev) {
            n += rev;
            rev = Long.valueOf(new StringBuilder().append(n).reverse().toString());
            noItrs++;
        }
        n = isNegative ? -n : n;

        return noItrs + " " + n;
    }
}
