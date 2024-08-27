package model.entities.enemies;

import model.LevelManagerModel;
import model.entities.PlayerModel;

import java.util.ArrayList;
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
    private ArrayList<EnemyModel> enemies;

    private Random random;

    public static EnemyManagerModel getInstance() {
        if (instance == null) {
            instance = new EnemyManagerModel();
        }
        return instance;
    }

    private EnemyManagerModel() {
        levelManagerModel = LevelManagerModel.getInstance();
        initEnemies();
        random = new Random();
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
            } else {
                if(!(zenChan.isDeathMovement()))
                    doDeathMovement(zenChan);
            }
        }
    }

    private void doDeathMovement(ZenChanModel zenChan) {
         if(!(zenChan.getEnemyTileY() >= TILES_IN_HEIGHT - 3)) {
             parableMovement(zenChan);
         } else {
             zenChan.setDeathMovement(true);
         }
    }

    private void parableMovement(ZenChanModel zenChan) {
        float xMov = random.nextInt(3) + 1; // tra 1 e 3
        float yMov = random.nextInt(2) + 1; // tra 1 e 2

        if(zenChan.isInvertDeathMovement()) {
            float randomNumberX = 0.1f + (0.3f - 0.1f) * random.nextFloat();;
            float randomNumberY = 2.0f + (3.0f - 2.0f) * random.nextFloat();
            zenChan.getHitbox().x -= 0.1;
            zenChan.getHitbox().y += 1.5;
        } else {
            if((zenChan.getEnemyTileX() >= TILES_IN_WIDTH - 1 || zenChan.getEnemyTileX() <= 1)) {
                zenChan.setInvertDeathMovement(true);
            } else {
                zenChan.getHitbox().x += xMov;
                zenChan.getHitbox().y -= yMov;
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
