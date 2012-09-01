package org.giccon.algorithms.challenges;

/* Solution: ad hoc, sorting */

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class StacksOfFlapjacks {
    public static void main(String[] args) {
        StacksOfFlapjacks.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            processFlapjacks(sc.nextLine());
        }
    }

    private static void processFlapjacks(String line) {
        List<Integer> flapjacks = new ArrayList<Integer>();
        List<Integer> sortedFlapjacks = new ArrayList<Integer>();
        List<Integer> flipIndices = new LinkedList<Integer>();

        // Read in the flapjacks
        Scanner sc = new Scanner(line);
        while (sc.hasNextInt()) {
            int n = sc.nextInt();
            flapjacks.add(n);
            sortedFlapjacks.add(n);
        }
        Collections.sort(sortedFlapjacks);

        /* Note: indexes for the flips are numbered from right to left.
        Compare with the original input with the sorted flapjacks.
        Place the flap jack in the correct order by flipping it once
        or twice. Once if the flapjack is at the top. Twice if not and
        doesn't belong to be at the top.
        One flip: e.g., 3 1 2 4 5 6, to place the number 3 in the
        correct position flip it using index 4. You will get
        2 1 3 4 5 6.
        Two flips: e.g., 1 2 4 3 5 6, to place the number 4 in the
        correct position flip it once using index 4. Now the order
        will become 4 2 1 3 5 6. Flip it again using index 3. You
        will get 3 1 2 4 5 6.
        */
        int length = flapjacks.size();
        for (int i = length - 1; i > 0; i--) {
            int n = sortedFlapjacks.get(i);
            if (n == flapjacks.get(i)) {
                continue;
            }

            int flipIndex = length - flapjacks.indexOf(n);
            if (flipIndex != length) {
                flip(flapjacks, flipIndex);
                flipIndices.add(flipIndex);
            }

            flipIndex = length - i;
            flip(flapjacks, flipIndex);
            flipIndices.add(flipIndex);
        }

        // Print the original stack.
        System.out.println(line);
        print(flipIndices);
    }

    private static void print(List<Integer> flipIndices) {
        StringBuilder sb = new StringBuilder();
        for (Integer flipIndex : flipIndices) {
            sb.append(flipIndex);
            sb.append(" ");
        }
        sb.append(0);
        System.out.println(sb);
    }

    private static void flip(List<Integer> flapjacks, int flipIndex) {
        Collections.reverse(flapjacks.subList(0, flapjacks.size() - flipIndex + 1));
    }
}
