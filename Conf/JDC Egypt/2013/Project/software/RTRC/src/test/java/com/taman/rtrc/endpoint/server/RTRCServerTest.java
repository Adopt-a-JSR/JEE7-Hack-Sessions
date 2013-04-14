package com.taman.rtrc.endpoint.server;

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
import org.glassfish.tyrus.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mohamed_taman
 */
public class RTRCServerTest {

    final static Logger logger = Logger.getLogger(RTRCServerTest.class.getName());
    private static String receivedMessage;
    private static CountDownLatch messageLatch;
    private static Server server = new Server("localhost", 2020, "/RTRC", RTRCServer.class);
    private ClientManager client = ClientManager.createClient();

    public RTRCServerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            server.start();
        } catch (DeploymentException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @AfterClass
    public static void tearDownClass() {
        server.stop();
    }

    @Test
    public void testMessageHandler() {
        try {
            messageLatch = new CountDownLatch(1);
            receivedMessage = null;
            String message = "Connected to RTRC server.";

            client.connectToServer(new RTRClientRunnerData(), new URI("ws://localhost:2020/RTRC/server/register"));

            messageLatch.await(5, TimeUnit.SECONDS);
            
            assertEquals(message, receivedMessage);

        } catch (InterruptedException | URISyntaxException | DeploymentException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testDoLogin() {
        try {
            messageLatch = new CountDownLatch(1);
            receivedMessage = null;
            
            String message = "Connected to RTRC server.";
            
            client.connectToServer(RTRClientLogin.class, new URI("ws://localhost:2020/RTRC/server/register"));

            messageLatch.await(5, TimeUnit.SECONDS);


            assertEquals(message, receivedMessage);

        } catch (InterruptedException | URISyntaxException | DeploymentException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @ClientEndpoint
    public static class RTRClientLogin {

        private static final String SENT_MESSAGE = "{\"username\":\"Mohamed.taman@gmail.com\",\"password\":\"xxxxx\"}";

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
            receivedMessage = message;
            messageLatch.countDown();

        }
    }

    @ClientEndpoint
    public static class RTRClientRunnerData {

        private static final String SENT_MESSAGE = "{\"name\":\"Mohamed\",\"longitude\":1.233333334,\"latitude\":2.333333,"
                + "\"timestamp\":123222233433,\"notes\":\"I am notes\"}";

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
            receivedMessage = message;
            messageLatch.countDown();
        }
    }
}