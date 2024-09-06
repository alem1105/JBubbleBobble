package model.utilz;

import model.LevelManagerModel;
import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;
import model.gamestate.UserStateModel;
import model.objects.bobbles.BubbleManagerModel;
import model.objects.projectiles.ProjectileManagerModel;
import view.entities.enemies.EnemiesManagerView;

public class UtilityMethods {
    public static int[][] getLvlData() {
        return LevelManagerModel.getInstance().getLevels().get(LevelManagerModel.getInstance().getLvlIndex()).getLvlData();
    }

    public static PlayerModel getPlayer() {
        return PlayerModel.getInstance();
    }

    public static void resetAll() {
        PlayerModel.getInstance().setLives(3);
        LevelManagerModel.getInstance().restartGame();
        LevelManagerModel.getInstance().setLvlIndex(0);
        EnemyManagerModel.getInstance().initEnemyAndFoodArrays();
        BubbleManagerModel.getInstance().resetBubbles();
        PlayerModel.getInstance().moveToSpawn();
        ProjectileManagerModel.getInstance().resetProjectiles();
        EnemiesManagerView.getInstance().setRestart(true);
        UserStateModel.getInstance().getCurrentUserModel().setTempScore(0);
        PlayerModel.getInstance().setGameOver(false);
    }
}
