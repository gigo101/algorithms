package org.giccon.algorithms.challenges;

/* Simple algorithm */

import java.util.Scanner;
import java.util.StringTokenizer;

public class Minesweeper {
    private static final char MINE = '*';
    private static final char EMPTY = '.';

    public static void main(String args[]) {
        Minesweeper.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        StringTokenizer idata;
        int r, c;
        char[][] m;
        int fi = 1;
        boolean isFirstTime = true;
        while (sc.hasNextLine()) {
            idata = new StringTokenizer(sc.nextLine());
            r = Integer.parseInt(idata.nextToken());
            c = Integer.parseInt(idata.nextToken());

            if (r == 0 && c == 0) {
                break;
            }

            if (!isFirstTime) {
                System.out.println();
            } else {
                isFirstTime = false;
            }

            m = new char[r][c];
            for (int i = 0; i < r; ++i) {
                String input = sc.nextLine();
                for (int ii = 0; ii < c; ++ii) {
                    m[i][ii] = input.charAt(ii);
                }
            }

            if (r == 1 || c == 1) {
                calcNrMinesSpecialCase(m, r, c);
            } else {
                for (int i = 0; i < r; ++i) {
                    for (int ii = 0; ii < c; ++ii) {
                        if (m[i][ii] == EMPTY) {
                            calcNrMines(m, i, ii);
                        }
                    }
                }
            }

            System.out.printf("Field #%s:\n", fi++);
            for (int i = 0; i < r; ++i) {
                StringBuilder line = new StringBuilder();
                for (int ii = 0; ii < c; ++ii) {
                    line.append(m[i][ii]);
                }
                System.out.println(line);
            }
        }
    }

    private static void calcNrMinesSpecialCase(char[][] m, int r, int c) {
        if (r == 1 && c == 1) {
            m[0][0] = m[0][0] == MINE ? MINE : '0';
        } else if (r == 1) {
            int colEdgeIndex = m[0].length - 1;
            for (int i = 0; i < c; ++i) {
                if (m[0][i] == MINE) {
                    continue;
                }

                if (i == 0) {
                    if (m[0][i + 1] == MINE) {
                        m[0][i] = '1';
                    } else {
                        m[0][i] = '0';
                    }
                } else if (i == colEdgeIndex) {
                    if (m[0][i - 1] == MINE) {
                        m[0][i] = '1';
                    } else {
                        m[0][i] = '0';
                    }
                } else {
                    int n = 0;
                    if (m[0][i - 1] == MINE) {
                        n++;
                    }
                    if (m[0][i + 1] == MINE) {
                        n++;
                    }
                    m[0][i] = (char) ('0' + n);
                }
            }
        } else {
            int rowEdgeIndex = m.length - 1;
            for (int i = 0; i < r; ++i) {
                if (m[i][0] == MINE) {
                    continue;
                }

                if (i == 0) {
                    if (m[i + 1][0] == MINE) {
                        m[i][0] = '1';
                    } else {
                        m[i][0] = '0';
                    }
                } else if (i == rowEdgeIndex) {
                    if (m[i - 1][0] == MINE) {
                        m[i][0] = '1';
                    } else {
                        m[i][0] = '0';
                    }
                } else {
                    int n = 0;
                    if (m[i - 1][0] == MINE) {
                        n++;
                    }
                    if (m[i + 1][0] == MINE) {
                        n++;
                    }
                    m[i][0] = (char) ('0' + n);
                }
            }
        }
    }

    private static void calcNrMines(char[][] m, int r, int c) {
        int rowEdgeIndex = m.length - 1;
        int colEdgeIndex = m[0].length - 1;
        if (c == 0) {
            if (r == 0) { // top left corner
                m[r][c] = getNrMinesCornerCase(m, r, c, 1, 1);
            } else if (r == rowEdgeIndex) { // bottom left corner
                m[r][c] = getNrMinesCornerCase(m, r, c, -1, 1);
            } else { // in between left
                m[r][c] = getNrMinesLRInBetwCase(m, r, c, 1);
            }
        } else if (c == colEdgeIndex) {
            if (r == 0) { // top right corner
                m[r][c] = getNrMinesCornerCase(m, r, c, 1, -1);
            } else if (r == rowEdgeIndex) { // bottom right corner
                m[r][c] = getNrMinesCornerCase(m, r, c, -1, -1);
            } else { // in between right
                m[r][c] = getNrMinesLRInBetwCase(m, r, c, -1);
            }
        } else {
            if (r == 0) { // in between top
                m[r][c] = getNrMinesTDInBetwCase(m, r, c, 1);
            } else if (r == rowEdgeIndex) { // in between bottom
                m[r][c] = getNrMinesTDInBetwCase(m, r, c, -1);
            } else { // in between in between
                m[r][c] = getNrMinesBBCase(m, r, c);
            }
        }
    }

    private static char getNrMinesCornerCase(char[][] m, int r, int c, int rAdjInd,
                                      int cAdjInd) {
        int n = 0;

        if (m[r][c + cAdjInd] == MINE) {
            n++;
        }
        if (m[r + rAdjInd][c] == MINE) {
            n++;
        }
        if (m[r + rAdjInd][c + cAdjInd] == MINE) {
            n++;
        }

        return (char) ('0' + n);
    }

    private static char getNrMinesLRInBetwCase(char[][] m, int r, int c, int cAdjInd) {
        int n = 0;

        if (m[r - 1][c] == MINE) {
            n++;
        }
        if (m[r - 1][c + cAdjInd] == MINE) {
            n++;
        }
        if (m[r][c + cAdjInd] == MINE) {
            n++;
        }
        if (m[r + 1][c + cAdjInd] == MINE) {
            n++;
        }
        if (m[r + 1][c] == MINE) {
            n++;
        }

        return (char) ('0' + n);
    }

    private static char getNrMinesTDInBetwCase(char[][] m, int r, int c, int rAdjInd) {
        int n = 0;

        if (m[r][c - 1] == MINE) {
            n++;
        }
        if (m[r + rAdjInd][c - 1] == MINE) {
            n++;
        }
        if (m[r + rAdjInd][c] == MINE) {
            n++;
        }
        if (m[r + rAdjInd][c + 1] == MINE) {
            n++;
        }
        if (m[r][c + 1] == MINE) {
            n++;
        }

        return (char) ('0' + n);
    }

    private static char getNrMinesBBCase(char[][] m, int r, int c) {
        int n = 0;

        if (m[r - 1][c] == MINE) {
            n++;
        }
        if (m[r - 1][c + 1] == MINE) {
            n++;
        }
        if (m[r][c + 1] == MINE) {
            n++;
        }
        if (m[r + 1][c + 1] == MINE) {
            n++;
        }
        if (m[r + 1][c] == MINE) {
            n++;
        }
        if (m[r + 1][c - 1] == MINE) {
            n++;
        }
        if (m[r][c - 1] == MINE) {
            n++;
        }
        if (m[r - 1][c - 1] == MINE) {
            n++;
        }

        return (char) ('0' + n);
    }
}
