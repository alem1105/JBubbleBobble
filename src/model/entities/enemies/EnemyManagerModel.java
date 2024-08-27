package model.entities.enemies;

import model.LevelManagerModel;
import model.entities.PlayerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static model.utilz.Constants.Enemies.DEAD;
import static model.utilz.Constants.GameConstants.TILES_IN_HEIGHT;
import static model.utilz.Constants.GameConstants.TILES_IN_WIDTH;
import static model.utilz.Constants.PlayerConstants.DEATH;

public class EnemyManagerModel {

    private static EnemyManagerModel instance;
    private LevelManagerModel levelManagerModel;
    private ArrayList<MaitaModel> maitas;
    private ArrayList<ZenChanModel> zenChans;
    private ArrayList<HidegonsModel> hidegons;
    private ArrayList<PulpulModel> pulpuls;
    private ArrayList<MonstaModel> monstas;
    private ArrayList<DrunkModel> drunks;
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
        maitas = levelManagerModel.getLevels().get(levelManagerModel.getLvlIndex()).getMaitas();
        monstas = new ArrayList<>();
        pulpuls = new ArrayList<>();
        hidegons = new ArrayList<>();
        drunks = new ArrayList<>();
        createGeneralEnemiesArray();
    }

    private void createGeneralEnemiesArray() {
        enemies = Stream.of(zenChans, pulpuls, monstas, maitas, hidegons)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void update() {
        checkIfAllEnemiesAreDead();
        checkEnemiesCollision();
    }

    private void checkEnemiesCollision(){
        for (EnemyModel enemyModel : enemies) {
            if (enemyModel.isActive()) {
                enemyModel.update();
                if (enemyModel.getHitbox().intersects(getPlayerModel().getHitbox()) && !getPlayerModel().isInvincible()) {
                    if (enemyModel.isInBubble()) {
                        enemyModel.setEnemyState(DEAD);
                        enemyModel.setActive(false);
                    }
                    else if (getPlayerModel().getPlayerAction() != DEATH) {
                        getPlayerModel().playerHasBeenHit();
                    }
                }
            } else {
                if(!(enemyModel.isDeathMovement()))
                    enemyModel.doDeathMovement(enemyModel);
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

    public ArrayList<MaitaModel> getMaitas() {
        return maitas;
    }
}
