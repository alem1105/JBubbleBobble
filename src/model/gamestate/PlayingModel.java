package model.gamestate;

import model.LevelManagerModel;
import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;
import model.objects.items.powerups.PowerUpsManagerModel;
import model.objects.projectiles.ProjectileManagerModel;
import model.objects.bobbles.BubbleManagerModel;

import static model.utilz.Constants.PlayerConstants.DEATH;

public class PlayingModel {

    private PlayerModel player;
    private static PlayingModel instance;
    private EnemyManagerModel enemyManagerModel;
    private BubbleManagerModel bubbleManagerModel;
    private ProjectileManagerModel projectileManagerModel;
    private PowerUpsManagerModel powerUpsManagerModel;
    private boolean paused;

    public static PlayingModel getInstance() {
        if (instance == null) {
            instance = new PlayingModel();
        }
        return instance;
    }

    private PlayingModel() {
        player = PlayerModel.getInstance();
        enemyManagerModel = EnemyManagerModel.getInstance();
        bubbleManagerModel = BubbleManagerModel.getInstance();
        projectileManagerModel = ProjectileManagerModel.getInstance();
        powerUpsManagerModel = PowerUpsManagerModel.getInstance();
    }

    public void update() {
        if (!player.isGameOver() && !paused) {
            if (!LevelManagerModel.getInstance().isNextLevel()){
                if (player.getPlayerAction() != DEATH)
                    player.update();
                projectileManagerModel.update();
                bubbleManagerModel.update();
                enemyManagerModel.update();
                powerUpsManagerModel.update();
            }
        }
    }

    public PlayerModel getPlayer() {
        return player;
    }

    public boolean isPaused() {
        return paused;
    }

    public void invertPaused() {
        paused = !paused;
    }

}
