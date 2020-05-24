package com.example.protocol.codec.protobuf;

import com.example.protocol.MsgProtos;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ProtobufDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {

        in.markReaderIndex();
        if (in.readableBytes() < 2) {
            return;
        }

        int length = in.readShort();

        if (length < 0) {
            ctx.close();
        }

        if (length > in.readableBytes()) {
            in.resetReaderIndex();
            return;
        }

        byte[] array;

        if (in.hasArray()) {
            ByteBuf slice = in.slice();
            array = slice.array();
        } else {
            array = new byte[length];
            in.readBytes(array, 0, length);
        }

        MsgProtos.Msg outMsg = MsgProtos.Msg.parseFrom(array);

        if (outMsg != null) {
            out.add(outMsg);
        }
    }
}
