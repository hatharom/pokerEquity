package pokereval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

public class Hand implements Comparable<Hand> {

    protected Card[] cards;
    protected int[] pair;
    protected int flushType;
    protected int flush;
    protected int straightType;
    protected int straightFlushType;
    protected int[] handValue = new int[6];
    HandEvaluator handEvaluator;

    public Hand(Card[] cards) {
        if (cards == null || cards.length != 7 || !(cards[0] instanceof Card)) {
            throw new IllegalArgumentException();
        }
        this.cards = cards;
        handEvaluator = new HandEvaluator(this);
        handEvaluator.evaluate();
    }

    @Override
    public String toString() {
        String hand = "";
        for (Card c : cards) {
            hand += c.toString();
        }
        return hand;
    }

    /**
     * returns the final handValue array of the hand
     */
    public int[] getHandValue() {
        return this.handValue;
    }

    /**
     * Returns the values of the hand in readable form by reading it from the
     * handValue array
     *
     * @return a String that describes the final value of the hand
     */
    public String getStringHandValue() {

        if (handValue[0] == 8) {
            return Card.getTrueFace(handValue[1]) + " high straighflush";
        }
        if (handValue[0] == 7) {
            return "four of a kind of " + Card.getTrueFace(handValue[1]);
        }
        if (handValue[0] == 6) {
            return "full house of " + Card.getTrueFace(handValue[1]) + " over " + Card.getTrueFace(handValue[2]);
        }
        if (handValue[0] == 5) {
            return Card.getTrueFace(handValue[1]) + " high flush";
        }
        if (handValue[0] == 4) {
            return Card.getTrueFace(handValue[1]) + " high straight";
        }

        if (handValue[0] == 3) {
            return "three of a kind of " + Card.getTrueFace(handValue[1]);
        }
        if (handValue[0] == 2) {
            return "two pairs of " + Card.getTrueFace(handValue[1]) + " and " + Card.getTrueFace(handValue[2]);
        }
        if (handValue[0] == 1) {
            return "pair of " + Card.getTrueFace(handValue[1]);
        }
        if (handValue[0] == 6) {
            return Card.getTrueFace(handValue[1]) + " high ";
        }
        return null;
    }

    @Override
    public int compareTo(Hand hand2) {

        for (int i = 0; i < 6; i++) {
            if (this.handValue[i] > hand2.handValue[i]) {
                return 1;
            } else if (this.handValue[i] < hand2.handValue[i]) {
                return -1;
            }
        }
        return 0;
    }

}
