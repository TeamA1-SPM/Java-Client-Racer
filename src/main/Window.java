package main;

import main.constants.Settings;

import javax.swing.*;

public class Window extends JFrame{

    public Window(){
        init();
    }

    private void init(){
        this.setSize(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        this.setTitle(Settings.SCREEN_TITLE);
        this.setResizable(Settings.SCREEN_RESIZABLE);
        this.setVisible(Settings.SCREEN_VISIBLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
