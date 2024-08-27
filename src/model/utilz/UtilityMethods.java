package model.utilz;

import model.LevelManagerModel;
import model.entities.PlayerModel;

public class UtilityMethods {
    public static int[][] getLvlData() {
        return LevelManagerModel.getInstance().getLevels().get(LevelManagerModel.getInstance().getLvlIndex()).getLvlData();
    }

    public static PlayerModel getPlayer() {
        return PlayerModel.getInstance();
    }
}
