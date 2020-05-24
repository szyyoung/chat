package com.example.server;

import com.example.protocol.codec.protobuf.ProtobufDecoder;
import com.example.protocol.codec.protobuf.ProtobufEncoder;
import com.example.server.config.ServerConfig;
import com.example.server.handler.AuthHandler;
import com.example.server.handler.ServerChannelInitializer;
import com.example.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class NettyServer {

    private final ServerBootstrap serverBootstrap;

    private final EventLoopGroup eventLoopGroupBoss;

    private final EventLoopGroup eventLoopGroupWorker;

    private final ServerConfig serverConfig;

    private int port;

    public NettyServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        this.serverBootstrap = new ServerBootstrap();
        int eventLoopGroupBossThreads = serverConfig.getEventLoopGroupBossThreads();
        int eventLoopGroupWorkerThreads = serverConfig.getEventLoopGroupWorkerThreads();
        if (useEpoll()) {
            this.eventLoopGroupBoss = new EpollEventLoopGroup(eventLoopGroupBossThreads, new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyServerBossEpollExecutor_%d_%d", eventLoopGroupBossThreads, threadIndex.incrementAndGet()));
                }
            });
            this.eventLoopGroupWorker = new EpollEventLoopGroup(serverConfig.getEventLoopGroupWorkerThreads(), new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyServerWorkEpollExecutor_%d_%d", eventLoopGroupWorkerThreads, threadIndex.incrementAndGet()));
                }
            });
        } else {
            this.eventLoopGroupBoss = new NioEventLoopGroup(serverConfig.getEventLoopGroupBossThreads(), new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyServerBossNioExecutor_%d_%d", eventLoopGroupBossThreads, threadIndex.incrementAndGet()));
                }
            });
            this.eventLoopGroupWorker = new NioEventLoopGroup(serverConfig.getEventLoopGroupWorkerThreads(), new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyServerWorkNioExecutor_%d_%d", eventLoopGroupWorkerThreads, threadIndex.incrementAndGet()));
                }
            });
        }
    }

    public void start() {

        this.serverBootstrap.group(eventLoopGroupBoss, eventLoopGroupWorker)
                .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_SNDBUF, serverConfig.getSndBuf())
                .childOption(ChannelOption.SO_RCVBUF, serverConfig.getRcvBuf())
                .localAddress(new InetSocketAddress(this.serverConfig.getListenPort()))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("protobuf-decoder", new ProtobufDecoder());
                        pipeline.addLast("protobuf-encoder", new ProtobufEncoder());
                        pipeline.addLast("auth-handler", new AuthHandler());
                        pipeline.addLast("idle-handler", new IdleStateHandler(0, 0,
                                serverConfig.getServerChannelMaxIdleTimeSeconds()));
                        pipeline.addLast("server-handler", new ServerHandler());
                    }
                });
        try {
            ChannelFuture sync = this.serverBootstrap.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) sync.channel().localAddress();
            this.port = addr.getPort();
        } catch (InterruptedException e1) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e1);
        }
    }

    public int getPort() {
        return this.port;
    }

    private boolean useEpoll() {
        return Epoll.isAvailable();
    }

    public void shutdown() {
        try {
            this.eventLoopGroupBoss.shutdownGracefully();
            this.eventLoopGroupWorker.shutdownGracefully();
        } catch (Exception e) {
            log.error("shut down err:", e);
        }
    }

}
