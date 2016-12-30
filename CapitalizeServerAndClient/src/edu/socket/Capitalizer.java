package edu.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Capitalizer extends Thread {

    private Socket socket;
    private int clientNumber;

    public Capitalizer(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        System.out.println("New connection with client #" + clientNumber + " at " + socket);
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("Hello, you are client #" + clientNumber);
            out.println("Enter a line with only a period to quite\n");

            while (true) {
                String input = in.readLine();
                if (input == null || input.equals(".")) {
                    break;
                }
                out.println(input.toUpperCase());
            }
        } catch (IOException e) {
            System.out.println("Error handling client #" + clientNumber + ": " + e);
        }
    }
}
