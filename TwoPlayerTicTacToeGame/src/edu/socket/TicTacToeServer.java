package edu.socket;

import java.io.IOException;
import java.net.ServerSocket;

public class TicTacToeServer {

    public static void main(String[] args) throws IOException {
        try (ServerSocket listener = new ServerSocket(9090)) {
            System.out.println("Tic Tac Toe Server is Running");

            while (true) {
                Game game = new Game();
                Game.Player playerX = game.new Player(listener.accept(), 'X');
                Game.Player playerO = game.new Player(listener.accept(), 'O');

                playerX.opponent = playerO;
                playerO.opponent = playerX;

                game.currentPlayer = playerX;

                playerX.start();
                playerO.start();
            }
        }
    }
}
