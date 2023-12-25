package main.menu;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import main.Game;
import main.Window;
import main.constants.GameMode;
import main.constants.Settings;
import main.gamehelper.Connection;
import main.menuhelper.*;

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

    private final JLabel mainMenuLbl = new JLabel("MAIN MENU");
    private final JLabel userLbl = new JLabel("Username:");
    private final JLabel pwLbl = new JLabel("Password:");
    private final JLabel waitingLbl = new JLabel("Waiting for another player...");
    private final JLabel emptyUserLbl = new JLabel("Username can't be empty!");
    private final JLabel emptyPwLbl = new JLabel("Password can't be empty!");
    private final JLabel emptyUserAndPwLbl = new JLabel("Username and Password can't be empty!");
    private final JLabel regSuccessLbl = new JLabel("Your registration was successful!");
    private final JLabel userAlreadyExistsLabel = new JLabel("The user is already registered!");
    private final JLabel userNotFoundLbl = new JLabel("The user doesn't exist!");
    private final JButton playBtn = new JButton();
    private final JButton mpBtn = new JButton();
    private final JButton exitBtn = new JButton();
    private final JButton loginBtn = new JButton();
    private final JButton regBtn = new JButton();
    private final JButton mpBackBtn = new JButton();
    private final JButton loginBackBtn = new JButton();
    private final JButton regBackBtn = new JButton();
    private final JButton continueBtn = new JButton();
    private final JButton acceptBtn = new JButton();
    private final JButton findLobbyBtn = new JButton();
    private final JButton logoutBtn = new JButton();
    private final JButton lbBtn = new JButton();
    private final JButton leaveLobbyBtn = new JButton();
    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private ImageIcon menuBackground;
    private JLabel arrowKeys;
    private PlayButtonListener playButtonListener;
    private final List<Character> usernameList = new ArrayList<>();
    private final List<Character> passwordList = new ArrayList<>();
    private String username;
    private String password;
    private final Connection connection = new Connection();
    List<JComponent> mainMenuButtons = Arrays.asList(playBtn, mpBtn, exitBtn);
    List<JComponent> multiplayerButtons = Arrays.asList(loginBtn, regBtn, mpBackBtn);
    List<JComponent> loginButtons = Arrays.asList(continueBtn, loginBackBtn);
    List<JComponent> registerButtons = Arrays.asList(acceptBtn, regBackBtn);
    List<JComponent> preLobbyButtons = Arrays.asList(findLobbyBtn, logoutBtn, lbBtn);
    List<JComponent> lobbyButtons = List.of(leaveLobbyBtn);
    boolean isLoginCorrect;

    public Connection getConnection() {
        return connection;
    }

    public MainMenu() {
        init();
        connection.connect();
    }

    // Initializes everything
    public void init(){
        initButtons();
        setupBackground();
        setupMainMenu();
    }

    // Initializes the buttons
    public void initButtons() {
        ButtonManager.createButton(playBtn, "Play", BoundsManager.getPlayBtnBounds(), this);
        ButtonManager.createButton(mpBtn, "Multiplayer", BoundsManager.getMpBtnBounds(), this);
        ButtonManager.createButton(exitBtn, "Exit", BoundsManager.getExitBtnBounds(), this);

        ButtonManager.createButton(loginBtn, "Login", BoundsManager.getLoginBtnBounds(), this);
        ButtonManager.createButton(regBtn, "Register", BoundsManager.getRegBtnBounds(), this);
        ButtonManager.createButton(mpBackBtn, "Back", BoundsManager.getMpBackBtnBounds(), this);

        ButtonManager.createButton(continueBtn, "Continue", BoundsManager.getContinueBtnBounds(), this);
        ButtonManager.createButton(loginBackBtn, "Back", BoundsManager.getLoginBackBtnBounds(), this);

        ButtonManager.createButton(acceptBtn, "Accept", BoundsManager.getAcceptBtnBounds(), this);
        ButtonManager.createButton(regBackBtn, "Back", BoundsManager.getRegBackBtnBounds(), this);

        ButtonManager.createButton(findLobbyBtn, "Find Lobby", BoundsManager.getFindLobbyBtnBounds(), this);
        ButtonManager.createButton(logoutBtn, "Logout", BoundsManager.getLogoutBtnBounds(), this);
        ButtonManager.createButton(lbBtn, "Leaderboard", BoundsManager.getLbBtnBounds(), this);

        ButtonManager.createButton(leaveLobbyBtn, "Leave Lobby", BoundsManager.getLeaveLobbyBtnBounds(), this);
    }

    // Sets the text of the background label
    public void setTextOfLabel(JLabel label, String text) {
        label.setText(text);
    }

    // Sets up the background image and corresponding text
    public void setupBackground() {
        menuBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/main/images/menu/MenuBackground.png")));
        arrowKeys = new JLabel(new ImageIcon("/main/images/menu/ArrowKeys.png"));
        //arrowKeys.setBounds(x, y, width, height);
        mainMenuLbl.setFont(FontManager.getSize65());
        mainMenuLbl.setHorizontalTextPosition(JLabel.CENTER);
        mainMenuLbl.setVerticalTextPosition(JLabel.TOP);
        mainMenuLbl.setIconTextGap(-150);
        mainMenuLbl.setIcon(menuBackground);
        //mainMenuLabel.add(arrowKeys);
        //arrowKeys.setVisible(true);

        addAllButtonsToBackground();
        setVisibilityOfAllButtons();
        addUsernameAndPasswordFields();
        setupLabels();
        addLabelsToMenu();
    }
    private void addAllButtonsToBackground() {
        addButtonToBackground(mainMenuLbl, playBtn);
        addButtonToBackground(mainMenuLbl, mpBtn);
        addButtonToBackground(mainMenuLbl, exitBtn);

        addButtonToBackground(mainMenuLbl, loginBtn);
        addButtonToBackground(mainMenuLbl, regBtn);
        addButtonToBackground(mainMenuLbl, mpBackBtn);

        addButtonToBackground(mainMenuLbl, continueBtn);
        addButtonToBackground(mainMenuLbl, loginBackBtn);

        addButtonToBackground(mainMenuLbl, acceptBtn);
        addButtonToBackground(mainMenuLbl, regBackBtn);

        addButtonToBackground(mainMenuLbl, findLobbyBtn);
        addButtonToBackground(mainMenuLbl, logoutBtn);
        addButtonToBackground(mainMenuLbl, lbBtn);

        addButtonToBackground(mainMenuLbl, leaveLobbyBtn);
    }

    public void addButtonToBackground(JLabel label, JButton button) {
        label.add(button);
    }
    public void setVisibilityOfAllButtons() {
        VisibilityManager.setVisibilityOfComponents(mainMenuButtons, true);
        VisibilityManager.setVisibilityOfComponents(multiplayerButtons, false);
        VisibilityManager.setVisibilityOfComponents(loginButtons, false);
        VisibilityManager.setVisibilityOfComponents(registerButtons, false);
        VisibilityManager.setVisibilityOfComponents(preLobbyButtons, false);
        VisibilityManager.setVisibilityOfComponents(lobbyButtons, false);
    }

    public void addUsernameAndPasswordFields() {
        usernameField = createUserField(12);
        usernameField.setBounds(BoundsManager.getUserFieldBounds());
        mainMenuLbl.add(usernameField);
        usernameField.setVisible(false);

        passwordField = createPasswordField(12);
        passwordField.setBounds(BoundsManager.getPwFieldBounds());
        mainMenuLbl.add(passwordField);
        passwordField.setVisible(false);
    }

    public void setupLabels() {
        LabelManager.createLabel(userLbl, FontManager.getSize40(), Color.BLACK, BoundsManager.getUserLblBounds());
        LabelManager.createLabel(pwLbl, FontManager.getSize40(), Color.BLACK, BoundsManager.getPwLblBounds());
        LabelManager.createLabel(waitingLbl, FontManager.getSize50(), Color.RED, BoundsManager.getWaitingLblBounds());
        LabelManager.createLabel(emptyUserLbl, FontManager.getSize40(), Color.RED, BoundsManager.getEmptyUserLblBounds());
        LabelManager.createLabel(emptyPwLbl, FontManager.getSize40(), Color.RED, BoundsManager.getEmptyPwLblBounds());
        LabelManager.createLabel(emptyUserAndPwLbl, FontManager.getSize40(), Color.RED, BoundsManager.getEmptyUserAndPwLblBounds());
        LabelManager.createLabel(regSuccessLbl, FontManager.getSize40(), new Color(0, 150, 0), BoundsManager.getRegSuccessLblBounds());
        LabelManager.createLabel(userNotFoundLbl, FontManager.getSize40(), Color.RED, BoundsManager.getUserNotFoundLblBounds());
    }

    public void addLabelsToMenu() {
        mainMenuLbl.add(userLbl);
        mainMenuLbl.add(pwLbl);
        mainMenuLbl.add(waitingLbl);
        mainMenuLbl.add(emptyUserLbl);
        mainMenuLbl.add(emptyPwLbl);
        mainMenuLbl.add(emptyUserAndPwLbl);
        mainMenuLbl.add(regSuccessLbl);
        mainMenuLbl.add(userNotFoundLbl);
    }

    // Sets up the main menu
    public void setupMainMenu() {
        this.setSize(menuBackground.getIconWidth(), menuBackground.getIconHeight());
        this.setTitle(Settings.SCREEN_TITLE);
        this.setResizable(Settings.SCREEN_RESIZABLE);
        this.setVisible(Settings.SCREEN_VISIBLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(mainMenuLbl);
        //this.add(arrowKeys);
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
        usernameField.setFont(FontManager.getSize30());

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
        passwordField.setFont(FontManager.getSize30());

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


    public void serverFunctionLogin(Socket socket) {
        socket.on("login_success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                isLoginCorrect = (boolean) args[0];
                System.out.println("Login " + isLoginCorrect);
            }
        });
    }

    public void serverFunctionRegister(Socket socket) {
        socket.on("register_success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                boolean register = (boolean) args[0];
                System.out.println("Register " + register);
            }
        });
    }

    public void serverFunctionStartGame(Socket socket) {
        socket.on("start_game", new Emitter.Listener() {
            @Override
            public void call(Object... args) { startGame(); }
        });
    }


    public void startGame() {
        connection.ready();
        main.Window window = new Window();
        Game game = new Game(window, connection, GameMode.MULTI_PLAYER);
        Thread t1 = new Thread(game);
        t1.start();
        dispose();
    }

    public void playClicked() {
        if (playButtonListener != null) {
            playButtonListener.playButtonClicked();
        }
        dispose();
    }

    public void multiplayerClicked() {
        VisibilityManager.setVisibilityOfComponents(mainMenuButtons, false);
        VisibilityManager.setVisibilityOfComponents(multiplayerButtons, true);
        setTextOfLabel(mainMenuLbl, "MULTIPLAYER");
    }

    public void exitClicked() {
        System.exit(0);
    }

    public void loginClicked() {
        VisibilityManager.setVisibilityOfComponents(multiplayerButtons, false);
        VisibilityManager.setVisibilityOfComponents(loginButtons, true);

        usernameField.setVisible(true);
        userLbl.setVisible(true);

        passwordField.setVisible(true);
        pwLbl.setVisible(true);

        setTextOfLabel(mainMenuLbl, "LOGIN");
    }

    public void registerClicked() {
        VisibilityManager.setVisibilityOfComponents(multiplayerButtons, false);
        VisibilityManager.setVisibilityOfComponents(registerButtons, true);

        usernameField.setVisible(true);
        userLbl.setVisible(true);

        passwordField.setVisible(true);
        pwLbl.setVisible(true);

        setTextOfLabel(mainMenuLbl, "REGISTER");
    }

    public void multiplayerBackClicked() {
        VisibilityManager.setVisibilityOfComponents(multiplayerButtons, false);
        VisibilityManager.setVisibilityOfComponents(mainMenuButtons, true);
        setTextOfLabel(mainMenuLbl, "MAIN MENU");
    }

    public void continueClicked() {
        // TODO display message on GUI for successful or unsuccessful login
        if (usernameList.isEmpty() || passwordList.isEmpty()) {
            emptyUserAndPwLbl.setVisible(true);
            userNotFoundLbl.setVisible(false);
        }
        else {
            username = listToString(usernameList);
            password = listToString(passwordList);
            connection.login(username, password);
            serverFunctionLogin(connection.getSocket());

            if (!isLoginCorrect) {
                emptyUserAndPwLbl.setVisible(false);
                userNotFoundLbl.setVisible(true);
            }
            else {
                emptyUserAndPwLbl.setVisible(false);
                userNotFoundLbl.setVisible(false);

                VisibilityManager.setVisibilityOfComponents(loginButtons, false);
                VisibilityManager.setVisibilityOfComponents(preLobbyButtons, true);

                userLbl.setVisible(false);
                usernameField.setVisible(false);
                usernameField.setText("");

                pwLbl.setVisible(false);
                passwordField.setVisible(false);
                passwordField.setText("");

                setTextOfLabel(mainMenuLbl, "PRE-LOBBY");
            }
        }
    }

    public void loginBackClicked() {
        VisibilityManager.setVisibilityOfComponents(loginButtons, false);
        VisibilityManager.setVisibilityOfComponents(multiplayerButtons, true);
        emptyUserAndPwLbl.setVisible(false);
        userNotFoundLbl.setVisible(false);

        userLbl.setVisible(false);
        usernameField.setVisible(false);
        usernameField.setText("");

        pwLbl.setVisible(false);
        passwordField.setVisible(false);
        passwordField.setText("");

        // Empties the lists after pressing the back button
        usernameList.clear();
        passwordList.clear();

        setTextOfLabel(mainMenuLbl, "MULTIPLAYER");
    }

    public void acceptClicked() {
        // TODO check if username is already registered (display message if yes)
        // TODO ask server people why users.json doesn't get updated anymore
        if (usernameList.isEmpty() && passwordList.isEmpty()) {
            emptyUserAndPwLbl.setVisible(true);
            emptyUserLbl.setVisible(false);
            emptyPwLbl.setVisible(false);
            regSuccessLbl.setVisible(false);
        }
        else if (usernameList.isEmpty()) {
            emptyUserLbl.setVisible(true);
            emptyPwLbl.setVisible(false);
            emptyUserAndPwLbl.setVisible(false);
            regSuccessLbl.setVisible(false);
        }
        else if (passwordList.isEmpty()){
            emptyPwLbl.setVisible(true);
            emptyUserLbl.setVisible(false);
            emptyUserAndPwLbl.setVisible(false);
            regSuccessLbl.setVisible(false);
        }
        else {
            emptyUserLbl.setVisible(false);
            emptyPwLbl.setVisible(false);
            emptyUserAndPwLbl.setVisible(false);
            regSuccessLbl.setVisible(true);

            username = listToString(usernameList);
            password = listToString(passwordList);
            connection.register(username, password);
            serverFunctionRegister(connection.getSocket());
        }
    }

    public void registerBackClicked() {
        VisibilityManager.setVisibilityOfComponents(registerButtons, false);
        VisibilityManager.setVisibilityOfComponents(multiplayerButtons, true);

        emptyUserLbl.setVisible(false);
        emptyPwLbl.setVisible(false);
        emptyUserAndPwLbl.setVisible(false);
        regSuccessLbl.setVisible(false);

        userLbl.setVisible(false);
        usernameField.setVisible(false);
        usernameField.setText("");

        pwLbl.setVisible(false);
        passwordField.setVisible(false);
        passwordField.setText("");

        setTextOfLabel(mainMenuLbl, "MULTIPLAYER");
    }

    public void findLobbyClicked() {
        // TODO start game after successfully finding another player
        // TODO display corresponding messages for the events on GUI
        VisibilityManager.setVisibilityOfComponents(preLobbyButtons, false);
        VisibilityManager.setVisibilityOfComponents(lobbyButtons, true);

        waitingLbl.setVisible(true);
        setTextOfLabel(mainMenuLbl, "LOBBY");
        connection.findLobby();
        serverFunctionStartGame(connection.getSocket());
        //if (playButtonListener != null) {
        //playButtonListener.playButtonClicked();
        //}
        //dispose();
    }

    public void logoutClicked() {
        VisibilityManager.setVisibilityOfComponents(preLobbyButtons, false);
        VisibilityManager.setVisibilityOfComponents(loginButtons, true);

        userLbl.setVisible(true);
        usernameField.setVisible(true);
        usernameField.setText("");

        passwordField.setVisible(true);
        pwLbl.setVisible(true);
        passwordField.setText("");

        // Empties the lists after pressing the back button
        usernameList.clear();
        passwordList.clear();

        setTextOfLabel(mainMenuLbl, "LOGIN");
    }

    public void leaderboardClicked() {
        // TODO implement a display of a leaderboard list
    }

    public void leaveLobbyClicked() {
        // TODO wait for server functionality
        VisibilityManager.setVisibilityOfComponents(lobbyButtons, false);
        VisibilityManager.setVisibilityOfComponents(preLobbyButtons, true);

        waitingLbl.setVisible(false);
        setTextOfLabel(mainMenuLbl, "PRE-LOBBY");
    }

    // Controls the actions of pressed buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playBtn)            { playClicked(); }
        else if (e.getSource() == mpBtn)         { multiplayerClicked(); }
        else if (e.getSource() == exitBtn)       { exitClicked(); }
        else if (e.getSource() == loginBtn)      { loginClicked(); }
        else if (e.getSource() == regBtn)        { registerClicked(); }
        else if (e.getSource() == mpBackBtn)     { multiplayerBackClicked(); }
        else if (e.getSource() == continueBtn)   { continueClicked(); }
        else if (e.getSource() == loginBackBtn)  { loginBackClicked(); }
        else if (e.getSource() == acceptBtn)     { acceptClicked(); }
        else if (e.getSource() == regBackBtn)    { registerBackClicked(); }
        else if (e.getSource() == findLobbyBtn)  { findLobbyClicked(); }
        else if (e.getSource() == logoutBtn)     { logoutClicked(); }
        else if (e.getSource() == lbBtn)         { leaderboardClicked(); }
        else if (e.getSource() == leaveLobbyBtn) { leaveLobbyClicked(); }
    }
}
