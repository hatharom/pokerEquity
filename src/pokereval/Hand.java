package pokereval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

public class Hand implements Comparable<Hand> {

    private Card[] cards;
    public int[] pair;
    private int flushType;
    private int flush;
    private int straightType;
    private int straightFlushType;
    private int[] handValue = new int[6];

    public Hand(Card[] cards) {
        if (cards == null || cards.length != 7 || !(cards[0] instanceof Card)) {
            throw new IllegalArgumentException();
        }
        this.cards = cards;
        evaluate();
    }

    /**
     * builds and stores the whole result for the current hand
     */
    private void evaluate() {
        evaluatePairs();
        this.flushType = evaluateFlush();
        this.straightType = evaluateStraight();
        this.straightFlushType = evaluateStraightFlush();
        parseHand();
    }

    /**
     * fills up the pair array with occurence of the given card e.g.
     * [0,1,2,0,0,0,0,1,0,2,0,0,0] == 3449JJ
     *
     */
    private void evaluatePairs() {
        int[] pairCounter = new int[13];

        for (int i = 0; i < cards.length; i++) {
            pairCounter[cards[i].getFace()]++;
        }
        pair = new int[13];
        pair = pairCounter;

        int cardOccurence1 = 0;
        int cardOccurence2 = 0;
        int higherFaceValue = 0;
        int lowerFaceValue = 0;

        /*
         *fills up the pair array with occurences and storing the values of faces in the same time
         */
        for (int i = 0; i < pairCounter.length; i++) {
            if (pairCounter[i] >= cardOccurence1) {
                if (cardOccurence1 > 1) {
                    cardOccurence2 = cardOccurence1;
                    lowerFaceValue = higherFaceValue;
                }
                cardOccurence1 = pairCounter[i];
                higherFaceValue = i;

            } else if (pairCounter[i] >= cardOccurence2) {
                cardOccurence2 = pairCounter[i];
                lowerFaceValue = i;
            }

        }

    }

    /**
     * returns the type of the flush by searching for the highest value among
     * faces with the same suit.
     *
     * @return an int that represents the highest card in a flush returns -1 if
     * flush isnt present
     */
    private int evaluateFlush() {
        int flushType = -1;
        int[] suitCounter = new int[4];
        TreeSet ts = null;
        for (int i = 0; i < cards.length; i++) {
            int actualSuit = cards[i].getSuit();
            suitCounter[actualSuit]++;
        }
        for (int i = 0; i < suitCounter.length; i++) {
            if (suitCounter[i] >= 5) {
                ts = new TreeSet();
                for (int j = 0; j < cards.length; j++) {
                    if (cards[j].getSuit() == i) {
                        ts.add(cards[j].getFace());
                    }
                }
                flushType = (Integer) ts.last();
                flush = i;
                break;
            }
        }
        return flushType;
    }

    /**
     * returns the type of the straightflush by traversing the facevalues with
     * the same suit of the hand.
     *
     * @return an int that represent the face value of the highest card in a
     * straightflush. returns -1 if straightflush isnt present.
     *
     */
    private int evaluateStraightFlush() {
        if (this.flushType < 0 || this.straightType < 0) {
            return -1;
        }
        int straightFlushType = -1;
        if (flushType == straightType) {
            return straightType;
        }
        TreeSet<Integer> straightFaces = new TreeSet<Integer>();
        for (Card card : cards) {
            if (card.getSuit() == flush) {
                straightFaces.add(card.getFace());
            }
        }
        int straightCounter = 1;
        int actualFace = -1;
        int previousFace = -1;
        int lockedStraightCounter = 0;
        for (Integer face : straightFaces) {
            actualFace = face;
            if (actualFace == previousFace + 1) {
                straightCounter++;
                if (straightCounter >= 5) {
                    lockedStraightCounter = straightCounter;
                    straightFlushType = actualFace;
                }
            } else {
                straightCounter = 1;
            }
            previousFace = actualFace;

        }

        return straightFlushType;
    }

