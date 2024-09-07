package view.ui;

import model.ui.buttons.QuitButtonModel;
import model.ui.buttons.StartButtonModel;
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

public class GamePausedScreenView {

    private static GamePausedScreenView instance;

    private BufferedImage[][] twinkleAnimation;
    private ArrayList<TwinkleView> twinkles;
    private Random random;

    private QuitButtonView quitButton;
    private StartButtonView startButton;

    private BufferedImage gamePausedBackground;


    public static GamePausedScreenView getInstance() {
        if (instance == null) {
            instance = new GamePausedScreenView();
        }
        return instance;
    }

    private GamePausedScreenView() {
        random = new Random();
        twinkleAnimation = LoadSave.loadAnimations(LoadSave.STARS_SPRITE, 1, 4, 8, 8);
        gamePausedBackground = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        initTwinkles();
        initButtons();
    }

    public void update(){
        updateTwinkles();
        quitButton.update();
        startButton.update();
    }


    public void render(Graphics g) {
        Color myColour = new Color(0, 0, 0, 230);
        g.setColor(myColour);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        renderTwinkles(g);
        renderBackground(g);
        renderButtons(g);
    }


    private void initButtons() {
        quitButton = new QuitButtonView(new QuitButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE), (int) (147 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)), true);
        startButton = new StartButtonView(new StartButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE), (int) (177 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)), true);
    }

    private void initTwinkles() {
        twinkles = new ArrayList<>();
        for (int i = 0; i < 25; i++)
            twinkles.add(new TwinkleView(random.nextInt(GAME_WIDTH), random.nextInt(GAME_HEIGHT), random.nextInt(3)));
    }

    private void renderTwinkles(Graphics g) {
        for (TwinkleView twinkle : twinkles) {
            g.drawImage(twinkleAnimation[0][twinkle.getAniIndex()], (int)twinkle.getX(), (int)twinkle.getY(), (int) (8 * SCALE), (int) (8 * SCALE), null);
        }
    }

    private void renderButtons(Graphics g) {
        quitButton.draw(g);
        startButton.draw(g);
    }

    private void renderBackground(Graphics g){
        g.drawImage(gamePausedBackground, (GAME_WIDTH / 2 - gamePausedBackground.getWidth() / 2), (GAME_HEIGHT / 2 - gamePausedBackground.getHeight() / 2), null);
    }

    private void updateTwinkles() {
        for (TwinkleView twinkle : twinkles) {
            twinkle.update();
        }
    }

    public QuitButtonView getQuitButton() {
        return quitButton;
    }

    public StartButtonView getStartButton() {
        return startButton;
    }
}
