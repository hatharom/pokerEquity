/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerevaltest;

import java.util.LinkedHashMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pokereval.*;

public class Dealer_Test {

    @Test
    public void testDealerConstructor() {
        Dealer d = new Dealer("QsQc", "JsTs", "8c7c8s9h2h");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDealerConstructor2() {
        //testing for duplicated card
        Dealer d2 = new Dealer("QsQc", "JsTs", "8c7c8c9h2h");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDealerConstructor3() {
        Dealer d2 = new Dealer("QsQc", "Js", "8c7cTc9h2h");
    }

    @Test
    public void testGetPvpResult() {
        Dealer d = new Dealer("QsQc", "JsTs", "8c7c8s9h2h");
        int[] result = d.getPvpResult();
        assertTrue(result[0] < result[1]);

        Dealer d2 = new Dealer("QsTc", "QsTs", "8c7c8s9hJh");
        int[] result2 = d2.getPvpResult();
        assertTrue(result2[0] == result2[1]);

    }

    @Test
    public void testGetResultMap() {
        Dealer d = new Dealer("QsQc", "2cTcJs9h");

        LinkedHashMap<String, Integer> result = d.getPlayer1ResultMap();
        assertNotNull(result);
        assertTrue(result.get("straightflush") == 0);
        assertTrue(result.get("straight") > result.get("highcard"));
        assertTrue(result.get("straight") < result.get("pair"));
        LinkedHashMap<String, Integer> result2 = d.getPlayer2ResultMap();
        assertNull(result2);

    }
}
