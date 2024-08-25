package model.gamestate;

import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;
import model.objects.BubbleManagerModel;
import model.objects.BubbleModel;

import static model.utilz.Constants.PlayerConstants.DEATH;

public class Playing {

    private PlayerModel player;
    private static Playing instance;
    private EnemyManagerModel enemyManagerModel;
    private BubbleManagerModel bubbleManagerModel;

    public static Playing getInstance() {
        if (instance == null) {
            instance = new Playing();
        }
        return instance;
    }

    private Playing() {
        player = PlayerModel.getInstance();
        enemyManagerModel = EnemyManagerModel.getInstance();
        bubbleManagerModel = BubbleManagerModel.getInstance();
    }

    public void update() {
        if (!(player.isGameOver())) {
            if (player.getPlayerAction() != DEATH)
                player.update();
            bubbleManagerModel.update();
            enemyManagerModel.update();
        }
    }

    public PlayerModel getPlayer() {
        return player;
    }

}
