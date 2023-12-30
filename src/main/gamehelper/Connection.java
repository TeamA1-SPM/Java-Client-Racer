package main.gamehelper;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import main.menu.MainMenu;

import java.net.URISyntaxException;
import java.util.HashMap;

import static main.constants.Server.*;

public class Connection {

    private Socket socket = null;

    public void connect(){

        try {
            socket = IO.socket(URI);

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    MainMenu.getServerOnlineLbl().setVisible(true);
                    MainMenu.getServerOfflineLbl().setVisible(false);
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    MainMenu.getServerOnlineLbl().setVisible(false);
                    MainMenu.getServerOfflineLbl().setVisible(true);
                }
            });
            socket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket(){
        return socket;
    }

    public void register(String username, String password){
        if(socket != null){
            HashMap<String, String> dict = new HashMap<>();
            dict.put("username", username);
            dict.put("passwort", password);
            dict.put("loggedIn", "false");
            socket.emit(REGISTER, dict);
        }
    }

    public void login(String username, String password){
        if(socket != null) {
            socket.emit(LOGIN, username, password);
        }
    }

    public void logout(){
        if(socket != null) {
            socket.emit(LOGOUT);
        }
    }

    public void findLobby(){
        if(socket != null) {
            socket.emit(FIND_LOBBY);
        }
    }

    public void leaveLobby(){
        if(socket != null) {
            socket.emit(LEAVE_LOBBY);
        }
    }

    public void sendLapTime(double lapTime){
        if(socket != null) {
            socket.emit(SEND_LAP_TIME, lapTime);
        }
    }

    public void sendFinishedRace(){
        if(socket != null) {
            socket.emit(FINISH_RACE);
        }
    }

    public void ready(){
        if(socket != null) {
            socket.emit(START_GAME);
        }
    }
}
