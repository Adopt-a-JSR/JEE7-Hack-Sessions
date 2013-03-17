package com.taman.rtrc.endpoint.server;

import com.taman.rtrc.endpoint.data.LoginMessage;
import com.taman.rtrc.endpoint.data.LoginMessageDecoder;
import com.taman.rtrc.endpoint.data.RunnerMessage;
import com.taman.rtrc.endpoint.data.RunnerMessageConverter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import static javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfigurator;
import org.glassfish.tyrus.server.Server;

/**
 *
 * @author mohamed_taman
 */
@ServerEndpoint(value = "/server/register",
        decoders = {RunnerMessageConverter.class, LoginMessageDecoder.class},
        encoders = {RunnerMessageConverter.class},
        configurator = ServerEndpointConfigurator.class)
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
    public void handleMessage(RunnerMessage msg) {
        logger.info(msg.toString());

        broadcast(msg);
    }

    @OnMessage
    public boolean doLogin(LoginMessage msg, Session runner) {

        logger.info(msg.toString());

        runners.put(msg.getUsername(), runner);

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
            // this.addToTranscriptAndNotify(username, " has just left...rather abruptly !");
        }
    }

    private void broadcast(RunnerMessage msg) {
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

    public static void main(String[] args) throws DeploymentException {

        Server server = new Server("localhost", 2020, "/RTRC", RTRCServer.class);

        try {
            server.start();
            System.out.println("Press any key to exit");
            System.in.read();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            server.stop();
            System.out.println("Server stopped.");
        }

    }
}
