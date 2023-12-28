package main.music;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    Clip clip;
    AudioInputStream audioInputStream;
    static String filePath;

    public MusicPlayer(){
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            // create clip reference
            clip = AudioSystem.getClip();

            // open audioInputStream to the clip
            clip.open(audioInputStream);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println();
        }



    }

}
