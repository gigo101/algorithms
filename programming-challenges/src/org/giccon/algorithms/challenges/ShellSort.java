package org.giccon.algorithms.challenges;

/* Simple algorithm */

import java.util.*;

public class ShellSort {
    public static void main(String[] args) {
        ShellSort.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int noTestCases = Integer.parseInt(sc.nextLine());
        for (int t = 0; t < noTestCases; t++) {
            Map<Integer, String> table = new HashMap<Integer, String>();
            int noTurtles = Integer.parseInt(sc.nextLine());

            List<Integer> sourceList = new LinkedList<Integer>();
            List<Integer> destList = new LinkedList<Integer>();

            // Read in the source list. Left is top of the stack.
            // Associate turtle names with numbers for quick comparison.
            for (int i = 0; i < noTurtles; i++) {
                String turtle = sc.nextLine();
                int key = turtle.hashCode();
                table.put(key, turtle);
                sourceList.add(key);
            }

            // Read in the destination list. left is top of the stack.
            for (int i = 0; i < noTurtles; i++) {
                destList.add(sc.nextLine().hashCode());
            }

            StringBuilder sb = new StringBuilder();
            ListIterator<Integer> sourceItr = sourceList.listIterator(noTurtles);
            ListIterator<Integer> destItr = destList.listIterator(noTurtles);
            /*
             * Bottom up approach (from right to left). Find the element in the destination
             * list in the source list. As long as the element is left to the previous
             * element, no crawling needs to be done. Otherwise, the remaining elements in
             * the destination list from right to left need to crawl to achieve the desired order. e.g.,
             * ABCDEFG -> source list
             * BCADEFG -> destination list
             * G,F,E,D,A is found. The C is not left to A in the source list which is left to
             * A in the destination list. So, it will not be found. Hence, we store the values
             * from right to left of A from the destination list. These are C and B.
             * Thus, by just letting C turtle crawl to the top and then the B turtle, we achieve
             * the desired order.
             */
            while (destItr.hasPrevious()) {
                int turtleKey = destItr.previous();
                boolean found = false;
                while (sourceItr.hasPrevious()) {
                    int sourceTurtleKey = sourceItr.previous();
                    if (sourceTurtleKey == turtleKey) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    destItr.next();
                    while (destItr.hasPrevious()) {
                        sb.append(table.get(destItr.previous())).append("\n");
                    }
                }
            }

            System.out.println(sb);
        }
    }
}
