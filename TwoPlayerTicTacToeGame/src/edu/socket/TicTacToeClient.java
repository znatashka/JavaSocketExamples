package edu.socket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.stream.IntStream;

public class TicTacToeClient {
    private static final String HOST = "localhost";
    private static final int PORT = 9090;

    private JFrame frame = new JFrame("Tic Toc Toe");
    private JLabel messageLabel = new JLabel("");
    private ImageIcon icon;
    private ImageIcon opponentIcon;

    private Square[] board = new Square[9];
    private Square currentSquare;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public static void main(String[] args) throws Exception {
        while (true) {
            TicTacToeClient client = new TicTacToeClient();
            client.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            client.frame.setSize(240, 160);
            client.frame.setVisible(true);
            client.frame.setResizable(false);
            client.play();
            if (!client.wantsToPlayAgain()) {
                break;
            }
        }
    }

    public TicTacToeClient() throws Exception {
        socket = new Socket(HOST, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        messageLabel.setBackground(Color.lightGray);
        frame.getContentPane().add(messageLabel, "South");

        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.black);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
        IntStream.range(0, board.length).forEach(i -> {
            int j = i;
            board[i] = new Square();
            board[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    currentSquare = board[j];
                    out.println("MOVE " + j);
                }
            });
            boardPanel.add(board[i]);
        });
        frame.getContentPane().add(boardPanel, "Center");
    }

    public void play() throws Exception {
        String response;
        try {
            response = in.readLine();
            if (response.startsWith("WELCOME")) {
                char mark = response.charAt(8);
                icon = new ImageIcon(mark == 'X' ? "x.gif" : "o.gif");
                opponentIcon = new ImageIcon(mark == 'X' ? "x.gif" : "o.gif");
                frame.setTitle("Tic Tac Toe - Player " + mark);
            }

            while (true) {
                response = in.readLine();
                if (response.startsWith("VALID_MOVE")) {
                    messageLabel.setText("Valid move, please wait");
                    currentSquare.setIcon(icon);
                    currentSquare.repaint();
                } else if (response.startsWith("OPPONENT_MOVED")) {
                    int loc = Integer.parseInt(response.substring(15));
                    board[loc].setIcon(opponentIcon);
                    board[loc].repaint();
                    messageLabel.setText("Opponent moved, your turn");
                } else if (response.startsWith("VICTORY")) {
                    messageLabel.setText("You win!");
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    messageLabel.setText("You lose");
                    break;
                } else if (response.startsWith("TIE")) {
                    messageLabel.setText("You tied");
                    break;
                } else if (response.startsWith("MESSAGE")) {
                    messageLabel.setText(response.substring(8));
                }
            }
            out.println("QUIT");
        } finally {
            socket.close();
        }
    }

    private boolean wantsToPlayAgain() {
        int response = JOptionPane.showConfirmDialog(
                frame,
                "Want to play again?",
                "Tic Tac Toe is Fun Fun Fun",
                JOptionPane.YES_NO_OPTION
        );
        frame.dispose();
        return response == JOptionPane.YES_OPTION;
    }
}
