package com.example.server;

import com.example.common.util.OsUtil;
import com.example.server.config.ServerConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerStartUp {


    public void startUp() {
        ServerConfig serverConfig = new ServerConfig();
        NettyServer nettyServer = new NettyServer(serverConfig);
        registerShutDownHook(nettyServer);
        nettyServer.start();
        String localIp = OsUtil.getAllIPV4().get(0);
        log.info("server start success addr:{}:{}", localIp, nettyServer.getPort());
    }

    public static void main(String[] args) {
        ServerStartUp serverStartUp = new ServerStartUp();
        serverStartUp.startUp();
    }


    private void registerShutDownHook(NettyServer nettyServer) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            private volatile boolean hasShutdown = false;

            @Override
            public void run() {
                synchronized (this) {
                    if (!this.hasShutdown) {
                        this.hasShutdown = true;
                        nettyServer.shutdown();
                        log.info("netty server shutdown!");
                    }
                }
            }
        }, "ShutdownHook"));
    }
}
