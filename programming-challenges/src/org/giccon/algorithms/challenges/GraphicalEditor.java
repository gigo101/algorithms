package org.giccon.algorithms.challenges;

/* Solution: ad hoc, simulation */

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GraphicalEditor {
    private static enum Command {
        CREATE('I'), CLEAR('C'), COLOR('L'), VDRAW('V'), HDRAW('H'), FILLRECT(
                'K'), FILLREG('F'), SAVE('S'), TERMINATE('X');

        private char cmdSymbol;

        private Command(char c) {
            this.cmdSymbol = c;
        }

        public static boolean isValidCommand(char c) {
            for (Command cmd : values()) {
                if (cmd.cmdSymbol == c) {
                    return true;
                }
            }
            return false;
        }

        public static Command getCommand(char c) {
            Command result = null;
            for (Command cmd : values()) {
                if (cmd.cmdSymbol == c) {
                    result = cmd;
                    break;
                }
            }

            return result;
        }
    }

    private char[][] bitmap;

    public static void main(String[] args) {
        new GraphicalEditor().begin();
    }

    private void begin() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            StringTokenizer iData = new StringTokenizer(sc.nextLine());
            boolean doQuit = processCommand(iData);
            if (doQuit) {
                break;
            }

        }
    }

    private boolean processCommand(StringTokenizer iData) {
        char cmdSymbol = iData.nextToken().charAt(0);
        if (!Command.isValidCommand(cmdSymbol)) {
            return false;
        }

        Command cmd = Command.getCommand(cmdSymbol);
        switch (cmd) {
            case CREATE:
                int cols = Integer.parseInt(iData.nextToken());
                int rows = Integer.parseInt(iData.nextToken());
                doCreate(rows, cols);
                break;

            case CLEAR:
                doClear();
                break;

            case COLOR:
                int c = Integer.parseInt(iData.nextToken());
                int r = Integer.parseInt(iData.nextToken());
                char colour = iData.nextToken().charAt(0);
                doColor(r - 1, c - 1, colour);
                break;

            case VDRAW:
                c = Integer.parseInt(iData.nextToken());
                int r1 = Integer.parseInt(iData.nextToken());
                int r2 = Integer.parseInt(iData.nextToken());
                colour = iData.nextToken().charAt(0);

                if (r1 > r2) {
                    int t = r1;
                    r1 = r2;
                    r2 = t;
                }

                doVerticalDraw(c - 1, r1 - 1, r2 - 1, colour);
                break;

            case HDRAW:
                int c1 = Integer.parseInt(iData.nextToken());
                int c2 = Integer.parseInt(iData.nextToken());
                r = Integer.parseInt(iData.nextToken());
                colour = iData.nextToken().charAt(0);

                if (c1 > c2) {
                    int t = c1;
                    c1 = c2;
                    c2 = t;
                }

                doHorizontalDraw(r - 1, c1 - 1, c2 - 1, colour);
                break;

            case FILLRECT:
                c1 = Integer.parseInt(iData.nextToken());
                r1 = Integer.parseInt(iData.nextToken());
                c2 = Integer.parseInt(iData.nextToken());
                r2 = Integer.parseInt(iData.nextToken());
                colour = iData.nextToken().charAt(0);

                doFillRect(r1 - 1, c1 - 1, r2 - 1, c2 - 1, colour);
                break;

            case FILLREG:
                c = Integer.parseInt(iData.nextToken());
                r = Integer.parseInt(iData.nextToken());
                colour = iData.nextToken().charAt(0);
                doFillRegion(c - 1, r - 1, colour);
                break;

            case SAVE:
                StringBuilder name = new StringBuilder(iData.nextToken());
                while (iData.hasMoreTokens()) {
                    name.append(iData.nextToken());
                }
                doSave(name.toString());
                break;

            case TERMINATE: {
                return true;
            }
        }

        return false;
    }

    private void doSave(String name) {
        System.out.printf("%s\n", name);
        for (char[] rc : bitmap) {
            String line = String.valueOf(rc);
            System.out.println(line);
        }
    }

    private void doFillRegion(int c, int r, char colour) {
        char origColour = bitmap[r][c];
        if (origColour == colour) {
            return;
        }

        Queue<int[]> l = new ArrayDeque<int[]>();
        bitmap[r][c] = colour;
        l.add(new int[]{r, c});

        while (!l.isEmpty()) {
            int a[] = l.poll();
            r = a[0];
            c = a[1];

            if (r > 0) {
                if (bitmap[r - 1][c] == origColour) {
                    bitmap[r - 1][c] = colour;
                    l.add(new int[]{r - 1, c});
                }
            }

            if (r < bitmap.length - 1) {
                if (bitmap[r + 1][c] == origColour) {
                    bitmap[r + 1][c] = colour;
                    l.add(new int[]{r + 1, c});
                }
            }

            if (c > 0) {
                if (bitmap[r][c - 1] == origColour) {
                    bitmap[r][c - 1] = colour;
                    l.add(new int[]{r, c - 1});
                }
            }

            if (c < bitmap[0].length - 1) {
                if (bitmap[r][c + 1] == origColour) {
                    bitmap[r][c + 1] = colour;
                    l.add(new int[]{r, c + 1});
                }
            }
        }
    }

    private void doFillRect(int r1, int c1, int r2, int c2, char colour) {
        for (int r = r1; r <= r2; ++r) {
            for (int c = c1; c <= c2; ++c) {
                bitmap[r][c] = colour;
            }
        }
    }

    private void doHorizontalDraw(int r, int c1, int c2, char colour) {
        for (int c = c1; c <= c2; ++c) {
            bitmap[r][c] = colour;
        }
    }

    private void doVerticalDraw(int c, int r1, int r2, char colour) {
        for (int r = r1; r <= r2; ++r) {
            bitmap[r][c] = colour;
        }
    }

    private void doColor(int r, int c, char colour) {
        bitmap[r][c] = colour;
    }

    private void doClear() {
        for (char[] rc : bitmap) {
            for (int i = 0; i < rc.length; ++i) {
                rc[i] = 'O';
            }
        }
    }

    private void doCreate(int rows, int cols) {
        bitmap = new char[rows][cols];
        doClear();
    }
}
