package com.example.server.handler;

import com.example.domain.user.User;
import com.example.protocol.MsgProtos;
import com.example.server.session.ServerSession;
import com.example.server.session.SessionContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<MsgProtos.Msg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgProtos.Msg msg) throws Exception {
        MsgProtos.HeadType type = msg.getType();
        if (MsgProtos.HeadType.MSG_NOTIFY.equals(type)) {
            return;
        }
        if (!MsgProtos.HeadType.CLIENT_AUTH.equals(type)) {
            ctx.close();
            log.warn("invalid client auth request,ctx closed");
        } else {
            String token = msg.getToken();
            //todo valid token
            boolean valid = false;
            if (valid) {
                log.warn("invalid client auth request,ctx closed");
                ctx.close();
            }
            //remove auth handler
            ctx.pipeline().remove(this);
            ServerSession serverSession = new ServerSession(ctx.channel(), new User(), token);
            serverSession.bind();//bind this token on this channel
            SessionContext.addSession(token, serverSession);
        }

    }

}
