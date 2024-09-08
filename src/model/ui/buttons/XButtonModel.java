package model.ui.buttons;

import model.LevelManagerModel;

import java.awt.*;
import java.util.Arrays;

import static model.utilz.Constants.GameConstants.*;

public class XButtonModel extends CustomButtonModel {

    private int[][] oldLvlData;
    private int[][] oldEnemiesData;
    private Point oldPlayerSpawn;

    public XButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    private void updateLvlData(int index) {
        int[][] lvlData = LevelManagerModel.getInstance().getLevels().get(index).getLvlData();
        oldLvlData = new int[TILES_IN_HEIGHT][TILES_IN_WIDTH];
        for (int i = 0; i < lvlData.length; i++) {
            oldLvlData[i] = Arrays.copyOf(lvlData[i], TILES_IN_WIDTH);
        }
    }

    private void updateEnemiesData(int index) {
        int[][] enemiesData = LevelManagerModel.getInstance().getLevels().get(index).getEnemiesData();
        oldEnemiesData = new int[TILES_IN_HEIGHT][TILES_IN_WIDTH];
        for (int i = 0; i < enemiesData.length; i++) {
            oldEnemiesData[i] = Arrays.copyOf(enemiesData[i], TILES_IN_WIDTH);
        }
    }

    private void updatePlayerSpawn(int index){
        oldPlayerSpawn = LevelManagerModel.getInstance().getLevels().get(index).getPlayerSpawn();
    }

    public void updateData(int index){
        updateLvlData(index);
        updateEnemiesData(index);
        updatePlayerSpawn(index);
    }

    public int[][] getOldEnemiesData() {
        return oldEnemiesData;
    }

    public int[][] getOldLvlData() {
        return oldLvlData;
    }

    public Point getOldPlayerSpawn() {
        return oldPlayerSpawn;
    }
}
