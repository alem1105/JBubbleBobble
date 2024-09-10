package view.ui;

import model.ui.buttons.QuitButtonModel;
import model.ui.buttons.StartButtonModel;
import view.entities.PlayerView;
import view.stateview.TwinkleView;
import view.ui.buttons.QuitButtonView;
import view.ui.buttons.StartButtonView;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.SCALE;
import static view.utilz.LoadSave.BUB_LEVEL_TRANSITION;
import static view.utilz.LoadSave.loadAnimations;

/**
 * La classe GamePausedScreenView gestisce la visualizzazione della schermata di pausa del gioco.
 * Visualizza animazioni e pulsanti.
 */
public class GamePausedScreenView {

    /**
     * Istanza singleton della GamePausedScreenView.
     */
    private static GamePausedScreenView instance;

    /**
     * Animazione delle stelle durante la pausa.
     */
    private BufferedImage[][] twinkleAnimation;

    /**
     * Lista delle stelle.
     */
    private ArrayList<TwinkleView> twinkles;

    /**
     * Oggetto Random per generare numeri casuali.
     */
    private Random random;

    /**
     * Immagini per l'animazione del personaggio.
     */
    private BufferedImage[][] bubTransitionImages;

    /**
     * Posizione target per Bob durante l'animazione di pausa.
     */
    private Point targetPlayerPos;

    /**
     * Booleani che tengono traccia dello stato della schermata di pausa.
     */
    private boolean justEnteredInPauseScreen = true, bubInTargetPos;

    /**
     * Timer per l'animazione di Bob.
     */
    private int bubAnimationTimer;

    /**
     * Velocità di movimento di Bob.
     */
    private int bubMovingSpeed = 1;

    /**
     * Scala attuale e desiderata per Bub durante la transizione.
     */
    private float curBubScale = 1f, wantedBubScale = 1.5f;

    /**
     * Indici per controllare l'animazione.
     */
    private int aniIndex, aniTick, stateIndex;

    /**
     * Pulsante per uscire dal gioco durante la pausa.
     */
    private QuitButtonView quitButton;

    /**
     * Pulsante per riprendere il gioco.
     */
    private StartButtonView startButton;

    /**
     * Sfondo per la schermata di pausa.
     */
    private BufferedImage gamePausedBackground;

    /**
     * Posizione corrente del personaggio Bub.
     */
    private Point curPlayerPos;

    /**
     * Ritorna l'istanza singleton della GamePausedScreenView.
     * Se non è stata ancora creata, viene inizializzata.
     *
     * @return L'istanza di GamePausedScreenView.
     */
    public static GamePausedScreenView getInstance() {
        if (instance == null) {
            instance = new GamePausedScreenView();
        }
        return instance;
    }

    /**
     * Costruttore privato della GamePausedScreenView.
     * Inizializza le animazioni e i pulsanti.
     */
    private GamePausedScreenView() {
        random = new Random();
        twinkleAnimation = LoadSave.loadAnimations(LoadSave.STARS_SPRITE, 1, 4, 8, 8);
        gamePausedBackground = LoadSave.getSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bubTransitionImages = loadAnimations(BUB_LEVEL_TRANSITION, 2, 2, 30, 34);
        targetPlayerPos = new Point((int)(250 * SCALE), (int) (200 * SCALE));
        initTwinkles();
        initButtons();
    }

