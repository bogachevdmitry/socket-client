package com.bogachevdmitry;

import com.bogachevdmitry.service.SocketClientService;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketClient {

    public static final String LOCALHOST = "localhost";
    public static final int PORT = 8081;

    public static void main(String[] args) {
        try {
            InetAddress ip = InetAddress.getByName(LOCALHOST);
            SocketClientService clientService = new SocketClientService(ip, PORT);
            clientService.start();
        } catch (UnknownHostException hostException) {
            System.out.printf("Host is unknown: %s:%s", LOCALHOST, PORT);
            hostException.printStackTrace();
        }
    }
}
