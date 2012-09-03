package org.giccon.algorithms.challenges;

/* Solution: game theory, logarithm theory. */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * For mathematical explanation see:
 * <a href="http://algorithms.giccon.org/wp-content/uploads/2012/07/proof-of-the-multiplication-game.pdf">
 * proof of the multiplication game
 * </a>
 */
public class MultiplicationGame {
    private static enum Player {
        STAN, OLLIE
    }

    private static final List<Integer> MAX_VALUES = new ArrayList<Integer>();

    static {
        int c = 1;
        for (int n = 0; n <= 13; n++) {
            if (n % 2 == 0) {
                c *= 9;
            } else {
                c *= 2;
            }
            MAX_VALUES.add(c);
        }
    }

    public static void main(String args[]) {
        MultiplicationGame.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLong()) {
            long g = sc.nextLong();
            Player winner = getWinner(g);
            switch (winner) {
                case STAN:
                    System.out.println("Stan wins.");
                    break;
                case OLLIE:
                    System.out.println("Ollie wins.");
                    break;
                default:
                    break;
            }
        }
    }

    private static Player getWinner(long g) {
        Player winner = Player.STAN;
        for (int n = MAX_VALUES.size() - 1; n >= 0; n--) {
            if (g > MAX_VALUES.get(n)) {
                if (n % 2 == 0) {
                    winner = Player.OLLIE;
                }
                break;
            }
        }

        return winner;
    }
}
