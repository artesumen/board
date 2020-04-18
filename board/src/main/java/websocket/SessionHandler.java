package websocket;


import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SessionHandler {

    @Getter
    private final List<Session> sessions = new ArrayList<>();

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
}
