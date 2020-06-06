package com.example.client.client.reponse;

import com.example.protocol.MsgProtos;
import io.netty.channel.Channel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResponseFuture {
    private final String seq;
    private final Channel processChannel;
    private final CallBack callBack;
    private volatile Throwable cause;
    private volatile AtomicBoolean alreadyCallback = new AtomicBoolean(false);
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private volatile MsgProtos.Msg result;

    public ResponseFuture(Channel channel, String seq, CallBack callBack) {
        this.processChannel = channel;
        this.seq = seq;
        this.callBack = callBack;
    }


    public MsgProtos.Msg waitResponse() throws InterruptedException {
        this.countDownLatch.await();
        return result;
    }

    public void setResult(MsgProtos.Msg result) {
        this.result = result;
        this.countDownLatch.countDown();
    }

    public void executeCallback() {
        if (callBack != null) {
            if (alreadyCallback.compareAndSet(false, true)) {
                callBack.operationComplete(this);
            }
        }
    }


    public String getSeq() {
        return seq;
    }

    public Channel getProcessChannel() {
        return processChannel;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public AtomicBoolean getAlreadyCallback() {
        return alreadyCallback;
    }

    public void setAlreadyCallback(AtomicBoolean alreadyCallback) {
        this.alreadyCallback = alreadyCallback;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public MsgProtos.Msg getResult() {
        return result;
    }
}
