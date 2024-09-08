package view.entities;

import model.LevelManagerModel;
import model.entities.PlayerModel;
import view.utilz.AudioManager;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PlayerConstants.*;
import static model.utilz.Constants.GameConstants.ANI_SPEED;
import static model.utilz.Constants.PlayerConstants.JUMP;
import static view.utilz.AudioManager.*;

/**
 * La classe {@code PlayerView} gestisce la rappresentazione grafica del personaggio giocante (Player)
 * nel gioco. È responsabile del rendering delle animazioni del giocatore e dell'aggiornamento del suo stato visivo
 * in base ai cambiamenti nel modello {@link PlayerModel}.
 */
public class PlayerView {

    /** L'indice della quantità di righe nello sprite sheet */
    private static final int ROW_INDEX = 6;

    /** L'indice della quantità di colonne nella sprite sheet*/
    private static final int COL_INDEX = 6;

    /** Istanza singleton della classe {@code PlayerView}. */
    private static PlayerView instance;

    /** Tick dell'animazione corrente per gestire la frequenza di aggiornamento. */
    private int aniTick;

    /** Indice dell'animazione corrente per scegliere il frame giusto dallo sprite. */
    private int aniIndex;

    /** Modello del giocatore che contiene lo stato logico del giocatore. */
    private PlayerModel playerModel;

    /** Animazioni del giocatore, suddivise per azioni. */
    private BufferedImage[][] animations;

    /** Offset di disegno sull'asse X. */
    private float xDrawOffset = 3 * SCALE;

    /** Offset di disegno sull'asse Y. */
    private float yDrawOffset = 3 * SCALE;

    /** Indica la larghezza del flip per invertire l'immagine del giocatore in base alla direzione. */
    private int flipW = 1;

    /** Indica l'offset orizzontale per il flip dell'immagine del giocatore. */
    private int flipX = 0;

    /** Posizione attuale del giocatore utilizzata per il rendering. */
    private Point curPlayerPos;

    /**
     * Restituisce l'istanza singleton di {@code PlayerView}. Se non esiste ancora, la crea.
     *
     * @return l'istanza di {@code PlayerView}.
     */
    public static PlayerView getInstance() {
        if (instance == null) {
            instance = new PlayerView();
        }
        return instance;
    }

    /**
     * Costruttore privato che inizializza il modello del giocatore e carica le animazioni.
     */
    private PlayerView() {
        this.playerModel = PlayerModel.getInstance();
        animations = LoadSave.loadAnimations(LoadSave.PLAYER_SPRITE, ROW_INDEX, COL_INDEX, 18, 18);
        curPlayerPos = new Point();
    }

    /**
     * Aggiorna lo stato visivo del giocatore, compreso il tick dell'animazione e le direzioni
     * in cui si muove.
     */
    public void update() {
        updateAnimationTick();
        updateDirections();
        checkAniTick();
        if (!LevelManagerModel.getInstance().isNextLevel()) {
            curPlayerPos.x = (int) playerModel.getHitbox().x;
            curPlayerPos.y = (int) playerModel.getHitbox().y;
        }
    }

    /**
     * Controlla se il tick dell'animazione deve essere resettato in base al modello.
     */
    private void checkAniTick() {
        if (playerModel.isResetAniTick()) {
            aniTick = 0;
            aniIndex = 0;
        }
    }

    /**
     * Aggiorna le direzioni del giocatore in base ai movimenti nel modello.
     */
    private void updateDirections() {
        if (playerModel.isLeft()) {
            flipX = playerModel.getWidth();
            flipW = -1;
        }
        if (playerModel.isRight()) {
            flipX = 0;
            flipW = 1;
        }
    }

    /**
     * Renderizza le animazioni del giocatore.
     *
     * @param g il contesto su cui disegnare.
     */
    public void render(Graphics g) {
        g.drawImage(animations[playerModel.getPlayerAction()][aniIndex],
                (int) (playerModel.getHitbox().x - xDrawOffset) + flipX,
                (int) (playerModel.getHitbox().y - yDrawOffset),
                playerModel.getWidth() * flipW, playerModel.getHeight(), null);
    }

    /**
     * Aggiorna il tick dell'animazione e gestisce il cambio degli indici dello sprite delle animazioni.
     */
    public void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(playerModel.getPlayerAction())) {
                if (playerModel.getPlayerAction() == DEATH) {
                    AudioManager.getInstance().oneTimePlay(PLAYER_DEATH);
                    playerModel.setPlayerAction(IDLE);
                    playerModel.getHitbox().x = playerModel.getPlayerSpawn().x;
                    playerModel.getHitbox().y = playerModel.getPlayerSpawn().y;
                    playerModel.setInAir(true);
                }

                if (playerModel.isAttack()) {
                    AudioManager.getInstance().oneTimePlay(SHOOT_BUBBLE);
                    playerModel.setAttack(false);
                    playerModel.setAttackingClick(false);
                }

                if (playerModel.getPlayerAction() == JUMP)
                    AudioManager.getInstance().oneTimePlay(AudioManager.JUMP);

                aniIndex = 0;
            }
        }
    }

    /**
     * Restituisce la quantità di frame da utilizzare per una determinata azione del giocatore.
     *
     * @param player_action l'azione corrente del giocatore.
     * @return il numero di sprite per l'azione.
     */
    public int getSpriteAmount(int player_action) {
        return switch (player_action) {
            case DEATH -> 6;
            case JUMP, FALL, IDLE, RUNNING -> 2;
            default -> 1; // Include anche Attack
        };
    }

// For Debugging
//    private void drawHitbox(Graphics g) {
//        g.setColor(Color.PINK);
//        g.drawRect((int) (playerModel.getHitbox().x), (int) (playerModel.getHitbox().y),
//                (int) playerModel.getHitbox().width,
//                (int) playerModel.getHitbox().height);
//    }

    public PlayerModel getPlayerModel() {
        return playerModel;
    }

    public Point getCurPlayerPos() {
        return curPlayerPos;
    }
}
