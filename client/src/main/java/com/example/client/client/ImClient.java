package com.example.client.client;

import com.alibaba.fastjson.JSONObject;
import com.example.client.client.handler.ClientHandler;
import com.example.client.client.reponse.CallBack;
import com.example.client.client.reponse.ResponseFuture;
import com.example.client.util.Constant;
import com.example.client.util.HttpUtil;
import com.example.client.util.ServerResponse;
import com.example.protocol.MsgProtos;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ImClient {

    public static volatile boolean init = false;

    static Bootstrap bootstrap = null;
    static NioEventLoopGroup nioEventLoopGroup = null;
    static Channel channel;

    public static ConcurrentHashMap<String, ResponseFuture> responseFutureConcurrentHashMap = new ConcurrentHashMap<>();

    public static void init() {
        try {
            String s = fetchServerAddr();
            String[] split = s.split(",");
            String ip = split[0];
            String port = split[1];
            bootstrap = new Bootstrap();
            nioEventLoopGroup = new NioEventLoopGroup(1);
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast();
                            pipeline.addLast();
                            pipeline.addLast("client-handler", new ClientHandler());
                        }
                    });
            ChannelFuture connectFuture = bootstrap.connect(ip, Integer.parseInt(port)).sync();
            boolean success = connectFuture.isSuccess();
            if (success) {
                log.info("connect server success,channel:{}", channel.id().asShortText());
                channel = connectFuture.channel();
                log.info("start auth,channel:{}", channel.id().asShortText());
                startAuth();
            }
        } catch (Exception e) {

        }
    }

    private static ResponseFuture send(String msg, String to, String msgType) {
        if (init) {
            //发送消息
            return null;
        } else {
            return null;
        }
    }


    private static void startAuth() {
        String authMsgId = UUID.randomUUID().toString().replaceAll("-", "");
        MsgProtos.HeadType headType = MsgProtos.HeadType.CLIENT_AUTH;
        MsgProtos.Msg authMsg = MsgProtos.Msg.newBuilder()
                .setType(headType)
                .setToken(Constant.HOLDER.get(Constant.TOKEN).toString())
                .setSeq(authMsgId)
                .build();
        ResponseFuture responseFuture = new ResponseFuture(channel, authMsgId, new CallBack() {
            @Override
            public void operationComplete(ResponseFuture responseFuture) {
                MsgProtos.Msg result = responseFuture.getResult();
                MsgProtos.Response response = result.getResponse();
                int code = response.getCode();
                if (0 == code) {
                    init = true;
                    log.info("auth success:{}", responseFuture.getProcessChannel().id().asShortText());
                    responseFutureConcurrentHashMap.remove(responseFuture.getSeq());
                }
            }
        });
        responseFutureConcurrentHashMap.put(authMsgId, responseFuture);
        channel.writeAndFlush(authMsg).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                //写成功
            } else {

            }

        });
    }


    public static String fetchServerAddr() {
        JSONObject serverResp = HttpUtil.post(Constant.SELECT_URL, null, null);
        ServerResponse serverData = JSONObject.parseObject(serverResp.toJSONString(), ServerResponse.class);
        return String.valueOf(serverData.getData());
    }
}
