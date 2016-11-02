/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokereval;

import java.util.Arrays;
import java.util.TreeSet;

public class HandEvaluator {

    Hand hand;

    public HandEvaluator(Hand hand) {
        this.hand = hand;
    }

    /**
     * builds and stores the whole result for the current hand
     */
    public void evaluate() {
        evaluatePairs();
        hand.flushType = evaluateFlush();
        hand.straightType = evaluateStraight();
        hand.straightFlushType = evaluateStraightFlush();
        parseHand();
    }

    /**
     * fills up the pair array with occurence of the given card e.g.
     * [0,1,2,0,0,0,0,1,0,2,0,0,0] == 3449JJ
     *
     */
    private void evaluatePairs() {
        int[] pairCounter = new int[13];

        for (int i = 0; i < hand.cards.length; i++) {
            pairCounter[hand.cards[i].getFace()]++;
        }
        hand.pair = new int[13];
        hand.pair = pairCounter;

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
        for (int i = 0; i < hand.cards.length; i++) {
            int actualSuit = hand.cards[i].getSuit();
            suitCounter[actualSuit]++;
        }
        for (int i = 0; i < suitCounter.length; i++) {
            if (suitCounter[i] >= 5) {
                ts = new TreeSet();
                for (int j = 0; j < hand.cards.length; j++) {
                    if (hand.cards[j].getSuit() == i) {
                        ts.add(hand.cards[j].getFace());
                    }
                }
                flushType = (Integer) ts.last();
                hand.flush = i;
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
        if (hand.flushType < 0 || hand.straightType < 0) {
            return -1;
        }
        int straightFlushType = -1;
        if (hand.flushType == hand.straightType) {
            return hand.straightType;
        }
        TreeSet<Integer> straightFaces = new TreeSet<Integer>();
        for (Card card : hand.cards) {
            if (card.getSuit() == hand.flush) {
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
        for (Card card : hand.cards) {
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
        for (int i = hand.pair.length - 1; i >= 0; i--) {
            if (hand.pair[i] == 1) {
                highCards[index] = i;
                index++;
            }
            if (hand.pair[i] > 1 && hand.pair[i] > cardOccurence1) {

                cardOccurence2 = cardOccurence1;
                lowerFaceValue = higherFaceValue;
                cardOccurence1 = hand.pair[i];
                higherFaceValue = i;

            } else if (hand.pair[i] > cardOccurence2) {
                cardOccurence2 = hand.pair[i];
                lowerFaceValue = i;
            }

        }

        //stroing straightFlush
        if (hand.straightFlushType >= 0) {
            hand.handValue[0] = 8;
            hand.handValue[1] = hand.straightFlushType;
            return;
        }

        //storing four of a kind
        if (cardOccurence1 == 4) {
            hand.handValue[0] = 7;
            hand.handValue[1] = higherFaceValue;
            hand.handValue[2] = highCards[0];
            return;
        }

        //storing fullhouse
        if (cardOccurence1 == 3 && cardOccurence2 >= 2) {
            hand.handValue[0] = 6;
            hand.handValue[1] = higherFaceValue;
            hand.handValue[2] = lowerFaceValue;
            return;
        }

        //storing flush
        if (hand.flushType >= 0) {
            hand.handValue[0] = 5;
            hand.handValue[1] = hand.flushType;
            return;
        }

        //storing straight
        if (hand.straightType >= 0) {
            hand.handValue[0] = 4;
            hand.handValue[1] = hand.straightType;
            return;
        }
        //storing trips
        if (cardOccurence1 == 3 && cardOccurence2 != 2) {
            hand.handValue[0] = 3;
            hand.handValue[1] = higherFaceValue;
            hand.handValue[2] = highCards[0];
            hand.handValue[3] = highCards[1];
            return;
        }
        //stroing two pairs
        if (cardOccurence1 == 2 && cardOccurence2 == 2) {
            hand.handValue[0] = 2;
            hand.handValue[1] = higherFaceValue;
            hand.handValue[2] = lowerFaceValue;
            hand.handValue[3] = highCards[0];
            return;
        }
        //stroing  pairs
        if (cardOccurence1 == 2 && cardOccurence2 <= 1) {
            hand.handValue[0] = 1;
            hand.handValue[1] = higherFaceValue;
            hand.handValue[2] = highCards[0];
            hand.handValue[3] = highCards[1];
            hand.handValue[4] = highCards[2];

            return;
        }
        //stroing  highcard
        if (cardOccurence1 <= 1 && cardOccurence2 <= 1) {
            hand.handValue[0] = 0;
            hand.handValue[1] = highCards[0];
            hand.handValue[2] = highCards[1];
            hand.handValue[3] = highCards[2];
            hand.handValue[4] = highCards[3];
            return;
        }

    }
}
