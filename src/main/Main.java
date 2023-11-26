package main;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        Window window = new Window();
        Game game = new Game(window);
        Thread t1 = new Thread(game);
        t1.start();
    }
}