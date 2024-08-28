package view.ui;

import model.ui.buttons.QuitButtonModel;
import model.ui.buttons.RestartButtonModel;
import view.stateview.LevelEditorView;
import view.ui.buttons.QuitButtonView;
import view.ui.buttons.RestartButtonView;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.Color.*;
import static model.utilz.Constants.GameConstants.*;

public class DeathScreenView {
    int x, y, width, height;
    private BufferedImage deathScreen;
    private QuitButtonView quitButton;
    private RestartButtonView restartButton;
    private static DeathScreenView instance;
    public static DeathScreenView getInstance() {
        if (instance == null) {
            instance = new DeathScreenView();
        }
        return instance;
    }

    private DeathScreenView() {
//        this.x = 100;
//        this.y = 500;
//        this.width = 300;
//        this.height = 500;
        initButtons();
    }

    private void initButtons() {
        quitButton = new QuitButtonView(new QuitButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE), (int) (147 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)));
        restartButton = new RestartButtonView(new RestartButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE), (int) (185 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)));
    }

    public void update(){
        quitButton.update();
        restartButton.update();
    }

    public void render(Graphics g) {
        Color myColour = new Color(0, 0, 0, 230);
        g.setColor(myColour);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        //g.drawImage(deathScreen, x, y, width, height, null);
        drawString(g);
        quitButton.draw(g);
        restartButton.draw(g);
    }

    private void drawString(Graphics g) {
        Font font = (LoadSave.BUBBLE_BOBBLE_FONT).deriveFont(33 * SCALE);
        g.setColor(GREEN);
        g.setFont(font);

        FontMetrics misure = g.getFontMetrics(font);
        g.drawString("GAME OVER", GAME_WIDTH / 2 - (misure.stringWidth("GAME OVER") / 2), (int) (130 * SCALE));
    }

    public QuitButtonView getQuitButtonView() {
        return quitButton;
    }

    public RestartButtonView getRestartButtonView() {
        return restartButton;
    }
}
