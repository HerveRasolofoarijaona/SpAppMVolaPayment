package com.payement.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPAddressService {
    public String getServerIPAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown IP Address";
        }
    }
}
