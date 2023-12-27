package main.menuhelper;

import java.awt.*;

public class BoundsManager {

    private static final int x = 350;
    private static final int y = 150;
    private static final int width = 280;
    private static final int height = 60;

    private static final Rectangle playBtnBounds = new Rectangle(x, y, width, height);
    private static final Rectangle mpBtnBounds = new Rectangle(x, y + 75, width, height);
    private static final Rectangle exitBtnBounds = new Rectangle(x, y + 150, width, height);
    private static final Rectangle loginBtnBounds = new Rectangle(x, y, width, height);
    private static final Rectangle regBtnBounds = new Rectangle(x, y + 75, width, height);
    private static final Rectangle mpBackBtnBounds= new Rectangle(x, y + 150, width, height);
    private static final Rectangle continueBtnBounds = new Rectangle(x, y + 150, width, height);
    private static final Rectangle loginBackBtnBounds = new Rectangle(x, y + 225, width, height);
    private static final Rectangle acceptBtnBounds = new Rectangle(x, y + 150, width, height);
    private static final Rectangle regBackBtnBounds = new Rectangle(x, y + 225, width, height);
    private static final Rectangle findLobbyBtnBounds = new Rectangle(x, y, width, height);
    private static final Rectangle logoutBtnBounds = new Rectangle(x, y + 75, width, height);
    private static final Rectangle lbBtnBounds = new Rectangle(x, y + 150, width, height);
    private static final Rectangle leaveLobbyBtnBounds = new Rectangle(x, y + 75, width, height);

    public static Rectangle getPlayBtnBounds() { return playBtnBounds; }
    public static Rectangle getMpBtnBounds() { return mpBtnBounds; }
    public static Rectangle getExitBtnBounds() { return exitBtnBounds; }
    public static Rectangle getLoginBtnBounds() { return loginBtnBounds; }
    public static Rectangle getRegBtnBounds() { return regBtnBounds; }
    public static Rectangle getMpBackBtnBounds() { return mpBackBtnBounds; }
    public static Rectangle getContinueBtnBounds() { return continueBtnBounds; }
    public static Rectangle getLoginBackBtnBounds() { return loginBackBtnBounds; }
    public static Rectangle getAcceptBtnBounds() { return acceptBtnBounds; }
    public static Rectangle getRegBackBtnBounds() { return regBackBtnBounds; }
    public static Rectangle getFindLobbyBtnBounds() { return findLobbyBtnBounds; }
    public static Rectangle getLogoutBtnBounds() { return logoutBtnBounds; }
    public static Rectangle getLbBtnBounds() { return lbBtnBounds; }
    public static Rectangle getLeaveLobbyBtnBounds() { return leaveLobbyBtnBounds; }

    private static final Rectangle userLblBounds = new Rectangle(x - 75, y, width - 90, height);
    private static final Rectangle pwLblBounds = new Rectangle(x - 75, y + 75, width  - 90, height);
    private static final Rectangle waitingLblBounds = new Rectangle(x - 150, y - 5, width + 325, height);
    private static final Rectangle emptyUserAndPwLblBounds = new Rectangle(x - 200, y + 300, width + 400, height - 10);
    private static final Rectangle regSuccessLblBounds = new Rectangle(x - 140, y + 300, width + 275, height - 10);
    private static final Rectangle userExistsBounds = new Rectangle(x - 120, y + 300, width + 225, height - 10);
    private static final Rectangle userNotFoundLblBounds = new Rectangle(x - 70, y + 300, width + 100, height - 10);

    public static Rectangle getUserLblBounds() { return userLblBounds; }
    public static Rectangle getPwLblBounds() { return pwLblBounds; }
    public static Rectangle getWaitingLblBounds() { return waitingLblBounds; }
    public static Rectangle getEmptyUserAndPwLblBounds() { return emptyUserAndPwLblBounds; }
    public static Rectangle getRegSuccessLblBounds() { return regSuccessLblBounds; }
    public static Rectangle getUserExistsBounds() { return userExistsBounds; }
    public static Rectangle getUserNotFoundLblBounds() { return userNotFoundLblBounds; }

    private static final Rectangle userFieldBounds = new Rectangle(x + 125, y, width - 55 , height);
    private static final Rectangle pwFieldBounds = new Rectangle(x + 125, y + 75, width - 55, height);

    public static Rectangle getUserFieldBounds() { return userFieldBounds; }
    public static Rectangle getPwFieldBounds() { return pwFieldBounds; }
}
