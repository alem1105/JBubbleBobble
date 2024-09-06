package view.utilz;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.io.BufferedInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {

    private static AudioManager instance;
    private Clip mainTheme;
    private Clip[] continuousAudios;
    private String[] continuousAudiosPaths;

    public final static String MAIN_THEME = "res/sounds/main_theme.wav";
    public final static int MAIN_THEME_INDEX = 0;

    public final static String GAME_WON = "res/sounds/real_ending.wav";
    public final static int GAME_WON_INDEX = 1;

    public final static String GAME_OVER = "res/sounds/game_over.wav";
    public final static int GAME_OVER_INDEX = 2;

    // Musica Boss


    public static AudioManager getInstance() {
        if (instance == null)
            instance = new AudioManager();
        return instance;
    }

    private AudioManager() {
        continuousAudios = new Clip[3];
        continuousAudiosPaths = new String[]{MAIN_THEME, GAME_WON, GAME_OVER};
        loadContinuousAudios(continuousAudiosPaths);
    }

    private void loadContinuousAudios(String[] continuousAudiosPaths) {
        try {
            for(int i = 0; i < continuousAudiosPaths.length; i++) {
                InputStream in = new BufferedInputStream(new FileInputStream(continuousAudiosPaths[i]));
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
                Clip currentClip = AudioSystem.getClip();
                currentClip.open(audioIn);
                continuousAudios[i] = currentClip;
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
        } catch (LineUnavailableException e1) {
            e1.printStackTrace();
        }
    }

    public void stopAllContinuousAudios() {
        for (int i = 0; i < continuousAudios.length; i++)
            continuousAudios[i].stop();
    }

    public void oneTimePlay(String filename) {
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filename));
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
        } catch (LineUnavailableException e1) {
            e1.printStackTrace();
        }
    }

    public void continuousSoundPlay(int index) {
        if (continuousAudios[index].isRunning())
            return;

        stopAllContinuousAudios();
        continuousAudios[index].start();
        continuousAudios[index].loop(Clip.LOOP_CONTINUOUSLY);
    }
}
