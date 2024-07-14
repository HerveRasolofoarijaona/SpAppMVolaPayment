package com.payement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IPController {
    @Autowired
    private IPAddressService ipAddressService;

    @GetMapping("/server-ip")
    public String getServerIPAddress() {
        return "Server IP Address: " + ipAddressService.getServerIPAddress();
    }
}
