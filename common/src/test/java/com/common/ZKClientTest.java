package com.common;

import com.alibaba.fastjson.JSONObject;
import com.example.common.remote.ServerInstance;
import com.example.common.remote.ZkClient;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ZKClientTest {
    ZkClient zkClient;

    @Before
    public void before() {
        zkClient = new ZkClient();
        zkClient.init();
    }

    public void test(String ip, int port) throws Exception {

        ServerInstance serverInstance = new ServerInstance();
        serverInstance.setIp(ip);
        serverInstance.setPort(port);
        String tempSeqPath = zkClient.createTempSeqPath(JSONObject.toJSONString(serverInstance));
        System.out.println("create: " + tempSeqPath);
    }


    @Test
    public void testServerInstance() throws Exception {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("------ 服务节点数：" + ZkClient.getServerInstances());
            }
        }, 1, 5, TimeUnit.SECONDS);
        for (int i = 0; i < 100; i++) {
            test("121.1.1." + i, new Random().nextInt(10000));
            TimeUnit.SECONDS.sleep(10);
        }

    }

}
