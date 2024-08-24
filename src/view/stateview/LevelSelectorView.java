package view.stateview;

import model.ui.buttons.ChangeLvlButtonModel;
import model.ui.buttons.EditButtonModel;
import view.ui.buttons.ChangeLvlButtonView;
import view.ui.buttons.EditButtonView;
import view.utilz.LoadSave;

import java.awt.*;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.GameConstants.*;

public class LevelSelectorView {
    private static LevelSelectorView instance;

    private LevelEditorView levelEditorView;
    private ChangeLvlButtonView nextLevelButtonView;
    private ChangeLvlButtonView prevLevelButtonView;
    private EditButtonView editButtonView;

    public static LevelSelectorView getInstance() {
        if (instance == null) {
            instance = new LevelSelectorView();
        }
        return instance;
    }

    private LevelSelectorView() {
        levelEditorView = LevelEditorView.getInstance();
        initButtons();
    }

    private void initButtons() {
        nextLevelButtonView = new ChangeLvlButtonView(new ChangeLvlButtonModel(
                levelEditorView.getLevelWidth() + ((GAME_WIDTH - levelEditorView.getLevelWidth()) / 2) + (int) (2 * SCALE),
                (int) (85 * SCALE),
                (int)(18 * SCALE),
                (int)(18 * SCALE),
                RIGHT));

        prevLevelButtonView = new ChangeLvlButtonView(new ChangeLvlButtonModel(
                levelEditorView.getLevelWidth() + ((GAME_WIDTH - levelEditorView.getLevelWidth()) / 2) - (int) (20 * SCALE),
                (int) (85 * SCALE),
                (int)(18 * SCALE),
                (int)(18 * SCALE),
                LEFT));

        editButtonView = new EditButtonView(new EditButtonModel(
                (levelEditorView.getLevelWidth() / 2 ) - (int)(30 * SCALE),
                levelEditorView.getLevelHeight() + ((GAME_HEIGHT - levelEditorView.getLevelHeight()) / 2) - (int)(30 * SCALE),
                (int) (60 * SCALE),
                (int) (50 * SCALE)
        ));
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        levelEditorView.drawTilesAndEnemies(g);
        levelEditorView.drawPlayer(g);
        nextLevelButtonView.draw(g);
        prevLevelButtonView.draw(g);
        editButtonView.draw(g);
        drawStrings(g);
    }

    private void drawStrings(Graphics g){
        Font font = (LoadSave.CUSTOM_FONT).deriveFont(33 * SCALE);
        g.setFont(font);

        // scritta level
        g.setColor(new Color(242, 70, 152));
        FontMetrics misure = g.getFontMetrics(font); // ottengo le misure della scritta per centrarla
        int offset = LevelEditorView.getInstance().getLevelWidth();
        int distance = (GAME_WIDTH - offset) / 2;
        int yLevel = (int) (40 * SCALE);
        g.drawString("Level:",  offset + distance - (misure.stringWidth("Level:") / 2), yLevel);

        // scritta dell'indice del livello
        g.setColor(new Color(254, 238, 31));
        String lvl = "" + (levelEditorView.getLevelIndex() + 1);
        g.drawString(lvl, offset + distance - (misure.stringWidth(lvl) / 2), yLevel + (int) (30 * SCALE));
    }

    public void update() {
        nextLevelButtonView.update();
        prevLevelButtonView.update();
        editButtonView.update();
    }

    public ChangeLvlButtonView getNextLevelButtonView() {
        return nextLevelButtonView;
    }

    public ChangeLvlButtonView getPrevLevelButtonView() {
        return prevLevelButtonView;
    }

    public EditButtonView getEditButtonView() {
        return editButtonView;
    }
}
