package org.giccon.algorithms.challenges;

/* Simple algorithm */

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class WhereIsWaldorf {
    private static class Pos implements Comparable<Pos> {
        private int r;
        private int c;

        public Pos(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public int compareTo(Pos o) {
            if (r < o.r) {
                return -1;
            } else if (r > o.r) {
                return 1;
            } else if (c < o.c) {
                return -1;
            } else if (c > o.c) {
                return 1;
            }

            return 0;
        }

        @Override
        public String toString() {
            return r + " " + c;
        }
    }

    public static void main(String[] args) {
        WhereIsWaldorf.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int nTestCases = Integer.parseInt(sc.nextLine());
        sc.nextLine(); // process empty line.

        /* @formatter:off
         * Decides how to search the word in the matrix.
         * 0,1 -> horizontally to the right
         * 1,0 -> vertically to the bottom
         * 1,1 -> diagonally to the right and bottom
         * 1,1 -> diagonally to the left and bottom
         */
        // @formatter:on
        int[][] dirs = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

        for (int t = 0; t < nTestCases; ++t) {
            int m = sc.nextInt();
            int n = sc.nextInt();
            sc.nextLine();

            // Read in the characters.
            char[][] matrix = new char[m + 1][n + 1];
            for (int r = 1; r <= m; ++r) {
                String line = sc.nextLine().toLowerCase();
                for (int c = 1; c <= n; ++c) {
                    matrix[r][c] = line.charAt(c - 1);
                }
            }

            int nWords = Integer.parseInt(sc.nextLine());
            StringBuilder testCaseOutput = new StringBuilder();
            // Read in the words and find its best position.
            while (--nWords >= 0) {
                String word = sc.nextLine().toLowerCase();
                List<Pos> positions = new LinkedList<Pos>();
                for (int dir[] : dirs) {
                    Pos pos = searchWord(matrix, word, dir[0], dir[1]);
                    if (pos != null) {
                        positions.add(pos);
                    }
                }

                // Store the best position of the word.
                if (positions.size() > 0) {
                    Collections.sort(positions);
                    testCaseOutput.append(positions.get(0)).append("\n");
                }
            }

            // Output the best positions of the words.
            if (testCaseOutput.length() > 0) {
                System.out.print(testCaseOutput);
            }

            if (t < nTestCases - 1) {
                System.out.println();
                sc.nextLine(); // process the empty line
            }
        }
    }

    private static Pos searchWord(char[][] matrix, String word, int vert,
                                  int hor) {

        // @formatter:off
        /*
         * 	Search horizontally to the right. (searching horizontally to the left
         *  is the same as searching the reverse word horizontally to the right)
         *  e.g., find search
         *  00search0
         *  000000000
         *  hcraes000
         *
         *  Search vertically to the bottom. (searching vertically to the top
         *  is the same as searching the reverse word vertically to the bottom)
         *  e.g., find search
         *  00s0000h0
         *  00e0000c0
         *  00a0000r0
         *  00r0000a0
         *  00c0000e0
         *  00h0000s0
         *
         *  Search diagonally to the right and bottom. (searching diagonally to the
         *  left and top is the same as searching the reverse word diagonally to
         *  the right and bottom)
         *  e.g., find search
         *  s0h000000
         *  0e0c00000
         *  00a0r0000
         *  000r0a000
         *  0000c0e00
         *  00000h0s0
         *
         *  Search diagonally to the left and bottom. (searching diagonally to the
         *  right and top is the same as searching the reverse word diagonally to
         *  the left and bottom)
         *  e.g., find search
         *  000000h0s
         *  00000c0e0
         *  0000r0a00
         *  000a0r000
         *  00e0c0000
         *  0s0h00000
         */
        // @formatter:on

        String revWord = new StringBuffer(word).reverse().toString();
        int length = word.length();

        int m = matrix.length - 1;
        int n = matrix[0].length - 1;

        if (vert > 0) {
            m -= length;
            m++;
        }

        if (hor > 0) {
            n -= length;
            n++;
        }

        for (int r = 1; r <= m; ++r) {
            for (int c = 1; c <= n; ++c) {
                if (hor < 0 && c < length) {
                    continue;
                }

                boolean isReverse = true;
                boolean isNormal = true;
                int iR = r;
                int iC = c;
                for (int i = 0; i < length; ++i) {
                    if (word.charAt(i) == matrix[iR][iC] && isNormal) {
                        isNormal = true;

                        if (i == length - 1) {
                            iR = vert > 0 ? iR - length + 1 : iR;
                            iC = hor > 0 ? iC - length + 1 : hor < 0 ? iC + length - 1 : iC;
                            return new Pos(iR, iC);
                        }
                    } else {
                        isNormal = false;
                    }

                    if (revWord.charAt(i) == matrix[iR][iC]
                            && isReverse) {

                        if (i == length - 1) {
                            return new Pos(iR, iC);
                        }
                    } else {
                        isReverse = false;
                    }

                    if (!isReverse && !isNormal) {
                        break;
                    }

                    iR += vert;
                    iC += hor;
                }
            }
        }

        return null;
    }
}