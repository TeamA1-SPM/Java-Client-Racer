package main.menuhelper;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import main.Game;
import main.Window;
import main.constants.GameMode;
import main.menu.MainMenu;

public class ServerFunctionManager {

    //private static MainMenu mainmenu;
    private static boolean isLoginCorrect;

    public static boolean isLoginCorrect() { return isLoginCorrect; }

    public static void serverFunctionLogin(Socket socket) {
        socket.on("login_success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                isLoginCorrect = (boolean) args[0];
                System.out.println("Login " + isLoginCorrect);
            }
        });
    }

    public static void serverFunctionRegister(Socket socket) {
        socket.on("register_success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                boolean register = (boolean) args[0];
                System.out.println("Register " + register);
            }
        });
    }

    public static void serverFunctionStartGame(Socket socket) {
        socket.on("start_game", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                MainMenu menu = new MainMenu();
                menu.startGame();
            }
        });
    }
}
