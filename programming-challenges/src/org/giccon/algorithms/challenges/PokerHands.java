package org.giccon.algorithms.challenges;

/* Simple algorithm */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.StringTokenizer;

public class PokerHands {
    private static class Card implements Comparable<Card> {
        public static enum Rank {
            TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN,
            KING, ACE;

            public int getValue() {
                return ordinal() + 2;
            }
        }

        public static enum Suit {
            CLUBS, DIAMONDS, HEARTS, SPADES
        }

        private Rank rank;
        private Suit suit;

        public Card(String s) {
            char rn = s.charAt(0);
            char st = s.charAt(1);

            if (rn >= '2' && rn <= '9') {
                rank = Rank.values()[rn - '0' - 2];
            } else {
                switch (rn) {
                    case 'T':
                        rank = Rank.TEN;
                        break;
                    case 'J':
                        rank = Rank.JACK;
                        break;
                    case 'Q':
                        rank = Rank.QUEEN;
                        break;
                    case 'K':
                        rank = Rank.KING;
                        break;
                    case 'A':
                        rank = Rank.ACE;
                        break;
                    default:
                        break;
                }
            }

            switch (st) {
                case 'C':
                    suit = Suit.CLUBS;
                    break;
                case 'D':
                    suit = Suit.DIAMONDS;
                    break;
                case 'H':
                    suit = Suit.HEARTS;
                    break;
                case 'S':
                    suit = Suit.SPADES;
                    break;
                default:
                    break;
            }
        }

        @Override
        public int compareTo(Card o) {
            if (rank.getValue() < o.rank.getValue()) {
                return -1;
            } else if (rank.getValue() > o.rank.getValue()) {
                return 1;
            }

            return 0;
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

    private static enum Hand {
        HIGHCARD, PAIR, TWOPAIR, THREEOFAKIND, STRAIGHT, FLUSH, FULLHOUSE,
        FOUROFAKIND, STRAIGHTFLUSH
    }

    private static final int TIE = -1;
    private static final int WHITE = 0;
    private static final int BLACK = 1;

    private List<Card> whiteCards = new ArrayList<Card>();
    private List<Card> whiteHand = new ArrayList<Card>();
    private List<Card> blackCards = new ArrayList<Card>();
    private List<Card> blackHand = new ArrayList<Card>();

    public static void main(String[] args) {
        new PokerHands().begin();
    }

    private void begin() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.isEmpty()) {
                break;
            }

            StringTokenizer st = new StringTokenizer(input);
            int i = 0;
            while (st.hasMoreTokens()) {
                if (i < 5) {
                    blackCards.add(new Card(st.nextToken()));
                } else {
                    whiteCards.add(new Card(st.nextToken()));
                }
                i++;
            }

            int winner = getWinner();

            if (winner == WHITE) {
                System.out.println("White wins.");
            } else if (winner == BLACK) {
                System.out.println("Black wins.");
            } else {
                System.out.println("Tie.");
            }

