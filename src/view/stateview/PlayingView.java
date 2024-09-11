package view.stateview;

import model.level.LevelManagerModel;
import model.UserModel;
import model.gamestate.PlayingModel;
import model.gamestate.UserStateModel;
import view.level.LevelView;
import view.entities.PlayerView;
import view.entities.enemies.EnemiesManagerView;
import view.objects.items.PowerUpManagerView;
import view.objects.projectiles.ProjectileManagerView;
import view.objects.bobbles.BubbleManagerView;
import view.ui.DeathScreenView;
import view.ui.GamePausedScreenView;
import view.ui.NextLevelScreenView;
import view.utilz.AudioManager;
import view.utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import static model.utilz.Constants.GameConstants.*;
import static view.utilz.AudioManager.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Rappresenta la vista del gioco durante il suo stato attivo, gestendo
 * l'input e il rendering degli elementi di gioco come il giocatore, nemici,
 * proiettili e schermi di gioco come quello di morte e pausa.
 */
public class PlayingView {

    private static PlayingView instance;

    private PlayerView playerView;
    private LevelView levelView;
    private EnemiesManagerView enemiesManagerView;
    private DeathScreenView deathScreenView;
    private BufferedImage heartLifeImage;
    private BubbleManagerView bubbleManagerView;
    private ProjectileManagerView projectileManagerView;
    private NextLevelScreenView nextLevelScreenView;
    private PowerUpManagerView powerUpManagerView;
    private GamePausedScreenView gamePausedScreenView;

    private DecimalFormat decFormat = new DecimalFormat("00");

    /**
     * Restituisce l'istanza singleton di PlayingView.
     *
     * @return L'istanza singleton di PlayingView.
     */
    public static PlayingView getInstance() {
        if (instance == null) {
            instance = new PlayingView();
        }
        return instance;
    }

    /**
     * Costruttore privato per inizializzare la vista di gioco.
     */
    private PlayingView() {
        initViews();
        heartLifeImage = LoadSave.getSpriteAtlas(LoadSave.HEART_LIFE_BUTTON);
    }

    /**
     * Inizializza le varie viste necessarie per il gioco, come il
     * giocatore, i nemici, e altri gestori di oggetti di gioco.
     */
    private void initViews() {
        playerView = PlayerView.getInstance();
        levelView = new LevelView();
        enemiesManagerView = EnemiesManagerView.getInstance();
        deathScreenView = DeathScreenView.getInstance();
        bubbleManagerView = BubbleManagerView.getInstance();
        projectileManagerView = ProjectileManagerView.getInstance();
        nextLevelScreenView = NextLevelScreenView.getInstance();
        powerUpManagerView = PowerUpManagerView.getInstance();
        gamePausedScreenView = GamePausedScreenView.getInstance();
    }

    /**
     * Esegue il rendering di tutti gli elementi di gioco
     *
     * @param g
     */
    public void render(Graphics g) {

        if (LevelManagerModel.getInstance().isNextLevel()) {
            nextLevelScreenView.render(g);
            return;
        }

        levelView.render(g);
        bubbleManagerView.draw(g);
        projectileManagerView.updateAndDraw(g);
        enemiesManagerView.render(g);
        powerUpManagerView.draw(g);
        playerView.render(g);
        drawLifeHearts(g);
        drawStats(g);

        if (playerView.getPlayerModel().isGameOver())
            deathScreenView.render(g);
        else if (PlayingModel.getInstance().isPaused())
            gamePausedScreenView.render(g);
    }

    /**
     * Aggiorna lo stato del gioco e gestisce le transizioni tra stati
     * come morte, pausa e avanzamento di livello.
     */
    public void update() {
        if (playerView.getPlayerModel().isGameOver()) {
            AudioManager.getInstance().continuousSoundPlay(GAME_OVER_INDEX);
            deathScreenView.update();
            return;
        }

        if (LevelManagerModel.getInstance().isNextLevel()) {
            nextLevelScreenView.update();
            return;
        }

        if (PlayingModel.getInstance().isPaused()) {
            gamePausedScreenView.update();
            return;
        }

        if (!gamePausedScreenView.isJustEnteredInPauseScreen())
            gamePausedScreenView.resetPauseScreen();

        chooseTrackToPlayBasedOnLevel();

        playerView.update();
        bubbleManagerView.update();
        enemiesManagerView.update();
        powerUpManagerView.update();
    }

    /**
     * Sceglie la traccia audio da riprodurre in base al livello attuale, cambia soltanto quella del boss finale.
     */
    private void chooseTrackToPlayBasedOnLevel() {
        if (LevelManagerModel.getInstance().getLvlIndex() + 1 == LevelManagerModel.getInstance().getLevels().size())
            AudioManager.getInstance().continuousSoundPlay(SUPER_DRUNK_INDEX);
        else
            AudioManager.getInstance().continuousSoundPlay(MAIN_THEME_INDEX);
    }

    /**
     * Disegna le icone delle vite del giocatore.
     *
     * @param g
     */
    private void drawLifeHearts(Graphics g) {
        for (int live = 0; live < playerView.getPlayerModel().getLives(); live++)
            g.drawImage(heartLifeImage, live * TILES_SIZE, GAME_HEIGHT - TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
    }

    /**
     * Disegna le statistiche del gioco, inclusi il livello attuale e il punteggio.
     *
     * @param g
     */
    private void drawStats(Graphics g) {
        UserModel currentUser = UserStateModel.getInstance().getCurrentUserModel();
        String currentLevelIndex = decFormat.format(LevelManagerModel.getInstance().getLvlIndex() + 1);

        Font font = LoadSave.NES_FONT.deriveFont(7f * SCALE);
        g.setFont(font);
        FontMetrics measures = g.getFontMetrics();

        // posizione del testo
        int x = TILES_SIZE / 7;
        int y = 2 * TILES_SIZE - measures.getHeight() + 2;

        // distanza del contorno in pixel
        int shadowOffset = 2;

        // disegna il contorno nero intorno al testo bianco
        g.setColor(Color.BLACK);
        for (int i = -shadowOffset; i <= shadowOffset; i++) {
            for (int j = -shadowOffset; j <= shadowOffset; j++) {
                // evitiamo di ridisegnare il testo al centro (0, 0) dove andrà il testo principale
                if (i != 0 || j != 0) {
                    g.drawString(currentLevelIndex, x + i, y + j);
                }
            }
        }

        // punteggio della partita corrente
        g.setColor(Color.WHITE);
        g.drawString(currentLevelIndex, x, y);
        DecimalFormat decFormat2 = new DecimalFormat("000000000");

        g.drawString(decFormat2.format(currentUser.getTempScore()), GAME_WIDTH / 2  - measures.stringWidth(decFormat2.format(currentUser.getTempScore()))/ 2, GAME_HEIGHT - (int) (5 * SCALE));

        // punteggio massimo dello user
        g.setColor(Color.RED);
        g.drawString(decFormat2.format(currentUser.getMaxScore()), (GAME_WIDTH - (int) (7 * SCALE)- measures.stringWidth(decFormat2.format(currentUser.getTempScore()))), GAME_HEIGHT - (int) (5 * SCALE));
    }
}
