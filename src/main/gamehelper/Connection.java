package main.gamehelper;

import io.socket.client.IO;
import io.socket.client.Socket;
import main.menu.MainMenu;

import java.net.URISyntaxException;
import java.util.HashMap;

import static main.constants.Server.*;

public class Connection {

    private Socket socket = null;

    public void connect(){

        try {
            socket = IO.socket(URI);

            socket.on(Socket.EVENT_CONNECT, args -> {
                MainMenu.getServerOnlineLbl().setVisible(true);
                MainMenu.getServerOfflineLbl().setVisible(false);
            }).on(Socket.EVENT_DISCONNECT, args -> {
                MainMenu.getServerOnlineLbl().setVisible(false);
                MainMenu.getServerOfflineLbl().setVisible(true);
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
            socket.emit(SEND_REGISTER, dict);
        }
    }

    public void login(String username, String password){
        if(socket != null) {
            socket.emit(SEND_LOGIN, username, password);
        }
    }

    public void logout(){
        if(socket != null) {
            socket.emit(SEND_LOGOUT);
        }
    }

    public void findLobby(){
        if(socket != null) {
            socket.emit(SEND_FIND_LOBBY);
        }
    }

    public void leaveLobby(){
        if(socket != null) {
            socket.emit(SEND_LEAVE_LOBBY);
        }
    }

    public void sendLapTime(double lapTime){
        if(socket != null) {
            socket.emit(SEND_LAP_TIME, lapTime);
        }
    }

    public void sendFinishedRace(){
        if(socket != null) {
            socket.emit(SEND_FINISH_RACE);
        }
    }

    public void ready(){
        if(socket != null) {
            socket.emit(SEND_START_GAME);
        }
    }

    public void sendPosition(double position, double playerX, double steer, double upDown){
        if(socket != null) {
            socket.emit(SEND_POSITION, position, playerX, steer, upDown);
        }
    }

    public void bestTrackTimes(int track) {
        if(socket != null) {
            socket.emit("best_track_times", track);
        }
    }
}
