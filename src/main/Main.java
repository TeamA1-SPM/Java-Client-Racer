package main;

import main.helper.Connection;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {

        Connection connection = new Connection();
        connection.connect();

        MainMenu menu = new MainMenu();
        //Window window = new Window();
        //Game game = new Game(window, connection);
        //Thread t1 = new Thread(game);
        //t1.start();
    }
}