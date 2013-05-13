package com.taman.rtrc.endpoint.data;

import java.util.logging.Logger;
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
    Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void init(EndpointConfig ec) {
        log.info("I am Initialized");
    }

    @Override
    public String encode(Message msg) throws EncodeException {
        log.info(msg.toString());
        return msg.toJSON();

    }

    @Override
    public boolean willDecode(String msg) {

        if (!((msg != null && !msg.isEmpty())
                && (msg.split(",").length == 2 || msg.split(",").length == 5))) {
            return false;
        }
        size = msg.split(",").length;
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

    //FIXME remove this block at final commit. and push request.
    public static void main(String[] args) {
        MessageConverter conv = new MessageConverter();

        System.out.println(conv.willDecode("{\"username\":\"mohamed.taman@gmail.com\""));
    }
}
