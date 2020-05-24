package com.example.server.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class NettyConnectManageHandler extends ChannelDuplexHandler {


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("NETTY SERVER PIPELINE: channelActive [{}],[{}]", ctx.channel().remoteAddress(), ctx.channel().id().asLongText());
        super.channelActive(ctx);
        //todo 定时任务检测连接有效性
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("NETTY SERVER PIPELINE: channelInactive [{}],[{}]", ctx.channel().remoteAddress(), ctx.channel().id().asLongText());
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state().equals(IdleState.ALL_IDLE)) {
                log.warn("NETTY SERVER PIPELINE: IDLE exception [{}],[{}]", ctx.channel().remoteAddress(), ctx.channel().id().asLongText());
                ctx.close();
                //释放相关关联资源 todo
            }
        }
        ctx.fireUserEventTriggered(evt);
    }
}