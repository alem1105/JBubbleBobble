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

/**
 * Questa classe si occupa di creare l'istanza dell'audio manager e riprodurre suoni
 */
public class AudioManager {

    private static AudioManager instance;
    /** Array utilizzato per collezionare le Clip audio delle varie colonne sonore */
    private Clip[] continuousAudios;
    /** Array che contiene i percorsi delle colonne sonore */
    private String[] continuousAudiosPaths;

    public final static String MAIN_THEME = "res/sounds/main_theme.wav";
    public final static int MAIN_THEME_INDEX = 0;

    public final static String GAME_WON = "res/sounds/real_ending.wav";
    public final static int GAME_WON_INDEX = 1;

    public final static String GAME_OVER = "res/sounds/game_over.wav";
    public final static int GAME_OVER_INDEX = 2;

    public final static String SUPER_DRUNK = "res/sounds/super_drunk.wav";
    public final static int SUPER_DRUNK_INDEX = 3;

    public final static String LEVEL_EDITOR = "res/sounds/level_editor.wav";
    public final static int LEVEL_EDITOR_INDEX = 4;

    // Interaction Sounds
    public static String FOOD_PICKUP = "res/sounds/interactionsounds/food_pickup.wav";
    public static String ITEM_PICKUP = "res/sounds/interactionsounds/item_pickup.wav";
    public static String JUMP = "res/sounds/interactionsounds/jump.wav";
    public static String PLAYER_DEATH = "res/sounds/interactionsounds/player_death.wav";
    public static String LETTER_BUBBLE_POP = "res/sounds/interactionsounds/letter_bubble_pop.wav";
    public static String POP_BUBBLE_SINGLE_ENEMY = "res/sounds/interactionsounds/pop_bubble_single_enemy.wav";
    public static String SHOOT_BUBBLE = "res/sounds/interactionsounds/shoot_bubble.wav";
    public static String WATER_FLOW = "res/sounds/interactionsounds/water_flow.wav";


    public static AudioManager getInstance() {
        if (instance == null)
            instance = new AudioManager();
        return instance;
    }

    private AudioManager() {
        continuousAudios = new Clip[5];
        continuousAudiosPaths = new String[]{MAIN_THEME, GAME_WON, GAME_OVER, SUPER_DRUNK, LEVEL_EDITOR};
        loadContinuousAudios(continuousAudiosPaths);
    }

    /**
     * Carica nell'array delle clip tutte le colonne sonore di: gioco, vittoria, sconfitta e boss finale
     * @param continuousAudiosPaths Array con i percorsi dei file audio
     */
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

    /**
     * Ferma tutte le colonne sonore che stanno venendo riprodotte, lo utilizziamo per farne partire una diversa
     */
    public void stopAllContinuousAudios() {
        for (int i = 0; i < continuousAudios.length; i++)
            continuousAudios[i].stop();
    }

    /**
     * Metodo utilizzato per riprodurre un file audio una sola volta, utilizzato per effetti sonori
     * @param filename
     */
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

    /**
     * Permette di riprodurre una colonna sonora prendendo come parametro l'indice corrispondente in {@code continuousAudios}
     * @param index
     */
    public void continuousSoundPlay(int index) {
        if (continuousAudios[index].isRunning())
            return;

        stopAllContinuousAudios();
        continuousAudios[index].start();

        if(index == MAIN_THEME_INDEX || index == LEVEL_EDITOR_INDEX || index == SUPER_DRUNK_INDEX)
            continuousAudios[index].loop(Clip.LOOP_CONTINUOUSLY);
    }
}
