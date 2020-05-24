package com.example.server.handler;

import com.example.protocol.codec.protobuf.ProtobufDecoder;
import com.example.protocol.codec.protobuf.ProtobufEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("protobuf-decoder", new ProtobufDecoder());
        pipeline.addLast("protobuf-encoder", new ProtobufEncoder());
        pipeline.addLast("server-handler", new ServerHandler());
    }
}
