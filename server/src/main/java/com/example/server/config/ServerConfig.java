package com.example.server.config;

import com.example.protocol.ProtocolEnum;

/**
 * todo 可以增加对应的 server.config 配置文件  或者 支持vm参数
 */
public class ServerConfig {

    public int sndBuf = 65535;
    private int rcvBuf = 65535;
    private int listenPort = 8888;

    //todo channel 空闲检测，间隔 4 mins，会不会太长？
    private int serverChannelMaxIdleTimeSeconds = 240;

    private int eventLoopGroupBossThreads = 1;
    private int eventLoopGroupWorkerThreads = 16;

    private String protocolType = ProtocolEnum.PROTOBUF.name();


    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public int getServerChannelMaxIdleTimeSeconds() {
        return serverChannelMaxIdleTimeSeconds;
    }

    public void setServerChannelMaxIdleTimeSeconds(int serverChannelMaxIdleTimeSeconds) {
        this.serverChannelMaxIdleTimeSeconds = serverChannelMaxIdleTimeSeconds;
    }


    public int getEventLoopGroupBossThreads() {
        return eventLoopGroupBossThreads;
    }

    public void setEventLoopGroupBossThreads(int eventLoopGroupBossThreads) {
        this.eventLoopGroupBossThreads = eventLoopGroupBossThreads;
    }

    public int getEventLoopGroupWorkerThreads() {
        return eventLoopGroupWorkerThreads;
    }

    public void setEventLoopGroupWorkerThreads(int eventLoopGroupWorkerThreads) {
        this.eventLoopGroupWorkerThreads = eventLoopGroupWorkerThreads;
    }

    public int getSndBuf() {
        return sndBuf;
    }

    public void setSndBuf(int sndBuf) {
        this.sndBuf = sndBuf;
    }

    public int getRcvBuf() {
        return rcvBuf;
    }

    public void setRcvBuf(int rcvBuf) {
        this.rcvBuf = rcvBuf;
    }
}
