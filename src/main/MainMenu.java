package main;

import main.constants.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class MainMenu extends JFrame {

    private Image lobby;
    private final JLabel label = new JLabel();

    public MainMenu() {
        init();
    }
    private void init(){
        this.setSize(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        this.setTitle(Settings.SCREEN_TITLE);
        this.setResizable(Settings.SCREEN_RESIZABLE);
        this.setVisible(Settings.SCREEN_VISIBLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        File f = new File("./src/main/images/menu/LobbyBackground.png");
        System.out.println(f.exists());

        String lobbyBackgroundPath = f.getPath();
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull("./src/main/images/menu/LobbyBackground.png"));
        label.setIcon(imageIcon);
        getContentPane().add(label, BorderLayout.CENTER);
    }

}
