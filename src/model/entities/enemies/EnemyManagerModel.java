package model.entities.enemies;

import model.LevelManager;
import model.entities.PlayerModel;

import java.util.ArrayList;

import static model.utilz.Constants.PlayerConstants.DEATH;

public class EnemyManagerModel {

    private static EnemyManagerModel instance;
    private LevelManager levelManager;

    private ArrayList<ZenChanModel> zenChans;

    public static EnemyManagerModel getInstance() {
        if (instance == null) {
            instance = new EnemyManagerModel();
        }
        return instance;
    }

    private EnemyManagerModel() {
        levelManager = LevelManager.getInstance();
        initEnemies();
    }

    private void initEnemies(){
        zenChans = levelManager.getLevels().get(levelManager.getLvlIndex()).getZenChans();
    }

    public void update() {
        for (ZenChanModel zenChan : zenChans) {
            zenChan.update();
            if (zenChan.getHitbox().intersects(getPlayerModel().getHitbox())) {
                if (getPlayerModel().getPlayerAction() != DEATH)
                    getPlayerModel().playerHasBeenHit();
            }
        }
    }

    public ArrayList<ZenChanModel> getZenChans() {
        return zenChans;
    }

    private PlayerModel getPlayerModel() {
        return PlayerModel.getInstance();
    }
}
