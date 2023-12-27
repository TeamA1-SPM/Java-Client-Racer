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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainMenu extends JFrame implements ActionListener {

    private final JLabel mainMenuLbl = new JLabel("MAIN MENU");
    private final JLabel userLbl = new JLabel("Username:");
    private final JLabel pwLbl = new JLabel("Password:");
    private final JLabel waitingLbl = new JLabel("Waiting for another player...");
    private final JLabel emptyUserAndPwLbl = new JLabel("Username and Password can't be empty!");
    private final JLabel regSuccessLbl = new JLabel("Your registration was successful!");
    private final JLabel userExistsLbl = new JLabel("The user is already registered!");
    private final JLabel userNotFoundLbl = new JLabel("The user doesn't exist!");
    private final JLabel serverStatusLbl = new JLabel("Server:");
    private static final JLabel serverOnlineLbl = new JLabel("Online");
    private static final JLabel serverOfflineLbl = new JLabel("Offline");
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
    private ImageIcon menuBackground;
    private JLabel arrowKeys;
    private String username;
    private String password;
    private final Connection connection = new Connection();
    List<JComponent> mainMenuButtons = Arrays.asList(playBtn, mpBtn, exitBtn);
    List<JComponent> multiplayerButtons = Arrays.asList(loginBtn, regBtn, mpBackBtn);
    List<JComponent> loginButtons = Arrays.asList(continueBtn, loginBackBtn);
    List<JComponent> registerButtons = Arrays.asList(acceptBtn, regBackBtn);
    List<JComponent> preLobbyButtons = Arrays.asList(findLobbyBtn, logoutBtn, lbBtn);
    List<JComponent> lobbyButtons = List.of(leaveLobbyBtn);
    List<JComponent> usernameComponents = Arrays.asList(UserInputManager.getUsernameField(), userLbl);
    List<JComponent> passwordComponents = Arrays.asList(UserInputManager.getPasswordField(), pwLbl);
    private boolean isLoginCorrect = false;
    private boolean registerBool = false;
    private boolean singlePlayer = false;
    private boolean multiPlayer = false;

    public static JLabel getServerOnlineLbl() { return serverOnlineLbl; }
    public static JLabel getServerOfflineLbl() { return serverOfflineLbl; }

    public MainMenu() {
        init();
        connection.connect();
        serverFunctions(connection.getSocket());
    }

    // Initializes everything
    public void init(){
        initButtons();
        setupMenuComponents();
        setupMenuWindow();
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
    public void setupMenuComponents() {
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
        addUserDataFields();
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

    public void addUserDataFields() {
        UserInputManager.setupInputField(UserInputManager.getUsernameField(),10);
        UserInputManager.getUsernameField().setBounds(BoundsManager.getUserFieldBounds());
        mainMenuLbl.add(UserInputManager.getUsernameField());
        UserInputManager.getUsernameField().setVisible(false);

        UserInputManager.setupInputField(UserInputManager.getPasswordField(),10);
        UserInputManager.getPasswordField().setBounds(BoundsManager.getPwFieldBounds());
        mainMenuLbl.add(UserInputManager.getPasswordField());
        UserInputManager.getPasswordField().setVisible(false);
    }

    public void setupLabels() {
        LabelManager.createLabel(userLbl, FontManager.getSize40(), Color.BLACK, BoundsManager.getUserLblBounds());
        LabelManager.createLabel(pwLbl, FontManager.getSize40(), Color.BLACK, BoundsManager.getPwLblBounds());
        LabelManager.createLabel(waitingLbl, FontManager.getSize50(), Color.RED, BoundsManager.getWaitingLblBounds());
        LabelManager.createLabel(emptyUserAndPwLbl, FontManager.getSize40(), Color.RED, BoundsManager.getEmptyUserAndPwLblBounds());
        LabelManager.createLabel(regSuccessLbl, FontManager.getSize40(), new Color(0, 150, 0), BoundsManager.getRegSuccessLblBounds());
        LabelManager.createLabel(userNotFoundLbl, FontManager.getSize40(), Color.RED, BoundsManager.getUserNotFoundLblBounds());
        LabelManager.createLabel(userExistsLbl, FontManager.getSize40(), new Color(255, 130, 0), BoundsManager.getUserExistsBounds());
        LabelManager.createLabel(serverStatusLbl, FontManager.getSize30(), Color.BLACK, BoundsManager.getServerStatusLblBounds());
        LabelManager.createLabel(serverOnlineLbl, FontManager.getSize30(), new Color(0, 150, 0), BoundsManager.getServerOnlineBounds());
        LabelManager.createLabel(serverOfflineLbl, FontManager.getSize30(), Color.RED, BoundsManager.getServerOfflineBounds());
        serverStatusLbl.setVisible(true);
    }

    public void addLabelsToMenu() {
        mainMenuLbl.add(userLbl);
        mainMenuLbl.add(pwLbl);
        mainMenuLbl.add(waitingLbl);
        mainMenuLbl.add(emptyUserAndPwLbl);
        mainMenuLbl.add(regSuccessLbl);
        mainMenuLbl.add(userExistsLbl);
        mainMenuLbl.add(userNotFoundLbl);
        mainMenuLbl.add(serverStatusLbl);
        mainMenuLbl.add(serverOnlineLbl);
        mainMenuLbl.add(serverOfflineLbl);
    }

    // Sets up the main menu
    public void setupMenuWindow() {
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

    public void serverFunctions(Socket socket) {
        socket.on("login_success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                isLoginCorrect = (boolean) args[0];
                verifyLoginData();
            }
        }).on("register_success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                registerBool = (boolean) args[0];
                checkIfUserExists();
            }
        }).on("start_game", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                checkGameMode(); }
        });
    }

    public void verifyLoginData() {
        if (isLoginCorrect) {
            emptyUserAndPwLbl.setVisible(false);
            userNotFoundLbl.setVisible(false);

            VisibilityManager.setVisibilityOfComponents(loginButtons, false);
            VisibilityManager.setVisibilityOfComponents(preLobbyButtons, true);
            VisibilityManager.setVisibilityOfComponents(usernameComponents, false);
            VisibilityManager.setVisibilityOfComponents(passwordComponents, false);

            UserInputManager.getUsernameField().setText("");
            UserInputManager.getPasswordField().setText("");

            setTextOfLabel(mainMenuLbl, "PRE-LOBBY");
        }
        else {
            emptyUserAndPwLbl.setVisible(false);
            userNotFoundLbl.setVisible(true);
        }
    }

    public void checkIfUserExists() {
        if (!registerBool) {
            emptyUserAndPwLbl.setVisible(false);
            regSuccessLbl.setVisible(false);
            userExistsLbl.setVisible(true);
        } else {
            userExistsLbl.setVisible(false);
            emptyUserAndPwLbl.setVisible(false);
            regSuccessLbl.setVisible(true);
        }
    }

    public void checkGameMode() {
        if (singlePlayer) {
            startGame(GameMode.SINGLE_PLAYER);
        } else if (multiPlayer) {
            startGame(GameMode.MULTI_PLAYER);
        }
    }
    public void startGame(GameMode mode) {
        connection.ready();
        main.Window window = new Window();
        Game game = new Game(window, connection, mode);
        Thread gameThread = new Thread(game);
        gameThread.start();
        dispose();
    }

    public void playClicked() {
        singlePlayer = true;
        checkGameMode();
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
        VisibilityManager.setVisibilityOfComponents(usernameComponents, true);
        VisibilityManager.setVisibilityOfComponents(passwordComponents, true);

        setTextOfLabel(mainMenuLbl, "LOGIN");
    }

    public void registerClicked() {
        VisibilityManager.setVisibilityOfComponents(multiplayerButtons, false);
        VisibilityManager.setVisibilityOfComponents(registerButtons, true);
        VisibilityManager.setVisibilityOfComponents(usernameComponents, true);
        VisibilityManager.setVisibilityOfComponents(passwordComponents, true);

        setTextOfLabel(mainMenuLbl, "REGISTER");
    }

    public void multiplayerBackClicked() {
        VisibilityManager.setVisibilityOfComponents(multiplayerButtons, false);
        VisibilityManager.setVisibilityOfComponents(mainMenuButtons, true);

        setTextOfLabel(mainMenuLbl, "MAIN MENU");
    }

    public void continueClicked() {
        if (UserInputManager.getUsernameList().isEmpty() || UserInputManager.getPasswordList().isEmpty()) {
            emptyUserAndPwLbl.setVisible(true);
            userNotFoundLbl.setVisible(false);
        }
        else {
            username = UserInputManager.listToString(UserInputManager.getUsernameList());
            password = UserInputManager.listToString(UserInputManager.getPasswordList());
            connection.login(username, password);
        }
    }

    public void loginBackClicked() {
        VisibilityManager.setVisibilityOfComponents(loginButtons, false);
        VisibilityManager.setVisibilityOfComponents(multiplayerButtons, true);
        VisibilityManager.setVisibilityOfComponents(usernameComponents, false);
        VisibilityManager.setVisibilityOfComponents(passwordComponents, false);

        emptyUserAndPwLbl.setVisible(false);
        userNotFoundLbl.setVisible(false);

        UserInputManager.getUsernameField().setText("");
        UserInputManager.getPasswordField().setText("");

        // Empties the lists after pressing the back button
        UserInputManager.getUsernameList().clear();
        UserInputManager.getPasswordList().clear();

        setTextOfLabel(mainMenuLbl, "MULTIPLAYER");
    }

    public void acceptClicked() {
        if (UserInputManager.getUsernameList().isEmpty() || UserInputManager.getPasswordList().isEmpty()) {
            emptyUserAndPwLbl.setVisible(true);
            userExistsLbl.setVisible(false);
            regSuccessLbl.setVisible(false);
        }
        else {
            username = UserInputManager.listToString(UserInputManager.getUsernameList());
            password = UserInputManager.listToString(UserInputManager.getPasswordList());
            connection.register(username, password);
        }
    }

    public void registerBackClicked() {
        VisibilityManager.setVisibilityOfComponents(registerButtons, false);
        VisibilityManager.setVisibilityOfComponents(multiplayerButtons, true);
        VisibilityManager.setVisibilityOfComponents(usernameComponents, false);
        VisibilityManager.setVisibilityOfComponents(passwordComponents, false);

        emptyUserAndPwLbl.setVisible(false);
        userExistsLbl.setVisible(false);
        regSuccessLbl.setVisible(false);

        UserInputManager.getUsernameField().setText("");
        UserInputManager.getPasswordField().setText("");

        setTextOfLabel(mainMenuLbl, "MULTIPLAYER");
    }

    public void findLobbyClicked() {
        VisibilityManager.setVisibilityOfComponents(preLobbyButtons, false);
        VisibilityManager.setVisibilityOfComponents(lobbyButtons, true);

        waitingLbl.setVisible(true);
        multiPlayer = true;
        connection.findLobby();

        setTextOfLabel(mainMenuLbl, "LOBBY");
    }

    public void logoutClicked() {
        VisibilityManager.setVisibilityOfComponents(preLobbyButtons, false);
        VisibilityManager.setVisibilityOfComponents(loginButtons, true);
        VisibilityManager.setVisibilityOfComponents(usernameComponents, true);
        VisibilityManager.setVisibilityOfComponents(passwordComponents, true);

        UserInputManager.getUsernameField().setText("");
        UserInputManager.getPasswordField().setText("");

        // Empties the lists after pressing the back button
        UserInputManager.getUsernameList().clear();
        UserInputManager.getPasswordList().clear();

        connection.logout();
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
        multiPlayer = false;
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
