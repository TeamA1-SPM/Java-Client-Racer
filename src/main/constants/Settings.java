package main.constants;

public class Settings {

    // Window settings
    public static final int SCREEN_WIDTH = 1024;    // logical canvas width
    public static final int SCREEN_HEIGHT = 768;    // logical canvas height
    public static final String SCREEN_TITLE = "Racer Java Client";  // game title
    public static final boolean SCREEN_RESIZABLE = false;           // window resize
    public static final boolean SCREEN_VISIBLE = true;              // window visible

    // Gameloop settings
    public static final double FPS = 60.0;      // how many 'update' frames per second
    public static final double STEP = 1/FPS;    // how long is each frame (in seconds)

    // Camera
    public static final int DRAW_DISTANCE = 300;
    public static final double CAMERA_HEIGHT = 1000;
    public static final double FOV = 100;
    public static final double CAMERA_DEPTH = 1 / (Math.tan((FOV /2) * Math.PI/180));
    public static final double PLAYER_Z = CAMERA_HEIGHT * CAMERA_DEPTH;     // player relative z distance from camera (computed)

    // Road
    public static final int LANES = 3;
    public static final int ROAD_WIDTH = 2000;
    public static final int SEGMENT_LENGTH = 200;
    public static final int RUMBLE_LENGTH = 3;
    public static final int TOTAL_CARS = 200;

    // Player
    public static final double CENTRIFUGAL = 0.3;
    public static final double MAX_SPEED = SEGMENT_LENGTH / STEP;
    public static final double ACCEL =  MAX_SPEED /5;
    public static final double BREAKING = -MAX_SPEED;
    public static final double DECEL = -MAX_SPEED /5;
    public static final double OFF_ROAD_DECEL = -MAX_SPEED /2;
    public static final double OFF_ROAD_LIMIT =  MAX_SPEED /4;
    public static final double PLAYER_X_LIMIT =  3;
    public static final double SPRITE_SCALE =  0.3/80;                // the reference sprite width should be 1/3rd the (half-)roadWidth
    public static final double PLAYER_W =  80 * SPRITE_SCALE;

    // Background
    public static final double SKY_SPEED = 0.006;                   // background sky layer scroll speed when going around curve (or up hill)
    public static final double HILL_SPEED = 0.009;                   // background hill layer scroll speed when going around curve (or up hill)
    public static final double TREE_SPEED = 0.02;                   // background tree layer scroll speed when going around curve (or up hill)

    // Server
    public static final String URI = "https://racing-server-test.onrender.com/";           // server address
    public static final String REGISTER = "register";                   // player register function
    public static final String LOGIN = "login";                         // player login function
    public static final String LOGOUT = "logout";                       // player logout function
    public static final String FIND_LOBBY = "find_lobby";
    public static final String START_GAME = "start_game";
    public static final String END_GAME = "end_game";
    public static final String FINISH_RACE = "finished_race";
    public static final String SEND_LAP_TIME = "lap_time";
    public static final String GET_BEST_LAP_TIMES = "best_lap_times";
    public static final String SERVER_COUNTDOWN = "countdown";


}
