package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import engine.*;

public class ConsoleChess2 {

    public static void main(String[] args) throws Exception {

        new ChessGame(
                new HumanPlayer("Graeme", 0),
                new HumanPlayer("GMac", 1)
        ).play();
    }
}
