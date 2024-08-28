package model.gamestate;

import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;
import model.objects.ProjectileManagerModel;
import model.objects.bobbles.BubbleManagerModel;

import static model.utilz.Constants.PlayerConstants.DEATH;

public class Playing {

    private PlayerModel player;
    private static Playing instance;
    private EnemyManagerModel enemyManagerModel;
    private BubbleManagerModel bubbleManagerModel;
    private ProjectileManagerModel projectileManagerModel;

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
        projectileManagerModel = ProjectileManagerModel.getInstance();
    }

    public void update() {
        if (!(player.isGameOver())) {
            if (player.getPlayerAction() != DEATH)
                player.update();
            projectileManagerModel.update();
            bubbleManagerModel.update();
            enemyManagerModel.update();
        }
    }

    public PlayerModel getPlayer() {
        return player;
    }

}
