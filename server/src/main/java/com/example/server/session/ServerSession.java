package com.example.server.session;

import com.example.domain.user.User;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;


public class ServerSession {

    public static final AttributeKey<ServerSession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    private Channel channel;

    private User user;

    private String token;

    public ServerSession(Channel channel) {
        this.channel = channel;
    }

    public ServerSession(Channel channel, User user, String token) {
        this.channel = channel;
        this.user = user;
        this.token = token;
    }


    public void bind() {
        this.channel.attr(ServerSession.SESSION_KEY).set(this);
        SessionContext.addSession(this.token, this);
    }


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
