package org.giccon.algorithms.challenges;

/* Solution: ad hoc, sorting */

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class FileFragmentation {
    public static void main(String[] args) {
        FileFragmentation.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int nTestCases = Integer.parseInt(sc.nextLine());
        sc.nextLine(); // process the empty line.

        Map<Integer, List<String>> fragmentsMap = new HashMap<Integer, List<String>>();
        Set<String> unique = new HashSet<String>();
        Set<Integer> sizes = new HashSet<Integer>();

        for (int t = 0; t < nTestCases; ++t) {
            /*
             * Read the fragments and store them in fragmentsMap mapped by
             * length. Only store the unique fragments. Also store the
             * fragments' their unique sizes in the sizes set. e.g, if we have
             * the set {1, 3, 4, 5, 7}, then this means that we have a file of
             * length 8(1+7 or 3+5 or 4+4).
             */
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isEmpty()) {
                    break;
                }

                int l = line.length();

                if (unique.add(line)) {
                    List<String> fragments = fragmentsMap.get(l);
                    if (fragments == null) {
                        fragments = new LinkedList<String>();
                        fragmentsMap.put(l, fragments);
                    }

                    fragments.add(line);
                    sizes.add(l);
                }
            }

            Integer[] ar = sizes.toArray(new Integer[1]);
            Arrays.sort(ar);
            String output = "";

            if (ar.length == 1) {
                // The fragments are all of the same size.
                // e.g., ar = [4]

                int l = ar[0];
                List<String> fragments = fragmentsMap.get(l);
                if (fragments.size() == 1) {
                    // e.g., fragmentsMap: {4=[1100]}
                    // output: "1100" + "1100" = "11001100"

                    output = fragments.get(0);
                    output += output;
                } else {
                    // e.g., fragmentsMap: {4=[1100, 0000]}
                    // output: "1100" + "0000" = "11000000"

                    output = fragments.get(0) + fragments.get(1);
                }
            } else if (ar.length == 2) {
                // There are two different sizes of fragments.
                // e.g., ar = [3, 4]

                int l1 = ar[0];
                int l2 = ar[ar.length - 1];
                List<String> fr1 = fragmentsMap.get(l1);
                List<String> fr2 = fragmentsMap.get(l2);
                if (fr1.size() == fr2.size()) {
                    // e.g., fragmentsMap: {3=[001, 101], 4=[1001, 1011]}
                    // output: "101" + "1001" = "1011001"
                    // output: "1011" + "001" = "1011001"

                    output = fr1.get(0) + fr2.get(0);
                } else {
                    // e.g., fragmentsMap: {3=[100, 001], 4=[1001]}
                    // output: "100" + "1001" = "1001001"
                    // output: "1001" + "001" = "1001001"

                    output = calcOutput(fr1, fr2);
                }
            } else if (ar.length > 0) {
                // There are more than two different sizes of fragments.
                // e.g., ar = [3, 4, 5]

                List<String> frA1 = fragmentsMap.get(ar[0]);
                List<String> frB1 = fragmentsMap.get(ar[ar.length - 1]);

                if (frA1.size() == 2 || frB1.size() == 2) {
                    // e.g., fragmentsMap: {3=[101, 001], 4=[0011, 0101],
                    // 5=[10101, 00110]}
                    // output: "001" + "10101" = "00110101"
                    // output: "00110" + "101" = "00110101"

                    output = calcOutput(frA1, frB1);
                } else {
                    List<String> frA2 = fragmentsMap.get(ar[1]);
                    List<String> frB2 = fragmentsMap.get(ar[ar.length - 2]);

                    if (ar.length > 3 && (frA2.size() == 2 || frB2.size() == 2)) {
                        // e.g., fragmentsMap: {1=[1], 3=[101, 001], 4=[0011,
                        // 0101], 5=[10101, 00110], 7=[0011010],}
                        // output: "00110" + "101" = "00110101"
                        // output: "001" + "10101" = "00110101"

                        output = calcOutput(frA2, frB2);
                    } else {
                        // e.g., fragmentsMap: {1=[1], 3=[001], 4=[0011,
                        // 0101], 5=[10101], 7=[0011010],}
                        // output: "0011010" + "1" = "00110101"
                        // output: "001" + "10101" = "00110101"

                        output = calcOutput(frA1.get(0), frB1.get(0),
                                frA2.get(0), frB2.get(0));
                    }
                }
            }

            System.out.println(output);

            if (t < nTestCases - 1) {
                System.out.println();

                fragmentsMap.clear();
                unique.clear();
                sizes.clear();
            }
        }
    }

    private static String calcOutput(List<String> fr1, List<String> fr2) {
        String sA1 = fr1.get(0);
        String sB1 = sA1;
        if (fr1.size() > 1) {
            sB1 = fr1.get(1);
        }

        String sA2 = fr2.get(0);
        String sB2 = sA2;
        if (fr2.size() > 1) {
            sB2 = fr2.get(1);
        }

        return calcOutput(sA1, sB2, sA2, sB1);
    }

    private static String calcOutput(String sA1, String sB1, String sA2,
                                     String sB2) {

        String output = sA1 + sB1;
        if (output.equals(sA2 + sB2) || output.equals(sB2 + sA2)) {
            return output;
        } else {
            return sB1 + sA1;
        }
    }
}
