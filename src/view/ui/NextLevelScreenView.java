package view.ui;

import model.level.LevelManagerModel;
import view.entities.PlayerView;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.SCALE;
import static view.utilz.LoadSave.BUB_LEVEL_TRANSITION;
import static view.utilz.LoadSave.loadAnimations;

/**
 * La classe {@code NextLevelScreenView} gestisce la transizione tra i livelli del gioco,
 * includendo la visualizzazione del passaggio da un livello all'altro e il movimento
 * del personaggio durante la transizione.
 */
public class NextLevelScreenView {

    /** Istanza singleton di {@code NextLevelScreenView}. */
    private static NextLevelScreenView instance;

    private LevelManagerModel levelManager;

    /** Array di immagini per le sprite del livello. */
    private BufferedImage[] lvlSprites;

    /** Timer per tenere traccia del tempo trascorso nella transizione di livello. */
    private int time = 0;

    /** Indice e tick dell'animazione per il personaggio Bub nella transizione. */
    private int aniIndex, aniTick;

    // Per la gestione del posizionamento del livello
    /** Dati dei blocchi del livello precedente. */
    private int[][] prevLevelData;

    /** Dati dei blocchi del livello successivo */
    private int[][] nextLevelData;

    /** Posizione verticale del livello precedente durante la transizione. */
    private int prevY = 0;

    /** Posizione verticale del livello successivo durante la transizione. */
    private int nextY = GAME_HEIGHT - TILES_SIZE;

    // Per la gestione del personaggio Bub
    private PlayerView playerView;
    private BufferedImage[][] bubTransitionImages;

    /** Posizione di spawn del giocatore nel nuovo livello. */
    private Point nextPlayerSpawn;

    /** Posizione corrente del giocatore durante la transizione. */
    private Point curPlayerPos;

    /**
     * Restituisce l'istanza singleton di {@code NextLevelScreenView}. Se non esiste ancora, la crea.
     *
     * @return l'istanza di {@code NextLevelScreenView}.
     */
    public static NextLevelScreenView getInstance() {
        if (instance == null) {
            instance = new NextLevelScreenView();
        }
        return instance;
    }

    /**
     * Costruttore privato che inizializza i dati necessari per la transizione del livello.
     */
    private NextLevelScreenView() {
        playerView = PlayerView.getInstance();
        levelManager = LevelManagerModel.getInstance();
        lvlSprites = LoadSave.importSprites();
        bubTransitionImages = loadAnimations(BUB_LEVEL_TRANSITION, 2, 2, 30, 34);
    }

    /**
     * Aggiorna lo stato della transizione, inclusi i movimenti del livello e del personaggio.
     */
    public void update() {
        if (nextY <= 0 && nextPlayerSpawn.equals(curPlayerPos)) {
            resetNextScreenView();
            return;
        }
        if (time == 0)
            getData();
        else {
            updateLvlPos();
            updateBubPosition();
        }
        time++;
    }

    /**
     * Recupera i dati necessari per la transizione tra i livelli, inclusi i dati del livello precedente
     * e successivo, e le posizioni del giocatore.
     */
    private void getData() {
        prevLevelData = levelManager.getLevels().get(levelManager.getLvlIndex() - levelManager.getLevelSkipped()).getLvlData();
        nextLevelData = levelManager.getLevels().get(levelManager.getLvlIndex()).getLvlData();
        curPlayerPos = playerView.getCurPlayerPos();
        nextPlayerSpawn = levelManager.getLevels().get(levelManager.getLvlIndex()).getPlayerSpawn();
        levelManager.setLevelSkipped(1);
    }

    /**
     * Aggiorna le posizioni verticali del livello precedente e successivo durante la transizione.
     */
    private void updateLvlPos() {
        if (!(nextY <= 0)) {
            nextY -= (int) (1 * SCALE);
            prevY -= (int) (1 * SCALE);
        }
    }

    /**
     * Aggiorna la posizione del personaggio Bub durante la transizione tra i livelli.
     */
    private void updateBubPosition() {
        if (curPlayerPos.x != nextPlayerSpawn.x) {
            if (curPlayerPos.x < nextPlayerSpawn.x)
                curPlayerPos.x++;
            else
                curPlayerPos.x--;
        }
        if (curPlayerPos.y != nextPlayerSpawn.y) {
            if (curPlayerPos.y < nextPlayerSpawn.y)
                curPlayerPos.y++;
            else
                curPlayerPos.y--;
        }
    }

    /**
     * Reimposta le variabili della schermata di transizione alla fine della transizione.
     */
    public void resetNextScreenView() {
        time = 0;
        aniTick = 0;
        aniIndex = 0;
        prevY = 0;
        nextY = GAME_HEIGHT - TILES_SIZE;
        levelManager.setNextLevel(false);
    }

    /**
     * Renderizza lo stato corrente della transizione di livello, compreso il livello precedente e successivo,
     * e l'animazione del personaggio Bub.
     *
     * @param g il contesto su cui disegnare.
     */
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        renderData(g, prevLevelData, prevY);
        renderData(g, nextLevelData, nextY);
        if (time < 80)
            renderBub(g, 0, 0);
        else {
            updateAni();
            renderBub(g, 1, aniIndex);
        }
    }

    /**
     * Aggiorna il tick dell'animazione di transizione di Bub.
     */
    private void updateAni() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
        }
        if (aniIndex > 1)
            aniIndex = 0;
    }

    /**
     * Renderizza i blocchi per la transizione.
     *
     * @param g l'oggetto su cui disegnare.
     * @param lvlData i dati del livello da renderizzare.
     * @param lvlY la posizione verticale del livello.
     */
    private void renderData(Graphics g, int[][] lvlData, int lvlY) {
        for (int y = 0; y < TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < lvlData[0].length; x++) {
                int index = lvlData[y][x];
                if (index == 0 || index == 255) continue;

                int rgb = lvlSprites[index - 1].getRGB(3, 3);
                g.setColor(LoadSave.getDarkenedColor(rgb));
                for (int i = 0; i < 8; i++) {
                    g.fillRect(x * TILES_SIZE + i, y * TILES_SIZE + i + lvlY, TILES_SIZE, TILES_SIZE);
                }

                g.drawImage(lvlSprites[index - 1], x * TILES_SIZE, (y * TILES_SIZE) + lvlY, TILES_SIZE, TILES_SIZE, null);
            }
        }
    }

    /**
     * Renderizza il personaggio Bub durante la transizione di livello.
     *
     * @param g l'oggetto Graphics su cui disegnare.
     * @param stateIndex l'indice dello stato dell'animazione (iniziale o in movimento).
     * @param aniIndex l'indice del frame dell'animazione corrente.
     */
    private void renderBub(Graphics g, int stateIndex, int aniIndex) {
        g.drawImage(bubTransitionImages[stateIndex][aniIndex],
                (curPlayerPos.x - (int) (8 * SCALE)), (curPlayerPos.y - (int) (9 * SCALE)),
                (int) (SCALE * 30), (int) (SCALE * 34), null);
    }
}

