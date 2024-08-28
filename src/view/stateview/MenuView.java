package view.stateview;

import model.ui.buttons.EditorButtonModel;
import model.ui.buttons.QuitButtonModel;
import model.ui.buttons.RestartButtonModel;
import model.ui.buttons.StartButtonModel;
import model.utilz.Constants;
import view.ui.DeathScreenView;
import view.ui.buttons.EditorButtonView;
import view.ui.buttons.QuitButtonView;
import view.ui.buttons.RestartButtonView;
import view.ui.buttons.StartButtonView;
import view.utilz.LoadSave;

import java.awt.*;

import static java.awt.Color.GREEN;
import static model.utilz.Constants.GameConstants.GAME_WIDTH;
import static model.utilz.Constants.GameConstants.SCALE;

public class MenuView {

    private EditorButtonView editorButton;
    private StartButtonView startButton;
    private static MenuView instance;

    public static MenuView getInstance() {
        if (instance == null) {
            instance = new MenuView();
        }
        return instance;
    }

    private MenuView() {
        initButtons();
    }

    private void initButtons() {
        startButton = new StartButtonView(new StartButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE), (int) (147 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)));
        editorButton = new EditorButtonView(new EditorButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE), (int) (185 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)));
    }

    public void update(){
        startButton.update();
        editorButton.update();
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constants.GameConstants.GAME_WIDTH, Constants.GameConstants.GAME_HEIGHT);
        drawString(g);
        startButton.draw(g);
        editorButton.draw(g);
    }

    private void drawString(Graphics g) {
        Font font = (LoadSave.BUBBLE_BOBBLE_FONT).deriveFont(55 * SCALE);
        g.setColor(GREEN);
        g.setFont(font);

        FontMetrics misure = g.getFontMetrics(font);
        g.drawString("MENU", GAME_WIDTH / 2 - (misure.stringWidth("MENU") / 2), (int) (130 * SCALE));
    }

    public EditorButtonView getEditorButton() {
        return editorButton;
    }

    public StartButtonView getStartButton() {
        return startButton;
    }
}
