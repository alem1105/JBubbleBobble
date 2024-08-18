package model.entities.enemies;

import model.LevelManager;

import java.util.ArrayList;

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
        }
    }

    public ArrayList<ZenChanModel> getZenChans() {
        return zenChans;
    }
}
