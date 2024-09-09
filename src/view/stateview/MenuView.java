package view.stateview;

import model.LevelManagerModel;
import model.ui.buttons.EditorButtonModel;
import model.ui.buttons.StartButtonModel;
import model.utilz.Constants;
import view.ui.GameWonScreenView;
import view.ui.buttons.EditorButtonView;
import view.ui.buttons.StartButtonView;
import view.utilz.AudioManager;
import view.utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static model.utilz.Constants.GameConstants.*;
import static view.utilz.AudioManager.GAME_WON_INDEX;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Rappresenta la vista del menu principale del gioco, inclusi i pulsanti per
 * iniziare il gioco e accedere all'editor, oltre a gestire le animazioni
 * del logo e delle stelle.
 */
public class MenuView {

    private EditorButtonView editorButton;
    private StartButtonView startButton;
    private static MenuView instance;
    private GameWonScreenView gameWonScreenView;

    // Animazione Logo e Stelle
    private BufferedImage[][] logoAnimation;
    private int logoY = -(int) (130 * SCALE), logoAniTick, logoAniIndex;
    private boolean logoFalling = true;

    private BufferedImage[][] twinkleAnimation;
    private ArrayList<TwinkleView> twinkles;
    private Random random;

    /**
     * Restituisce l'istanza singleton di MenuView.
     *
     * @return L'istanza singleton di MenuView.
     */
    public static MenuView getInstance() {
        if (instance == null) {
            instance = new MenuView();
        }
        return instance;
    }

    /**
     * Costruttore privato per inizializzare il menu del gioco.
     */
    private MenuView() {
        random = new Random();
        initButtons();
        initStars();
        gameWonScreenView = GameWonScreenView.getInstance();
        logoAnimation = LoadSave.loadAnimations(LoadSave.MENU_LOGO, 1, 6, 176, 130);
        twinkleAnimation = LoadSave.loadAnimations(LoadSave.STARS_SPRITE, 1, 4, 8, 8);
    }

    /**
     * Aggiorna lo stato del menu, gestendo l'animazione del logo, delle stelle
     * e il controllo dello stato di vittoria.
     */
    public void update() {
        if (LevelManagerModel.getInstance().isGameWon()) {
            gameWonScreenView.update();
            AudioManager.getInstance().continuousSoundPlay(GAME_WON_INDEX);
        } else {
            updateStars();
            updateLogo();
            startButton.update();
            editorButton.update();
            AudioManager.getInstance().stopAllContinuousAudios();
        }
    }

    /**
     * Aggiorna la posizione delle stelle nell'animazione.
     */
    private void updateStars() {
        for (TwinkleView twinkle : twinkles) {
            twinkle.update();
        }
    }

    /**
     * Aggiorna l'animazione del logo, gestendo il suo movimento e il ciclo
     * delle animazioni.
     */
    private void updateLogo() {
        if (logoY < (int) (40 * SCALE))
            logoY++;
        else
            logoFalling = false;
        logoAniTick++;
        if (logoAniTick >= ANI_SPEED) {
            logoAniTick = 0;
            logoAniIndex++;
            if (logoAniIndex >= 6)
                logoAniIndex = 0;
        }
    }

    /**
     * Inizializza le stelle nell'animazione, creando una serie di oggetti
     * TwinkleView in posizioni casuali.
     */
    private void initStars() {
        twinkles = new ArrayList<>();
        for (int i = 0; i < 50; i++)
            twinkles.add(new TwinkleView(random.nextInt(GAME_WIDTH), random.nextInt(GAME_HEIGHT), random.nextInt(3)));
    }

    /**
     * Inizializza i pulsanti del menu principale, inclusi il pulsante per
     * iniziare il gioco e il pulsante per l'editor.
     */
    private void initButtons() {
        startButton = new StartButtonView(new StartButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE), (int) (177 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)), false);
        editorButton = new EditorButtonView(new EditorButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE), (int) (205 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)));
    }

    /**
     * Disegna il menu principale, compresa l'animazione del logo e dei pulsanti.
     *
     * @param g
     */
    public void draw(Graphics g) {
        if (LevelManagerModel.getInstance().isGameWon()) {
            gameWonScreenView.draw(g);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, Constants.GameConstants.GAME_WIDTH, Constants.GameConstants.GAME_HEIGHT);
            drawStars(g);
            drawLogo(g);
            if (!logoFalling) {
                drawString(g);
                startButton.draw(g);
                editorButton.draw(g);
            }
        }
    }

    /**
     * Disegna le stelle nel menu.
     *
     * @param g
     */
    private void drawStars(Graphics g) {
        for (TwinkleView twinkle : twinkles)
            g.drawImage(twinkleAnimation[0][twinkle.getAniIndex()], (int) twinkle.getX(), (int) twinkle.getY(), (int) (8 * SCALE), (int) (8 * SCALE), null);
    }

    /**
     * Disegna il logo del gioco.
     *
     * @param g
     */
    private void drawLogo(Graphics g) {
        g.drawImage(logoAnimation[0][logoAniIndex], GAME_WIDTH / 2 - (int) (88 * SCALE), logoY, (int) (176 * SCALE), (int) (130 * SCALE), null);
    }

    /**
     * Disegna la stringa informativa sotto il logo.
     *
     * @param g
     */
    private void drawString(Graphics g) {
        Font font = (LoadSave.NES_FONT);
        g.setColor(Color.WHITE);
        g.setFont(font);

        FontMetrics metrics = g.getFontMetrics(font);
        g.drawString("A. MARINI - E. NOTO - G. GULLO", GAME_WIDTH / 2 - (metrics.stringWidth("A. MARINI - E. NOTO - G. GULLO") / 2), (int) (260 * SCALE));
    }

    public EditorButtonView getEditorButton() {
        return editorButton;
    }

    public StartButtonView getStartButton() {
        return startButton;
    }

    public boolean getLogoFalling() {
        return logoFalling;
    }
}
