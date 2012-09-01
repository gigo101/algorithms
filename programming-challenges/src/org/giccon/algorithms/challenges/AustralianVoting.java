package org.giccon.algorithms.challenges;

/* Solution: ad hoc, simulation */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.StringTokenizer;

public class AustralianVoting {
    private static class Candidate {
        private String name;
        private int vote;
        private boolean isPlaying;
        private List<List<Integer>> ballots = new LinkedList<List<Integer>>();

        public Candidate(String name) {
            this.name = name;
            vote = 0;
            isPlaying = true;
        }

        public int getVote() {
            return vote;
        }

        public void incrVote() {
            this.vote++;
        }

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean isPlaying) {
            this.isPlaying = isPlaying;
        }

        public String getName() {
            return name;
        }

        private List<List<Integer>> getBallots() {
            return ballots;
        }
    }

    private List<Candidate> candidates = new ArrayList<Candidate>();
    private List<List<Integer>> ballots = new LinkedList<List<Integer>>();

    public static void main(String[] args) {
        new AustralianVoting().begin();
    }

    private void begin() {
        Scanner sc = new Scanner(System.in);
        int testCases = Integer.parseInt(sc.nextLine());
        // process the blank line.
        sc.nextLine();

        for (int i = 0; i < testCases; ++i) {
            int nbrCandidates = Integer.parseInt(sc.nextLine());
            for (int c = 0; c < nbrCandidates; ++c) {
                candidates.add(new Candidate(sc.nextLine()));
            }

            while (sc.hasNextLine()) {
                String input = sc.nextLine();
                if (input.isEmpty()) {
                    break;
                }

                List<Integer> ll = new ArrayList<Integer>();
                ballots.add(ll);

                StringTokenizer st = new StringTokenizer(input);
                while (st.hasMoreTokens()) {
                    ll.add(Integer.parseInt(st.nextToken()) - 1);
                }
            }

            List<Candidate> winners = decideWinners();
            for (Candidate c : winners) {
                System.out.println(c.getName());
            }

            reset();

            if (i < testCases - 1) {
                System.out.println();
            }
        }
    }

    private void reset() {
        ballots.clear();
        candidates.clear();
    }

    private List<Candidate> decideWinners() {
        for (List<Integer> l : ballots) {
            int ci = l.get(0);
            Candidate c = candidates.get(ci);
            c.incrVote();
            c.getBallots().add(l.subList(1, l.size()));
        }

        int lowestVotes = getLowestVotes(candidates);

        List<Candidate> wonCandidates = new LinkedList<Candidate>(candidates);

        while (true) {
            List<Candidate> lostCandidates = new LinkedList<Candidate>();
            ListIterator<Candidate> it = (ListIterator<Candidate>) wonCandidates
                    .iterator();

            while (it.hasNext()) {
                Candidate c = it.next();
                if (c.getVote() == lowestVotes) {
                    lostCandidates.add(c);
                    c.setPlaying(false);
                    it.remove();
                } else {
                    double d = (double) c.getVote() / ballots.size();
                    if (d > 0.5) {
                        List<Candidate> winners = new LinkedList<Candidate>();
                        winners.add(c);
                        return winners;
                    }
                }
            }

            if (wonCandidates.isEmpty()) {
                wonCandidates = lostCandidates;
                break;
            }

            for (Candidate lc : lostCandidates) {
                List<List<Integer>> lostCndBlts = lc.getBallots();
                for (List<Integer> l : lostCndBlts) {
                    for (int i = 0; i < l.size(); ++i) {
                        Candidate c = candidates.get(l.get(i));
                        if (c.isPlaying()) {
                            c.incrVote();
                            c.getBallots().add(l.subList(i + 1, l.size()));

                            break;
                        }
                    }
                }
            }

            lowestVotes = getLowestVotes(wonCandidates);
        }

        return wonCandidates;
    }

    private int getLowestVotes(List<Candidate> candidates) {
        int lowestVotes = Integer.MAX_VALUE;
        for (Candidate c : candidates) {
            int votes = c.getVote();
            if (votes < lowestVotes) {
                lowestVotes = votes;
            }
        }

        return lowestVotes;
    }
}
