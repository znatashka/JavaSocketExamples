package edu.socket;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.stream.IntStream;

import static edu.socket.CapitalizeClient.Executor.exec;

public class CapitalizeClient {

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Capitalize Client");
    private JTextField dataField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 60);

    public static void main(String[] args) throws IOException {
        CapitalizeClient client = new CapitalizeClient();
        client.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        client.frame.pack();
        client.frame.setVisible(true);
        client.connectToServer();
    }

    private CapitalizeClient() {
        messageArea.setEnabled(false);
        frame.getContentPane().add(dataField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");

        dataField.addActionListener(e -> {
            out.println(dataField.getText());
            String response;
            try {
                response = in.readLine();
                if (response == null || response.equals("")) {
                    System.exit(0);
                }
            } catch (IOException ex) {
                response = "Error: " + ex;
            }
            messageArea.append(response + "\n");
            dataField.selectAll();
        });
    }

    private void connectToServer() throws IOException {
        Socket socket = new Socket("localhost", 9090);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());

        IntStream.range(0, 3).forEach(i -> exec(() -> messageArea.append(in.readLine() + "\n")));
    }

    static class Executor {
        static void exec(Action action) {
            try {
                action.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        interface Action {
            void run() throws Exception;
        }
    }
}
