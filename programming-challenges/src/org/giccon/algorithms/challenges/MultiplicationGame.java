package org.giccon.algorithms.challenges;

/* Solution: game theory, logarithm theory. */

import java.util.Scanner;

/**
 * For mathematical explanation see:
 * <a href="http://algorithms.giccon.org/wp-content/uploads/2012/07/proof-of-the-multiplication-game.pdf">
 * proof of the multiplication game
 * </a>
 */
public class MultiplicationGame {
    private static final double LOG_OF_9 = Math.log(9);
    private static final double LOG_OF_18 = Math.log(18);

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
        double n1 = (Math.log(g) - LOG_OF_9) / LOG_OF_18;
        double n2 = (Math.log(g) - LOG_OF_18) / LOG_OF_18;

        n1 = Math.ceil(n1 - 0.000000000000004) - n1;
        n2 = Math.ceil(n2 - 0.000000000000004) - n2;

        return n1 < n2 ? 1 : 2;
    }
}
