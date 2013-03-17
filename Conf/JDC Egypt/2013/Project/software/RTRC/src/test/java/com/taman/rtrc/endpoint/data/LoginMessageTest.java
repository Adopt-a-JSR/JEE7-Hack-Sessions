/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taman.rtrc.endpoint.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mohamed_taman
 */
public class LoginMessageTest {
    
    public LoginMessageTest() {
    }

    /**
     * Test of fromJSON method, of class LoginMessage.
     */
    @Test
    public void testFromJSON() {
        
        String jsonValue = "{\"username\":\"Mohamed.taman@gmail.com\",\"password\":\"xxxxx\"}";
        
        LoginMessage expResult = new LoginMessage();
        
        expResult.setUsername("Mohamed.taman@gmail.com");
        expResult.setPassword("xxxxx");
        
        LoginMessage result = LoginMessage.fromJSON(jsonValue);
        
        assertEquals(expResult, result);
       
    }
}