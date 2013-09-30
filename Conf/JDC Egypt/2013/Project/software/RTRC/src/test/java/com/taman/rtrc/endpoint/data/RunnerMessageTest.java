/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taman.rtrc.endpoint.data;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author mohamed_taman
 */
public class RunnerMessageTest {

    private final String jsonValue = "{\"action\":\"UPDATE\",\"userId\":\"Mohamed\",\"name\":\"Mohamed\",\"longitude\":1.233333334,\"latitude\":2.333333,"
            + "\"timestamp\":123222233433}";
    
    private final RunnerMessage instance = new RunnerMessage("UPDATE","Mohamed@Taman.com","Mohamed", 1.233333334d, 2.333333d, 123222233433l);

    public RunnerMessageTest() {
    }

    /**
     * Test of toJSON method, of class RunnerMessage.
     */
    @Ignore @Test
    public void testToJSON() {

        assertEquals(jsonValue, instance.toJSON());

    }

    /**
     * Test of fromJSON method, of class RunnerMessage.
     */
    @Ignore @Test 
    public void testFromJSON() {

        assertEquals(instance, RunnerMessage.fromJSON(jsonValue));

    }
}