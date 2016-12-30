package edu.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private static final Set<String> NAMES = new HashSet<>();
    private static final Set<PrintWriter> WRITERS = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running");

        try (ServerSocket listener = new ServerSocket(9090)) {
            while (true) {
                new Handler(listener.accept()).start();
            }
        }
    }

    static class Handler extends Thread {

        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());

                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (NAMES) {
                        if (!NAMES.contains(name)) {
                            NAMES.add(name);
                            break;
                        }
                    }
                }

                out.println("NAMEACCEPTED");
                WRITERS.add(out);

                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    WRITERS.forEach(writer -> writer.println("MESSAGE " + name + ": " + input));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (name != null) {
                    NAMES.remove(name);
                }
                if (out != null) {
                    WRITERS.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
