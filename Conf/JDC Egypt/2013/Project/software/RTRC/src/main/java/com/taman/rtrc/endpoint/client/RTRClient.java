/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taman.rtrc.endpoint.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;

/**
 *
 * @author mohamed_taman
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

    public static void main(String[] args) {
        ClientManager client = ClientManager.createClient();
        messageLatch = new CountDownLatch(1);

        try {
            client.connectToServer(new RTRClient(), new URI("ws://localhost:2020/RTRC/server/register"));
            messageLatch.await(5, TimeUnit.SECONDS);
        } catch (URISyntaxException | DeploymentException | InterruptedException e) {
            logger.log(Level.SEVERE, null, e);
        }

    }
}
