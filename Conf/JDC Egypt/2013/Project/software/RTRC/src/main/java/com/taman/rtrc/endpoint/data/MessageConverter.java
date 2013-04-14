package com.taman.rtrc.endpoint.data;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * This Class represents text message converter, for RTRC Server endpoint.
 *
 * @author mohamed_taman
 * @version 1.2
 */
public class MessageConverter implements Decoder.Text<Message>, Encoder.Text<Message> {

    private int size = 0;

    @Override
    public void init(EndpointConfig ec) {
    }

    @Override
    public String encode(Message msg) throws EncodeException {
        
        return msg.toJSON();
        
    }

    @Override
    public boolean willDecode(String msg) {

        if (msg != null && !msg.isEmpty()) {
            size = msg.split(",").length;
        } else {
            return false;
        }

        if ((size != 2 || size != 5)) {
            return false;
        }

        return true;
    }

    @Override
    public Message decode(String msg) throws DecodeException {

        Message message = null;

        if (size == 2) {
            message = LoginMessage.fromJSON(msg);
        } else {
            message = RunnerMessage.fromJSON(msg);
        }

        return message;
    }

    @Override
    public void destroy() {
    }
}
