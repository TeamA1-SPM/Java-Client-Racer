package main.helper;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import main.constants.Settings;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Connection {

    private Socket socket = null;

    public void connect(){

        try {
            socket = IO.socket(Settings.URI);

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Connected to server");
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) { System.out.println("Disconnected from server"); }
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
            Map<String, String> dict = new HashMap<>();
            dict.put("passwort", password);
            dict.put("username", username);
            socket.emit(Settings.REGISTER, dict);
        }
    }

    public void login(String username, String password){
        if(socket != null) {
            socket.emit(Settings.LOGIN, username, password);
        }
    }

    public void logout(){
        if(socket != null) {
            socket.emit(Settings.LOGOUT);
        }
    }

    public void findLobby(){
        if(socket != null) {
            socket.emit(Settings.FIND_LOBBY);
        }
    }

    public void sendLapTime(double lapTime){
        if(socket != null) {
            socket.emit(Settings.SEND_LAP_TIME, lapTime);
        }
    }

    public void sendFinishedRace(){
        if(socket != null) {
            socket.emit(Settings.FINISH_RACE);
        }
    }




}
