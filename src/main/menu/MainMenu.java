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

import org.json.JSONArray;
import org.json.JSONException;

public class MainMenu extends JFrame implements ActionListener {

    // All JLabels
    private final JLabel mainMenuLbl = new JLabel("MAIN MENU");
    private final JLabel userLbl = new JLabel("Username:");
    private final JLabel pwLbl = new JLabel("Password:");
    private final JLabel waitingLbl = new JLabel("Waiting for another player...");
    private final JLabel emptyUserAndPwLbl = new JLabel("Username and Password can't be empty!");
    private final JLabel regSuccessLbl = new JLabel("Your registration was successful!");
    private final JLabel userExistsLbl = new JLabel("The user is already registered!");
    private final JLabel loginFailedLbl = new JLabel("The login has failed!");
    private final JLabel serverStatusLbl = new JLabel("Server:");
    private static final JLabel serverOnlineLbl = new JLabel("Online");
    private static final JLabel serverOfflineLbl = new JLabel("Offline");

    // All JButtons
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
    private final JButton leaderboardBtn = new JButton();
    private final JButton lbBackBtn = new JButton();
    private final JButton leaveLobbyBtn = new JButton();

    // Grouped lists of menu components
    List<JComponent> mainMenuButtons = Arrays.asList(playBtn, mpBtn, controlsBtn, exitBtn);
    List<JComponent> multiplayerButtons = Arrays.asList(loginBtn, regBtn, mpBackBtn);
    List<JComponent> loginButtons = Arrays.asList(continueBtn, loginBackBtn);
    List<JComponent> registerButtons = Arrays.asList(acceptBtn, regBackBtn);
    List<JComponent> preLobbyButtons = Arrays.asList(findLobbyBtn, logoutBtn, leaderboardBtn);
    List<JComponent> controlsButtons = List.of(controlsBackBtn);
    List<JComponent> matchHistoryButtons = List.of(lbBackBtn);
    List<JComponent> lobbyButtons = List.of(leaveLobbyBtn);
    List<JComponent> usernameComponents = Arrays.asList(UsernameField, userLbl);
    List<JComponent> passwordComponents = Arrays.asList(PasswordField, pwLbl);

    // Booleans for some menu functions
    private boolean isLoginCorrect = false;
    private boolean registerBool = false;
    private boolean arrowKeysVisible = false;
    private boolean lbBtnPressed = false;

    // Other required variables
    private ImageIcon menuBackground;
    private ImageIcon arrowKeys;
    private String username;
    private String password;
    private final String[] leaderboard = new String[20];
    private final Connection connection = new Connection();

    // Online and Offline Status gets updated in the Connection class
    public static JLabel getServerOnlineLbl() { return serverOnlineLbl; }
    public static JLabel getServerOfflineLbl() { return serverOfflineLbl; }

    // Calls the init method and establishes the server connection
    public MainMenu() {
        init();
        connection.connect();
        connection.bestTrackTimes(1);
        serverFunctions(connection.getSocket());
    }

    // Initializes everything
    private void init(){
        initButtons();
        setupMenuComponents();
        setupMenuWindow();
    }

