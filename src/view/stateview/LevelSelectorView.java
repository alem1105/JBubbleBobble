package view.stateview;

import model.ui.ChangeLvlButtonModel;
import model.ui.EditButtonModel;
import model.utilz.Constants;
import view.ui.ChangeLvlButtonView;
import view.ui.EditButtonView;
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
                levelEditorView.getLevelWidth() + (int)(40 * SCALE),
                (int) (50 * SCALE),
                (int)(18 * SCALE),
                (int)(18 * SCALE),
                RIGHT));

        prevLevelButtonView = new ChangeLvlButtonView(new ChangeLvlButtonModel(
                levelEditorView.getLevelWidth() + (int)(20 * SCALE),
                (int) (50 * SCALE),
                (int)(18 * SCALE),
                (int)(18 * SCALE),
                LEFT));

        editButtonView = new EditButtonView(new EditButtonModel(
                (levelEditorView.getLevelWidth() / 2 ) - (int)(25 * SCALE),
                levelEditorView.getLevelHeight() + (int)(20 * SCALE),
                (int) (50 * SCALE),
                (int) (20 * SCALE)
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
