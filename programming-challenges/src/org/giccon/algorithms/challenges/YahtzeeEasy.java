package org.giccon.algorithms.challenges;

/* Algorithm using dynamic programming with bitmasks */

import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class YahtzeeEasy {
    private static enum Category {
        ONE, TWO, THREE, FOUR, FIVE, SIX, CHANCE, THREEOFAKIND, FOUROFAKIND,
        FIVEOFAKIND, SHORTSTRAIGHT, LONGSTRAIGHT, FULLHOUSE
    }

    private static final int NROUNDS = 13;
    private static final int NCATEGORIES = 13;
    private static final int NCOMBINATIONS = 8192; // 2 << CATEGORIES
    private static final int NBONUSSLOTS = 64;
    private static final int NDICES = 5;
    private static final int BONUSPTS = 35;

    public static void main(String[] args) {
        YahtzeeEasy.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int count = 0;
        int[][] rounds = new int[NROUNDS][5];

        while (sc.hasNext()) {
            String input = sc.nextLine();

            int i = 0;
            StringTokenizer st = new StringTokenizer(input);
            while (st.hasMoreTokens()) {
                rounds[count][i++] = Integer.parseInt(st.nextToken());
            }
            Arrays.sort(rounds[count]);

            count++;
            if (count % NCATEGORIES == 0) {
                solve(rounds);
                count = 0;
            }
        }
    }

    private static void solve(int[][] rounds) {
        int point[][] = new int[NROUNDS][NCATEGORIES];
        for (int r = 0; r < NROUNDS; ++r) {
            for (Category cat : Category.values()) {
                point[r][cat.ordinal()] = getPoints(rounds[r], cat);
            }
        }

        int sum[][] = new int[NCOMBINATIONS][NBONUSSLOTS];
        int choice[][][] = new int[NCOMBINATIONS][NBONUSSLOTS][2];
        for (int[] row : sum) {
            Arrays.fill(row, -1);
        }
        sum[0][0] = 0;

        for (int m = 0; m < NCOMBINATIONS; ++m) {
            for (int c = 0; c < NCATEGORIES; ++c) {
                if ((m & (1 << c)) > 0) {
                    continue;
                }

                int r = Integer.bitCount(m);
                int t = m | (1 << c);
                int p = point[r][c];
                int a = c <= Category.SIX.ordinal() ? p : 0;

                for (int oldB = 0; oldB < NBONUSSLOTS; ++oldB) {
                    if (sum[m][oldB] < 0) {
                        continue;
                    }

                    int newB = (a + oldB) < NBONUSSLOTS - 1 ? a + oldB
                            : NBONUSSLOTS - 1;
                    if (sum[t][newB] < sum[m][oldB] + p) {
                        sum[t][newB] = sum[m][oldB] + p;
                        choice[t][newB][0] = c;
                        choice[t][newB][1] = oldB;
                    }
                }
            }
        }

        int maxScore = 0;
        int last = NCOMBINATIONS - 1;
        int bonusSlot = 0;
        for (int b = 0; b < NBONUSSLOTS; ++b) {
            if (maxScore < sum[last][b]) {
                maxScore = sum[last][b];
                bonusSlot = b;
            }
        }

        int total = maxScore;
        if (sum[last][NBONUSSLOTS - 1] > -1) {
            total = sum[last][NBONUSSLOTS - 1] + BONUSPTS;
        }

        int bonus = 0;
        if (total > maxScore) {
            maxScore = total;
            bonusSlot = NBONUSSLOTS - 1;
            bonus = BONUSPTS;
        }

        int[] catRoundMapping = new int[NROUNDS];
        for (int r = NROUNDS - 1; r >= 0; --r) {
            int c = choice[last][bonusSlot][0];
            catRoundMapping[c] = r;
            bonusSlot = choice[last][bonusSlot][1];
            last ^= (1 << c);
        }

        StringBuilder line = new StringBuilder();
        for (int c = 0; c < 13; ++c) {
            line.append(point[catRoundMapping[c]][c]).append(" ");
        }
        line.append(bonus).append(" ").append(maxScore);

        System.out.println(line);
    }

    private static int getPoints(int[] round, Category cat) {
        int points = 0;
        int c = cat.ordinal();

        if (cat.compareTo(Category.SIX) <= 0) {
            for (int n : round) {
                if (n == c + 1) {
                    points += n;
                }
            }

            return points;
        }

        switch (cat) {
            case CHANCE:
                for (int n : round) {
                    points += n;
                }

                break;
            case THREEOFAKIND:
                if (round[0] == round[2] || round[1] == round[3]
                        || round[2] == round[4]) {

                    for (int n : round) {
                        points += n;
                    }
                }
                break;
            case FOUROFAKIND:
                if (round[0] == round[3] || round[1] == round[4]) {
                    for (int n : round) {
                        points += n;
                    }
                }
                break;
            case SHORTSTRAIGHT:
                if (isStraight(round, 0, NDICES - 1)) {
                    points = 25;
                } else if (isStraight(round, 1, NDICES - 1)) {
                    points = 25;
                }
                break;
            case LONGSTRAIGHT:
                if (isStraight(round, 0, NDICES)) {
                    points = 35;
                }
                break;
            case FULLHOUSE:
                if (round[0] == round[1] && round[2] == round[4]
                        || round[0] == round[2] && round[3] == round[4]) {
                    points = 40;
                }
                break;
            case FIVEOFAKIND:
                points = round[0] == round[round.length - 1] ? 50 : 0;
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
