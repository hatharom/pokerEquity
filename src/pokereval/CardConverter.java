package pokereval;

public class CardConverter {

    /**
    * returns a card array converted from a String
    * *@param hand - hand waiting for conversion from String to Card array 
    *@return a Card array that symbolizes the hand      
    * @throws IllegalArgumentException
    */
    public static Card[] convertHand(String hand) {
        if (hand == null || hand.length() > 14 || hand.length() < 10) {
            throw new IllegalArgumentException();
        }
        Card[] convertedHand = new Card[hand.length() / 2];
        for (int i = 0; i < convertedHand.length; i++) {
            int startIndex = i * 2;
            convertedHand[i] = convertCard(hand.substring(startIndex, startIndex + 2));
        }
        return convertedHand;
       
    }

    /**
     * returns a Card object transformed from a string
     * @param card- single card with a lengh of 2 ("8s")
     * @return Card object     
     * @throws IllegalArgumentException
     */
    private static Card convertCard(String card) {
        Card convertedCard = null;
        char face = Character.toUpperCase(card.charAt(0));
        char suit = card.charAt(1);
        int faceValue = 0;
        int suitValue = 0;

        switch (face) {
            case '2':
                faceValue = 0;
                break;
            case '3':
                faceValue = 1;
                break;
            case '4':
                faceValue = 2;
                break;
            case '5':
                faceValue = 3;
                break;
            case '6':
                faceValue = 4;
                break;
            case '7':
                faceValue = 5;
                break;
            case '8':
                faceValue = 6;
                break;
            case '9':
                faceValue = 7;
                break;
            case 'T':
                faceValue = 8;
                break;
            case 'J':
                faceValue = 9;
                break;
            case 'Q':
                faceValue = 10;
                break;
            case 'K':
                faceValue = 11;
                break;
            case 'A':
                faceValue = 12;
                break;
            default:
                throw new IllegalArgumentException();

        }
        switch (suit) {
            case 's':
                suitValue = 0;
                break;
            case 'h':
                suitValue = 1;
                break;
            case 'c':
                suitValue = 2;
                break;
            case 'd':
                suitValue = 3;
                break;
            default:
                throw new IllegalArgumentException();
        }
        convertedCard = new Card(faceValue, suitValue);
        return convertedCard;
    }
}
