/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerevaltest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pokereval.Card;


public class Card_Test {
    

  @Test
    public void testCardConstructor() {
        Card c = new Card(3, 3);
        int faceResult = c.getFace();
        int suitResult = c.getSuit();
        int faceExpResult = 3;
        int suitExpResult = 3;
        String stringResult=c.toString();
        String expStringResult="5d";
        assertEquals(faceExpResult, faceResult);
        assertEquals(suitExpResult, suitResult);
        assertEquals(expStringResult, stringResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCardConstructor2() {
        Card c = new Card(20, 20);
    }
}
