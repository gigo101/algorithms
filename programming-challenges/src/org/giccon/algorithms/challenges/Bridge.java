package org.giccon.algorithms.challenges;

/* Simple algorithm */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Bridge {
    public static void main(String[] args) {
        Bridge.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int noTestCases = Integer.valueOf(sc.nextLine());
        for (int t = 0; t < noTestCases; t++) {
            sc.nextLine(); // read in blank line.
            int noPeople = Integer.valueOf(sc.nextLine());
            List<Integer> speeds = new ArrayList<Integer>();
            for (int p = 0; p < noPeople; p++) {
                speeds.add(Integer.valueOf(sc.nextLine()));
            }

            Collections.sort(speeds);

            processBridgeCrossings(speeds);
            if (t < noTestCases - 1) {
                System.out.println();
            }
        }
    }

    private static void processBridgeCrossings(List<Integer> speeds) {
        int length = speeds.size();
        if (length == 0) {  // no people to cross the bridge.
            System.out.println(0);
        } else if (length == 1) { // only one person needs to cross the bridge.
            System.out.println(speeds.get(0));
            System.out.println(speeds.get(0));
        } else if (length == 2) { // only two people need to cross the bridge.
            System.out.println(Math.max(speeds.get(0), speeds.get(1)));
            System.out.printf("%d %d\n", speeds.get(0), speeds.get(1));
        } else { // more than two people need to cross the bridge.
            List<Integer> left = new ArrayList<Integer>();
            List<Integer> right = new ArrayList<Integer>();
            left.addAll(speeds);
            int travelTime = 0;
            StringBuilder sb = new StringBuilder();
            boolean is2ndFastestOnTheRightSide = false;

            /*
            The algorithm works by first letting the two fastest people cross
            the bridge and having the fastest person then return the light.
            Then either the two slowest people should cross the bridge, and
            having the fastest person return the light or the fastest and the
            slowest people should cross the bridge, and having the fastest person
            return the light. The criteria is documented below under the section
            Criteria A. This pattern repeats until the last two remaining people
            cross the bridge without the need to return the light.
            */
            while (!left.isEmpty()) {
                int l1 = left.get(0);
                int l2 = left.get(1);
                if (left.size() == 2) {
                    // Let the last remaining people cross the bridge.
                    travelTime += crossBridge(left, right, l1, l2, sb, false);
                } else {
                    if (is2ndFastestOnTheRightSide) {
                        /* Criteria A: Let only the last two slowest people cross the bridge
                        when the travel time of the slowest person and the travel
                        time of the second fastest person * 2 (return the light
                        and cross the bridge again to end up on the right side)
                        is less than the travel time of the fastest person,
                        slowest person, and the second slowest person (as the person
                        eventually needs to cross the bridge).
                        */
                        l1 = left.get(left.size() - 2);
                        l2 = left.get(left.size() - 1);
                        int lFast = left.get(0);
                        if (l2 + right.get(0) * 2 < lFast + l2 + l1) {
                            is2ndFastestOnTheRightSide = false;

                            travelTime += crossBridge(left, right, l1, l2, sb, true);
                            continue;
                        }

                        travelTime += crossBridge(left, right, lFast, l2, sb, true);
                    } else {
                        // Let the first and second fastest person cross the bridge.
                        is2ndFastestOnTheRightSide = true;
                        travelTime += crossBridge(left, right, l1, l2, sb, true);
                    }
                }
            }

            System.out.println(travelTime);
            System.out.print(sb);
        }
    }

    /**
     * Ensures that the specified people do cross the bridge. If the light needs to be returned
     * then the fastest person returns with the light. Any operations performed on left or right
     * collections leave them in sorted order.
     *
     * @param left          the speeds of the people on the left side of the bridge.
     * @param right         the speeds of the people on the right side of the bridge.
     * @param l1            the index of the speed of the person on the left side of the bridge.
     * @param l2            the index of the speed of the person on the left side of the bridge.
     * @param sb            contains the necessary output information.
     * @param doReturnLight true if the light needs to be returned, false otherwise.
     * @return returns the travel time for crossing the bridge and possibly returning the light.
     */
    private static int crossBridge(List<Integer> left, List<Integer> right,
                                   int l1, int l2, StringBuilder sb, boolean doReturnLight) {

        int sp1 = left.remove(left.indexOf(l1));
        int sp2 = left.remove(left.indexOf(l2));
        right.add(sp1);
        right.add(sp2);
        sb.append(sp1 + " " + sp2 + "\n");
        int travelTime = sp2;

        if (doReturnLight) {
            Collections.sort(right);
            sp1 = right.remove(0);
            left.add(sp1);
            Collections.sort(left);
            travelTime += sp1;
            sb.append(sp1).append("\n");
        }

        return travelTime;
    }
}
