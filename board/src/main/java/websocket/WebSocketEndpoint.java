package websocket;


import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/{topic}/")
public class WebSocketEndpoint {

    @Inject
    private Peers peers;

    @Inject
    private SessionHandler sessionHandler;

//    private Session session;

    @OnMessage
    public void onServerMessage(Session session,String message){
            session.getAsyncRemote().sendText(message);
    }


    @OnOpen
    public void open(@PathParam("topic") String topic, Session peer,EndpointConfig config) {
        sessionHandler.addSession(peer);
        System.out.println("Peer " + peer.getId() + " joined & subscribed to topic " + topic);
        peer.getUserProperties().put("topic", topic);
        peers.add(peer);
    }

    @OnClose
    public void close(Session peer) {
        peers.remove(peer);
//        sessionHandler.removeSession(peer);
        System.out.println("Peer " + peer.getId() + " left");
    }

}
