package org.giccon.algorithms.challenges;

/* Solution: ad hoc */

import java.util.Scanner;
import java.util.StringTokenizer;

public class LCDisplay {
    private static final int EMPTY = 0;
    private static final int HORIZONTAL = 1;
    private static final int VERTICAL = 2;

    private int[][][] dfr = new int[10][5][3]; // digit format rules

    public static void main(String[] args) {
        new LCDisplay().begin();
    }

    private void begin() {
        initialize();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            StringTokenizer idata = new StringTokenizer(sc.nextLine());
            int s = Integer.parseInt(idata.nextToken());
            String n = idata.nextToken();

            if (s == 0) {
                break;
            }

            // total number of columns of a digit.
            int digitCols = s + 2;

            // Total number of rows of a digit: 2s + 3
            int digitRows = 2 * s + 3;

            int middleRowI = digitRows / 2;

            for (int r = 0; r < digitRows; ++r) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < n.length(); ++i) {
                    int digit = n.charAt(i) - '0';
                    for (int c = 0; c < digitCols; ++c) {
                        int normC = 0;
                        if (c >= 1 && c <= s) {
                            normC = 1;
                        } else if (c == digitCols - 1) {
                            normC = 2;
                        }

                        int normR = 0;
                        if (r >= 1 && r <= s) {
                            normR = 1;
                        } else if (r == middleRowI) {
                            normR = 2;
                        } else if (r >= middleRowI + 1 && r <= middleRowI + s) {
                            normR = 3;
                        } else if (r == digitRows - 1) {
                            normR = 4;
                        }

                        if (dfr[digit][normR][normC] == HORIZONTAL) {
                            line.append("-");
                        } else if (dfr[digit][normR][normC] == VERTICAL) {
                            line.append("|");
                        } else if (dfr[digit][normR][normC] == EMPTY) {
                            line.append(" ");
                        }
                    }
                    if (i < n.length() - 1) {
                        line.append(" ");
                    }
                }
                System.out.println(line);
            }

            System.out.println();
        }
    }

    private void initialize() {
        // Initialise digit 0.
        dfr[0][0][1] = HORIZONTAL;
        dfr[0][1][0] = VERTICAL;
        dfr[0][1][2] = VERTICAL;
        dfr[0][3][0] = VERTICAL;
        dfr[0][3][2] = VERTICAL;
        dfr[0][4][1] = HORIZONTAL;

        // Initialise digit 1.
        dfr[1][1][2] = VERTICAL;
        dfr[1][3][2] = VERTICAL;

        // Initialise digit 2.
        dfr[2][0][1] = HORIZONTAL;
        dfr[2][1][2] = VERTICAL;
        dfr[2][2][1] = HORIZONTAL;
        dfr[2][3][0] = VERTICAL;
        dfr[2][4][1] = HORIZONTAL;

        dfr[3][0][1] = HORIZONTAL;
        dfr[3][1][2] = VERTICAL;
        dfr[3][2][1] = HORIZONTAL;
        dfr[3][3][2] = VERTICAL;
        dfr[3][4][1] = HORIZONTAL;

        // Initialise digit 4.
        dfr[4][1][0] = VERTICAL;
        dfr[4][1][2] = VERTICAL;
        dfr[4][2][1] = HORIZONTAL;
        dfr[4][3][2] = VERTICAL;

        // Initialise digit 5.
        dfr[5][0][1] = HORIZONTAL;
        dfr[5][1][0] = VERTICAL;
        dfr[5][2][1] = HORIZONTAL;
        dfr[5][3][2] = VERTICAL;
        dfr[5][4][1] = HORIZONTAL;

        // Initialise digit 6.
        dfr[6][0][1] = HORIZONTAL;
        dfr[6][1][0] = VERTICAL;
        dfr[6][2][1] = HORIZONTAL;
        dfr[6][3][0] = VERTICAL;
        dfr[6][3][2] = VERTICAL;
        dfr[6][4][1] = HORIZONTAL;

        // Initialise digit 7.
        dfr[7][0][1] = HORIZONTAL;
        dfr[7][1][2] = VERTICAL;
        dfr[7][3][2] = VERTICAL;

        // Initialise digit 8.
        dfr[8][0][1] = HORIZONTAL;
        dfr[8][1][0] = VERTICAL;
        dfr[8][1][2] = VERTICAL;
        dfr[8][2][1] = HORIZONTAL;
        dfr[8][3][0] = VERTICAL;
        dfr[8][3][2] = VERTICAL;
        dfr[8][4][1] = HORIZONTAL;

        // Initialise digit 9.
        dfr[9][0][1] = HORIZONTAL;
        dfr[9][1][0] = VERTICAL;
        dfr[9][1][2] = VERTICAL;
        dfr[9][2][1] = HORIZONTAL;
        dfr[9][3][2] = VERTICAL;
        dfr[9][4][1] = HORIZONTAL;
    }
}
