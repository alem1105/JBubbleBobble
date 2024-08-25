package model.utilz;

import model.LevelManager;

public class UtilityMethods {
    public static int[][] getLvlData() {
        return LevelManager.getInstance().getLevels().get(LevelManager.getInstance().getLvlIndex()).getLvlData();
    }
}
