package org.giccon.algorithms.challenges;

/* Solution: game theory, logarithm theory */

import java.security.SecureRandom;
import java.util.*;

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
        //MultiplicationGame.begin();

        SecureRandom r = new SecureRandom();

        List<Long> numbers = new LinkedList<Long>();
        for (long y = 2; y < 10000000; y++) {
            numbers.add(y);
        }

        Collections.shuffle(numbers, r);
        runWinner(numbers);
        runWinner2(numbers);
        runWinner3(numbers);

        Collections.shuffle(numbers, r);
        runWinner2(numbers);
        runWinner(numbers);
        runWinner3(numbers);

        Collections.shuffle(numbers);
        runWinner3(numbers);
        runWinner2(numbers);
        runWinner(numbers);
    }

    private static void runWinner(List<Long> numbers) {
        long total = 0;
        for (int i = 0; i < 5; i++) {
            long start = System.currentTimeMillis();
            for (long g : numbers) {
                getWinner(g);
            }
            total += System.currentTimeMillis() - start;
        }
        System.out.println("Winner = " + total / 5);
    }

    private static void runWinner2(List<Long> numbers) {
        long total = 0;
        for (int i = 0; i < 5; i++) {
            long start = System.currentTimeMillis();
            for (long g : numbers) {
                getWinner2(g);
            }
            total += System.currentTimeMillis() - start;
        }
        System.out.println("Winner2 = " + total / 5);
    }

    private static void runWinner3(List<Long> numbers) {
        long total = 0;
        for (int i = 0; i < 5; i++) {
            long start = System.currentTimeMillis();
            for (long g : numbers) {
                getWinner3(g);
            }
            total += System.currentTimeMillis() - start;
        }
        System.out.println("Winner3 = " + total / 5);
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLong()) {
            long g = sc.nextLong();
            Player winner = getWinner3(g);
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
