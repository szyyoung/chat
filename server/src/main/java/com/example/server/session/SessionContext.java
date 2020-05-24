package com.example.server.session;

import java.util.concurrent.ConcurrentHashMap;

public class SessionContext {

    public static final ConcurrentHashMap<String/** token */, ServerSession /**user and channel*/> SESSION_MAP = new ConcurrentHashMap<>();


    public static void addSession(String token, ServerSession serverSession) {
        SESSION_MAP.put(token, serverSession);
    }

    public static ServerSession getSession(String token) {
        if (SESSION_MAP.contains(token)) {
            return SESSION_MAP.get(token);
        } else {
            return null;
        }
    }


    public static void remove(String token) {
        SESSION_MAP.remove(token);
    }

}
