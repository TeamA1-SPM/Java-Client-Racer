package main.constants;

public class Server {

    // Client invoked
    public static final String URI = "https://racing-server.onrender.com";           // server address
    public static final String REGISTER = "register";                   // player register function
    public static final String LOGIN = "login";                         // player login function
    public static final String LOGOUT = "logout";                       // player logout function
    public static final String FIND_LOBBY = "find_lobby";               // player find lobby function
    public static final String START_GAME = "game_rendered";            // client ready state
    public static final String FINISH_RACE = "finished_race";           // client finished race
    public static final String SEND_LAP_TIME = "lap_time";              // client finished lap
    public static final String SEND_POSITION = "display_player";        // send player position to server

    // Server invoked
    public static final String GET_BEST_LAP_TIMES = "best_lap_times";   // get enemy player lap time
    public static final String SERVER_COUNTDOWN = "countdown";          // get countdown from server
    public static final String GET_POSITION = "epp";                    // get enemy position from server
    public static final String END_GAME = "end_game";
}
