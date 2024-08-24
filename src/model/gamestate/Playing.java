package model.gamestate;

import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;

import static model.utilz.Constants.PlayerConstants.DEATH;

public class Playing {

    private PlayerModel player;
    private static Playing instance;
    private EnemyManagerModel enemyManagerModel;

    public static Playing getInstance() {
        if (instance == null) {
            instance = new Playing();
        }
        return instance;
    }

    private Playing() {
        player = PlayerModel.getInstance();
        enemyManagerModel = EnemyManagerModel.getInstance();
    }

    public void update() {
        if (!(player.isGameOver())) {
            if (player.getPlayerAction() != DEATH)
                player.update();
            enemyManagerModel.update();
        }
    }

    public PlayerModel getPlayer() {
        return player;
    }

}
