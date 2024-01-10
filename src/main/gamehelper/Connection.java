package main.gamehelper;

import io.socket.client.IO;
import io.socket.client.Socket;
import main.menu.MainMenu;

import java.net.URISyntaxException;
import java.util.HashMap;

import static main.constants.Server.*;

/*
 * Manage the server connection
 * - client invoked server functions
 */
public class Connection {

    private Socket socket = null;

    // connect to the server
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
            System.out.println(e);
        }
    }

    public Socket getSocket(){
        return socket;
    }

    // send player registration data
    public void register(String username, String password){
        if(socket != null){
            HashMap<String, String> dict = new HashMap<>();
            dict.put("username", username);
            dict.put("passwort", password);
            dict.put("loggedIn", "false");
            socket.emit(SEND_REGISTER, dict);
        }
    }

    // send player login data
    public void login(String username, String password){
        if(socket != null) {
            socket.emit(SEND_LOGIN, username, password);
        }
    }

    // send player logged out
    public void logout(){
        if(socket != null) {
            socket.emit(SEND_LOGOUT);
        }
    }

    // send find lobby request
    public void findLobby(){
        if(socket != null) {
            socket.emit(SEND_FIND_LOBBY);
        }
    }

    // send player left lobby
    public void leaveLobby(){
        if(socket != null) {
            socket.emit(SEND_LEAVE_LOBBY);
        }
    }

    // send player lap time when lap is finished
    public void sendLapTime(double lapTime){
        if(socket != null) {
            socket.emit(SEND_LAP_TIME, lapTime);
        }
    }

    // send player finished the race when race is finished
    public void sendFinishedRace(){
        if(socket != null) {
            socket.emit(SEND_FINISH_RACE);
        }
    }

    // send client is ready when game is loaded
    public void ready(){
        if(socket != null) {
            socket.emit(SEND_START_GAME);
        }
    }

    // send player position each frame
    public void sendPosition(double position, double playerX, double steer, double upDown){
        if(socket != null) {
            socket.emit(SEND_POSITION, position, playerX, steer, upDown);
        }
    }

    // send highscore request for track number
    public void bestTrackTimes(int track) {
        if(socket != null) {
            socket.emit("best_track_times", track);
        }
    }
}
