package org.giccon.algorithms.challenges;

/* Solution: ad hoc */

import java.util.Scanner;
import java.util.StringTokenizer;

public class JollyJumpers {
    public static void main(String[] args) {
        JollyJumpers.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine());
            int nbrInts = Integer.parseInt(st.nextToken());

            if (nbrInts <= 0) {
                System.out.println("Not jolly");
                continue;
            } else if (nbrInts == 1) {
                System.out.println("Jolly");
                continue;
            }

            boolean[] data = new boolean[nbrInts - 1];
            int prev = Integer.parseInt(st.nextToken());
            while (st.hasMoreTokens()) {
                int curr = Integer.parseInt(st.nextToken());
                int absDiff = Math.abs(prev - curr);
                if (absDiff > 0 && absDiff <= nbrInts - 1) {
                    data[absDiff - 1] = true;
                }
                prev = curr;
            }

            boolean isJolly = true;
            for (boolean b : data) {
                if (!b) {
                    isJolly = false;
                    break;
                }
            }

            if (isJolly) {
                System.out.println("Jolly");
            } else {
                System.out.println("Not jolly");
            }
        }
    }
}
