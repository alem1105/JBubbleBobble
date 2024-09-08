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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.Color.WHITE;
import static model.utilz.Constants.GameConstants.*;
import static view.utilz.AudioManager.GAME_WON_INDEX;

public class MenuView {

    private EditorButtonView editorButton;
    private StartButtonView startButton;
    private static MenuView instance;
    private GameWonScreenView gameWonScreenView;

    // Animazione Logo e Stelle

    private BufferedImage[][] logoAnimation;
    private int logoY = -(int)(130 * SCALE), logoAniTick, logoAniIndex;
    private boolean logoFalling = true;

    private BufferedImage[][] twinkleAnimation;
    private ArrayList<TwinkleView> twinkles;
    private Random random;

    public static MenuView getInstance() {
        if (instance == null) {
            instance = new MenuView();
        }
        return instance;
    }

    private MenuView() {
        random = new Random();
        initButtons();
        initStars();
        gameWonScreenView = GameWonScreenView.getInstance();
        logoAnimation = LoadSave.loadAnimations(LoadSave.MENU_LOGO,1, 6, 176, 130);
        twinkleAnimation = LoadSave.loadAnimations(LoadSave.STARS_SPRITE, 1, 4, 8, 8);
    }

    public void update(){
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

    private void updateStars() {
        for (TwinkleView twinkle : twinkles) {
            twinkle.update();
        }
    }

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

    private void initStars() {
        twinkles = new ArrayList<>();
        for (int i = 0; i < 50; i++)
            twinkles.add(new TwinkleView(random.nextInt(GAME_WIDTH), random.nextInt(GAME_HEIGHT), random.nextInt(3)));
    }

    private void initButtons() {
        startButton = new StartButtonView(new StartButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE), (int) (177 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)), false);
        editorButton = new EditorButtonView(new EditorButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE), (int) (205 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)));
    }

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

    private void drawStars(Graphics g) {
        for (TwinkleView twinkle : twinkles) {
            g.drawImage(twinkleAnimation[0][twinkle.getAniIndex()], (int)twinkle.getX(), (int)twinkle.getY(), (int) (8 * SCALE), (int) (8 * SCALE), null);
        }
    }

    private void drawLogo(Graphics g) {
        g.drawImage(logoAnimation[0][logoAniIndex], GAME_WIDTH / 2 - (int) (88 * SCALE), logoY, (int)(176 * SCALE), (int)(130 * SCALE), null);
    }

    private void drawString(Graphics g) {
        Font font = (LoadSave.NES_FONT);
        g.setColor(WHITE);
        g.setFont(font);

        FontMetrics misure = g.getFontMetrics(font);
        g.drawString("ALESSIO - EDOARDO - GIADA", GAME_WIDTH / 2 - (misure.stringWidth("ALESSIO - EDOARDO - GIADA") / 2), (int) (260* SCALE));
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
