package com.example.common.remote;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class ZkClient {


    private static String DEFAULT_SERVER_ADDR = "localhost:2181";

    private static ConcurrentHashMap<String/**ip,port*/, ServerInstance> serverTables = new ConcurrentHashMap<>();

    private static final String IM_SERVER_ROOT_PATH = "/chatServer";

    private static final RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

    private CuratorFramework client;
    private TreeCache treeCache;


    private String ServerAddr;


    public ZkClient() {
        this(DEFAULT_SERVER_ADDR);
    }

    public ZkClient(String serverAddr) {
        this.ServerAddr = serverAddr;
    }

    public void init() {
        synchronized (ZkClient.class) {
            try {
                client = CuratorFrameworkFactory.newClient(ServerAddr, retryPolicy);
                client.start();
                treeCache = new TreeCache(client, IM_SERVER_ROOT_PATH);
                treeCache.getListenable().addListener((client, event) -> {
                    byte[] data = event.getData().getData();
                    ServerInstance instance = JSONObject.parseObject(data, ServerInstance.class);
                    String key = instance.getIp() + "," + instance.getPort();
                    switch (event.getType()) {
                        case NODE_ADDED:
                        case NODE_UPDATED:
                            serverTables.putIfAbsent(key, instance);
                            break;
                        case NODE_REMOVED:
                            serverTables.remove(key);
                        default:
                            break;

                    }
                    log.info("curator tree cache node change: {}-{}", event.getType(), event.getData().getPath());
                });
                treeCache.start();
            } catch (Exception e) {
                throw new RuntimeException("zk client init err: ", e);
            }
            log.info(" client init success!");
        }
    }

    public static ConcurrentHashMap<String, ServerInstance> getServerInstances() {
        return serverTables;
    }

    public String createTempSeqPath(String value) {

        try {
            String s = client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(IM_SERVER_ROOT_PATH + "/");
            client.setData().forPath(s, value.getBytes());
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public String getData(String path) {
        try {
            byte[] bytes = client.getData().forPath(path);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public List<String> getChildren() {
        try {
            List<String> strings = client.getChildren().forPath(IM_SERVER_ROOT_PATH);
            return strings;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getChildren(String path) {
        try {
            List<String> strings = client.getChildren().forPath(path);
            return strings;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
