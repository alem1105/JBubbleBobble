package model.utilz;

import model.LevelManager;
import model.entities.PlayerModel;

public class UtilityMethods {
    public static int[][] getLvlData() {
        return LevelManager.getInstance().getLevels().get(LevelManager.getInstance().getLvlIndex()).getLvlData();
    }

    public static PlayerModel getPlayer() {
        return PlayerModel.getInstance();
    }
}
