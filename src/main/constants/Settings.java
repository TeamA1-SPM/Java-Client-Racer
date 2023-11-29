package main.constants;

import io.socket.client.Socket;

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
    public static final int drawDistance = 300;
    public static final double cameraHeight = 1000;
    public static final double FOV = 100;
    public static final double cameraDepth = 1 / (Math.tan((FOV /2) * Math.PI/180));

    // Road
   public static final int lanes = 3;
   public static final int roadWidth = 2000;
   public static final int segmentLength = 200;
   public static final int segmentQuantity = 500;
   public static final int rumbleLength = 3;
   public static final int trackLength = segmentLength * segmentQuantity;


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
