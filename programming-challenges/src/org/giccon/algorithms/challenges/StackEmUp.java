package org.giccon.algorithms.challenges;

/* Solution: ad hoc */

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class StackEmUp {
    private static class Card implements Comparable<Card> {
        public static enum Rank {
            TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN,
            KING, ACE;

            public int getValue() {
                return ordinal() + 2;
            }

            public String getName() {
                if (getValue() <= 10) {
                    return String.valueOf(getValue());
                } else {
                    return name().substring(0, 1)
                            + name().substring(1).toLowerCase();
                }
            }

            @Override
            public String toString() {
                return getName();
            }
        }

        public static enum Suit {
            CLUBS, DIAMONDS, HEARTS, SPADES;

            public int getValue() {
                return ordinal();
            }

            public String getName() {
                return name().substring(0, 1)
                        + name().substring(1).toLowerCase();
            }

            @Override
            public String toString() {
                return getName();
            }
        }

        private Rank rank;
        private Suit suit;

        public Card(int i) {
            int r = (i - 1) % 13;
            int s = (i - 1) / 13;

            rank = Rank.values()[r];
            suit = Suit.values()[s];
        }

        @Override
        public int compareTo(Card o) {
            if (rank.getValue() < o.rank.getValue()) {
                return -1;
            } else if (rank.getValue() > o.rank.getValue()) {
                return 1;
            }

            return suit.getValue() < o.suit.getValue() ? -1
                    : suit.getValue() == o.suit.getValue() ? 0 : 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o == null) {
                return false;
            } else if (!(o instanceof Card)) {
                return false;
            }

            Card c = (Card) o;
            return (rank == c.rank && suit == c.suit);
        }

        @Override
        public int hashCode() {
            assert false : "hashCode not designed";
            return 42;
        }

        @Override
        public String toString() {
            return rank + " of " + suit;
        }
    }

    private static final int NBR_CARDS = 52;

    public static void main(String[] args) {
        StackEmUp.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        int nbrTestCases = Integer.parseInt(sc.nextLine());
        // process the empty line
        sc.nextLine();

        for (int t = 0; t < nbrTestCases; ++t) {
            int nbrShuffles = Integer.parseInt(sc.nextLine());
            int[][] shuffles = new int[nbrShuffles][NBR_CARDS];
            for (int s = 0; s < nbrShuffles; ++s) {
                int i = 0;
                while (i < NBR_CARDS) {
                    StringTokenizer st = new StringTokenizer(sc.nextLine());

                    while (st.hasMoreTokens()) {
                        shuffles[s][i++] = Integer.parseInt(st.nextToken());
                    }
                }
            }
            List<Integer> obsvShuffles = new LinkedList<Integer>();
            String input;
            while (sc.hasNext() && !(input = sc.nextLine()).isEmpty()) {
                obsvShuffles.add(Integer.parseInt(input));
            }

            List<Card> deck = new LinkedList<Card>();
            initDeck(deck);
            List<Card> cards = new LinkedList<Card>(deck);
            for (int shuffle : obsvShuffles) {
                for (int j = 0; j < NBR_CARDS; ++j) {
                    int i = shuffles[shuffle - 1][j] - 1;
                    if (i != j) {
                        cards.set(j, deck.get(i));
                    }
                }
                deck = new LinkedList<Card>(cards);
            }

            for (Card c : deck) {
                System.out.println(c);
            }

            if (t < nbrTestCases - 1) {
                System.out.println();
            }
        }
    }

    private static void initDeck(List<Card> deck) {
        deck.clear();
        for (int i = 1; i <= 52; ++i) {
            deck.add(new Card(i));
        }
    }
}
