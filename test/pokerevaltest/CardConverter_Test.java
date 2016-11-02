/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerevaltest;

import java.util.Arrays;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pokereval.*;

public class CardConverter_Test {

    @Test(expected = IllegalArgumentException.class)
    public void TestConvertHand() {
        CardConverter.convertHand(null);

    }
    
    @Test
    public void TestConvertHand2() {
       Card[] cards= CardConverter.convertHand("AsAd8c6d2s");
        Card expectedSampleCard= new Card(12,3);
        Card resultSampleCard=cards[1];
        assertEquals(expectedSampleCard,resultSampleCard);

    }

}
