package main;

import main.constants.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MainMenu extends JFrame implements ActionListener {

    private final JLabel labelBackground = new JLabel();
    private final JButton playButton = new JButton("PLAY");
    private final JButton multiplayerButton = new JButton("MULTIPLAYER");
    private final JButton exitButton = new JButton("EXIT");
    private ImageIcon background;
    private PlayButtonListener playButtonListener;

    public MainMenu() {
        init();
    }
    public void init(){
        setupButtons();
        setupBackground();
        setupMainMenu();
    }
    // Sets up the buttons
    public void setupButtons() {
        // Sets up the play button
        playButton.setBounds(350,150,280,60);
        playButton.setFont(new Font("Calibri", Font.BOLD, 40));
        playButton.addActionListener(this);

        // Sets up the multiplayer button
        multiplayerButton.setBounds(350,225,280,60);
        multiplayerButton.setFont(new Font("Calibri", Font.BOLD, 40));
        multiplayerButton.addActionListener(this);

        // Sets up the exit button
        exitButton.setBounds(350,300,280,60);
        exitButton.setFont(new Font("Calibri", Font.BOLD, 40));
        exitButton.addActionListener(this);
    }

    // Sets up the background image and corresponding text
    public void setupBackground() {
        background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/main/images/menu/LobbyBackground.png")));
        labelBackground.setFont(new Font("Calibri", Font.BOLD, 65));
        labelBackground.setText("MAIN MENU");
        labelBackground.setHorizontalTextPosition(JLabel.CENTER);
        labelBackground.setVerticalTextPosition(JLabel.TOP);
        labelBackground.setIconTextGap(-150);
        labelBackground.setIcon(background);

        // Adds the buttons to the background image
        labelBackground.add(playButton);
        labelBackground.add(multiplayerButton);
        labelBackground.add(exitButton);
    }
    // Sets up the main menu
    public void setupMainMenu() {
        this.setSize(background.getIconWidth(), background.getIconHeight());
        this.setTitle(Settings.SCREEN_TITLE);
        this.setResizable(Settings.SCREEN_RESIZABLE);
        this.setVisible(Settings.SCREEN_VISIBLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(labelBackground);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void setPlayButtonListener(PlayButtonListener listener) {
        this.playButtonListener = listener;
    }

    // Controls the actions of pressed buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            if (playButtonListener != null) {
                playButtonListener.playButtonClicked();
            }
            dispose();
        } else if (e.getSource() == multiplayerButton) {

        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }
}
