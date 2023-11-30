package main.constants;

public class Settings {

    // Window settings
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;
    // public static final int SCREEN_WIDTH = 2048;
    // public static final int SCREEN_HEIGHT = 1536;
    public static final String SCREEN_TITLE = "Racer Java Client";
    public static final boolean SCREEN_RESIZABLE = false;
    public static final boolean SCREEN_VISIBLE = true;

    // Gameloop settings
    public static final double FPS = 60.0;
    public static final double STEP = 1/FPS;

    // Camera
    public static final int DRAW_DISTANCE = 300;
    public static final double CAMERA_HEIGHT = 1000;
    public static final double FOV = 100;
    public static final double CAMERA_DEPTH = 1 / (Math.tan((FOV /2) * Math.PI/180));


    // Road
    public static final int LANES = 3;
    public static final int ROAD_WIDTH = 2000;
    public static final int SEGMENT_LENGTH = 200;
    public static final int RUMBLE_LENGTH = 3;


    // Player
    public static final double PLAYER_Z = CAMERA_HEIGHT * CAMERA_DEPTH;
    public static final double CENTRIFUGAL = 0.3;
    public static final double MAX_SPEED = SEGMENT_LENGTH / STEP;
    public static final double ACCEL =  MAX_SPEED /5;
    public static final double BREAKING = -MAX_SPEED;
    public static final double DECEL = -MAX_SPEED /5;
    public static final double OFF_ROAD_DECEL = -MAX_SPEED /2;
    public static final double OFF_ROAD_LIMIT =  MAX_SPEED /4;
    public static final double PLAYERX_LIMIT =  2;


   // Server
   public static final String URI = "http://localhost:3000";
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String FIND_LOBBY = "find_lobby";
    public static final String START_GAME = "start_game";
    public static final String END_GAME = "end_game";
    public static final String FINISH_RACE = "finished_race";
    public static final String SEND_LAP_TIME = "lap_time";
    public static final String GET_BEST_LAP_TIMES = "best_lap_times";


}
