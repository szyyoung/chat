package com.example.protocol;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MsgTest {

    @Test
    public void msgGenerate() {
        MsgProtos.Msg message = buildMsg();
        assert message.getSeq().equals("1");
    }


    @Test
    public void serializationAndDeserialization() throws IOException {
        MsgProtos.Msg message = buildMsg();
        byte[] bytes = message.toByteArray();
        System.out.printf("message bytes length: " + bytes.length);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(bytes);
        bytes = byteArrayOutputStream.toByteArray();
        MsgProtos.Msg msg = MsgProtos.Msg.parseFrom(bytes);
        assert msg.getSeq().equals("1");
    }


    @Test
    public void serializationAndDeserialization1() throws IOException {
        MsgProtos.Msg message = buildMsg();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeDelimitedTo(outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        MsgProtos.Msg msg = MsgProtos.Msg.parseDelimitedFrom(inputStream);
        assert msg.getSeq().equals("1");

    }


    public MsgProtos.Msg buildMsg() {
        MsgProtos.Msg.Builder builder = MsgProtos.Msg.newBuilder();
        builder.setSeq("1");
        return builder.build();
    }


}
