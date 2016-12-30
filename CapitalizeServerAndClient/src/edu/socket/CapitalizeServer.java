package edu.socket;

import java.io.IOException;
import java.net.ServerSocket;

public class CapitalizeServer {

    public static void main(String[] args) throws IOException {
        System.out.println("The capitalization server is running");
        int clientNumber = 0;
        try (ServerSocket listener = new ServerSocket(9090)) {
            while (true) {
                new Capitalizer(listener.accept(), clientNumber++).start();
            }
        }
    }
}
