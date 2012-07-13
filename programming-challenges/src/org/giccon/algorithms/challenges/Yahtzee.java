package org.giccon.algorithms.challenges;

/* Algorithm using dynamic programming with bitmasks */

import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Yahtzee {
    private static enum Category {
        // ordinal: 0..12
        ONE, TWO, THREE, FOUR, FIVE, SIX, CHANCE, THREEOFAKIND, FOUROFAKIND,
        FIVEOFAKIND, SHORTSTRAIGHT, LONGSTRAIGHT, FULLHOUSE
    }

    private static final int NROUNDS = 13;
    private static final int NCATEGORIES = 13;
    // 2^(NCATEGORIES) = 8192
    private static final int NCOMBINATIONS = 1 << NCATEGORIES;
    // max achievable score in categories 0..6 = 105 slots + slot 0.
    private static final int NBONUSSLOTS = 106;
    private static final int BONUSTHRESHOLD = 63;
    private static final int NDICES = 5;
    private static final int BONUSPTS = 35;
    private static final int FIVEOFAKINDPTS = 50;
    private static final int SHORTSTRAIGHTPTS = 25;
    private static final int LONGSTRAIGHTPTS = 35;
    private static final int FULLHOUSEPTS = 40;

    public static void main(String[] args) {
        Yahtzee.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int count = 0;
        int[][] rounds = new int[NROUNDS][NDICES];

        // Start reading the problem instance and solving it.
        while (sc.hasNext()) {
            String input = sc.nextLine();

            int i = 0;
            StringTokenizer st = new StringTokenizer(input);
            // Read in the dices per line.
            while (st.hasMoreTokens()) {
                rounds[count][i++] = Integer.parseInt(st.nextToken());
            }
            // Sort the dices for efficient points calculation.
            Arrays.sort(rounds[count]);

            if (++count % NCATEGORIES == 0) {
                solve(rounds);
                count = 0;
            }
        }
    }

    private static void solve(int[][] rounds) {
        // Calculate the points (rounds x category).
        int point[][] = new int[NROUNDS][NCATEGORIES];
        for (int r = 0; r < NROUNDS; ++r) {
            for (Category cat : Category.values()) {
                point[r][cat.ordinal()] = getPoints(rounds[r], cat);
            }
        }

        // Data structure for storing DP results.
        int[][] sum = new int[NCOMBINATIONS][NBONUSSLOTS];
        int[][] choice = new int[NCOMBINATIONS][NBONUSSLOTS];

        // Initialize.
        for (int j = 0; j < NCOMBINATIONS; ++j) {
            Arrays.fill(sum[j], -1);
        }
        sum[0][0] = 0;

        for (int r = 0; r < NROUNDS; ++r) {
            for (int t = 0; t < NCOMBINATIONS; ++t) {
                // @formatter:off
                /* Ensure that each r+1 is processed only when it equals to
                 * the number of bits in t. (e.g., 00..01 -> round 0,
                 * 00..10 -> round 0, 00..11 -> round 1, 00..100 -> round 0, etc.).
                 */
                // @formatter:on
                int nbits = Integer.bitCount(t);
                if (nbits != r + 1) {
                    continue;
                }

                for (int c = 0; c < NCATEGORIES; ++c) {
                    // @formatter:off
                    /*
                     * Ensure that the category is set in t. (e.g., 00..11100: categories {2,3,4}
                     * are set and may be used, skip all others. The previous round (r = 1)
                     * may have selected {00..11000, 00..10100, 00..01100}, so in this
                     * round (r = 2), we may only have selected {00..00100, 00..01000, 00..10000}.
                     * 00..11000	00..10100	00..01100 r = 1
                     * 00..00100	00..01000	00..10000 c = {2,3,4}
                     * ---------------------------------- | (bitwise OR)
                     * 00..11100	00..11100	00..11100	r = 2
                     */
                    // @formatter:on
                    if ((t & (1 << c)) == 0) {
                        continue;
                    }

                    // @formatter:off
                    /*
                     * toggle of the c bit to select the previous round.
                     * 00..11100 r = 3
                     * 00..01000 c = 3
                     * --------- ^ (bitwise XOR)
                     * 00..10100
                     */
                    // @formatter:on
                    int m = t ^ (1 << c);

                    // The scored points for this round and category.
                    int p = point[r][c];

                    // @formatter:off
                    /*
                     * decide which bonus slots are available for use.
                     * if (c < 6) { // e.g., p = 10
                     * 		previous round bonus slots are: 0..95(0..NBONUSSLOTS - 10 - 1)
                     * 		this round bonus slots are: 10..NBONUSSLOTS(0+10..NBONUSSLOTS - 1)
                     * } else {
                     *   	previous round bonus slots are: 0..NBONUSSLOTS - 1
                     *   	this round bonus slots are: 0..NBONUSSLOTS - 1
                     * }
                     */
                    // @formatter:on
                    int bPts = 0;
                    if (c < 6) {
                        bPts = p;
                    }
                    int n = NBONUSSLOTS - bPts;

                    for (int prevB = 0; prevB < n; ++prevB) {
                        // Skip slot if prevB bonus points have not been
                        // found, hence prevB slot is not filled.
                        if (sum[m][prevB] < 0) {
                            continue;
                        }

                        int currB = prevB + bPts;

                        // Calculate the points up to this round with or without
                        // the bonus points.
                        int newPts;
                        if (currB >= BONUSTHRESHOLD && prevB < BONUSTHRESHOLD) {
                            newPts = 35 + sum[m][prevB] + p;
                        } else {
                            newPts = sum[m][prevB] + p;
                        }

                        // Update the data if the new points is greater then old
                        // points.
                        if (sum[t][currB] < newPts) {
                            sum[t][currB] = newPts;
                            choice[t][currB] = c;
                        }
                    }
                }
            }
        }

        // Calculate the maxScore and remember the bonusSlot.
        int maxScore = 0;
        int last = NCOMBINATIONS - 1;
        int bonusSlot = 0;
        for (int b = 0; b < NBONUSSLOTS; ++b) {
            if (maxScore < sum[last][b]) {
                maxScore = sum[last][b];
                bonusSlot = b;
            }
        }

        // Map each category to the correct round.
        int bonus = bonusSlot >= BONUSTHRESHOLD ? BONUSPTS : 0;
        int[] catRoundMapping = new int[NROUNDS];
        for (int r = 12; r >= 0; --r) {
            int c = choice[last][bonusSlot];
            catRoundMapping[c] = r;

            /*
             * if (c < 6) previous bonus slot = current bonus slot - p, else
             * previous bonus slot = current bonus slot.
             */
            int p = 0;
            if (c < 6) {
                p = point[r][c];
            }
            bonusSlot -= p;

            // @formatter:off
            /*
             * toggle of the c bit to select the previous round.
             * 00..11100 r = 3
             * 00..01000 c = 3
             * --------- ^ (bitwise XOR)
             * 00..10100
             */
            // @formatter:off:on
            last ^= (1 << c);
        }

        // Print the solution as one line.
        StringBuilder line = new StringBuilder();
        for (int c = 0; c < NCATEGORIES; ++c) {
            line.append(point[catRoundMapping[c]][c]).append(" ");
        }
        line.append(bonus).append(" ").append(maxScore);

        System.out.println(line);
    }

    /**
     * Returns the number of points obtainable by a set
     * of dices for a specific category.
     *
     * @param dices must be sorted in ascending order.
     * @param cat   the category
     * @return the points
     */
    private static int getPoints(int[] dices, Category cat) {
        int points = 0;
        int c = cat.ordinal();

        // Sum of all dice thrown which equal to cat.
        if (cat.compareTo(Category.SIX) <= 0) {
            for (int n : dices) {
                if (n == c + 1) {
                    points += n;
                }
            }

            return points;
        }

        switch (cat) {
            // Sum of all dice thrown.
            case CHANCE: // Sum of all dice thrown.
                for (int n : dices) {
                    points += n;
                }
                break;
            // e.g., 1,1,1,3,4
            case THREEOFAKIND:
                if (dices[0] == dices[2] || dices[1] == dices[3] || dices[2] == dices[4]) {

                    for (int n : dices) {
                        points += n;
                    }
                }
                break;
            // e.g., 1,1,1,1,4
            case FOUROFAKIND:
                if (dices[0] == dices[3] || dices[1] == dices[4]) {
                    for (int n : dices) {
                        points += n;
                    }
                }
                break;
            // e.g., 1,2,3,4,6
            case SHORTSTRAIGHT:
                if (isStraight(dices, 0, NDICES - 1)) {
                    points = SHORTSTRAIGHTPTS;
                } else if (isStraight(dices, 1, NDICES - 1)) {
                    points = SHORTSTRAIGHTPTS;
                }
                break;
            // e.g., 1,2,3,4,5
            case LONGSTRAIGHT:
                if (isStraight(dices, 0, NDICES)) {
                    points = LONGSTRAIGHTPTS;
                }
                break;
            // e.g., 1,1,1,1,1, 1,1,1,4,4
            case FULLHOUSE:
                if (dices[0] == dices[1] && dices[2] == dices[4] || dices[0] == dices[2]
                        && dices[3] == dices[4]) {
                    points = FULLHOUSEPTS;
                }
                break;
            // e.g., 1,1,1,1,1
            case FIVEOFAKIND:
                points = dices[0] == dices[dices.length - 1] ? FIVEOFAKINDPTS : 0;
                break;
        }

        return points;
    }

    private static boolean isStraight(int[] ar, int start, int n) {
        for (int i = start + 1; i < start + n; ++i) {
            if (ar[i - 1] + 1 != ar[i]) {
                return false;
            }
        }

        return true;
    }
}
