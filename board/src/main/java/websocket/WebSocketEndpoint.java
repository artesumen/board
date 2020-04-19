//package websocket;
//
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import javax.inject.Inject;
//import javax.inject.Singleton;
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//
//@Singleton
//@ServerEndpoint("/{topic}/")
//public class WebSocketEndpoint {
//
//    @PostConstruct
//    public void init(){
//        System.out.println("WebSocketEndpoint created");
//    }
//
//    @Inject
//    private Peers peers;
//
////    @Inject
////    private SessionHandler sessionHandler;
//
////    private Session session;
//
//    @OnMessage
//    public void onServerMessage(Session s,String message) throws IOException {
//        for (Session session : s.getOpenSessions()) {
//            session.getBasicRemote().sendText(message);
//        }
//    }
//
//    @PreDestroy
//    public void destroy(){
//        System.out.println("WebSocketEndpoint destroyed");
//    }
//
//
//    @OnOpen
//    public void open(@PathParam("topic") String topic, Session peer,EndpointConfig config) {
////        sessionHandler.addSession(peer);
//        System.out.println("Peer " + peer.getId() + " joined & subscribed to topic " + topic);
//        peer.getUserProperties().put("topic", topic);
//        peers.add(peer);
//    }
//
//    @OnClose
//    public void close(Session peer) {
//        peers.remove(peer);
////        sessionHandler.removeSession(peer);
//        System.out.println("Peer " + peer.getId() + " left");
//    }
//
//}
