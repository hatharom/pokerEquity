package pokereval;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Dealer {

    private HashMap<String, Integer> resultMap1 = new HashMap<String, Integer>();
    private HashMap<String, Integer> resultMap2 = null;
    private int[] pvpResult = new int[3];
    private int runTime = 1000000;

    public Dealer(String holeCard, String board) {
        if (holeCard == null || holeCard.length() != 4) {
            throw new IllegalArgumentException();
        }
        if (board == null || board.length() < 6 || board.length() > 10 || board.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }

        Card[] hand = CardConverter.convertHand(holeCard + board);
        if (!checkDuplicatedInput(hand)) {
            throw new IllegalArgumentException();
        }
        initMap(resultMap1);
        runHand(hand);
    }

    public Dealer(String holeCard1, String holeCard2, String board) {
        if (holeCard1 == null || holeCard1.length() != 4 || holeCard2 == null || holeCard2.length() != 4) {
            throw new IllegalArgumentException();
        }
        if (board == null || board.length() < 6 || board.length() > 10 || board.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        resultMap2 = new HashMap<String, Integer>();
        Card[] hand1 = CardConverter.convertHand(holeCard1 + board);
        Card[] hand2 = CardConverter.convertHand(holeCard2 + board);
        if (!checkDuplicatedInput(hand1) || !checkDuplicatedInput(hand1)) {
            throw new IllegalArgumentException();
        }
        initMap(resultMap1);
        initMap(resultMap2);

        runPvpHand(hand1, hand2);
    }

    public HashMap<String, Integer> getPlayer1ResultMap() {
        return this.resultMap1;
    }

    public HashMap<String, Integer> getPlayer2ResultMap() {
        return this.resultMap2;
    }

    public int[] getPvpResult() {
        return this.pvpResult;
    }

    private void runHand(Card[] hand) {
        if (hand.length == 7) {
            Hand h = new Hand(hand);
            fillUpResultMap(h, resultMap1);
        } else {
            runMore(hand);
        }
        System.out.println(resultMap1);
    }

    private void runMore(Card[] hand) {
        Card[] completedHand = new Card[7];
        System.arraycopy(hand, 0, completedHand, 0, hand.length);

        for (int i = 0; i < runTime; i++) {
            completedHand[6] = getRndCard(completedHand);
            if (hand.length == 5) {
                completedHand[5] = getRndCard(completedHand);
            }
            Hand h = new Hand(completedHand);
            fillUpResultMap(h, resultMap1);
        }

    }

    private void runMorePvp(Card[] hand1, Card[] hand2) {
        Card[] completedHand1 = new Card[7];
        Card[] completedHand2 = new Card[7];
        System.arraycopy(hand1, 0, completedHand1, 0, hand1.length);
        System.arraycopy(hand2, 0, completedHand2, 0, hand2.length);
        
        Card riverCard;
        Card turnCard;
        for (int i = 0; i < runTime; i++) {
            do {
                riverCard = getRndCard(completedHand1);
            } while (Arrays.asList(hand2).contains(riverCard));
            completedHand1[6] = riverCard;
            completedHand2[6] = riverCard;
            if (hand1.length == 5) {
                do {
                    turnCard = getRndCard(completedHand1);
                } while (Arrays.asList(hand2).contains(turnCard));
                completedHand1[5] = turnCard;
                completedHand2[5] = turnCard;
            }
            Hand h1 = new Hand(completedHand1);
            Hand h2 = new Hand(completedHand2);
            fillUpResultMap(h1, resultMap1);
            fillUpResultMap(h2, resultMap2);
            int actualWinner = h1.compareTo(h2);
            switch (actualWinner) {
                case 1:
                    pvpResult[0]++;
                    break;
                case -1:
                    pvpResult[1]++;
                    break;
                case 0:
                    System.out.println("tie!!!!-----  "+h1 +" vs "+h2+"  "+h1.getStringHandValue());
                    pvpResult[2]++;
                    break;
            }
        }

    }

    private Card getRndCard(Card[] usedCards) {
        Card generatedCard = new Card(0, 0);
        Random rndFace = new Random();
        Random rndSuit = new Random();
        do {
            generatedCard = new Card(rndFace.nextInt(13), rndSuit.nextInt(4));
        } while (Arrays.asList(usedCards).contains(generatedCard));
        return generatedCard;
    }

    private void runPvpHand(Card[] hand1, Card[] hand2) {
        if (hand1.length == 7) {
            Hand h1 = new Hand(hand1);
            Hand h2 = new Hand(hand2);
            System.out.println(h1.compareTo(h2));
            fillUpResultMap(h1, resultMap1);
            fillUpResultMap(h2, resultMap2);
        } else {
            runMorePvp(hand1, hand2);
        }

    }

    private void fillUpResultMap(Hand actualHand, HashMap<String, Integer> resultMap) {
        int count = 0;

        switch (actualHand.getHandValue()[0]) {

            case 8:
                count = resultMap.get("straightflush");
                resultMap.put("straightflush", ++count);
                break;
            case 7:
                count = resultMap.get("fourofakind");
                resultMap.put("fourofakind", ++count);
                break;
            case 6:
                count = resultMap.get("fullhouse");
                resultMap.put("fullhouse", ++count);
                break;
            case 5:
                count = resultMap.get("flush");
                resultMap.put("flush", ++count);
                break;
            case 4:
                count = resultMap.get("straight");
                resultMap.put("straight", ++count);
                break;
            case 3:
                count = resultMap.get("threeofakind");
                resultMap.put("threeofakind", ++count);
                break;
            case 2:
                count = resultMap.get("twopair");
                resultMap.put("twopair", ++count);
                break;
            case 1:
                count = resultMap.get("pair");
                resultMap.put("pair", ++count);
                break;
            case 0:
                count = resultMap.get("highcard");
                resultMap.put("highcard", ++count);
                break;

        }

    }

    private void initMap(HashMap resultMap) {
        resultMap.put("straightflush", 0);
        resultMap.put("fourofakind", 0);
        resultMap.put("fullhouse", 0);
        resultMap.put("flush", 0);
        resultMap.put("straight", 0);
        resultMap.put("threeofakind", 0);
        resultMap.put("twopair", 0);
        resultMap.put("pair", 0);
        resultMap.put("highcard", 0);

    }

    private boolean checkDuplicatedInput(Card[] cards) {
        HashSet<Card> hs = new HashSet<Card>();
        for (int i = 0; i < cards.length; i++) {
            hs.add(cards[i]);
        }
        if (hs.size() != cards.length) {
            return false;
        } else {
            return true;
        }
    }

}
