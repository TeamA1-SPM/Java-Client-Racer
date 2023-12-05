package main;

import main.constants.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MainMenu extends JFrame implements ActionListener {

    private final JLabel mainMenuLabel = new JLabel("MAIN MENU");
    private final JLabel multiplayerLabel = new JLabel("MULTIPLAYER");
    private final JButton playButton = new JButton();
    private final JButton multiplayerButton = new JButton();
    private final JButton exitButton = new JButton();
    private final JButton loginButton = new JButton();
    private final JButton registerButton = new JButton();
    private final JButton backButton = new JButton();
    private ImageIcon background;
    private PlayButtonListener playButtonListener;
    private final int xBTN = 350;
    private final int yBTN = 150;
    private final int widthBTN = 280;
    private final int heightBTN = 60;

    public MainMenu() {
        init();
    }

    public void init(){
        setupButton(playButton, xBTN, yBTN, widthBTN, heightBTN, "Play");
        setupButton(multiplayerButton, xBTN, yBTN + 75, widthBTN, heightBTN, "Multiplayer");
        setupButton(exitButton, xBTN, yBTN + 150, widthBTN, heightBTN, "Exit");

        setupButton(loginButton, xBTN, yBTN, widthBTN, heightBTN, "Login");
        setupButton(registerButton, xBTN, yBTN + 75, widthBTN, heightBTN, "Register");
        setupButton(backButton, xBTN, yBTN + 150, widthBTN, heightBTN, "Back");

        setupBackground();
        setupMainMenu();
    }
    // Sets up the button
    public void setupButton(JButton button, int x, int y, int width, int height, String text) {
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Calibri", Font.BOLD, 40));
        button.setText(text);
        button.addActionListener(this);
    }

    public void addButtonToBackground(JLabel label, JButton button) {
        label.add(button);
    }

    public void setVisibilityOfButton(JButton button, boolean isVisible) {
        button.setVisible(isVisible);
    }

    public void setTextOfLabel(JLabel label, String text) {
        label.setText(text);
    }


    // Sets up the background image and corresponding text
    public void setupBackground() {
        background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/main/images/menu/LobbyBackground.png")));
        mainMenuLabel.setFont(new Font("Calibri", Font.BOLD, 65));
        mainMenuLabel.setHorizontalTextPosition(JLabel.CENTER);
        mainMenuLabel.setVerticalTextPosition(JLabel.TOP);
        mainMenuLabel.setIconTextGap(-150);
        mainMenuLabel.setIcon(background);

        // Adds the buttons to the background image
        addButtonToBackground(mainMenuLabel, playButton);
        addButtonToBackground(mainMenuLabel, multiplayerButton);
        addButtonToBackground(mainMenuLabel, exitButton);

        addButtonToBackground(mainMenuLabel, loginButton);
        addButtonToBackground(mainMenuLabel, registerButton);
        addButtonToBackground(mainMenuLabel, backButton);

        setVisibilityOfButton(loginButton,false);
        setVisibilityOfButton(registerButton,false);
        setVisibilityOfButton(backButton,false);

        mainMenuLabel.add(multiplayerLabel);
    }

    // Sets up the main menu
    public void setupMainMenu() {
        this.setSize(background.getIconWidth(), background.getIconHeight());
        this.setTitle(Settings.SCREEN_TITLE);
        this.setResizable(Settings.SCREEN_RESIZABLE);
        this.setVisible(Settings.SCREEN_VISIBLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(mainMenuLabel);
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
            setVisibilityOfButton(playButton,false);
            setVisibilityOfButton(multiplayerButton,false);
            setVisibilityOfButton(exitButton,false);

            setVisibilityOfButton(loginButton,true);
            setVisibilityOfButton(registerButton,true);
            setVisibilityOfButton(backButton,true);

            setTextOfLabel(mainMenuLabel, "MULTIPLAYER");
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        } else if (e.getSource() == backButton) {
            setVisibilityOfButton(loginButton,false);
            setVisibilityOfButton(registerButton,false);
            setVisibilityOfButton(backButton,false);

            setVisibilityOfButton(playButton,true);
            setVisibilityOfButton(multiplayerButton,true);
            setVisibilityOfButton(exitButton,true);

            setTextOfLabel(mainMenuLabel, "MAIN MENU");
        }
    }
}
