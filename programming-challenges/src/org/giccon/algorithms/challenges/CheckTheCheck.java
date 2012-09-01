package org.giccon.algorithms.challenges;

/* Solution: ad hoc, simulation */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CheckTheCheck {
    private static enum Piece {
        PAWN('p'), ROOK('r'), BISCHOP('b'), QUEEN('q'), KING('k'), KNIGHT('n');

        private char repr;

        private Piece(char c) {
            repr = c;
        }

        public static boolean isWhitePiece(char c) {
            return Character.isUpperCase(c);
        }

        public static Piece getPiece(char c) {
            c = Character.toLowerCase(c);

            for (Piece p : values()) {
                if (p.repr == c) {
                    return p;
                }
            }

            return null;
        }

        public char getRepr() {
            return repr;
        }
    }

    private static final char EMPTY = '.';
    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final int WHITE = 0;
    private static final int BLACK = 1;

    private char[][] board = new char[ROWS][COLS];
    private PlayerPiece[] kings = new PlayerPiece[2];
    private List<List<PlayerPiece>> playersPieces = new ArrayList<List<PlayerPiece>>();

    public static void main(String[] args) {
        new CheckTheCheck().begin();
    }

    private CheckTheCheck() {
        playersPieces.add(new LinkedList<PlayerPiece>());
        playersPieces.add(new LinkedList<PlayerPiece>());
    }

    private void begin() {
        Scanner sc = new Scanner(System.in);
        int r = 0;
        int game = 1;
        while (sc.hasNextLine()) {
            String input = sc.nextLine();

            char king = Piece.KING.getRepr();
            for (int c = 0; c < COLS; ++c) {
                char repr = input.charAt(c);
                board[ROWS - 1 - r][c] = repr;

                if (repr == EMPTY) {
                    continue;
                }

                if (Piece.isWhitePiece(repr)) {
                    if (Character.toLowerCase(repr) == king) {
                        kings[WHITE] = new PlayerPiece(Piece.KING,
                                ROWS - 1 - r, c);
                    }

                    playersPieces.get(WHITE).add(
                            new PlayerPiece(Piece.getPiece(repr), ROWS - 1 - r,
                                    c));
                } else {
                    if (repr == king) {
                        kings[BLACK] = new PlayerPiece(Piece.KING,
                                ROWS - 1 - r, c);
                    }
                    playersPieces.get(BLACK).add(
                            new PlayerPiece(Piece.getPiece(repr), ROWS - 1 - r,
                                    c));
                }
            }
            r++;

            if (r == ROWS) {
                boolean isBoardEmpty = true;
                // Check whether board is empty.
                for (char[] col : board) {
                    for (char c : col) {
                        if (c != EMPTY) {
                            isBoardEmpty = false;
                            break;
                        }
                    }
                    if (!isBoardEmpty) {
                        break;
                    }
                }
                // Quit if board is empty.
                if (isBoardEmpty) {
                    return;
                }

                verifyCheck(game++);
                reset();

                // Process the blank line.
                sc.nextLine();

                r = 0;
            }
        }
    }

    private void reset() {
        kings[WHITE] = null;
        kings[BLACK] = null;

        playersPieces.get(WHITE).clear();
        playersPieces.get(BLACK).clear();
    }

    private void verifyCheck(int game) {
        for (int i = 0; i < kings.length; ++i) {
            PlayerPiece king = kings[i];
            boolean isCheck = isCheck(king, i == WHITE);
            if (isCheck) {
                System.out.printf("Game #%d: %s king is in check.\n", game,
                        i == WHITE ? "white" : "black");
                return;
            }
        }

        System.out.printf("Game #%d: no king is in check.\n", game);
    }

    private boolean isCheck(PlayerPiece king, boolean isWhite) {
        int opp = (isWhite ? BLACK : WHITE);
        for (PlayerPiece p : playersPieces.get(opp)) {
            int r1 = p.getRow();
            int c1 = p.getCol();
            int r2 = king.getRow();
            int c2 = king.getCol();

            boolean isValidMove = false;
            switch (p.getPiece()) {
                case PAWN:
                    isValidMove = isValidMovePawn(r1, c1, r2, c2, isWhite);
                    break;
                case ROOK:
                    isValidMove = isValidMoveRook(r1, c1, r2, c2);
                    break;
                case BISCHOP:
                    isValidMove = isValidMoveBischop(r1, c1, r2, c2);
                    break;
                case QUEEN:
                    isValidMove = isValidMoveQueen(r1, c1, r2, c2);
                    break;
                case KING:
                    isValidMove = false;
                    break;
                case KNIGHT:
                    isValidMove = isValidMoveKnight(r1, c1, r2, c2);
                    break;
            }

            if (isValidMove) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidMoveKnight(int r1, int c1, int r2, int c2) {
        int diffR = Math.abs(r1 - r2);
        int diffC = Math.abs(c1 - c2);

        return (diffR == 1 && diffC == 2 || diffR == 2 && diffC == 1);
    }

    private boolean isValidMoveQueen(int r1, int c1, int r2, int c2) {
        if (isValidHorMove(r1, c1, r2, c2)) {
            return true;
        } else if (isValidVertMove(r1, c1, r2, c2)) {
            return true;
        } else if (isValidDiagonalMove(r1, c1, r2, c2)) {
            return true;
        }

        return false;
    }

    private boolean isValidMoveBischop(int r1, int c1, int r2, int c2) {
        return isValidDiagonalMove(r1, c1, r2, c2);
    }

    private boolean isValidMoveRook(int r1, int c1, int r2, int c2) {
        if (isValidHorMove(r1, c1, r2, c2)) {
            return true;
        } else if (isValidVertMove(r1, c1, r2, c2)) {
            return true;
        }

        return false;
    }

    private boolean isValidMovePawn(int r1, int c1, int r2, int c2,
                                    boolean isWhite) {
        if (isWhite) {
            if (r2 - r1 == -1 && Math.abs(c2 - c1) == 1) {
                return true;
            }
        } else {
            if (r2 - r1 == 1 && Math.abs(c2 - c1) == 1) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidHorMove(int r1, int c1, int r2, int c2) {
        if (r1 != r2) {
            return false;
        }

        int step = c1 - c2 < 0 ? 1 : -1;
        if (step > 0) {
            for (int c = c1 + step; c < c2; c += step) {
                if (board[r1][c] != EMPTY) {
                    return false;
                }
            }
        } else if (step < 0) {
            for (int c = c1 + step; c > c2; c += step) {
                if (board[r1][c] != EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isValidVertMove(int r1, int c1, int r2, int c2) {
        if (c1 != c2) {
            return false;
        }

        int step = r1 - r2 < 0 ? 1 : -1;
        if (step > 0) {
            for (int r = r1 + step; r < r2; r += step) {
                if (board[r][c1] != EMPTY) {
                    return false;
                }
            }
        } else if (step < 0) {
            for (int r = r1 + step; r > r2; r += step) {
                if (board[r][c1] != EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isValidDiagonalMove(int r1, int c1, int r2, int c2) {
        if (Math.abs(r1 - r2) != Math.abs(c1 - c2)) {
            return false;
        }

        int stepR = r1 - r2 < 0 ? 1 : -1;
        int stepC = c1 - c2 < 0 ? 1 : -1;
        if (stepC > 0) {
            for (int r = r1 + stepR, c = c1 + stepC; c < c2; c += stepC, r += stepR) {
                if (board[r][c] != EMPTY) {
                    return false;
                }
            }
        } else if (stepC < 0) {
            for (int r = r1 + stepR, c = c1 + stepC; c > c2; c += stepC, r += stepR) {
                if (board[r][c] != EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    private static class PlayerPiece {
        private Piece piece;
        private int row;
        private int col;

        public PlayerPiece(Piece piece, int row, int col) {
            this.piece = piece;
            this.row = row;
            this.col = col;
        }

        public Piece getPiece() {
            return piece;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }
}
