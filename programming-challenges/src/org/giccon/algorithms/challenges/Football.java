package org.giccon.algorithms.challenges;

/* Simple Algorithm */

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Football {
    public static void main(String[] args) {
        Football.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in, "ISO-8859-1");
        PrintWriter cout;
        try {
            cout = new PrintWriter(new OutputStreamWriter(System.out, "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            return;
        }
        int noTournaments = Integer.parseInt(sc.nextLine());

        for (int t = 0; t < noTournaments; t++) {
            Map<String, Team> teamsMap = new HashMap<String, Team>();
            String tournament = sc.nextLine();

            int noTeams = Integer.parseInt(sc.nextLine());
            for (int i = 0; i < noTeams; i++) {
                String teamName = sc.nextLine();
                teamsMap.put(teamName, new Team(teamName));
            }

            int noGames = Integer.parseInt(sc.nextLine());
            for (int i = 0; i < noGames; i++) {
                String line = sc.nextLine();
                parseGameResults(line, teamsMap);
            }
            cout.println(tournament);

            List<Team> teams = new ArrayList<Team>(teamsMap.values());
            Collections.sort(teams);
            int i = 1;
            StringBuilder sb = new StringBuilder();
            for (Team team : teams) {
                sb.append(i).append(") ").append(team).append("\n");
                i++;
            }
            cout.print(sb);

            if (t < noTournaments - 1) {
                cout.println();
            }

            cout.flush();
        }
    }

    private static void parseGameResults(String line, Map<String, Team> teams) {
        StringTokenizer st = new StringTokenizer(line, "#");
        String teamNameA = st.nextToken();
        String result = st.nextToken();
        String teamNameB = st.nextToken();

        st = new StringTokenizer(result, "@");
        int scoreA = Integer.parseInt(st.nextToken());
        int scoreB = Integer.parseInt(st.nextToken());

        teams.get(teamNameA).addGoals(scoreA, scoreB);
        teams.get(teamNameB).addGoals(scoreB, scoreA);
    }

    private static class Team implements Comparable<Team> {
        private String name;
        private int points;
        private int noGamesPlayed;
        private int noWins;
        private int noTies;
        private int noLosses;
        private int goalDiff;
        private int noGoalsScored;
        private int noGoalsAgainst;

        public Team(String name) {
            this.name = name;
        }

        public void addGoals(int goalsScored, int goalsAgainst) {
            noGoalsScored += goalsScored;
            noGoalsAgainst += goalsAgainst;
            goalDiff = noGoalsScored - noGoalsAgainst;

            if (goalsScored > goalsAgainst) {
                incWins();
            } else if (goalsScored < goalsAgainst) {
                incLosses();
            } else {
                incTies();
            }

            noGamesPlayed++;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(" ").append(points).append("p, ").append(noGamesPlayed).append("g (");
            sb.append(noWins).append("-").append(noTies).append("-").append(noLosses).append("), ");
            sb.append(goalDiff).append("gd (").append(noGoalsScored).append("-").append(noGoalsAgainst).append(")");
            return sb.toString();
        }

        private void incWins() {
            noWins += 1;
            points += 3;
        }

        private void incLosses() {
            noLosses += 1;
        }

        private void incTies() {
            noTies += 1;
            points += 1;
        }

        @Override
        public int compareTo(Team o) {
            if (o == null) {
                return -1;
            }

            // Next criteria: most points earned.
            if (points > o.points) {
                return -1;
            } else if (points < o.points) {
                return 1;
            }

            // Points earned is equal. Next criteria: most wins.
            if (noWins > o.noWins) {
                return -1;
            } else if (noWins < o.noWins) {
                return 1;
            }

            // Wins are equal. Next criteria: most goal difference.
            if (goalDiff > o.goalDiff) {
                return -1;
            } else if (goalDiff < o.goalDiff) {
                return 1;
            }

            // Goal difference is equal. Next criteria: most goals scored.
            if (noGoalsScored > o.noGoalsScored) {
                return -1;
            } else if (noGoalsScored < o.noGoalsScored) {
                return 1;
            }

            // Goals scored is equal. Next criteria: Fewest games played.
            if (noGamesPlayed < o.noGamesPlayed) {
                return -1;
            } else if (noGamesPlayed > o.noGamesPlayed) {
                return 1;
            }

            // Games played is equal. Next criteria: Case-insensitive lexicographic order of name.
            return name.toLowerCase().compareTo(o.name.toLowerCase());
        }
    }
}
