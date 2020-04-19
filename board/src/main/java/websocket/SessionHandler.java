//package websocket;
//
//
//import lombok.Getter;
//
//import javax.ejb.Singleton;
//import javax.enterprise.context.ApplicationScoped;
//import javax.websocket.Session;
//import java.util.ArrayList;
//import java.util.List;
//
//@ApplicationScoped
//@Singleton
//public class SessionHandler {
//
//    @Getter
//    private final List<Session> sessions = new ArrayList<>();
//
//    public void addSession(Session session) {
//        sessions.add(0,session);
//    }
//
//    public void removeSession(Session session) {
//        sessions.remove(session);
//    }
//}
