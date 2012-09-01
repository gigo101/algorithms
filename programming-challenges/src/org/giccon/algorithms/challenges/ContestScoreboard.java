package org.giccon.algorithms.challenges;

/* Solution: ad hoc */

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ContestScoreboard {
    private static class Problem {
        private int time;
        private boolean isSolved;
    }

    private static class Contestant implements Comparable<Contestant> {
        private int id;
        private int nbrSolved;
        private int penaltyTime;

        @Override
        public int compareTo(Contestant c) {
            if (nbrSolved > c.nbrSolved) {
                return -1;
            } else if (nbrSolved < c.nbrSolved) {
                return 1;
            }

            if (penaltyTime < c.penaltyTime) {
                return -1;
            } else if (penaltyTime > c.penaltyTime) {
                return 1;
            }

            if (id < c.id) {
                return -1;
            } else if (id > c.id) {
                return 1;
            }

            return 0;
        }

        @Override
        public String toString() {
            return id + " " + nbrSolved + " " + penaltyTime;
        }
    }

    private static final String CORRECT = "C";
    private static final String INCORRECT = "I";
    private static final int PENALTYTIME = 20;

    public static void main(String[] args) {
        ContestScoreboard.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int nbrTestCases = Integer.parseInt(sc.nextLine());
        // Process the empty line.
        sc.nextLine();
        for (int t = 0; t < nbrTestCases; ++t) {
            Map<Integer, Map<Integer, Problem>> data = new HashMap<Integer, Map<Integer, Problem>>();
            Map<Integer, Contestant> contestants = new HashMap<Integer, Contestant>();

            String input;
            while (sc.hasNextLine() && !(input = sc.nextLine()).isEmpty()) {
                StringTokenizer st = new StringTokenizer(input);

                int contestant = Integer.parseInt(st.nextToken());
                int problem = Integer.parseInt(st.nextToken());
                int time = Integer.parseInt(st.nextToken());
                String answer = st.nextToken();

                Contestant contestantObj = contestants.get(contestant);
                if (contestantObj == null) {
                    contestantObj = new Contestant();
                    contestantObj.id = contestant;
                    contestants.put(contestant, contestantObj);
                }

                if (!answer.equals(CORRECT) && !answer.equals(INCORRECT)) {
                    continue;
                }

                Map<Integer, Problem> problems = data.get(contestant);
                if (problems == null) {
                    problems = new HashMap<Integer, Problem>();
                    data.put(contestant, problems);
                }

                Problem problemObj = problems.get(problem);
                if (problemObj == null) {
                    problemObj = new Problem();
                    problems.put(problem, problemObj);
                }

                if (problemObj.isSolved) {
                    continue;
                }

                if (answer.equals(INCORRECT)) {
                    problemObj.time += PENALTYTIME;
                } else if (answer.equals(CORRECT)) {
                    problemObj.time += time;
                    problemObj.isSolved = true;

                    contestantObj.penaltyTime += problemObj.time;
                    contestantObj.nbrSolved++;
                }
            }

            List<Contestant> cList = new LinkedList<Contestant>(
                    contestants.values());
            Collections.sort(cList);
            for (Contestant c : cList) {
                System.out.println(c);
            }

            if (t < nbrTestCases - 1) {
                System.out.println();
            }
        }
    }
}
