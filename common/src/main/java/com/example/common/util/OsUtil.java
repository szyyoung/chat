package com.example.common.util;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class OsUtil {

    private static volatile int PROCESS_NO = 0;

    private static volatile List<String> IPV4_LIST;


    public static int getProcessNo(){
        if (PROCESS_NO == 0) {
            try {
                PROCESS_NO = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
            } catch (Exception e) {
                PROCESS_NO = -1;
            }
        }
        return PROCESS_NO;
    }


    public static List<String> getAllIPV4() {
        if (IPV4_LIST == null) {
            IPV4_LIST = new LinkedList<String>();
            try {
                Enumeration<NetworkInterface> interfs = NetworkInterface.getNetworkInterfaces();
                while (interfs.hasMoreElements()) {
                    NetworkInterface networkInterface = interfs.nextElement();
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress address = inetAddresses.nextElement();
                        if (address instanceof Inet4Address) {
                            String addressStr = address.getHostAddress();
                            if ("127.0.0.1".equals(addressStr)) {
                                continue;
                            }
                            IPV4_LIST.add(addressStr);
                        }
                    }
                }
            } catch (SocketException e) {

            }
        }
        return IPV4_LIST;
    }
}
