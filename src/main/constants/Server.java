package main.constants;

// Multiplayer server function names
public class Server {

    // server address
    public static final String URI = "https://racing-server.onrender.com";

    // Client invoked on server
    public static final String SEND_REGISTER = "register";                  // player register function
    public static final String SEND_LOGIN = "login";                        // player login function
    public static final String SEND_LOGOUT = "logout";                      // player logout function
    public static final String SEND_FIND_LOBBY = "find_lobby";              // player find lobby function
    public static final String SEND_START_GAME = "game_rendered";           // client ready state
    public static final String SEND_FINISH_RACE = "finished_race";          // client finished race
    public static final String SEND_LAP_TIME = "lap_time";                  // client finished lap
    public static final String SEND_POSITION = "display_player";            // send player position to server
    public static final String SEND_LEAVE_LOBBY = "leave_lobby";            // send player position to server

    // Server invoked on client
    public static final String GET_BEST_LAP_TIMES = "best_lap_times";   // get enemy player lap time
    public static final String GET_SERVER_COUNTDOWN = "countdown";      // get countdown from server
    public static final String GET_POSITION = "epp";                    // get enemy position from server
    public static final String GET_RESULT = "end_game";                 // get game results
}
