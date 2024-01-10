package main.constants;

// Game constant parameters
public class Settings {

    // Window settings
    public static final int SCREEN_WIDTH = 1024;          // logical canvas width
    public static final int SCREEN_HEIGHT = 768;          // logical canvas height
    public static final String SCREEN_TITLE = "Racer Java Client";  // game title
    public static final boolean SCREEN_RESIZABLE = false;           // window resize
    public static final boolean SCREEN_VISIBLE = true;              // window visible

    // Game loop settings
    public static final double FPS = 60.0;            // how many 'update' frames per second
    public static final double STEP = 1/FPS;          // how long is each frame (in seconds)

    // Camera
    public static final int DRAW_DISTANCE = 300;      // max segments drawn on the screen
    public static final double CAMERA_HEIGHT = 1000;  // z height of camera
    public static final double FOV = 100;             // angle (degrees) for field of view
    public static final double CAMERA_DEPTH = 1 / (Math.tan((FOV /2) * Math.PI/180));   // z distance camera is from screen (computed)
    public static final double PLAYER_Z = CAMERA_HEIGHT * CAMERA_DEPTH;                 // player relative z distance from camera (computed)

    // Road
    public static final int LANES = 3;                      // number of road lanes
    public static final int ROAD_WIDTH = 2000;              // actually half the roads width, easier math if the road spans from -roadWidth to +roadWidth
    public static final int SEGMENT_LENGTH = 200;           // length of a single segment
    public static final int RUMBLE_LENGTH = 3;              // number of segments per black/white rumble strip

    // Player
    public static final double CENTRIFUGAL = 0.3;                   // centrifugal force multiplier when going around curves
    public static final double MAX_SPEED = SEGMENT_LENGTH / STEP;   // top speed (ensure we can't move more than 1 segment in a single frame to make collision detection easier)
    public static final double ACCEL =  MAX_SPEED /5;               // acceleration rate - tuned until it 'felt' right
    public static final double BREAKING = -MAX_SPEED;               // deceleration rate when braking
    public static final double DECEL = -MAX_SPEED /5;               // 'natural' deceleration rate when neither accelerating, nor braking
    public static final double OFF_ROAD_DECEL = -MAX_SPEED /2;      // off-road deceleration is somewhere in between
    public static final double OFF_ROAD_LIMIT =  MAX_SPEED /4;      // limit when off-road deceleration no longer applies (e.g. you can always go at least this speed even when off-road)
    public static final double PLAYER_X_LIMIT =  2;                 // limit player left and right max movement
    public static final double SPRITE_SCALE =  0.3/80;              // the reference sprite width should be 1/3rd the (half-)roadWidth
    public static final double PLAYER_W =  80 * SPRITE_SCALE;       // player sprite width

}
