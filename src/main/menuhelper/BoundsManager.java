package main.menuhelper;

import java.awt.*;

public class BoundsManager {

    // Rectangle attributes
    private static final int x = 350;
    private static final int y = 150;
    private static final int width = 280;
    private static final int height = 60;

    // Button Bounds
    public static final Rectangle PlayBtnBounds = new Rectangle(x, y, width, height);
    public static final Rectangle MpBtnBounds = new Rectangle(x, y + 75, width, height);
    public static final Rectangle ControlsBtnBounds = new Rectangle(x, y + 150, width, height);
    public static final Rectangle ExitBtnBounds = new Rectangle(x, y + 225, width, height);
    public static final Rectangle ControlsBackBtnBounds = new Rectangle(x, y + 225, width, height);
    public static final Rectangle LoginBtnBounds = new Rectangle(x, y, width, height);
    public static final Rectangle RegBtnBounds = new Rectangle(x, y + 75, width, height);
    public static final Rectangle MpBackBtnBounds= new Rectangle(x, y + 150, width, height);
    public static final Rectangle ContinueBtnBounds = new Rectangle(x, y + 150, width, height);
    public static final Rectangle LoginBackBtnBounds = new Rectangle(x, y + 225, width, height);
    public static final Rectangle AcceptBtnBounds = new Rectangle(x, y + 150, width, height);
    public static final Rectangle RegBackBtnBounds = new Rectangle(x, y + 225, width, height);
    public static final Rectangle FindLobbyBtnBounds = new Rectangle(x, y, width, height);
    public static final Rectangle LogoutBtnBounds = new Rectangle(x, y + 75, width, height);
    public static final Rectangle LbBtnBounds = new Rectangle(x, y + 150, width, height);
    public static final Rectangle LbBackBtnBounds = new Rectangle(x, y + 500, width, height);
    public static final Rectangle LeaveLobbyBtnBounds = new Rectangle(x, y + 75, width, height);

    // Label Bounds
    public static final Rectangle UserBounds = new Rectangle(x - 75, y, width - 90, height);
    public static final Rectangle PwBounds = new Rectangle(x - 75, y + 75, width  - 90, height);
    public static final Rectangle WaitingBounds = new Rectangle(x - 150, y - 5, width + 325, height);
    public static final Rectangle NoInputBounds = new Rectangle(x - 200, y + 300, width + 400, height - 10);
    public static final Rectangle RegSuccessBounds = new Rectangle(x - 140, y + 300, width + 275, height - 10);
    public static final Rectangle UserExistsBounds = new Rectangle(x - 120, y + 300, width + 225, height - 10);
    public static final Rectangle LoginFailedBounds = new Rectangle(x - 70, y + 300, width + 100, height - 10);
    public static final Rectangle ServerBounds = new Rectangle(x - 350, y - 150, width - 185, height - 25);
    public static final Rectangle ServerOnlineBounds = new Rectangle(x - 255, y - 150, width - 195, height - 25);
    public static final Rectangle ServerOfflineBounds = new Rectangle(x - 255, y - 150, width - 185, height - 25);

    // User Data Bounds
    public static final Rectangle UserFieldBounds = new Rectangle(x + 125, y, width - 55 , height);
    public static final Rectangle PwFieldBounds = new Rectangle(x + 125, y + 75, width - 55, height);

}
