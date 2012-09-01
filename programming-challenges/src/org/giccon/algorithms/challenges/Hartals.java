package org.giccon.algorithms.challenges;

/* Solution: ad hoc, simulation */

import java.util.Scanner;

public class Hartals {
    public static void main(String[] args) {
        Hartals.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int nbrTestCases = sc.nextInt();

        boolean isHartalEachDay = false;
        for (int t = 0; t < nbrTestCases; ++t) {
            int nbrDays = sc.nextInt();
            int nbrParties = sc.nextInt();
            int[] parties = new int[nbrParties];
            for (int i = 0; i < nbrParties; ++i) {
                parties[i] = sc.nextInt();
                if (parties[i] == 1) {
                    isHartalEachDay = true;
                    break;
                }
            }

            if (isHartalEachDay) {
                int nbrFridays = (nbrDays + 1) / 7;
                int nbrSaturdays = nbrDays / 7;

                System.out.println(nbrDays - nbrFridays - nbrSaturdays);
            } else {
                System.out.println(getNbrWorkingDaysLost(nbrDays, parties));
            }
        }
    }

    private static int getNbrWorkingDaysLost(int nbrDays, int[] parties) {
        int nbrWorkingDaysLost = 0;
        for (int d = 1; d <= nbrDays; ++d) {
            // Disregard Fridays and Saturdays and Sundays.
            if ((d + 1) % 7 == 0 || d % 7 == 0) {
                continue;
            }

            for (int hp : parties) {
                if (d % hp == 0) {
                    nbrWorkingDaysLost++;
                    break;
                }
            }
        }

        return nbrWorkingDaysLost;
    }
}
