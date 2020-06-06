package com.example.client;

import com.example.client.util.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpUtilsTest {


    @Test
    public void testGet() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "adfd");
        map.put("b", "2134");
        HttpUtil.get("http://abc", map, null);

    }

}
