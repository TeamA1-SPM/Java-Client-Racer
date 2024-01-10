package main.music;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.File;

/*
 * Basic music player implementation
 */
public class MusicPlayer {
    Clip clip;
    AudioInputStream audioInputStream;

    public MusicPlayer(){
        try {
            String filePath = "./music/racer.wav";
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void stop(){
        if(clip != null) {
            clip.stop();
        }
    }
}