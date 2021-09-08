package com.bogachevdmitry;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
    public static void main(String[] args) {
        final String LOCALHOST = "localhost";
        final int PORT = 8081;

        InetAddress ip = null;
        try {
            ip = InetAddress.getByName(LOCALHOST);
        } catch (UnknownHostException hostException) {
            System.out.printf("Host is unknown: %s:%s", LOCALHOST, PORT);
            hostException.printStackTrace();
        }

        Socket socket = null;
        int tryCount = 0;
        System.out.printf("Connecting to the socket with ip %s and port %s...\n", ip, PORT);
        while(socket == null) {
            socket = createNewSocket(ip, PORT);

            if (socket == null && tryCount == 5) {
                System.out.println("Couldn't connect to the server after 5 attempts. Connection denied.");
                return;
            }

            if (socket == null) {
                System.out.println("Couldn't connect to the server. Trying again...");
                tryCount++;
            }
        }

        System.out.println("Successfully connected to the socket");
        try (DataInputStream in = new DataInputStream (socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Type something and press `Enter` to send it to the server.\n");

            String message;
            while (true) {
                message = keyboard.readLine();
                System.out.println("Sending your input to the server...");
                out.writeUTF(message);
                out.flush();
                message = in.readUTF();
                System.out.printf("The server return the result `%s`\n", message);
                System.out.println("You can continue your conversation with the server.\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Socket createNewSocket(InetAddress ip, int port) {
        Socket socket;
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            return null;
        }

        return socket;
    }
}
