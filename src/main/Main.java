package main;

import main.constants.GameMode;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {

        MainMenu menu = new MainMenu();

        PlayButtonListener playButtonListener = new PlayButtonListener() {
            @Override
            public void playButtonClicked() {
                // Gets executed after the "PLAY"-Button is pressed
                Window window = new Window();
                Game game = new Game(window, menu.getConnection(), GameMode.MARIO);
                Thread t1 = new Thread(game);
                t1.start();

            }
        };
        menu.setPlayButtonListener(playButtonListener);
    }
}