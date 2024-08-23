package model.ui;

import model.LevelManager;
import view.stateview.LevelEditorView;

import java.util.Arrays;

import static model.utilz.Constants.GameConstants.*;

public class XButtonModel extends CustomButtonModel {

    private int[][] oldLvlData;

    public XButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void updateLvlData(int index) {
        int[][] lvlData = LevelManager.getInstance().getLevels().get(index).getLvlData();
        oldLvlData = new int[TILES_IN_HEIGHT][TILES_IN_WIDTH];
        for (int i = 0; i < lvlData.length; i++) {
            oldLvlData[i] = Arrays.copyOf(lvlData[i], TILES_IN_WIDTH);
        }
    }

    public void isClicked() {
        LevelManager.getInstance()
                .getLevels()
                .get(LevelEditorView
                        .getInstance()
                        .getLevelIndex())
                .setLvlData(oldLvlData);
    }

}
