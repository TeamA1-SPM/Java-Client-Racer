package main;

import main.constants.Settings;

import javax.swing.*;

public class Window extends JFrame{


    private Game racingGame;

    public Window(){
        init();
        racingGame = new Game(this);

    }

    private void init(){
        this.setSize(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        this.setTitle(Settings.SCREEN_TITLE);
        this.setResizable(Settings.SCREEN_RESIZABLE);
        this.setVisible(Settings.SCREEN_VISIBLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
