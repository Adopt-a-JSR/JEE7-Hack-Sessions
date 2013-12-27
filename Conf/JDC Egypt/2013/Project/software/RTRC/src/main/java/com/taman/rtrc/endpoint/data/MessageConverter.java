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
public class MessageConverter implements Decoder.Text<RunnerMessage>, Encoder.Text<RunnerMessage> {

    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void init(EndpointConfig ec) {
        log.info("Messages Decoder/Encoder: Is Initialized..");
    }

    @Override
    public String encode(RunnerMessage msg) throws EncodeException {
        log.info(msg.toJSON());
        return msg.toJSON();

    }

    @Override
    public boolean willDecode(String msg) {
        return true;
    }

    @Override
    public RunnerMessage decode(String msg) throws DecodeException {

        System.out.println(msg);
        RunnerMessage message = RunnerMessage.fromJSON(msg);

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
