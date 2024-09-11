package model.gamestate;

import model.level.LevelManagerModel;
import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;
import model.objects.items.powerups.PowerUpsManagerModel;
import model.objects.projectiles.ProjectileManagerModel;
import model.objects.bobbles.BubbleManagerModel;

import static model.utilz.Constants.PlayerConstants.DEATH;

/**
 * La classe {@code PlayingModel} gestisce le classi del modello durante la fase di gioco.
 * Si occupa dell'aggiornamento del giocatore, dei nemici, dei proiettili, delle bolle e dei potenziamenti.
 * Inoltre, blocca gli aggiornamenti se il gioco è in pausa o in gameOver.
 * Questa classe utilizza il pattern Singleton.
 */
public class PlayingModel {

    /** Il modello del giocatore. */
    private PlayerModel player;

    private static PlayingModel instance;

    /** Il gestore dei nemici nel gioco. */
    private EnemyManagerModel enemyManagerModel;

    /** Il gestore delle bolle nel gioco. */
    private BubbleManagerModel bubbleManagerModel;

    /** Il gestore dei proiettili nel gioco. */
    private ProjectileManagerModel projectileManagerModel;

    /** Il gestore dei powerUp nel gioco. */
    private PowerUpsManagerModel powerUpsManagerModel;

    /** Indica se il gioco è attualmente in pausa. */
    private boolean paused;

    /**
     * Restituisce l'istanza singleton della classe {@code PlayingModel}.
     * Se l'istanza non esiste, viene creata una nuova.
     *
     * @return l'istanza corrente di {@code PlayingModel}.
     */
    public static PlayingModel getInstance() {
        if (instance == null) {
            instance = new PlayingModel();
        }
        return instance;
    }

    /**
     * Costruttore privato per la classe {@code PlayingModel}.
     * Inizializza i modelli per il giocatore, i nemici, le bolle, i proiettili e i potenziamenti.
     */
    private PlayingModel() {
        player = PlayerModel.getInstance();
        enemyManagerModel = EnemyManagerModel.getInstance();
        bubbleManagerModel = BubbleManagerModel.getInstance();
        projectileManagerModel = ProjectileManagerModel.getInstance();
        powerUpsManagerModel = PowerUpsManagerModel.getInstance();
    }

    /**
     * Aggiorna lo stato di gioco, compresi giocatore, nemici, proiettili, bolle e potenziamenti,
     * se il gioco non è in pausa e non è terminato.
     */
    public void update() {
        if (!player.isGameOver() && !paused) {
            if (!LevelManagerModel.getInstance().isNextLevel()) {
                if (player.getPlayerAction() != DEATH)
                    player.update();
                projectileManagerModel.update();
                bubbleManagerModel.update();
                enemyManagerModel.update();
                powerUpsManagerModel.update();
            }
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void invertPaused() {
        paused = !paused;
    }

}
