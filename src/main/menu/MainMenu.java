package main.menu;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import main.Game;
import main.Window;
import main.constants.GameMode;
import main.constants.Settings;
import main.gamehelper.Connection;
import main.menuhelper.ButtonManager;
import main.menuhelper.VisibilityManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainMenu extends JFrame implements ActionListener {

    private final JLabel mainMenuLabel = new JLabel("MAIN MENU");
    private final JLabel usernameLabel = new JLabel("Username:");
    private final JLabel passwordLabel = new JLabel("Password:");
    private final JLabel waitingLabel = new JLabel("Waiting for another player...");
    private JButton playButton = new JButton();
    private JButton multiplayerButton = new JButton();
    private JButton exitButton = new JButton();
    private JButton loginButton = new JButton();
    private JButton registerButton = new JButton();
    private JButton backButtonMultiplayer = new JButton();
    private JButton backButtonLogin = new JButton();
    private JButton backButtonRegister = new JButton();
    private JButton continueButton = new JButton();
    private JButton acceptButton = new JButton();
    private JButton findLobbyButton = new JButton();
    private JButton logoutButton = new JButton();
    private JButton leaderboardButton = new JButton();
    private JButton leaveLobbyButton = new JButton();
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
    private final Connection connection = new Connection();
    List<JComponent> mainMenuButtons = Arrays.asList(playButton, multiplayerButton, exitButton);
    List<JComponent> multiplayerButtons = Arrays.asList(loginButton, registerButton, backButtonMultiplayer);
    List<JComponent> loginButtons = Arrays.asList(continueButton, backButtonLogin);
    List<JComponent> registerButtons = Arrays.asList(acceptButton, backButtonRegister);
    List<JComponent> preLobbyButtons = Arrays.asList(findLobbyButton, logoutButton, leaderboardButton);
    List<JComponent> lobbyButtons = List.of(leaveLobbyButton);
    List<JComponent> usernameComponents = Arrays.asList(usernameLabel, usernameField);
    List<JComponent> passwordComponents = Arrays.asList(passwordLabel, passwordField);

    public Connection getConnection() {
        return connection;
    }

    public MainMenu() {
        init();
        connection.connect();
        serverFunctions(connection.getSocket());
    }

    // Initializes everything
    public void init(){
        initButtons();
        setupBackground();
        setupMainMenu();
    }

    // Initializes the buttons
    public void initButtons() {
        playButton = ButtonManager.createButton("Play", x, y, width, height, this);
        multiplayerButton = ButtonManager.createButton("Multiplayer", x, y + 75, width, height, this);
        exitButton = ButtonManager.createButton("Exit", x, y + 150, width, height, this);

        loginButton = ButtonManager.createButton("Login", x, y, width, height, this);
        registerButton = ButtonManager.createButton("Register", x, y + 75, width, height, this);
        backButtonMultiplayer = ButtonManager.createButton("Back", x, y + 150, width, height, this);

        continueButton = ButtonManager.createButton("Continue", x, y + 150, width, height, this);
        backButtonLogin = ButtonManager.createButton("Back", x, y + 225, width, height, this);

        acceptButton = ButtonManager.createButton("Accept", x, y + 150, width, height, this);
        backButtonRegister = ButtonManager.createButton("Back", x, y + 225, width, height, this);

        findLobbyButton = ButtonManager.createButton("Find Lobby", x, y, width, height, this);
        logoutButton = ButtonManager.createButton("Logout", x, y + 75, width, height, this);
        leaderboardButton = ButtonManager.createButton("Leaderboard", x, y + 150, width, height, this);

        leaveLobbyButton = ButtonManager.createButton("Leave Lobby", x, y + 75, width, height, this);
    }

    // Sets the text of the background label
    public void setTextOfLabel(JLabel label, String text) {
        label.setText(text);
    }

    // Sets up the background image and corresponding text
    public void setupBackground() {
        background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/main/images/menu/MenuBackground.png")));
        mainMenuLabel.setFont(new Font("Calibri", Font.BOLD, 65));
        mainMenuLabel.setHorizontalTextPosition(JLabel.CENTER);
        mainMenuLabel.setVerticalTextPosition(JLabel.TOP);
        mainMenuLabel.setIconTextGap(-150);
        mainMenuLabel.setIcon(background);

        addAllButtonsToBackground();
        setVisibilityOfAllButtons();
        addUsernameAndPasswordFields();
        addLabels();
    }
    private void addAllButtonsToBackground() {
        List<JButton> allButtons = Arrays.asList(
                playButton, multiplayerButton, exitButton,
                loginButton, registerButton, backButtonMultiplayer,
                continueButton, backButtonLogin,
                acceptButton, backButtonRegister,
                findLobbyButton, logoutButton, leaderboardButton,
                leaveLobbyButton);
        ButtonManager.addButtonsToLabel(mainMenuLabel, allButtons);
    }
    public void setVisibilityOfAllButtons() {
        VisibilityManager.setVisibilityOfComponents(mainMenuButtons, false);
        VisibilityManager.setVisibilityOfComponents(multiplayerButtons, false);
        VisibilityManager.setVisibilityOfComponents(loginButtons, false);
        VisibilityManager.setVisibilityOfComponents(registerButtons, false);
        VisibilityManager.setVisibilityOfComponents(preLobbyButtons, false);
        VisibilityManager.setVisibilityOfComponents(lobbyButtons, false);
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

        mainMenuLabel.add(waitingLabel);
        waitingLabel.setFont(new Font("Calibri", Font.BOLD, 60));
        waitingLabel.setForeground(Color.RED);
        waitingLabel.setBounds(x - 200, y - 20, width + 500, height + 40);
        waitingLabel.setVisible(false);
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
            @Override
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                // Remove characters from the list when the user deletes them
                for (int i = 0; i < len; i++) {
                    usernameList.remove(offs);
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
            @Override
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                // Remove characters from the list when the user deletes them
                for (int i = 0; i < len; i++) {
                    passwordList.remove(offs);
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

    // Helper method for the listener
    public void setPlayButtonListener(PlayButtonListener listener) {
        this.playButtonListener = listener;
    }

    private void serverFunctions(Socket socket){

        socket.on("login_success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                boolean login = (boolean) args[0];
                System.out.println("Login " + args[0]);
                // global oder hier weitere Funktion
            }
        }).on("register_success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                boolean register = (boolean) args[0];
                // global oder hier weitere Funktion
            }
        }).on("start_game", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                connection.ready();
                main.Window window = new Window();
                Game game = new Game(window, connection, GameMode.MULTI_PLAYER);
                Thread t1 = new Thread(game);
                t1.start();
                dispose();
            }
        });
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
            VisibilityManager.setVisibilityOfComponents(mainMenuButtons, false);
            VisibilityManager.setVisibilityOfComponents(multiplayerButtons, true);
            setTextOfLabel(mainMenuLabel, "MULTIPLAYER");
        }
        else if (e.getSource() == exitButton) {
            System.exit(0);
        }
        else if (e.getSource() == backButtonMultiplayer) {
            VisibilityManager.setVisibilityOfComponents(multiplayerButtons, false);
            VisibilityManager.setVisibilityOfComponents(mainMenuButtons, true);
            setTextOfLabel(mainMenuLabel, "MAIN MENU");
        }
        else if (e.getSource() == loginButton) {
            VisibilityManager.setVisibilityOfComponents(multiplayerButtons, false);
            VisibilityManager.setVisibilityOfComponents(loginButtons, true);
            VisibilityManager.setVisibilityOfComponents(usernameComponents, true);
            VisibilityManager.setVisibilityOfComponents(passwordComponents, true);

            setTextOfLabel(mainMenuLabel, "LOGIN");
        }
        else if (e.getSource() == registerButton) {
            VisibilityManager.setVisibilityOfComponents(multiplayerButtons, false);
            VisibilityManager.setVisibilityOfComponents(registerButtons, true);
            VisibilityManager.setVisibilityOfComponents(usernameComponents, true);
            VisibilityManager.setVisibilityOfComponents(passwordComponents, true);

            setTextOfLabel(mainMenuLabel, "REGISTER");
        }
        else if (e.getSource() == continueButton) {
            // TODO display message on GUI for successful or unsuccessful login
            if (usernameList.isEmpty() || passwordList.isEmpty()) {
                System.out.println("Both the username and the password have to be filled out!");
            } else {
                username = listToString(usernameList);
                password = listToString(passwordList);
                connection.login(username, password);

                VisibilityManager.setVisibilityOfComponents(loginButtons, false);
                VisibilityManager.setVisibilityOfComponents(preLobbyButtons, true);
                VisibilityManager.setVisibilityOfComponents(usernameComponents, false);
                VisibilityManager.setVisibilityOfComponents(passwordComponents, false);

                usernameField.setText("");
                passwordField.setText("");

                setTextOfLabel(mainMenuLabel, "PRE-LOBBY");
            }
        }
        else if (e.getSource() == backButtonLogin) {
            VisibilityManager.setVisibilityOfComponents(loginButtons, false);
            VisibilityManager.setVisibilityOfComponents(multiplayerButtons, true);
            VisibilityManager.setVisibilityOfComponents(usernameComponents, false);
            VisibilityManager.setVisibilityOfComponents(passwordComponents, false);

            usernameField.setText("");
            passwordField.setText("");

            // Empties the lists after pressing the back button
            usernameList.clear();
            passwordList.clear();

            setTextOfLabel(mainMenuLabel, "MULTIPLAYER");
        }
        else if (e.getSource() == acceptButton) {
            // TODO display message on GUI for empty username and password
            // TODO display message on GUI for successful registration
            if (usernameList.isEmpty() || passwordList.isEmpty()) {
                System.out.println("Both the username and the password have to be filled out!");
            } else {
                username = listToString(usernameList);
                password = listToString(passwordList);
                connection.register(username, password);
            }
        }
        else if (e.getSource() == backButtonRegister) {
            VisibilityManager.setVisibilityOfComponents(registerButtons, false);
            VisibilityManager.setVisibilityOfComponents(multiplayerButtons, true);
            VisibilityManager.setVisibilityOfComponents(usernameComponents, false);
            VisibilityManager.setVisibilityOfComponents(passwordComponents, false);

            usernameField.setText("");
            passwordField.setText("");

            setTextOfLabel(mainMenuLabel, "MULTIPLAYER");
        }
        else if (e.getSource() == findLobbyButton) {
            // TODO start game after successfully finding another player
            // TODO implement a 'Leave Lobby' Button
            // TODO display corresponding messages for the events on GUI
            VisibilityManager.setVisibilityOfComponents(preLobbyButtons, false);
            VisibilityManager.setVisibilityOfComponents(lobbyButtons, true);

            waitingLabel.setVisible(true);
            setTextOfLabel(mainMenuLabel, "LOBBY");
            //connection.findLobby();
            //if (playButtonListener != null) {
                //playButtonListener.playButtonClicked();
            //}
            //dispose();
        }
        else if (e.getSource() == logoutButton) {
            VisibilityManager.setVisibilityOfComponents(preLobbyButtons, false);
            VisibilityManager.setVisibilityOfComponents(loginButtons, true);
            VisibilityManager.setVisibilityOfComponents(usernameComponents, true);
            VisibilityManager.setVisibilityOfComponents(passwordComponents, true);

            usernameField.setText("");
            passwordField.setText("");

            // Empties the lists after pressing the back button
            usernameList.clear();
            passwordList.clear();

            setTextOfLabel(mainMenuLabel, "LOGIN");
        }
        else if (e.getSource() == leaderboardButton) {
            // TODO implement a display of a leaderboard list
        }
        else if (e.getSource() == leaveLobbyButton) {
            VisibilityManager.setVisibilityOfComponents(lobbyButtons, false);
            VisibilityManager.setVisibilityOfComponents(preLobbyButtons, true);

            waitingLabel.setVisible(false);
            setTextOfLabel(mainMenuLabel, "PRE-LOBBY");
        }
    }
}
