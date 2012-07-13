package org.giccon.algorithms.puzzles;

/* Simple algorithm using recursion */

import java.util.Scanner;

public class TowerOfHanoi {
    private static final int PEGSUM = 1 + 2 + 3;

    public static void main(String[] args) {
        TowerOfHanoi.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int noDisks = sc.nextInt();
        int srcPeg = sc.nextInt();
        int dstPeg = sc.nextInt();

        processSolution(noDisks, srcPeg, dstPeg);
    }

    private static void processSolution(int noDisks, int srcPeg, int dstPeg) {
        if (noDisks == 0) {
            return;
        }

        int tmpPeg = PEGSUM - srcPeg - dstPeg;

        processSolution(noDisks - 1, srcPeg, tmpPeg);
        System.out.printf("Move disc %d from peg %d to peg %d.\n", noDisks, srcPeg, dstPeg);
        processSolution(noDisks - 1, tmpPeg, dstPeg);
    }
}
