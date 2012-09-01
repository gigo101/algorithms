package org.giccon.algorithms.challenges;

/* Solution: graph, breadth first search */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class ErdosNumbers {
    private static final String ERDOS = "Erdos, P.";

    private Map<String, Set<String>> authors = new HashMap<String, Set<String>>();

    public static void main(String[] args) {
        new ErdosNumbers().begin();
    }

    private void begin() {
        Scanner sc = new Scanner(System.in);
        int nbrTestCases = sc.nextInt();
        for (int t = 1; t <= nbrTestCases; ++t) {
            int nbrPapers = sc.nextInt();
            int nbrNames = sc.nextInt();
            sc.nextLine(); // process the empty line.

            for (int p = 0; p < nbrPapers; ++p) {
                String input = sc.nextLine();
                StringTokenizer st = new StringTokenizer(input.substring(0,
                        input.indexOf(':')));

                List<String> paperAuthors = new ArrayList<String>();
                while (st.hasMoreTokens()) {
                    String name = st.nextToken();
                    String initials = st.nextToken();
                    initials = initials.replaceAll(",", "");
                    String author = name + " " + initials;
                    paperAuthors.add(author);
                }

                processPaper(paperAuthors);
            }

            Map<String, Integer> erdosNumbers = getErdosNumbers();

            System.out.printf("Scenario %d\n", t);
            for (int n = 0; n < nbrNames; ++n) {
                String author = sc.nextLine();
                int erdosNumber = 0;
                if (erdosNumbers.containsKey(author)) {
                    erdosNumber = erdosNumbers.get(author);
                }

                if (erdosNumber == 0) {
                    System.out.printf("%s infinity\n", author);
                } else {
                    System.out.printf("%s %d\n", author, erdosNumber);
                }
            }

            reset();
        }
    }

    private void reset() {
        authors.clear();
    }

    private Map<String, Integer> getErdosNumbers() {
        Map<String, Boolean> visitedAuthors = new HashMap<String, Boolean>();
        Queue<String> queue = new ArrayDeque<String>();
        queue.add(ERDOS);
        visitedAuthors.put(ERDOS, true);
        Map<String, Integer> erdosNumbers = new HashMap<String, Integer>();

        while (!queue.isEmpty()) {
            String mainAuthor = queue.poll();
            int erdosNumber = 0;
            if (erdosNumbers.containsKey(mainAuthor)) {
                erdosNumber = erdosNumbers.get(mainAuthor);
            }

            Set<String> authorsWorkedWith = authors.get(mainAuthor);
            for (String author : authorsWorkedWith) {
                boolean isVisited = false;
                if (visitedAuthors.containsKey(author)) {
                    isVisited = visitedAuthors.get(author);
                }

                if (!isVisited) {
                    queue.add(author);
                    visitedAuthors.put(author, true);
                    erdosNumbers.put(author, erdosNumber + 1);
                }
            }
        }

        return erdosNumbers;
    }

    private void processPaper(List<String> paperAuthors) {
        for (int i = 0; i < paperAuthors.size(); ++i) {
            swapPaperAuthors(paperAuthors, 0, i);

            String mainAuthor = paperAuthors.get(0);
            Set<String> authorsWorkedWith = authors.get(mainAuthor);
            if (authorsWorkedWith == null) {
                authorsWorkedWith = new LinkedHashSet<String>();
                authors.put(mainAuthor, authorsWorkedWith);
            }
            for (int a = 1; a < paperAuthors.size(); ++a) {
                String workedWith = paperAuthors.get(a);
                authorsWorkedWith.add(workedWith);
            }
        }
    }

    private void swapPaperAuthors(List<String> paperAuthors, int i1, int i2) {
        if (i1 == i2) {
            return;
        }

        String temp = paperAuthors.get(i1);
        paperAuthors.set(i1, paperAuthors.get(i2));
        paperAuthors.set(i2, temp);
    }
}