    // Sets up the menu frame
    private void setupMenuWindow() {
        this.setSize(menuBackground.getIconWidth(), menuBackground.getIconHeight());
        this.setTitle(Settings.SCREEN_TITLE);
        this.setResizable(Settings.SCREEN_RESIZABLE);
        this.setVisible(Settings.SCREEN_VISIBLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(mainMenuLbl);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    // Sets up all menu components
    private void setupMenuComponents() {
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
        setupUserDataFields();
        setupLabels();
        addLabelsToMenu();
    }

    // Functions that are addressed by the server
    private void serverFunctions(Socket socket) {
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
                GameSetup setup = new GameSetup(GameMode.MULTI_PLAYER, 1, 1, username);
                setup.setMultiplayerParameters((String)args[0], (String)args[1]);
                connection.ready();
                startGame(setup);
            }
        }).on("score_board", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                int leaderboardIndex = 0; // Index für leaderboard
                for (Object arg : args) {  // Iterieren über alle args-Elemente
                    System.out.println(args[0]);
                    //System.out.println(args[1]);
                    if (arg instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) arg;

                        try {
                            // Iterieren über das JSONArray und Extrahieren jedes Elements
                            for (int i = 0; i < jsonArray.length() && leaderboardIndex < leaderboard.length; i++) {
                                Object item = jsonArray.get(i); // Holen des Elements am Index i

                                if (item instanceof JSONArray) {
                                    JSONArray innerArray = (JSONArray) item;
                                    for (int k = 0; k < innerArray.length() && leaderboardIndex < leaderboard.length; k++) {
                                        leaderboard[leaderboardIndex++] = innerArray.optString(k, null);
                                    }
                                } else {
                                    if (item instanceof String) {
                                        // Wenn das Element ein String ist
                                        leaderboard[leaderboardIndex++] = (String) item;
                                    } else if (item != null) {
                                        // Für andere Typen (wie Zahlen), konvertieren Sie es zu einem String
                                        leaderboard[leaderboardIndex++] = item.toString();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Zum Testen: Ausgabe des aktualisierten Leaderboards
                for (int i = 0; i < leaderboard.length; i++) {
                    System.out.println(leaderboard[i]);
                }
            }
        });
    }


    // Initializes all buttons
    private void initButtons() {
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
        ButtonManager.createButton(leaderboardBtn, "Leaderboard", LbBtnBounds, this);

        ButtonManager.createButton(lbBackBtn, "Back", LbBackBtnBounds, this);

        ButtonManager.createButton(leaveLobbyBtn, "Leave Lobby", LeaveLobbyBtnBounds, this);
    }

    // Adds all buttons to the main menu label
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
        addButtonToLabel(mainMenuLbl, leaderboardBtn);

        addButtonToLabel(mainMenuLbl, lbBackBtn);

        addButtonToLabel(mainMenuLbl, leaveLobbyBtn);
    }

    // Adds one Button to the label
    private void addButtonToLabel(JLabel label, JButton button) {
        label.add(button);
    }

    // Setups the initial visibility of the menu components
    private void setVisibilityOfAllButtons() {
        VisibilityManager.showComponents(mainMenuButtons);
        VisibilityManager.hideComponents(controlsButtons);
        VisibilityManager.hideComponents(multiplayerButtons);
        VisibilityManager.hideComponents(loginButtons);
        VisibilityManager.hideComponents(registerButtons);
        VisibilityManager.hideComponents(preLobbyButtons);
        VisibilityManager.hideComponents(matchHistoryButtons);
        VisibilityManager.hideComponents(lobbyButtons);
    }

    // Sets up the username and password fields
    private void setupUserDataFields() {
        UserInputManager.setupInputField(UsernameField,8);
        UsernameField.setBounds(UserFieldBounds);
        mainMenuLbl.add(UsernameField);
        UsernameField.setVisible(false);

        UserInputManager.setupInputField(PasswordField,12);
        PasswordField.setBounds(PwFieldBounds);
        mainMenuLbl.add(PasswordField);
        PasswordField.setVisible(false);
    }

    // Sets up all labels
    private void setupLabels() {
        LabelManager.createLabel(userLbl, false, FontSize40, Color.BLACK, UserBounds);
        LabelManager.createLabel(pwLbl, false, FontSize40, Color.BLACK, PwBounds);
        LabelManager.createLabel(waitingLbl, false, FontSize50, Color.RED, WaitingBounds);
        LabelManager.createLabel(emptyUserAndPwLbl, false, FontSize40, Color.RED, NoInputBounds);
        LabelManager.createLabel(regSuccessLbl, false, FontSize40, new Color(0, 150, 0), RegSuccessBounds);
        LabelManager.createLabel(loginFailedLbl, false, FontSize40, Color.RED, LoginFailedBounds);
        LabelManager.createLabel(userExistsLbl, false, FontSize40, new Color(255, 130, 0), UserExistsBounds);
        LabelManager.createLabel(serverStatusLbl, true, FontSize30, Color.BLACK, ServerBounds);
        LabelManager.createLabel(serverOnlineLbl, false, FontSize30, new Color(0, 150, 0), ServerOnlineBounds);
        LabelManager.createLabel(serverOfflineLbl, true,  FontSize30, Color.RED, ServerOfflineBounds);
    }

    // Sets the text of the label
    private void setTextOfLabel(JLabel label, String text) {
        label.setText(text);
    }

    // Adds all label to the menu
    private void addLabelsToMenu() {
        mainMenuLbl.add(userLbl);
        mainMenuLbl.add(pwLbl);
        mainMenuLbl.add(waitingLbl);
        mainMenuLbl.add(emptyUserAndPwLbl);
        mainMenuLbl.add(regSuccessLbl);
        mainMenuLbl.add(userExistsLbl);
        mainMenuLbl.add(loginFailedLbl);
        mainMenuLbl.add(serverStatusLbl);
        mainMenuLbl.add(serverOnlineLbl);
        mainMenuLbl.add(serverOfflineLbl);
    }

    // Displays the game controls
    private void drawArrowKeys(Graphics g) {
        if (arrowKeysVisible) {
            int x = 345;
            int y = 175;
            int width = arrowKeys.getIconWidth();
            int height = arrowKeys.getIconHeight();
            g.drawImage(arrowKeys.getImage(), x, y, width, height, null);
        }
    }

    private void drawTable(Graphics g) {
        if (lbBtnPressed) {
            // White Background
            drawRect(g, 275, 175, 450, 450, Color.BLACK);

            Graphics2D g2 = (Graphics2D) g;
            float lineWidth = 4.0f;
            g2.setStroke(new BasicStroke(lineWidth));

            // Horizontal lines
            g.drawLine(275, 175, 725, 175);
            g.drawLine(275, 225, 725, 225);
            g.drawLine(275, 265, 725, 265);
            g.drawLine(275, 305, 725, 305);
            g.drawLine(275, 345, 725, 345);
            g.drawLine(275, 385, 725, 385);
            g.drawLine(275, 425, 725, 425);
            g.drawLine(275, 465, 725, 465);
            g.drawLine(275, 505, 725, 505);
            g.drawLine(275, 545, 725, 545);
            g.drawLine(275, 585, 725, 585);
            g.drawLine(275, 625, 725, 625);

            // Vertical lines
            g.drawLine(275, 175, 275, 625);
            g.drawLine(360, 175, 360, 625);
            g.drawLine(575, 175, 575, 625);
            g.drawLine(725, 175, 725, 625);

            // Column headlines
            drawText(g, "Rank", 280, 210, Color.RED, FontSize35);
            drawText(g, "Player", 365, 210, Color.RED, FontSize35);
            drawText(g, "Time", 580, 210, Color.RED, FontSize35);

            // Text positions of rank data
            drawText(g, "1", 280, 257, Color.BLUE, FontSize30);
            drawText(g, "2", 280, 297, Color.BLUE, FontSize30);
            drawText(g, "3", 280, 337, Color.BLUE, FontSize30);
            drawText(g, "4", 280, 377, Color.BLUE, FontSize30);
            drawText(g, "5", 280, 417, Color.BLUE, FontSize30);
            drawText(g, "6", 280, 457, Color.BLUE, FontSize30);
            drawText(g, "7", 280, 497, Color.BLUE, FontSize30);
            drawText(g, "8", 280, 537, Color.BLUE, FontSize30);
            drawText(g, "9", 280, 577, Color.BLUE, FontSize30);
            drawText(g, "10", 280, 617, Color.BLUE, FontSize30);

            // Text positions of player data
            drawText(g, leaderboard[0], 365, 257, Color.BLUE, FontSize30);
            /*
            drawText(g, leaderboard[2], 365, 297, Color.BLUE, FontSize30);
            drawText(g, leaderboard[4], 365, 337, Color.BLUE, FontSize30);
            drawText(g, leaderboard[6], 365, 377, Color.BLUE, FontSize30);
            drawText(g, leaderboard[8], 365, 417, Color.BLUE, FontSize30);
            drawText(g, leaderboard[10], 365, 457, Color.BLUE, FontSize30);
            drawText(g, leaderboard[12], 365, 497, Color.BLUE, FontSize30);
            drawText(g, leaderboard[14], 365, 537, Color.BLUE, FontSize30);
            drawText(g, leaderboard[16], 365, 577, Color.BLUE, FontSize30);
            drawText(g, leaderboard[18], 365, 617, Color.BLUE, FontSize30);
            */


            // Text positions of time data
            drawText(g, leaderboard[1], 580, 257, Color.BLUE, FontSize30);
            /*
            drawText(g, leaderboard[3], 580, 297, Color.BLUE, FontSize30);
            drawText(g, leaderboard[5], 580, 337, Color.BLUE, FontSize30);
            drawText(g, leaderboard[7], 580, 377, Color.BLUE, FontSize30);
            drawText(g, leaderboard[9], 580, 417, Color.BLUE, FontSize30);
            drawText(g, leaderboard[11], 580, 457, Color.BLUE, FontSize30);
            drawText(g, leaderboard[13], 580, 497, Color.BLUE, FontSize30);
            drawText(g, leaderboard[15], 580, 537, Color.BLUE, FontSize30);
            drawText(g, leaderboard[17], 580, 577, Color.BLUE, FontSize30);
            drawText(g, leaderboard[19], 580, 617, Color.BLUE, FontSize30);
            */



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

    private void drawText(Graphics g, String text, int x, int y, Color color, Font font) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
    }

    // Verifies the login data
    private void verifyLoginData() {
        if (isLoginCorrect) {
            emptyUserAndPwLbl.setVisible(false);
            loginFailedLbl.setVisible(false);

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
            loginFailedLbl.setVisible(true);
        }
    }

    // Checks if the user already exists
    private void checkIfUserExists() {
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

    // Starts the game
    private void startGame(GameSetup setup) {
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

    // Executes certain actions after pressing the play button
    private void playClicked() {
        GameSetup setup = new GameSetup(GameMode.SINGLE_PLAYER,1,1,"PLAYER");
        startGame(setup);
    }

    // Executes certain actions after pressing the multiplayer button
    private void multiplayerClicked() {
        VisibilityManager.hideComponents(mainMenuButtons);
        VisibilityManager.showComponents(multiplayerButtons);

        setTextOfLabel(mainMenuLbl, "MULTIPLAYER");
    }

    // Executes certain actions after pressing the controls button
    private void controlsClicked() {
        VisibilityManager.hideComponents(mainMenuButtons);
        VisibilityManager.showComponents(controlsButtons);

        setTextOfLabel(mainMenuLbl, "CONTROLS");

        arrowKeysVisible = true;
        repaint();
    }

    // Executes certain actions after pressing the exit button
    private void exitClicked() {
        System.exit(0);
    }

    // Executes certain actions after pressing the (controls) back button
    private void controlsBackClicked() {
        VisibilityManager.hideComponents(controlsButtons);
        VisibilityManager.showComponents(mainMenuButtons);

        arrowKeysVisible = false;
        setTextOfLabel(mainMenuLbl, "MAIN MENU");

    }

    // Executes certain actions after pressing the login button
    private void loginClicked() {
        VisibilityManager.hideComponents(multiplayerButtons);
        VisibilityManager.showComponents(loginButtons);
        VisibilityManager.showComponents(usernameComponents);
        VisibilityManager.showComponents(passwordComponents);

        setTextOfLabel(mainMenuLbl, "LOGIN");
    }

    // Executes certain actions after pressing the register button
    private void registerClicked() {
        VisibilityManager.hideComponents(multiplayerButtons);
        VisibilityManager.showComponents(registerButtons);
        VisibilityManager.showComponents(usernameComponents);
        VisibilityManager.showComponents(passwordComponents);

        setTextOfLabel(mainMenuLbl, "REGISTER");
    }

    // Executes certain actions after pressing the (multiplayer) back button
    private void multiplayerBackClicked() {
        VisibilityManager.hideComponents(multiplayerButtons);
        VisibilityManager.showComponents(mainMenuButtons);

        setTextOfLabel(mainMenuLbl, "MAIN MENU");

    }

    // Executes certain actions after pressing the continue button
    private void continueClicked() {
        if (UsernameList.isEmpty() || PasswordList.isEmpty()) {
            emptyUserAndPwLbl.setVisible(true);
            loginFailedLbl.setVisible(false);
        }
        else {
            username = UserInputManager.listToString(UsernameList);
            password = UserInputManager.listToString(PasswordList);
            connection.login(username, password);
        }
    }

    // Executes certain actions after pressing the (login) back button
    private void loginBackClicked() {
        VisibilityManager.hideComponents(usernameComponents);
        VisibilityManager.hideComponents(passwordComponents);
        VisibilityManager.hideComponents(loginButtons);
        VisibilityManager.showComponents(multiplayerButtons);

        emptyUserAndPwLbl.setVisible(false);
        loginFailedLbl.setVisible(false);

        UsernameField.setText("");
        PasswordField.setText("");

        // Empties the lists after pressing the back button
        UsernameList.clear();
        PasswordList.clear();

        setTextOfLabel(mainMenuLbl, "MULTIPLAYER");
    }

    // Executes certain actions after pressing the accept button
    private void acceptClicked() {
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

    // Executes certain actions after pressing the (register) back button
    private void registerBackClicked() {
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

    // Executes certain actions after pressing the find lobby button
    private void findLobbyClicked() {
        VisibilityManager.hideComponents(preLobbyButtons);
        VisibilityManager.showComponents(lobbyButtons);

        waitingLbl.setVisible(true);
        connection.findLobby();

        setTextOfLabel(mainMenuLbl, "LOBBY");
    }

    // Executes certain actions after pressing the logout button
    private void logoutClicked() {
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

    // Executes certain actions after pressing the leaderboard button
    private void leaderboardClicked() {
        // TODO implement a complete dynamic display of a match history
        VisibilityManager.hideComponents(preLobbyButtons);
        VisibilityManager.showComponents(matchHistoryButtons);


        setTextOfLabel(mainMenuLbl, "LEADERBOARD");
        lbBtnPressed = true;
        repaint();
    }

    // Executes certain actions after pressing the (leaderboard) back button
    private void leaderboardBackClicked() {
        VisibilityManager.hideComponents(matchHistoryButtons);
        VisibilityManager.showComponents(preLobbyButtons);

        lbBtnPressed = false;
        setTextOfLabel(mainMenuLbl, "PRE-LOBBY");
    }

    // Executes certain actions after pressing the leave lobby button
    private void leaveLobbyClicked() {
        VisibilityManager.hideComponents(lobbyButtons);
        VisibilityManager.showComponents(preLobbyButtons);

        waitingLbl.setVisible(false);
        connection.leaveLobby();

        setTextOfLabel(mainMenuLbl, "PRE-LOBBY");
    }

    // Controls the actions of every button
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
        else if (e.getSource() == leaderboardBtn)   { leaderboardClicked(); }
        else if (e.getSource() == lbBackBtn)        { leaderboardBackClicked(); }
        else if (e.getSource() == leaveLobbyBtn)    { leaveLobbyClicked(); }
    }

}
