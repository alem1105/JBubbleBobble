package model.utilz;

import model.level.LevelManagerModel;
import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;
import model.gamestate.UserStateModel;
import model.objects.bobbles.BubbleManagerModel;
import model.objects.projectiles.ProjectileManagerModel;

/**
 * Classe utilizzata per alcuni metodi utili nel model
 */
public class UtilityMethods {
    public static int[][] getLvlData() {
        return LevelManagerModel.getInstance().getLevels().get(LevelManagerModel.getInstance().getLvlIndex()).getLvlData();
    }

    public static PlayerModel getPlayer() {
        return PlayerModel.getInstance();
    }

    /**
     * Resetta lo stato del gioco per iniziare una nuova partita
     */
    public static void resetAll() {
        PlayerModel.getInstance().setLives(3);
        LevelManagerModel.getInstance().restartGame();
        LevelManagerModel.getInstance().setLvlIndex(0);
        EnemyManagerModel.getInstance().initEnemyAndFoodArrays();
        BubbleManagerModel.getInstance().resetBubbles();
        PlayerModel.getInstance().moveToSpawn();
        ProjectileManagerModel.getInstance().resetProjectiles();
        UserStateModel.getInstance().getCurrentUserModel().setTempScore(0);
        PlayerModel.getInstance().setGameOver(false);
    }
}
