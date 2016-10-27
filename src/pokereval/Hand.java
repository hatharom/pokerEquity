package pokereval;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

public class Hand implements Comparable<Hand> {

    private Card[] cards;
    public int[] pair;
    private int flushType;
    private int straightType;
    private int[] handValue = new int[6];

    public Hand(Card[] cards) {
        if (cards == null || cards.length != 7 || !(cards[0] instanceof Card)) {
            throw new IllegalArgumentException();
        }
        this.cards = cards;
        evaluate();
    }


    private void evaluate() {
        evaluatePairs();
        this.flushType = evaluateFlush();
        this.straightType = evaluateStraight();
        parseHand();
    }

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
    //    System.out.println(cardOccurence1 + " of " + higherFaceValue + " and " + cardOccurence2 + " of " + lowerFaceValue);

    }

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
          //      System.out.println("flush cards:" + ts);
                flushType = (Integer) ts.last();
                break;
            }
        }
        return flushType;
    }

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
        //  System.out.println(straightFaces);
        int actualFace = -1;
        int previousFace = -1;
        for (Integer face : straightFaces) {
            actualFace = face;
            //    System.out.println("comparing:"+actualFace+"vs"+previousFace);
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
        //  System.out.println(lockedStraightCounter+"...."+straightType);
        return straightType;
    }

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
  //      System.out.println(cardOccurence1 + " of " + higherFaceValue + " and " + cardOccurence2 + " of " + lowerFaceValue);
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
        if (cardOccurence1 == 2 && cardOccurence2 == 1) {
            handValue[0] = 1;
            handValue[1] = higherFaceValue;
            handValue[2] = highCards[0];
            handValue[3] = highCards[1];
            handValue[4] = highCards[2];
            return;
        }
        //stroing  highcard
        if (cardOccurence1 == 1 && cardOccurence2 == 1) {
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

    // helper method for development
    public void display() {
        System.out.println(this.toString());
        System.out.println("pais: " + Arrays.toString(this.pair));
        System.out.println("flush: " + this.flushType);
        System.out.println("straight: " + this.straightType);
        System.out.println("total handvalue: " + Arrays.toString(handValue));
    }

    /*
     * returns the final handValue array of the hand
     */
    public int[] getHandValue() {
        return this.handValue;
    }

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
