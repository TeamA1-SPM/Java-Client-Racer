package main.menu;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import main.Game;
import main.Window;
import main.constants.GameMode;
import main.constants.Settings;
import main.gamehelper.Connection;
import main.gamehelper.GameSetup;
import main.menuhelper.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static main.menuhelper.BoundsManager.*;
import static main.menuhelper.FontManager.*;
import static main.menuhelper.UserInputManager.*;
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
    private final JButton controlsBtn = new JButton();
    private final JButton exitBtn = new JButton();
    private final JButton controlsBackBtn = new JButton();
    private final JButton loginBtn = new JButton();
    private final JButton regBtn = new JButton();
    private final JButton mpBackBtn = new JButton();
    private final JButton loginBackBtn = new JButton();
    private final JButton regBackBtn = new JButton();
    private final JButton continueBtn = new JButton();
    private final JButton acceptBtn = new JButton();
    private final JButton findLobbyBtn = new JButton();
    private final JButton logoutBtn = new JButton();
    private final JButton matchHistoryBtn = new JButton();
    private final JButton mhBackBtn = new JButton();
    private final JButton leaveLobbyBtn = new JButton();
    private ImageIcon menuBackground;
    private ImageIcon arrowKeys;
    private String username;
    private String password;
    private final Connection connection = new Connection();
    List<JComponent> mainMenuButtons = Arrays.asList(playBtn, mpBtn, controlsBtn, exitBtn);
    List<JComponent> multiplayerButtons = Arrays.asList(loginBtn, regBtn, mpBackBtn);
    List<JComponent> loginButtons = Arrays.asList(continueBtn, loginBackBtn);
    List<JComponent> registerButtons = Arrays.asList(acceptBtn, regBackBtn);
    List<JComponent> preLobbyButtons = Arrays.asList(findLobbyBtn, logoutBtn, matchHistoryBtn);
    List<JComponent> controlsButtons = List.of(controlsBackBtn);
    List<JComponent> matchHistoryButtons = List.of(mhBackBtn);
    List<JComponent> lobbyButtons = List.of(leaveLobbyBtn);
    List<JComponent> usernameComponents = Arrays.asList(UsernameField, userLbl);
    List<JComponent> passwordComponents = Arrays.asList(PasswordField, pwLbl);
    private boolean isLoginCorrect = false;
    private boolean registerBool = false;
    private boolean arrowKeysVisible = false;
    private boolean mhBtnPressed = false;

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
        ButtonManager.createButton(playBtn, "Play", PlayBtnBounds, this);
        ButtonManager.createButton(mpBtn, "Multiplayer", MpBtnBounds, this);
        ButtonManager.createButton(controlsBtn, "Controls", ControlsBtnBounds, this);
        ButtonManager.createButton(exitBtn, "Exit", ExitBtnBounds, this);

        ButtonManager.createButton(controlsBackBtn, "Back", ControlsBackBtnBounds, this);

        ButtonManager.createButton(loginBtn, "Login", LoginBtnBounds, this);
        ButtonManager.createButton(regBtn, "Register", RegBtnBounds, this);
        ButtonManager.createButton(mpBackBtn, "Back", MpBackBtnBounds, this);

        ButtonManager.createButton(continueBtn, "Continue", ContinueBtnBounds, this);
        ButtonManager.createButton(loginBackBtn, "Back", LoginBackBtnBounds, this);

        ButtonManager.createButton(acceptBtn, "Accept", AcceptBtnBounds, this);
        ButtonManager.createButton(regBackBtn, "Back", RegBackBtnBounds, this);

        ButtonManager.createButton(findLobbyBtn, "Find Lobby", FindLobbyBtnBounds, this);
        ButtonManager.createButton(logoutBtn, "Logout", LogoutBtnBounds, this);
        ButtonManager.createButton(matchHistoryBtn, "Match History", MhBtnBounds, this);

        ButtonManager.createButton(mhBackBtn, "Back", MhBackBtnBounds, this);

        ButtonManager.createButton(leaveLobbyBtn, "Leave Lobby", LeaveLobbyBtnBounds, this);
    }

    // Sets the text of the background label
    public void setTextOfLabel(JLabel label, String text) {
        label.setText(text);
    }

    // Sets up the background image and corresponding text
    public void setupMenuComponents() {
        menuBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/main/images/menu/MenuBackground.png")));
        arrowKeys = new ImageIcon(Objects.requireNonNull(getClass().getResource("/main/images/menu/ArrowKeys.png")));
        mainMenuLbl.setFont(FontSize65);
        mainMenuLbl.setForeground(new Color(65, 0, 255));
        mainMenuLbl.setHorizontalTextPosition(JLabel.CENTER);
        mainMenuLbl.setVerticalTextPosition(JLabel.TOP);
        mainMenuLbl.setIconTextGap(-150);
        mainMenuLbl.setIcon(menuBackground);

        addAllButtonsToLabel();
        setVisibilityOfAllButtons();
        addUserDataFields();
        setupLabels();
        addLabelsToMenu();
    }
    private void addAllButtonsToLabel() {
        addButtonToLabel(mainMenuLbl, playBtn);
        addButtonToLabel(mainMenuLbl, mpBtn);
        addButtonToLabel(mainMenuLbl, controlsBtn);
        addButtonToLabel(mainMenuLbl, exitBtn);

        addButtonToLabel(mainMenuLbl, controlsBackBtn);

        addButtonToLabel(mainMenuLbl, loginBtn);
        addButtonToLabel(mainMenuLbl, regBtn);
        addButtonToLabel(mainMenuLbl, mpBackBtn);

        addButtonToLabel(mainMenuLbl, continueBtn);
        addButtonToLabel(mainMenuLbl, loginBackBtn);

        addButtonToLabel(mainMenuLbl, acceptBtn);
        addButtonToLabel(mainMenuLbl, regBackBtn);

        addButtonToLabel(mainMenuLbl, findLobbyBtn);
        addButtonToLabel(mainMenuLbl, logoutBtn);
        addButtonToLabel(mainMenuLbl, matchHistoryBtn);

        addButtonToLabel(mainMenuLbl, mhBackBtn);

        addButtonToLabel(mainMenuLbl, leaveLobbyBtn);
    }

    public void addButtonToLabel(JLabel label, JButton button) {
        label.add(button);
    }
    public void setVisibilityOfAllButtons() {
        VisibilityManager.showComponents(mainMenuButtons);
        VisibilityManager.hideComponents(controlsButtons);
        VisibilityManager.hideComponents(multiplayerButtons);
        VisibilityManager.hideComponents(loginButtons);
        VisibilityManager.hideComponents(registerButtons);
        VisibilityManager.hideComponents(preLobbyButtons);
        VisibilityManager.hideComponents(matchHistoryButtons);
        VisibilityManager.hideComponents(lobbyButtons);
    }

    public void addUserDataFields() {
        UserInputManager.setupInputField(UsernameField,10);
        UsernameField.setBounds(UserFieldBounds);
        mainMenuLbl.add(UsernameField);
        UsernameField.setVisible(false);

        UserInputManager.setupInputField(PasswordField,10);
        PasswordField.setBounds(PwFieldBounds);
        mainMenuLbl.add(PasswordField);
        PasswordField.setVisible(false);
    }

    public void setupLabels() {
        LabelManager.createLabel(userLbl, false, FontSize40, Color.BLACK, UserBounds);
        LabelManager.createLabel(pwLbl, false, FontSize40, Color.BLACK, PwBounds);
        LabelManager.createLabel(waitingLbl, false, FontSize50, Color.RED, WaitingBounds);
        LabelManager.createLabel(emptyUserAndPwLbl, false, FontSize40, Color.RED, NoInputBounds);
        LabelManager.createLabel(regSuccessLbl, false, FontSize40, new Color(0, 150, 0), RegSuccessBounds);
        LabelManager.createLabel(userNotFoundLbl, false, FontSize40, Color.RED, UserNotFoundBounds);
        LabelManager.createLabel(userExistsLbl, false, FontSize40, new Color(255, 130, 0), UserExistsBounds);
        LabelManager.createLabel(serverStatusLbl, true, FontSize30, Color.BLACK, ServerBounds);
        LabelManager.createLabel(serverOnlineLbl, false, FontSize30, new Color(0, 150, 0), ServerOnlineBounds);
        LabelManager.createLabel(serverOfflineLbl, true,  FontSize30, Color.RED, ServerOfflineBounds);
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
                GameSetup setup = new GameSetup(GameMode.MULTI_PLAYER,1,1,username);
                setup.setMultiplayerParameters((String)args[0], (String)args[1]);
                connection.ready();
                startGame(setup);
            }
        });
    }

    public void drawArrowKeys(Graphics g) {
        if (arrowKeysVisible) {
            int x = 345;
            int y = 175;
            int width = arrowKeys.getIconWidth();
            int height = arrowKeys.getIconHeight();
            g.drawImage(arrowKeys.getImage(), x, y, width, height, null);
        }
    }

    public void drawTable(Graphics g) {
        if (mhBtnPressed) {
            drawRect(g, 200, 175, 600, 250, Color.BLACK);

            Graphics2D g2 = (Graphics2D) g;
            float lineWidth = 4.0f;
            g2.setStroke(new BasicStroke(lineWidth));

            g.drawLine(200, 175, 800, 175);
            g.drawLine(200, 225, 800, 225);
            g.drawLine(350, 275, 650, 275);
            g.drawLine(200, 325, 800, 325);
            g.drawLine(350, 375, 650, 375);
            g.drawLine(200, 425, 800, 425);

            g.drawLine(200, 175, 200, 425);
            g.drawLine(350, 175, 350, 425);
            g.drawLine(500, 175, 500, 425);
            g.drawLine(650, 175, 650, 425);
            g.drawLine(800, 175, 800, 425);

            drawText(g, "Lobby ID", 205, 210, Color.BLUE, FontSize35);
            drawText(g, "Players", 355, 210, Color.BLUE, FontSize35);
            drawText(g, "Time", 505, 210, Color.BLUE, FontSize35);
            drawText(g, "Winner", 655, 210, Color.BLUE, FontSize35);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawArrowKeys(g);
        drawTable(g);
    }

    private void drawRect(Graphics g, int x, int y, int width, int height, Color color) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
        g.setColor(color);
        g.drawRect(x, y, width, height);
    }

    public void drawText(Graphics g, String text, int x, int y, Color color, Font font) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
    }

    public void verifyLoginData() {
        if (isLoginCorrect) {
            emptyUserAndPwLbl.setVisible(false);
            userNotFoundLbl.setVisible(false);

            VisibilityManager.hideComponents(usernameComponents);
            VisibilityManager.hideComponents(passwordComponents);
            VisibilityManager.hideComponents(loginButtons);
            VisibilityManager.showComponents(preLobbyButtons);

            UsernameField.setText("");
            PasswordField.setText("");

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

    public void startGame(GameSetup setup) {
        this.setVisible(false);

        if(setup.getGameMode() == GameMode.MULTI_PLAYER){
            VisibilityManager.hideComponents(lobbyButtons);
            VisibilityManager.showComponents(preLobbyButtons);
            waitingLbl.setVisible(false);
            setTextOfLabel(mainMenuLbl, "PRE-LOBBY");
        }

        main.Window window = new Window();
        Game game = new Game(this, window, connection, setup);
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    public void playClicked() {
        GameSetup setup = new GameSetup(GameMode.SINGLE_PLAYER,1,1,"PLAYER");
        startGame(setup);
    }

    public void multiplayerClicked() {
        VisibilityManager.hideComponents(mainMenuButtons);
        VisibilityManager.showComponents(multiplayerButtons);

        setTextOfLabel(mainMenuLbl, "MULTIPLAYER");
    }

    public void controlsClicked() {
        VisibilityManager.hideComponents(mainMenuButtons);
        VisibilityManager.showComponents(controlsButtons);

        setTextOfLabel(mainMenuLbl, "CONTROLS");

        arrowKeysVisible = true;
        repaint();
    }

    public void exitClicked() {
        System.exit(0);
    }

    public void controlsBackClicked() {
        VisibilityManager.hideComponents(controlsButtons);
        VisibilityManager.showComponents(mainMenuButtons);

        arrowKeysVisible = false;
        setTextOfLabel(mainMenuLbl, "MAIN MENU");

    }

    public void loginClicked() {
        VisibilityManager.hideComponents(multiplayerButtons);
        VisibilityManager.showComponents(loginButtons);
        VisibilityManager.showComponents(usernameComponents);
        VisibilityManager.showComponents(passwordComponents);

        setTextOfLabel(mainMenuLbl, "LOGIN");
    }

    public void registerClicked() {
        VisibilityManager.hideComponents(multiplayerButtons);
        VisibilityManager.showComponents(registerButtons);
        VisibilityManager.showComponents(usernameComponents);
        VisibilityManager.showComponents(passwordComponents);

        setTextOfLabel(mainMenuLbl, "REGISTER");
    }

    public void multiplayerBackClicked() {
        VisibilityManager.hideComponents(multiplayerButtons);
        VisibilityManager.showComponents(mainMenuButtons);

        setTextOfLabel(mainMenuLbl, "MAIN MENU");

    }

    public void continueClicked() {
        if (UsernameList.isEmpty() || PasswordList.isEmpty()) {
            emptyUserAndPwLbl.setVisible(true);
            userNotFoundLbl.setVisible(false);
        }
        else {
            username = UserInputManager.listToString(UsernameList);
            password = UserInputManager.listToString(PasswordList);
            connection.login(username, password);
        }
    }

    public void loginBackClicked() {
        VisibilityManager.hideComponents(usernameComponents);
        VisibilityManager.hideComponents(passwordComponents);
        VisibilityManager.hideComponents(loginButtons);
        VisibilityManager.showComponents(multiplayerButtons);

        emptyUserAndPwLbl.setVisible(false);
        userNotFoundLbl.setVisible(false);

        UsernameField.setText("");
        PasswordField.setText("");

        // Empties the lists after pressing the back button
        UsernameList.clear();
        PasswordList.clear();

        setTextOfLabel(mainMenuLbl, "MULTIPLAYER");
    }

    public void acceptClicked() {
        if (UsernameList.isEmpty() || PasswordList.isEmpty()) {
            emptyUserAndPwLbl.setVisible(true);
            userExistsLbl.setVisible(false);
            regSuccessLbl.setVisible(false);
        }
        else {
            username = UserInputManager.listToString(UsernameList);
            password = UserInputManager.listToString(PasswordList);
            connection.register(username, password);
        }
    }

    public void registerBackClicked() {
        VisibilityManager.hideComponents(usernameComponents);
        VisibilityManager.hideComponents(passwordComponents);
        VisibilityManager.hideComponents(registerButtons);
        VisibilityManager.showComponents(multiplayerButtons);

        emptyUserAndPwLbl.setVisible(false);
        userExistsLbl.setVisible(false);
        regSuccessLbl.setVisible(false);

        UsernameField.setText("");
        PasswordField.setText("");

        setTextOfLabel(mainMenuLbl, "MULTIPLAYER");
    }

    public void findLobbyClicked() {
        VisibilityManager.hideComponents(preLobbyButtons);
        VisibilityManager.showComponents(lobbyButtons);

        waitingLbl.setVisible(true);
        connection.findLobby();

        setTextOfLabel(mainMenuLbl, "LOBBY");
    }

    public void logoutClicked() {
        VisibilityManager.hideComponents(preLobbyButtons);
        VisibilityManager.showComponents(loginButtons);
        VisibilityManager.showComponents(usernameComponents);
        VisibilityManager.showComponents(passwordComponents);

        UsernameField.setText("");
        PasswordField.setText("");

        // Empties the lists after pressing the back button
        UsernameList.clear();
        PasswordList.clear();

        connection.logout();
        setTextOfLabel(mainMenuLbl, "LOGIN");
    }

    public void matchHistoryClicked() {
        // TODO implement a complete dynamic display of a match history
        VisibilityManager.hideComponents(preLobbyButtons);
        VisibilityManager.showComponents(matchHistoryButtons);

        setTextOfLabel(mainMenuLbl, "MATCH-HISTORY");
        mhBtnPressed = true;
        repaint();
    }

    public void matchHistoryBackClicked() {
        VisibilityManager.hideComponents(matchHistoryButtons);
        VisibilityManager.showComponents(preLobbyButtons);

        mhBtnPressed = false;
        setTextOfLabel(mainMenuLbl, "PRE-LOBBY");
    }

    public void leaveLobbyClicked() {
        VisibilityManager.hideComponents(lobbyButtons);
        VisibilityManager.showComponents(preLobbyButtons);

        waitingLbl.setVisible(false);
        connection.leaveLobby();

        setTextOfLabel(mainMenuLbl, "PRE-LOBBY");
    }

    // Controls the actions of pressed buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playBtn)               { playClicked(); }
        else if (e.getSource() == mpBtn)            { multiplayerClicked(); }
        else if (e.getSource() == controlsBtn)      { controlsClicked(); }
        else if (e.getSource() == exitBtn)          { exitClicked(); }
        else if (e.getSource() == controlsBackBtn)  { controlsBackClicked(); }
        else if (e.getSource() == loginBtn)         { loginClicked(); }
        else if (e.getSource() == regBtn)           { registerClicked(); }
        else if (e.getSource() == mpBackBtn)        { multiplayerBackClicked(); }
        else if (e.getSource() == continueBtn)      { continueClicked(); }
        else if (e.getSource() == loginBackBtn)     { loginBackClicked(); }
        else if (e.getSource() == acceptBtn)        { acceptClicked(); }
        else if (e.getSource() == regBackBtn)       { registerBackClicked(); }
        else if (e.getSource() == findLobbyBtn)     { findLobbyClicked(); }
        else if (e.getSource() == logoutBtn)        { logoutClicked(); }
        else if (e.getSource() == matchHistoryBtn)  { matchHistoryClicked(); }
        else if (e.getSource() == mhBackBtn)        { matchHistoryBackClicked(); }
        else if (e.getSource() == leaveLobbyBtn)    { leaveLobbyClicked(); }
    }

}