    /**
     * returns the type of the straight by traversing the facevalues of the hand
     *
     * @return an int that represent the face value of the highest card in a
     * straight. returns -1 if straight isnt present.
     *
     */
    private int evaluateStraight() {

        int straightType = -1;
        int straightCounter = 1;
        int lockedStraightCounter = 0;
        TreeSet<Integer> straightFaces = new TreeSet<Integer>();
        for (Card card : cards) {
            straightFaces.add(card.getFace());
        }

        if (straightFaces.size() < 5) {
            return straightType;
        }
        int actualFace = -1;
        int previousFace = -10;
        for (Integer face : straightFaces) {
            actualFace = face;
            if (actualFace == previousFace + 1) {
                straightCounter++;
                if (straightCounter >= 5) {
                    lockedStraightCounter = straightCounter;
                    straightType = actualFace;
                }
            } else {
                straightCounter = 1;
            }
            previousFace = actualFace;

        }
        if (straightType == -1) {
            Integer[] aceHighStraight = {0, 1, 2, 3, 12};
            if (straightFaces.containsAll(Arrays.asList(aceHighStraight))) {
                straightType = 3;
            }
        }
        return straightType;
    }

    /**
     * fills up the handValue array with the help of auxiliar primitives,arrays
     * of cardOccurence,straightFlushType,flushType,straightType,pair
     *
     */
    private void parseHand() {
        int[] highCards = new int[7];
        int index = 0;
        int cardOccurence1 = 0;
        int cardOccurence2 = 0;
        int higherFaceValue = 0;
        int lowerFaceValue = 0;
        for (int i = pair.length - 1; i >= 0; i--) {
            if (pair[i] == 1) {
                highCards[index] = i;
                index++;
            }
            if (pair[i] > 1 && pair[i] > cardOccurence1) {

                cardOccurence2 = cardOccurence1;
                lowerFaceValue = higherFaceValue;
                cardOccurence1 = pair[i];
                higherFaceValue = i;

            } else if (pair[i] > cardOccurence2) {
                cardOccurence2 = pair[i];
                lowerFaceValue = i;
            }

        }

        //stroing straightFlush
        if (straightFlushType >= 0) {
            handValue[0] = 8;
            handValue[1] = straightFlushType;
            return;
        }

        //storing four of a kind
        if (cardOccurence1 == 4) {
            handValue[0] = 7;
            handValue[1] = higherFaceValue;
            handValue[2] = highCards[0];
            return;
        }

        //storing fullhouse
        if (cardOccurence1 == 3 && cardOccurence2 >= 2) {
            handValue[0] = 6;
            handValue[1] = higherFaceValue;
            handValue[2] = lowerFaceValue;
            return;
        }

        //storing flush
        if (flushType >= 0) {
            handValue[0] = 5;
            handValue[1] = flushType;
            return;
        }

        //storing straight
        if (straightType >= 0) {
            handValue[0] = 4;
            handValue[1] = straightType;
            return;
        }
        //storing trips
        if (cardOccurence1 == 3 && cardOccurence2 != 2) {
            handValue[0] = 3;
            handValue[1] = higherFaceValue;
            handValue[2] = highCards[0];
            handValue[3] = highCards[1];
            return;
        }
        //stroing two pairs
        if (cardOccurence1 == 2 && cardOccurence2 == 2) {
            handValue[0] = 2;
            handValue[1] = higherFaceValue;
            handValue[2] = lowerFaceValue;
            handValue[3] = highCards[0];
            return;
        }
        //stroing  pairs
        if (cardOccurence1 == 2 && cardOccurence2 <= 1) {
            handValue[0] = 1;
            handValue[1] = higherFaceValue;
            handValue[2] = highCards[0];
            handValue[3] = highCards[1];
            handValue[4] = highCards[2];

            return;
        }
        //stroing  highcard
        if (cardOccurence1 <= 1 && cardOccurence2 <= 1) {
            handValue[0] = 0;
            handValue[1] = highCards[0];
            handValue[2] = highCards[1];
            handValue[3] = highCards[2];
            handValue[4] = highCards[3];
            return;
        }

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
