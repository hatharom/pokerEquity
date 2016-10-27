package pokereval;

public class Card  {

    private int face;
    private int suit;

    private static String[] suits = {"s","h","c","d"};
    private static String[] faces = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};

    public Card(int face, int suit) {
        if ((face < 0 || face > 12) || (suit < 0 || suit > 3)) {
            throw new IllegalArgumentException();
        }
        this.face = face;
        this.suit = suit;
    }
    
    
    public static String getTrueFace(int face){
        return faces[face];
    }
    public int getFace() {
        return this.face;
    }

    public int getSuit() {
        return this.suit;
    }
    
    @Override 
    public String toString(){
        return faces[face]+suits[suit];
    }

    @Override
    public int hashCode(){
        int hashCode=0;
        hashCode=((this.face*10)+(this.suit));
        return hashCode;
    }
    
    @Override
    public boolean equals(Object o){
        if (o==null) {
            return false;
        }
       Card otherCard=(Card)o;
        if (this.face==otherCard.face&&this.suit==otherCard.suit) {
            return true;
        }
        else {
            return false;
        }
    }
}
