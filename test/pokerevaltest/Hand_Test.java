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
import pokereval.*;


public class Hand_Test {

@Test
    public void testHandConstructor(){
        Card[] cards=new Card[7];
    
        for (int i = 0; i < cards.length; i++) {
           cards[i]=new Card(i,3);
        }
        Hand h = new Hand(cards);
       
        String stringHand=h.toString();
        String expStringHand="2d3d4d5d6d7d8d";
        assertEquals(expStringHand,stringHand);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHandConstructor2(){
        Card[] cards=new Card[3];
        Hand h = new Hand(cards);
        
    }
    
    @Test
    public void testGetStringHandValue(){
        Card[] cards=new Card[7];
        cards[0]=new Card(3,1);
        cards[1]=new Card(1,0);
        cards[2]=new Card(12,2);
        cards[3]=new Card(3,0);
        cards[4]=new Card(12,3);
        cards[5]=new Card(7,2);
        cards[6]=new Card(1,1);
                
        Hand h = new Hand(cards);
        String result=h.getStringHandValue();
        String expectedResult="two pairs of A and 5";                       
        assertEquals(expectedResult,result);
    }
}
