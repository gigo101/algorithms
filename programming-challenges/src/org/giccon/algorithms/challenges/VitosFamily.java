package org.giccon.algorithms.challenges;

/* Solution: sorting, median selection */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class VitosFamily {
    public static void main(String[] args) {
        VitosFamily.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int nbrTestCases = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < nbrTestCases; i++) {
            System.out.println(getMinDistance(sc.nextLine()));
        }
    }

    private static int getMinDistance(String line) {
        Scanner sc = new Scanner(line);
        sc.nextInt(); // skip the number of relatives.

        List<Integer> addresses = new ArrayList<Integer>();

        // Read in the addresses.
        while (sc.hasNextInt()) {
            addresses.add(sc.nextInt());
        }

        Collections.sort(addresses);

        // Calculate the median.
        int median;
        if (addresses.size() % 2 == 0) {
            // even number
            int index1 = addresses.size() / 2 - 1;
            int index2 = addresses.size() / 2;
            median = (addresses.get(index1) + addresses.get(index2)) / 2;
        } else {
            // odd number
            int index = addresses.size() / 2;
            median = addresses.get(index);
        }

        // Calculate the distance.
        int dist = 0;
        for (Integer addr : addresses) {
            dist += Math.abs(median - addr);
        }

        return dist;
    }
}
