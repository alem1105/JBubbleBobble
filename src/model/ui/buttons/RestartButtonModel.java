package model.ui.buttons;

import model.LevelManagerModel;
import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;
import model.gamestate.Gamestate;
import model.objects.ProjectileManagerModel;
import model.objects.bobbles.BubbleManagerModel;
import view.entities.enemies.EnemiesManagerView;

import static model.gamestate.Gamestate.MENU;

public class RestartButtonModel extends CustomButtonModel{
    public RestartButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void restart(){
        PlayerModel.getInstance().setLives(3);
        LevelManagerModel.getInstance().restartGame();
        LevelManagerModel.getInstance().setLvlIndex(0);
        EnemyManagerModel.getInstance().initEnemies();
        BubbleManagerModel.getInstance().resetBubbles();
        PlayerModel.getInstance().moveToSpawn();
        ProjectileManagerModel.getInstance().resetProjectiles();
        EnemiesManagerView.getInstance().setRestart(true);
        PlayerModel.getInstance().setGameOver(false);
    }
}