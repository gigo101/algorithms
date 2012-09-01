package org.giccon.algorithms.challenges;

/* Solution: ad hoc */

import java.util.Scanner;
import java.util.StringTokenizer;

public class TheTrip {
    public static void main(String[] args) {
        TheTrip.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            StringTokenizer idata = new StringTokenizer(sc.nextLine());
            int n = Integer.parseInt(idata.nextToken());
            if (n <= 0) {
                break;
            }

            double exp[] = new double[n];
            double total = 0.0; // total of expenses
            for (int i = 0; i < n; ++i) {
                idata = new StringTokenizer(sc.nextLine());
                exp[i] = (Double.parseDouble(idata.nextToken()));
                total += exp[i];
            }
            double avg = total / n; // average expenses per person
            double negDiff = 0.0;
            double posDiff = 0.0;
            for (double d : exp) {
                double diff = avg - d;
                if (diff < 0) {
                    negDiff += Math.ceil(diff * 100) / 100;
                } else {
                    posDiff += Math.floor(diff * 100) / 100;
                }
            }

            negDiff = -negDiff;
            System.out.printf("$%.2f%n", negDiff > posDiff ? negDiff : posDiff);
        }
    }
}