    /**
     * Inizializza i pulsanti per la schermata di pausa.
     */
    private void initButtons() {
        quitButton = new QuitButtonView(new QuitButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE) - (int)(10 * SCALE), (int) (147 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)), true);
        startButton = new StartButtonView(new StartButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE) - (int)(10 * SCALE), (int) (177 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)), true);
    }

    /**
     * Inizializza le stelle.
     */
    private void initTwinkles() {
        twinkles = new ArrayList<>();
        for (int i = 0; i < 15; i++)
            twinkles.add(new TwinkleView(random.nextInt(GAME_WIDTH), random.nextInt(GAME_HEIGHT), random.nextInt(3)));
    }

    /**
     * Disegna la schermata di pausa, incluse animazioni e pulsanti.
     *
     * @param g Il contesto grafico su cui disegnare.
     */
    public void render(Graphics g) {
        Color myColour = new Color(0, 0, 0, 230);
        g.setColor(myColour);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        renderTwinkles(g);
        renderBackground(g);
        renderButtons(g);
        renderBub(g);
    }

    /**
     * Aggiorna lo stato della schermata di pausa, inclusi i movimenti del giocatore e le animazioni.
     */
    public void update() {
        if (justEnteredInPauseScreen) {
            curPlayerPos = PlayerView.getInstance().getCurPlayerPos();
            justEnteredInPauseScreen = false;
        }
        updateTwinkles();
        quitButton.update();
        startButton.update();
        bubUpdate();
        playerMovement();
        bubAnimationTimer++;
    }

    /**
     * Gestisce l'aggiornamento dell'animazione di Bob.
     */
    private void bubUpdate() {
        if (bubAnimationTimer < 80) {
            stateIndex = 0;
            aniIndex = 0;
        } else {
            updateAni();
            stateIndex = 1;
        }
    }

    /**
     * Controlla il movimento del personaggio Bob verso la posizione target.
     */
    private void playerMovement() {
        if (bubInTargetPos) {
            bubFloatingMovement();
            return;
        }

        if (currentBubXCloseToTargetX() && currentBubYCloseToTargetY()) {
            bubInTargetPos = true;
            curPlayerPos.y = targetPlayerPos.y;
        }
        bubTransformation();
    }

    /**
     * Gestisce il movimento fluttuante di Bob.
     */
    private void bubFloatingMovement() {
        if (curPlayerPos.y == targetPlayerPos.y || curPlayerPos.y == targetPlayerPos.y - (60 * SCALE))
            bubMovingSpeed = -bubMovingSpeed;

        curPlayerPos.y += bubMovingSpeed;
    }

    /**
     * Gestisce la trasformazione di Bob durante l'animazione.
     */
    private void bubTransformation() {
        if (curBubScale < wantedBubScale)
            curBubScale += 0.015f;

        if (!currentBubXCloseToTargetX()) {
            if (curPlayerPos.x < targetPlayerPos.x)
                curPlayerPos.x += 2;
            else
                curPlayerPos.x -= 2;
        }

        if (!currentBubYCloseToTargetY()) {
            if (curPlayerPos.y < targetPlayerPos.y)
                curPlayerPos.y += 2;
            else
                curPlayerPos.y -= 2;
        }
    }

    /**
     * Disegna l'animazione di Bob
     *
     * @param g
     */
    private void renderBub(Graphics g) {
        g.drawImage(bubTransitionImages[stateIndex][aniIndex],
                (curPlayerPos.x - (int)(8 * SCALE)), (curPlayerPos.y - (int) (9 * SCALE)),
                (int) ((SCALE * 30) * curBubScale), (int) ((SCALE * 34) * curBubScale), null);
    }

    /**
     * Disegna le stelle.
     *
     * @param g
     */
    private void renderTwinkles(Graphics g) {
        for (TwinkleView twinkle : twinkles) {
            g.drawImage(twinkleAnimation[0][twinkle.getAniIndex()], (int) twinkle.getX(), (int) twinkle.getY(), (int) (8 * SCALE), (int) (8 * SCALE), null);
        }
    }

    /**
     * Disegna i pulsanti nella schermata di pausa.
     *
     * @param g
     */
    private void renderButtons(Graphics g) {
        quitButton.draw(g);
        startButton.draw(g);
    }

    /**
     * Disegna lo sfondo della schermata di pausa.
     *
     * @param g
     */
    private void renderBackground(Graphics g) {
        g.drawImage(gamePausedBackground, (GAME_WIDTH / 2 - gamePausedBackground.getWidth() / 2) - (int)(10 * SCALE), (GAME_HEIGHT / 2 - gamePausedBackground.getHeight() / 2), null);
    }

    /**
     * Aggiorna le stelle.
     */
    private void updateTwinkles() {
        for (TwinkleView twinkle : twinkles) {
            twinkle.update();
        }
    }

    /**
     * Aggiorna l'animazione di Bob.
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
     * Resetta lo stato della schermata di pausa.
     */
    public void resetPauseScreen() {
        justEnteredInPauseScreen = true;
        bubInTargetPos = false;
        bubAnimationTimer = 0;
    }

    private boolean currentBubXCloseToTargetX() {
        return (curPlayerPos.x <= targetPlayerPos.x + (int) (2 * SCALE)) && (curPlayerPos.x >= targetPlayerPos.x - (int) (2 * SCALE));
    }

    private boolean currentBubYCloseToTargetY() {
        return (curPlayerPos.y <= targetPlayerPos.y + (int) (2 * SCALE)) && (curPlayerPos.y >= targetPlayerPos.y - (int) (2 * SCALE));
    }

    public QuitButtonView getQuitButton() {
        return quitButton;
    }

    public StartButtonView getStartButton() {
        return startButton;
    }

    public boolean isJustEnteredInPauseScreen() {
        return justEnteredInPauseScreen;
    }
}
