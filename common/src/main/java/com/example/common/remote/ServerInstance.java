package com.example.common.remote;

import lombok.Data;

import java.util.Objects;

@Data
public class ServerInstance {

    private String ip;
    private int port;

    private int weight;//todo server端参数

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerInstance that = (ServerInstance) o;
        return port == that.port &&
                Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
