package com.taman.rtrc.endpoint.server;

import com.taman.rtrc.endpoint.data.Message;
import com.taman.rtrc.endpoint.data.MessageConverter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import static javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Class RTRC (Real Time Runners Champion) server endpoint, is a Websocket based,
 * which is responsible for runners registration, and notifications for all new runners,
 * with newly registered runners alongside the de-registration of runner when connection
 * is lost with notification for all runners.
 * 
 * @author mohamed_taman
 * @since v1.0
 * @version 1.2
 */
@ServerEndpoint(value = "/server/register",
        decoders = {MessageConverter.class},
        encoders = {MessageConverter.class} )
public class RTRCServer {

    final static Logger logger = Logger.getLogger(RTRCServer.class.getName());
    
    private static ConcurrentHashMap<String, Session> runners = new ConcurrentHashMap<>();

    @OnOpen
    public void init(Session runner) {
        try {
            logger.info(runner.getId());
            runner.getBasicRemote().sendText("Connected to RTRC server.");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @OnMessage
    public void processMessage(Message msg, Session runner) {
        
        logger.info(msg.toString());

        register(null);
        
        //broadcast(msg);
        
    }
    
    private boolean register(Session runner) {

//        logger.info(msg.toString());
//
//        runners.put(msg.getUsername(), runner);

        logger.log(Level.INFO, "Current runneres = {0}", runners.size());

        return true;
    }

    @OnClose
    public void disconnected(Session runner) {
        String username = null;
        logger.info("The web socket closed");
        for (String s : runners.keySet()) {
            if (runners.equals(runners.get(s))) {
                username = s;
                runners.remove(s);
                break;
            }
        }

        logger.log(Level.INFO, "Current runneres = {0}", runners.size());

        if (username != null) {
            // this.notifyAll(username, " has just left...rather abruptly !");
        }
    }

    private void broadcast(Message msg) {
        logger.info("Broadcasting updated user list");

        for (Session nextSession : runners.values()) {
            Basic remote = nextSession.getBasicRemote();
            try {
                remote.sendText(msg.toJSON());
            } catch (IOException ioe) {
                logger.log(Level.WARNING, "Error updating a client {0} : {1}", new Object[]{remote, ioe.getMessage()});
            }
        }
    }
}