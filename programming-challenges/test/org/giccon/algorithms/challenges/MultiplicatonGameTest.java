package org.giccon.algorithms.challenges;

/* Solution: game theory, logarithm theory */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * For mathematical explanation see:
 * <a href="http://algorithms.giccon.org/wp-content/uploads/2012/07/proof-of-the-multiplication-game.pdf">
 * proof of the multiplication game
 * </a>
 */
class MultiplicationGameTest {
    private static enum Player {
        STAN, OLLIE
    }

    private static final double LOG_OF_18 = Math.log(18);
    private static final double LOG_2_DIV_LOG_18 = Math.log(2) / Math.log(18);
    private static final List<Integer> MAX_VALUES = new ArrayList<Integer>();
    private static final List<Range> RANGE_VALUES = new ArrayList<Range>();

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

        for (int n = 0; n <= 7; n++) {
            long min = (1 << n) * (long) Math.pow(9, n) + 1;
            long max = (1 << n) * (long) Math.pow(9, n + 1);
            RANGE_VALUES.add(new Range(min, max));
        }
    }

    private static class Range implements Comparable<Range> {
        private long min;
        private long max;

        public Range(long min, long max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public int compareTo(Range o) {
            if (o.min < min) return 1;
            if (o.min > max) return -1;

            return 0;
        }
    }

    public static void main(String args[]) {
        runGetWinnerSolution1();
        runGetWinnerSolution2();
        runGetWinnerSolution3();
    }

    private static void runGetWinnerSolution1() {
        long[] scores = new long[2];
        long start = System.currentTimeMillis();
        for (long n = 2; n < 4294967295l; n++) {
            scores[getWinner1(n).ordinal()]++;
        }

        printTestResults("runGetWinnerSolution1", scores, (System.currentTimeMillis() - start));

    }

    private static void runGetWinnerSolution2() {
        long[] scores = new long[2];
        long start = System.currentTimeMillis();
        for (long n = 2; n < 4294967295l; n++) {
            scores[getWinner2(n).ordinal()]++;
        }

        printTestResults("runGetWinnerSolution2", scores, (System.currentTimeMillis() - start));
    }

    private static void runGetWinnerSolution3() {
        long[] scores = new long[2];
        long start = System.currentTimeMillis();
        for (long n = 2; n < 4294967295l; n++) {
            scores[getWinner3(n).ordinal()]++;
        }

        printTestResults("runGetWinnerSolution3", scores, (System.currentTimeMillis() - start));
    }

    private static void printTestResults(String winner, long[] scores, long duration) {
        System.out.println(winner + " " + duration);
        System.out.println((double) scores[0] / scores[1]);
        System.out.println(scores[0]);
        System.out.println(scores[1]);
        System.out.println();
    }

    private static Player getWinner1(long g) {
        double q = Math.log(g) / LOG_OF_18;
        return Math.ceil(q + LOG_2_DIV_LOG_18) > Math.ceil(q) ? Player.OLLIE : Player.STAN;
    }

    private static Player getWinner2(long g) {
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

    private static Player getWinner3(long g) {
        if (Collections.binarySearch(RANGE_VALUES, new Range(g, g)) >= 0) {
            return Player.STAN;
        }

        return Player.OLLIE;
    }
}
