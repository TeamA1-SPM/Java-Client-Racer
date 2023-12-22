package main.constants;

public class Server {

    // Server
    public static final String URI = "https://racing-server.onrender.com";           // server address
    public static final String REGISTER = "register";                   // player register function
    public static final String LOGIN = "login";                         // player login function
    public static final String LOGOUT = "logout";                       // player logout function
    public static final String FIND_LOBBY = "find_lobby";
    public static final String START_GAME = "game_rendered";
    public static final String END_GAME = "end_game";
    public static final String FINISH_RACE = "finished_race";
    public static final String SEND_LAP_TIME = "lap_time";
    public static final String SEND_POSITION = "display_player";

    // Server invoked
    public static final String GET_BEST_LAP_TIMES = "best_lap_times";
    public static final String SERVER_COUNTDOWN = "countdown";
    public static final String GET_POSITION = "epp";
}
