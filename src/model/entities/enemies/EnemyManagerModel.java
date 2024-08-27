package model.entities.enemies;

import model.LevelManagerModel;
import model.entities.PlayerModel;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static model.utilz.Constants.Enemies.DEAD;
import static model.utilz.Constants.PlayerConstants.DEATH;

public class EnemyManagerModel {

    private static EnemyManagerModel instance;
    private LevelManagerModel levelManagerModel;
    private ArrayList<MaitaModel> maitas;
    private ArrayList<ZenChanModel> zenChans;
    private ArrayList<EnemyModel> enemies;

    public static EnemyManagerModel getInstance() {
        if (instance == null) {
            instance = new EnemyManagerModel();
        }
        return instance;
    }

    private EnemyManagerModel() {
        levelManagerModel = LevelManagerModel.getInstance();
        initEnemies();
    }

    public void initEnemies(){
        zenChans = levelManagerModel.getLevels().get(levelManagerModel.getLvlIndex()).getZenChans();
        maitas = new ArrayList<>();
        createGeneralEnemiesArray();
    }

    private void createGeneralEnemiesArray() {
        enemies = Stream
                .concat(zenChans.stream(), maitas.stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void update() {
        checkIfAllEnemiesAreDead();
        checkEnemiesCollision();
    }

    private void checkEnemiesCollision(){
        for (ZenChanModel zenChan : zenChans) {
            if (zenChan.isActive()) {
                zenChan.update();
                if (zenChan.getHitbox().intersects(getPlayerModel().getHitbox())) {
                    if (zenChan.isInBubble()) {
                        zenChan.setEnemyState(DEAD);
                        zenChan.setResetAniTick(true);
                        zenChan.setActive(false);
                    }
                    else if (getPlayerModel().getPlayerAction() != DEATH) {
                        getPlayerModel().playerHasBeenHit();
                    }
                }
            }
        }
    }

    private void checkIfAllEnemiesAreDead() {
        int i = 0;
        if (enemies.isEmpty()){
            LevelManagerModel.getInstance().loadNextLevel();
            return;
        }
        while(!(enemies.get(i).isActive())){
            if(i == enemies.size() - 1) {
                LevelManagerModel.getInstance().loadNextLevel();
                break;
            }
            i++;
        }
    }

    public ArrayList<ZenChanModel> getZenChans() {
        return zenChans;
    }

    private PlayerModel getPlayerModel() {
        return PlayerModel.getInstance();
    }

    public ArrayList<EnemyModel> getEnemies() {
        return enemies;
    }
}
