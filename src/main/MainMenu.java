package main;

import main.constants.Settings;
import main.helper.Connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainMenu extends JFrame implements ActionListener {

    private final JLabel mainMenuLabel = new JLabel("MAIN MENU");
    private final JLabel usernameLabel = new JLabel("Username:");
    private final JLabel passwordLabel = new JLabel("Password:");
    private final JButton playButton = new JButton();
    private final JButton multiplayerButton = new JButton();
    private final JButton exitButton = new JButton();
    private final JButton loginButton = new JButton();
    private final JButton registerButton = new JButton();
    private final JButton backButtonMP = new JButton();
    private final JButton backButtonLogin = new JButton();
    private final JButton continueButton = new JButton();
    private final JButton acceptButton = new JButton();
    private final JButton findLobbyButton = new JButton();
    private final JButton logoutButton = new JButton();
    private final JButton leaderboardButton = new JButton();
    private JTextField usernameField;
    private JPasswordField passwordField;
    private ImageIcon background;
    private PlayButtonListener playButtonListener;
    private final int x = 350;
    private final int y = 150;
    private final int width = 280;
    private final int height = 60;
    private final List<Character> usernameList = new ArrayList<>();
    private final List<Character> passwordList = new ArrayList<>();
    private String username;
    private String password;
    private Connection connection;

    public MainMenu() {
        init();
    }

    // Initializes everything
    public void init(){
        initButtons();
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
    // Initializes the buttons
    public void initButtons() {
        setupButton(playButton, x, y, width, height, "Play");
        setupButton(multiplayerButton, x, y + 75, width, height, "Multiplayer");
        setupButton(exitButton, x, y + 150, width, height, "Exit");

        setupButton(loginButton, x, y, width, height, "Login");
        setupButton(registerButton, x, y + 75, width, height, "Register");
        setupButton(backButtonMP, x, y + 150, width, height, "Back");

        setupButton(continueButton, x, y + 150, width, height, "Continue");
        setupButton(acceptButton, x, y + 150, width, height, "Accept");
        setupButton(backButtonLogin, x, y + 225, width, height, "Back");

        setupButton(findLobbyButton, x, y, width, height, "Find Lobby");
        setupButton(logoutButton, x, y + 75, width, height, "Logout");
        setupButton(leaderboardButton, x, y + 150, width, height, "Leaderboard");
    }

    // Adds the button to the background image
    public void addButtonToBackground(JLabel label, JButton button) {
        label.add(button);
    }

    // Toggles the visibility of the Button
    public void setVisibilityOfButton(JButton button, boolean isVisible) {
        button.setVisible(isVisible);
    }

    // Sets the text of the background label
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

        addAllButtonsToBackground();
        setVisibilityOfMultipleButtons();
        addUsernameAndPasswordFields();
        addLabels();
    }
    private void addAllButtonsToBackground() {
        addButtonToBackground(mainMenuLabel, playButton);
        addButtonToBackground(mainMenuLabel, multiplayerButton);
        addButtonToBackground(mainMenuLabel, exitButton);

        addButtonToBackground(mainMenuLabel, loginButton);
        addButtonToBackground(mainMenuLabel, registerButton);
        addButtonToBackground(mainMenuLabel, backButtonMP);

        addButtonToBackground(mainMenuLabel, backButtonLogin);
        addButtonToBackground(mainMenuLabel, continueButton);
        addButtonToBackground(mainMenuLabel, acceptButton);

        addButtonToBackground(mainMenuLabel, findLobbyButton);
        addButtonToBackground(mainMenuLabel, logoutButton);
        addButtonToBackground(mainMenuLabel, leaderboardButton);
    }
    public void setVisibilityOfMultipleButtons() {
        setVisibilityOfButton(loginButton, false);
        setVisibilityOfButton(registerButton, false);
        setVisibilityOfButton(backButtonMP, false);

        setVisibilityOfButton(backButtonLogin, false);
        setVisibilityOfButton(continueButton, false);
        setVisibilityOfButton(acceptButton, false);

        setVisibilityOfButton(findLobbyButton, false);
        setVisibilityOfButton(logoutButton, false);
        setVisibilityOfButton(leaderboardButton, false);
    }

    public void addUsernameAndPasswordFields() {
        usernameField = createUserField(12);
        usernameField.setBounds(x + 125, y, width - 80, height);
        mainMenuLabel.add(usernameField);
        usernameField.setVisible(false);

        passwordField = createPasswordField(12);
        passwordField.setBounds(x + 125, y + 75, width - 80, height);
        mainMenuLabel.add(passwordField);
        passwordField.setVisible(false);
    }

    public void addLabels() {
        mainMenuLabel.add(usernameLabel);
        usernameLabel.setFont(new Font("Calibri", Font.BOLD, 40));
        usernameLabel.setForeground(Color.RED);
        usernameLabel.setBounds(x - 75, y, width, height);
        usernameLabel.setVisible(false);

        mainMenuLabel.add(passwordLabel);
        passwordLabel.setFont(new Font("Calibri", Font.BOLD, 40));
        passwordLabel.setForeground(Color.RED);
        passwordLabel.setBounds(x - 75, y + 75, width, height);
        passwordLabel.setVisible(false);
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

    // Creates a username and checks the maximum length of the username
    public JTextField createUserField(int maxLength) {
        JTextField usernameField = new JTextField();
        PlainDocument document = new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                // Checks if the maximum length is exceeded
                if ((getLength() + str.length()) <= maxLength) {
                    super.insertString(offs, str, a);
                    for (char c : str.toCharArray()) {
                        usernameList.add(c);
                    }
                }
            }
        };
        // Assign document to the username field
        usernameField.setDocument(document);
        Font font = new Font("Calibri", Font.BOLD, 30);
        usernameField.setFont(font);

        return usernameField;
    }

    // Creates a password and checks the maximum length of the password
    public JPasswordField createPasswordField(int maxLength) {
        JPasswordField passwordField = new JPasswordField();
        PlainDocument document = new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                // Checks if the maximum length is exceeded
                if ((getLength() + str.length()) <= maxLength) {
                    super.insertString(offs, str, a);
                    for (char c : str.toCharArray()) {
                        passwordList.add(c);
                    }
                }
            }
        };
        // Assign document to the password field
        passwordField.setDocument(document);
        Font font = new Font("Calibri", Font.BOLD, 30);
        passwordField.setFont(font);

        return passwordField;
    }

    // Extracts the content of the ArrayList into a String
    public String listToString(List<Character> charList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : charList) {
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    // Prints the username and password in the console
    public void login(String username, String password) {
        System.out.println("Benutzername: " + username);
        System.out.println("Passwort: " + password);
    }

    // Helper method for the listener
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
        }
        else if (e.getSource() == multiplayerButton) {
            setVisibilityOfButton(playButton,false);
            setVisibilityOfButton(multiplayerButton,false);
            setVisibilityOfButton(exitButton,false);

            setVisibilityOfButton(loginButton,true);
            setVisibilityOfButton(registerButton,true);
            setVisibilityOfButton(backButtonMP,true);

            setTextOfLabel(mainMenuLabel, "MULTIPLAYER");
        }
        else if (e.getSource() == exitButton) {
            System.exit(0);
        }
        else if (e.getSource() == backButtonMP) {
            setVisibilityOfButton(loginButton,false);
            setVisibilityOfButton(registerButton,false);
            setVisibilityOfButton(backButtonMP,false);

            setVisibilityOfButton(playButton,true);
            setVisibilityOfButton(multiplayerButton,true);
            setVisibilityOfButton(exitButton,true);

            setTextOfLabel(mainMenuLabel, "MAIN MENU");
        }
        else if (e.getSource() == loginButton) {
            setVisibilityOfButton(loginButton,false);
            setVisibilityOfButton(registerButton,false);
            setVisibilityOfButton(backButtonMP,false);

            setVisibilityOfButton(continueButton,true);
            setVisibilityOfButton(backButtonLogin,true);

            usernameField.setVisible(true);
            usernameLabel.setVisible(true);

            passwordField.setVisible(true);
            passwordLabel.setVisible(true);

            setTextOfLabel(mainMenuLabel, "LOGIN");
        }
        else if (e.getSource() == registerButton) {
            setVisibilityOfButton(loginButton,false);
            setVisibilityOfButton(registerButton,false);
            setVisibilityOfButton(backButtonMP,false);

            setVisibilityOfButton(acceptButton,true);
            setVisibilityOfButton(backButtonLogin,true);

            usernameField.setVisible(true);
            usernameLabel.setVisible(true);

            passwordField.setVisible(true);
            passwordLabel.setVisible(true);

            setTextOfLabel(mainMenuLabel, "REGISTER");
        }
        else if (e.getSource() == acceptButton) {
            // TODO display message on GUI for empty username and password
            // TODO display message on GUI for successful registration
            if (usernameList.isEmpty() || passwordList.isEmpty()) {
                System.out.println("Both the username and the password have to be filled out!");
            } else {
                username = listToString(usernameList);
                password = listToString(passwordList);
            }
        }
        else if (e.getSource() == continueButton) {
            // TODO display message on GUI for successful or unsuccessful login
            if (usernameList.isEmpty() || passwordList.isEmpty()) {
                System.out.println("Both the username and the password have to be filled out!");
            } else {
                username = listToString(usernameList);
                password = listToString(passwordList);
                //connection.login(username, password);
                //login(username, password);

                setVisibilityOfButton(continueButton, false);
                setVisibilityOfButton(backButtonLogin, false);

                setVisibilityOfButton(findLobbyButton, true);
                setVisibilityOfButton(logoutButton, true);
                setVisibilityOfButton(leaderboardButton, true);

                usernameLabel.setVisible(false);
                usernameField.setVisible(false);
                usernameField.setText("");

                passwordLabel.setVisible(false);
                passwordField.setVisible(false);
                passwordField.setText("");

                setTextOfLabel(mainMenuLabel, "LOBBY");
            }
        }
        else if (e.getSource() == backButtonLogin) {
            setVisibilityOfButton(continueButton, false);
            setVisibilityOfButton(acceptButton, false);
            setVisibilityOfButton(backButtonLogin, false);

            setVisibilityOfButton(loginButton, true);
            setVisibilityOfButton(registerButton, true);
            setVisibilityOfButton(backButtonMP, true);

            usernameLabel.setVisible(false);
            usernameField.setVisible(false);
            usernameField.setText("");

            passwordLabel.setVisible(false);
            passwordField.setVisible(false);
            passwordField.setText("");

            // Empties the lists after pressing the back button
            usernameList.clear();
            passwordList.clear();

            setTextOfLabel(mainMenuLabel, "MULTIPLAYER");
        }
        else if (e.getSource() == findLobbyButton) {
            // TODO implement a waiting area after pressing 'Find Lobby'
            // TODO start game after successfully finding another player
            // TODO implement a 'Leave Lobby' Button
            // TODO display corresponding messages for the events on GUI
            //connection.findLobby();
        }
        else if (e.getSource() == logoutButton) {
            usernameField.setVisible(true);
            usernameLabel.setVisible(true);
            usernameField.setText("");
            usernameList.clear();

            passwordField.setVisible(true);
            passwordLabel.setVisible(true);
            passwordField.setText("");
            passwordList.clear();

            setVisibilityOfButton(findLobbyButton, false);
            setVisibilityOfButton(logoutButton, false);
            setVisibilityOfButton(leaderboardButton, false);

            setVisibilityOfButton(continueButton, true);
            setVisibilityOfButton(backButtonLogin, true);
        }
        else if (e.getSource() == leaderboardButton) {
            // TODO implement a display of a leaderboard list
        }
    }
}
