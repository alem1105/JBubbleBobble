package model;

import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;
import model.gamestate.Gamestate;
import model.gamestate.UserStateModel;
import model.objects.projectiles.ProjectileManagerModel;
import model.objects.bobbles.BubbleManagerModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static model.gamestate.Gamestate.MENU;

/**
 * Classe che gestisce i livelli del gioco, fornendo metodi per caricare,
 * costruire e navigare tra i vari livelli.
 */
public class LevelManagerModel {

    private static LevelManagerModel instance;
    /** Lista di tutti i livelli */
    private ArrayList<LevelModel> levels;
    /** Indice del livello corrente */
    private int lvlIndex = 0;
    /** Indica se passare al prossimo livello */
    private boolean nextLevel = false;
    /** Usato per indicare quanti livelli saltare con gli ombrelli */
    private int levelSkipped = 1;
    /** Indica se sono stati superati tutti i livelli */
    private boolean gameWon;

    /**
     * Ottiene l'istanza singleton di LevelManagerModel.
     *
     * @return L'istanza di LevelManagerModel.
     */
    public static LevelManagerModel getInstance() {
        if (instance == null) {
            instance = new LevelManagerModel();
        }
        return instance;
    }

    /**
     * Costruttore privato per inizializzare il manager dei livelli.
     * Costruisce tutti i livelli al momento della creazione dell'istanza.
     */
    private LevelManagerModel() {
        levels = new ArrayList<>();
        buildAllLevels();
    }

    /**
     * Costruisce tutti i livelli leggendo le immagini dalla directory.
     */
    private void buildAllLevels() {
        BufferedImage[] allLevels = getAllLevels();
        for (BufferedImage img : allLevels) {
            levels.add(new LevelModel(img));
        }
    }

    /**
     * Ottiene tutte le immagini dei livelli dalla directory specificata.
     *
     * @return Un array di BufferedImage contenente tutte le immagini dei livelli.
     */
    public static BufferedImage[] getAllLevels() {
        URL url = LevelManagerModel.class.getResource("/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++)
            for (int j = 0; j < files.length; j++)
                if (files[j].getName().equals((i + 1) + ".png"))
                    filesSorted[i] = files[j];

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for (int i = 0; i < imgs.length; i++)
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return imgs;
    }

    /**
     * Carica il livello successivo, aggiornando lo stato del gioco e le
     * statistiche del giocatore
     * Se abbiamo superato l'ultimo livello termina la partita e aggiorna le statistiche dell'utente
     */
    public void loadNextLevel(){
        if(lvlIndex >= levels.size() - 1) {
            UserModel user = UserStateModel.getInstance().getCurrentUserModel();
            user.incrementTempScore(1000000);
            user.incrementMatches();
            user.incrementWins();
            user.setMaxScore();
            user.updateLevelScore();
            user.serialize("res/users/" + user.getNickname() + ".bb");
            Gamestate.state = MENU;
            gameWon = true;
            lvlIndex = 0;
        } else {
            lvlIndex++;
            nextLevel = true;
        }
        EnemyManagerModel.getInstance().initEnemyAndFoodArrays();
        BubbleManagerModel.getInstance().resetBubbles();
        PlayerModel.getInstance().moveToSpawn();
        ProjectileManagerModel.getInstance().resetProjectiles();
    }

    /**
     * Riavvia il gioco, ricostruendo tutti i livelli.
     */
    public void restartGame() {
        levels.clear();
        buildAllLevels();
    }

    public ArrayList<LevelModel> getLevels() {
        return levels;
    }

    public int getLvlIndex() {
        return lvlIndex;
    }

    public void setLvlIndex(int lvlIndex) {
        this.lvlIndex = lvlIndex;
    }

    public boolean isNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(boolean nextLevel) {
        this.nextLevel = nextLevel;
    }

    public int getLevelSkipped() {
        return levelSkipped;
    }

    public void setLevelSkipped(int levelSkipped) {
        this.levelSkipped = levelSkipped;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }
}
