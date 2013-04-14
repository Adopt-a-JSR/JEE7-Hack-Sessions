/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taman.rtrc.endpoint.data;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;

/**
 *
 * @author mohamed_taman
 */
public class LoginMessageDecoder implements Decoder.Text<LoginMessage> {

    @Override
    public LoginMessage decode(String msg) throws DecodeException {
        
        return LoginMessage.fromJSON(msg);
    }

    @Override
    public boolean willDecode(String msg) { 
        return (msg != null && !msg.isEmpty()&& msg.split(",").length == 2);
    }
    
}
