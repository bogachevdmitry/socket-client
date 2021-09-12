package com.bogachevdmitry.service;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClientService extends Thread {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public SocketClientService(InetAddress ip, int port) {
        this.socket = createNewSocket(ip, port);
        if (!isSocketCreated(socket, ip, port)) {
            System.out.println("Couldn't connect to the server after 5 attempts. Connection denied.");
        }

        System.out.println("Successfully connected to the socket");
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type something and press `Enter` to send it to the server.\n");

        String message;
        try {
            while (true) {
                message = keyboard.readLine();
                System.out.println("Sending your input to the server...");
                out.writeUTF(message);
                out.flush();

                if (message.equalsIgnoreCase("q:")) {
                    this.closeSocketServer();
                    System.out.println("Server shut down.");
                    break;
                }

                message = in.readUTF();
                System.out.printf("The server return the result `%s`\n", message);
                System.out.println("You can continue your conversation with the server.\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeSocketServer() throws IOException {
        if (!socket.isClosed()) {
            System.out.println("Server is shutting down...");
            socket.close();
            in.close();
            out.close();
        }
    }

    private boolean isSocketCreated(Socket socket, InetAddress ip, int port) {
        this.socket = socket;
        int tryCount = 0;
        System.out.printf("Connecting to the socket with ip %s and port %s...\n", ip, port);

        while(this.socket == null) {
            this.socket = createNewSocket(ip, port);

            if (this.socket == null && tryCount == 5) {
                return false;
            }

            if (this.socket == null) {
                System.out.println("Couldn't connect to the server. Trying again...");
                tryCount++;
            }
        }

        return true;
    }

    private Socket createNewSocket(InetAddress ip, int port) {
        Socket socket;
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            return null;
        }

        return socket;
    }
}