            reset();
        }
    }

    private int getWinner() {
        Hand whType = getHand(whiteCards, whiteHand);
        Hand bhType = getHand(blackCards, blackHand);
        int winner;

        int cmp = whType.compareTo(bhType);
        if (cmp == 0) {
            switch (whType) {
                case STRAIGHTFLUSH:
                    cmp = cmpByHighCard(whiteHand, blackHand);
                    break;
                case FOUROFAKIND:
                    cmp = cmpByHighCard(whiteHand, blackHand);
                    break;
                case FULLHOUSE:
                    whiteHand.clear();
                    blackHand.clear();
                    isThreeOfAKind(whiteCards, whiteHand);
                    isThreeOfAKind(blackCards, blackHand);
                    cmp = cmpByHighCard(whiteHand, blackHand);
                    break;
                case FLUSH:
                    cmp = cmpByRecursiveHighCard(whiteHand, blackHand);
                    break;
                case STRAIGHT:
                    cmp = cmpByHighCard(whiteHand, blackHand);
                    break;
                case THREEOFAKIND:
                    cmp = cmpByHighCard(whiteHand, blackHand);
                    break;
                case TWOPAIR:
                    cmp = cmpByHighCard(whiteHand, blackHand);
                    if (cmp == 0) {
                        cmp = cmpByHighCard(whiteHand.subList(0, 2),
                                blackHand.subList(0, 2));
                    }
                    if (cmp == 0) {
                        List<Card> wRemaining = new ArrayList<Card>(whiteCards);
                        wRemaining.removeAll(whiteHand);
                        List<Card> bRemaining = new ArrayList<Card>(blackCards);
                        bRemaining.removeAll(blackHand);
                        cmp = cmpByHighCard(wRemaining, bRemaining);
                    }
                    break;
                case PAIR:
                    List<Card> wRemaining = new ArrayList<Card>(whiteCards);
                    wRemaining.removeAll(whiteHand);
                    List<Card> bRemaining = new ArrayList<Card>(blackCards);
                    bRemaining.removeAll(blackHand);
                    cmp = cmpByRecursiveHighCard(wRemaining, bRemaining);
                    break;
                case HIGHCARD:
                    cmp = cmpByRecursiveHighCard(whiteCards, blackCards);
                    break;
            }
        }

        winner = cmp > 0 ? WHITE : cmp < 0 ? BLACK : TIE;
        return winner;
    }

    private int cmpByHighCard(List<Card> hand1, List<Card> hand2) {
        Card hwc = hand1.get(hand1.size() - 1);
        Card hbc = hand2.get(hand2.size() - 1);

        return hwc.compareTo(hbc);
    }

    private int cmpByRecursiveHighCard(List<Card> hand1, List<Card> hand2) {
        int cmp = 0;

        ListIterator<Card> it1 = hand1.listIterator(hand1.size());
        ListIterator<Card> it2 = hand2.listIterator(hand2.size());
        while (it1.hasPrevious() && it2.hasPrevious()) {
            Card c1 = it1.previous();
            Card c2 = it2.previous();
            cmp = c1.compareTo(c2);
            if (cmp != 0) {
                break;
            }
        }
        return cmp;
    }

    private Hand getHand(List<Card> cards, List<Card> hand) {
        Collections.sort(cards);

        Hand h;
        if (isStraightFlush(cards, hand)) {
            h = Hand.STRAIGHTFLUSH;
        } else if (isFourOfAKind(cards, hand)) {
            h = Hand.FOUROFAKIND;
        } else if (isFullHouse(cards, hand)) {
            h = Hand.FULLHOUSE;
        } else if (isFlush(cards, hand)) {
            h = Hand.FLUSH;
        } else if (isStraight(cards, hand)) {
            h = Hand.STRAIGHT;
        } else if (isThreeOfAKind(cards, hand)) {
            h = Hand.THREEOFAKIND;
        } else if (isTwoPair(cards, hand)) {
            h = Hand.TWOPAIR;
        } else if (isPair(cards, hand)) {
            h = Hand.PAIR;
        } else {
            h = Hand.HIGHCARD;
        }

        return h;
    }

    private boolean isPair(List<Card> cards, List<Card> hand) {
        Collections.sort(cards);

        Iterator<Card> it = cards.iterator();
        Card prev = it.next();

        while (it.hasNext()) {
            Card c = it.next();

            if (c.rank.getValue() == prev.rank.getValue()) {
                hand.add(prev);
                hand.add(c);
                break;
            }
            prev = c;
        }

        if (hand.size() == 2) {
            return true;
        }

        hand.clear();
        return false;
    }

    private boolean isTwoPair(List<Card> cards, List<Card> hand) {
        Collections.sort(cards);

        Iterator<Card> it = cards.iterator();
        Card prev = it.next();

        while (it.hasNext()) {
            Card c = it.next();

            if (c.rank.getValue() == prev.rank.getValue()) {
                hand.add(prev);
                hand.add(c);
                if (it.hasNext()) {
                    c = it.next();
                }
            }
            prev = c;
        }

        if (hand.size() == 4) {
            return true;
        }

        hand.clear();
        return false;
    }

    private boolean isThreeOfAKind(List<Card> cards, List<Card> hand) {
        Iterator<Card> it = cards.iterator();
        Card prev = it.next();

        int beginIndex = 0;
        int i = 1;
        while (it.hasNext() && i - beginIndex < 3 && beginIndex < 3) {
            Card c = it.next();

            if (c.rank.getValue() != prev.rank.getValue()) {
                beginIndex = i;
            }

            prev = c;
            i++;
        }

        if (beginIndex < 3) {
            hand.addAll(cards.subList(beginIndex, beginIndex + 3));
            return true;
        }

        return false;
    }

    private boolean isStraight(List<Card> cards, List<Card> hand) {
        Iterator<Card> it = cards.iterator();
        Card prev = it.next();
        hand.add(prev);
        while (it.hasNext()) {
            Card c = it.next();
            hand.add(c);

            if (c.rank.getValue() - prev.rank.getValue() != 1) {
                hand.clear();
                return false;
            }

            prev = c;
        }

        return true;
    }

    private boolean isFlush(List<Card> cards, List<Card> hand) {
        Iterator<Card> it = cards.iterator();
        Card prev = it.next();
        hand.add(prev);
        while (it.hasNext()) {
            Card c = it.next();
            hand.add(c);

            if (prev.suit != c.suit) {
                hand.clear();
                return false;
            }

            prev = c;
        }

        return true;
    }

    private boolean isFullHouse(List<Card> cards, List<Card> hand) {
        if (isThreeOfAKind(cards, hand)) {
            List<Card> pair = new ArrayList<Card>(cards);
            pair.removeAll(hand);
            if (pair.get(0).rank.getValue() == pair.get(1).rank.getValue()) {
                hand.clear();
                hand.addAll(cards);
                return true;
            } else {
                hand.clear();
            }
        }

        return false;
    }

    private boolean isFourOfAKind(List<Card> cards, List<Card> hand) {
        Iterator<Card> it = cards.iterator();
        Card prev = it.next();

        int beginIndex = 0;
        int i = 1;
        while (it.hasNext() && i - beginIndex < 4 && beginIndex < 2) {
            Card c = it.next();

            if (c.rank.getValue() != prev.rank.getValue()) {
                beginIndex = i;
            }

            prev = c;
            i++;
        }

        if (beginIndex < 2) {
            hand.addAll(cards.subList(beginIndex, beginIndex + 4));
            return true;
        }

        return false;
    }

    private boolean isStraightFlush(List<Card> cards, List<Card> hand) {
        Iterator<Card> it = cards.iterator();
        Card prev = it.next();
        hand.add(prev);
        while (it.hasNext()) {
            Card c = it.next();
            hand.add(c);

            if (prev.suit != c.suit) {
                hand.clear();
                return false;
            }

            if (c.rank.getValue() - prev.rank.getValue() != 1) {
                hand.clear();
                return false;
            }

            prev = c;
        }

        return true;
    }

    private void reset() {
        whiteCards.clear();
        whiteHand.clear();
        blackCards.clear();
        blackHand.clear();
    }
}
