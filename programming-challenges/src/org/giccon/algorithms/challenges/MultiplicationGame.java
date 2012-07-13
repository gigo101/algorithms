package org.giccon.algorithms.challenges;

import java.util.Scanner;

public class MultiplicationGame {
    public static void main(String args[]) {
        MultiplicationGame.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLong()) {
            long g = sc.nextLong();
            int w = getWinner(g);
            switch (w) {
                case 1:
                    System.out.println("Stan wins.");
                    break;
                case 2:
                    System.out.println("Ollie wins.");
                    break;
                default:
                    break;
            }
        }
    }

    private static int getWinner(long g) {
        int winner = 2;
        int n = 0;
        while (calcLeft(n) <= g) {
            n++;
        }
        n--;
        if (calcRight(n) >= g) {
            winner = 1;
        }

        return winner;
    }

    private static long calcLeft(int n) {
        return (1 << n) * (long) Math.pow(9, n) + 1;
    }

    private static long calcRight(int n) {
        return (1 << n) * (long) Math.pow(9, n + 1);
    }
}
