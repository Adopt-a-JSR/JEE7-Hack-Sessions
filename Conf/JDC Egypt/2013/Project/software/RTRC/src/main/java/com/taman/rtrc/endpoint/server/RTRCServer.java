package com.taman.rtrc.endpoint.server;

import com.taman.rtrc.endpoint.data.LoginMessage;
import com.taman.rtrc.endpoint.data.Message;
import com.taman.rtrc.endpoint.data.MessageConverter;
import com.taman.rtrc.endpoint.data.RunnerMessage;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import static javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Class RTRC (Real Time Runners Champion) server endpoint, is a Websocket
 * based, which is responsible for runners registration, and notifications for
 * all new runners, with newly registered runners alongside the de-registration
 * of runner when connection is lost with notification for all other runners.
 *
 * @author mohamed_taman
 * @since v1.0
 * @version 1.2
 */
@ServerEndpoint(value = "/server/runners/engage",
        decoders = {MessageConverter.class},
        encoders = {MessageConverter.class})
public class RTRCServer {

    final static Logger logger = Logger.getLogger(RTRCServer.class.getName());
    private static final List<RunnerMessage> runners = Collections.synchronizedList(new LinkedList<RunnerMessage>());
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void init(Session session) {
        try {
            sessions.add(session);
            logger.info(session.getId());
            String result = "{\"action\":\"CONNECT\",\"message\":\"Connected to RTRCT server successfully.\"}";
            session.getBasicRemote().sendText(result);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @OnMessage
    public void processMessage(RunnerMessage message, Session session) {

        logger.log(Level.INFO, "Processed message: {0}", message);

        try {
            if (message.getAction().equals("RIGISTER")) {
                String result = "{\"action\":\"RIGISTER\",\"result\":\"SUCCEED\",\"message\":\"Registered successfully in the system.\"}";
                session.getBasicRemote().sendText(result);

                logger.log(Level.INFO, "Runners Size:{0}", runners.size());
                
                for (RunnerMessage runner : runners) {
                    session.getBasicRemote().sendObject(runner);
                }

                runners.add(message);


            }
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(RTRCServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnClose
    public void disconnected(Session session) {

        sessions.remove(session);
        logger.info("The web socket closed");



    }

    @OnError
    public void onError(Throwable err) {
        logger.warning("Errors happend");
    }
}