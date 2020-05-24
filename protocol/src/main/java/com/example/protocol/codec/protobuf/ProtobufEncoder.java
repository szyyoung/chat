package com.example.protocol.codec.protobuf;

import com.example.protocol.MsgProtos;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtobufEncoder extends MessageToByteEncoder<MsgProtos.Msg> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MsgProtos.Msg msg, ByteBuf out) throws Exception {
        byte[] bytes = msg.toByteArray();
        int length = bytes.length;
        out.writeShort(length);
        out.writeBytes(bytes);
    }
}
