package com.bogachevdmitry;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
    public static void main(String[] args) {
        final String LOCALHOST = "localhost";
        final int PORT = 8081;

        try {
            InetAddress ip = InetAddress.getByName(LOCALHOST);
            try (Socket socket = new Socket(ip, PORT)) {
                System.out.printf("Connected to the socket with ip %s and port %s...\n", ip, PORT);
                if (socket.isConnected()) {
                    System.out.println("Successfully connected to the socket");
                }

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
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
