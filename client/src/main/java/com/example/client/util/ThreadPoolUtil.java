package com.example.client.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {


    public static ExecutorService MSG_POOL = Executors.newFixedThreadPool(8);


}
