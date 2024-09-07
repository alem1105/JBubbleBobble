package model.entities.enemies;

import model.LevelManagerModel;
import model.entities.PlayerModel;
import model.gamestate.UserStateModel;
import model.objects.items.FoodModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.DEAD;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.PlayerConstants.DEATH;

public class EnemyManagerModel {

    private static EnemyManagerModel instance;
    private LevelManagerModel levelManagerModel;
    private ArrayList<MaitaModel> maitas;
    private ArrayList<ZenChanModel> zenChans;
    private ArrayList<HidegonsModel> hidegons;
    private ArrayList<InvaderModel> invaders;
    private ArrayList<MonstaModel> monstas;
    private ArrayList<DrunkModel> drunks;
    private ArrayList<EnemyModel> enemies;

    private ArrayList<FoodModel> foods;
    private Random random;

    private boolean levelEndTimer;
    private int levelEndTick = 0;
    private int levelEndTimerDuration = 720;

    // Power Up
    private boolean timeFrozen;

    public static EnemyManagerModel getInstance() {
        if (instance == null) {
            instance = new EnemyManagerModel();
        }
        return instance;
    }

    private EnemyManagerModel() {
        levelManagerModel = LevelManagerModel.getInstance();
        initEnemyAndFoodArrays();
        random = new Random();
    }

    public void initEnemyAndFoodArrays(){
        zenChans = levelManagerModel.getLevels().get(levelManagerModel.getLvlIndex()).getZenChans();
        maitas = levelManagerModel.getLevels().get(levelManagerModel.getLvlIndex()).getMaitas();
        monstas = levelManagerModel.getLevels().get(levelManagerModel.getLvlIndex()).getMonstas();
        drunks = levelManagerModel.getLevels().get(levelManagerModel.getLvlIndex()).getDrunks();
        invaders = levelManagerModel.getLevels().get(levelManagerModel.getLvlIndex()).getInvaders();
        hidegons = levelManagerModel.getLevels().get(levelManagerModel.getLvlIndex()).getHidegons();
        foods = new ArrayList<>();
        createGeneralEnemiesArray();
    }

    private void createGeneralEnemiesArray() {
        enemies = Stream.of(zenChans, invaders, monstas, maitas, hidegons, drunks)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));
        if (levelManagerModel.getLvlIndex() + 1 == levelManagerModel.getLevels().size())
            enemies.add(new SuperDrunkModel(100, 100, (int) (36 * SCALE), (int) (39 * SCALE)));
    }

    public void update() {
        checkIfAllEnemiesAreDead();
        checkEnemiesCollisionAndUpdatePos();
        checkLevelEndTimer();
        checkFoodCollision();
    }

    private void checkFoodCollision() {
        for(FoodModel food : foods) {
            if (food.getHitbox().intersects(getPlayerModel().getHitbox())) {
                if (food.isActive()) {
                    food.setActive(false);
                    UserStateModel.getInstance().getCurrentUserModel().incrementTempScore(food.getGivenScoreAmount());
                }
            }
        }
    }

    private void checkLevelEndTimer() {
        if(levelEndTimer) {
            if (levelEndTick >= levelEndTimerDuration) {
                LevelManagerModel.getInstance().loadNextLevel();
                levelEndTimer = false;
                levelEndTick = 0;
            }
            levelEndTick++;
        }
    }

    private void checkEnemiesCollisionAndUpdatePos() {
        for(EnemyModel enemy : enemies) {
            if(enemy.isActive())
                handleActiveEnemy(enemy);
            else
                handleInactiveEnemy(enemy);
        }
    }

    private void handleActiveEnemy(EnemyModel enemy) {
        if(!timeFrozen)
            enemy.update();

        if(enemy.getHitbox().intersects(getPlayerModel().getHitbox()))
            handleCollisionWithPlayer(enemy);
    }

    private void handleInactiveEnemy(EnemyModel enemy) {
        if(!enemy.isDeathMovement()) {
            int sideHit = (enemy.walkDir == RIGHT) ? LEFT : RIGHT;
            enemy.doDeathMovement(enemy, sideHit);
        } else {
           spawnFood(enemy);
        }
    }

    private void handleCollisionWithPlayer(EnemyModel enemy) {
        PlayerModel player = getPlayerModel();

        if(enemy.isInBubble()) {
            enemy.setEnemyState(DEAD);
            enemy.setActive(false);
        } else if(player.getPlayerAction() != DEATH && !player.isInvincible()) {
            player.playerHasBeenHit();
        }
    }

    private void spawnFood(EnemyModel enemyModel) {
        if(!enemyModel.isFoodSpawned()) {
            foods.add(new FoodModel(enemyModel.getEnemyTileX() * TILES_SIZE, enemyModel.getEnemyTileY() * TILES_SIZE, (int) (18 * SCALE), (int) (18 * SCALE), random.nextInt(5)));
            enemyModel.setFoodSpawned(true);
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
                levelEndTimer = true;
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

    public ArrayList<FoodModel> getFoods() {
        return foods;
    }

    public void setTimeFrozen(boolean timeFrozen) {
        this.timeFrozen = timeFrozen;
    }
}
