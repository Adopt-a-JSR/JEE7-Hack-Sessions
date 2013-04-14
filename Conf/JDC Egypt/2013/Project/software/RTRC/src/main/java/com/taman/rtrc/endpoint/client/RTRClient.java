package com.taman.rtrc.endpoint.client;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * 
 * @author mohamed_taman
 * @version 1.2
 */
@ClientEndpoint
public class RTRClient {

    final static Logger logger = Logger.getLogger(RTRClient.class.getName());
    private static final String SENT_MESSAGE = "{\"username\":\"Mohamed.taman@gmail.com\",\"password\":\"xxxxx\"}";
    private static CountDownLatch messageLatch;

    @OnOpen
    public void onOpen(Session p) {
        try {
            p.getBasicRemote().sendText(SENT_MESSAGE);

        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    @OnMessage
    public void onMessage(String message) {

        logger.info(message);
    }
}
