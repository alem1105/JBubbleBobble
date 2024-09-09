package model.entities.enemies;

import model.LevelManagerModel;
import model.entities.PlayerModel;
import model.gamestate.UserStateModel;
import model.objects.bobbles.BobBubbleModel;
import model.objects.bobbles.BubbleManagerModel;
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

/**
 * La classe {@code EnemyManagerModel} gestisce tutti i nemici nel gioco, inclusi
 * il loro stato, la posizione e le interazioni con il giocatore. È implementata come
 * un singleton per garantire un'unica istanza durante il ciclo di vita dell'applicazione.
 */
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

    /**
     * Restituisce l'istanza singleton della classe {@code EnemyManagerModel}.
     *
     * @return l'istanza singleton di {@code EnemyManagerModel}
     */
    public static EnemyManagerModel getInstance() {
        if (instance == null) {
            instance = new EnemyManagerModel();
        }
        return instance;
    }

    /**
     * Costruttore privato per inizializzare il gestore dei nemici.
     */
    private EnemyManagerModel() {
        levelManagerModel = LevelManagerModel.getInstance();
        initEnemyAndFoodArrays();
        random = new Random();
    }

    /**
     * Inizializza gli array di nemici e cibo per il livello attuale.
     */
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

    /**
     * Crea un array generale di nemici combinando tutti i nemici attivi nel livello corrente.
     */
    private void createGeneralEnemiesArray() {
        enemies = Stream.of(zenChans, invaders, monstas, maitas, hidegons, drunks)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));
        if (levelManagerModel.getLvlIndex() + 1 == levelManagerModel.getLevels().size())
            enemies.add(new SuperDrunkModel(100, 100, (int) (36 * SCALE), (int) (39 * SCALE)));
    }

    /**
     * Aggiorna lo stato di tutti i nemici, controllando collisioni e posizioni.
     */
    public void update() {
        checkIfAllEnemiesAreDead();
        checkEnemiesCollisionAndUpdatePos();
        checkLevelEndTimer();
        checkFoodCollision();
    }

    /**
     * Controlla le collisioni tra il cibo e il giocatore.
     */
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

    /**
     * Controlla il timer per la fine del livello e carica il livello successivo se necessario.
     */
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

    /**
     * Controlla le collisioni tra i nemici e aggiorna le loro posizioni.
     */
    private void checkEnemiesCollisionAndUpdatePos() {
        for(EnemyModel enemy : enemies) {
            if(enemy.isActive())
                handleActiveEnemy(enemy);
            else
                handleInactiveEnemy(enemy);
        }
    }

    /**
     * Gestisce un nemico attivo, aggiornandone lo stato e controllando le collisioni con il giocatore.
     *
     * @param enemy il nemico attivo da gestire
     */
    private void handleActiveEnemy(EnemyModel enemy) {
        if(!timeFrozen)
            enemy.update();

        if(enemy.getHitbox().intersects(getPlayerModel().getHitbox()))
            handleCollisionWithPlayer(enemy);
    }

    /**
     * Gestisce un nemico inattivo e la sua morte.
     *
     * @param enemy il nemico inattivo da gestire
     */
    private void handleInactiveEnemy(EnemyModel enemy) {
        if(!enemy.isDeathMovement()) {
            int sideHit = (enemy.walkDir == RIGHT) ? LEFT : RIGHT;
            enemy.doDeathMovement(enemy, sideHit);
        } else {
            spawnFood(enemy);
        }
    }

    /**
     * Gestisce la collisione tra un nemico e il giocatore.
     *
     * @param enemy il nemico con cui il giocatore ha colliso
     */
    private void handleCollisionWithPlayer(EnemyModel enemy) {
        PlayerModel player = getPlayerModel();

        if(enemy.isInBubble()) {
            enemy.setEnemyState(DEAD);
            enemy.setActive(false);
            checkCollisionWithOtherBubbles(enemy);
        } else if(player.getPlayerAction() != DEATH && !player.isInvincible()) {
            player.playerHasBeenHit();
        }
    }

    /**
     * Controolla se i nemici in bolla quando vengono fatti scoppiare stanno toccando altre bolle o nemici
     * @param enemy1
     */
    public void checkCollisionWithOtherBubbles(EnemyModel enemy1) {
        for (BobBubbleModel bob : BubbleManagerModel.getInstance().getBobBubbles())
            if (bob.isActive() && enemy1.isActive())
                if (bob.getHitbox().intersects(enemy1.getHitbox())) {
                    bob.setActive(false);
                    bob.setTimeout(true);
                    BubbleManagerModel.getInstance().checkIntersects(bob);
                }
        for (EnemyModel enemy2 : enemies) {
            if (enemy2.isActive() && enemy2.isInBubble())
                if (enemy1.getHitbox().intersects(enemy2.getHitbox())){
                    enemy2.setEnemyState(DEAD);
                    enemy2.setActive(false);
                    checkCollisionWithOtherBubbles(enemy2);
                }

        }
    }

    /**
     * Genera cibo quando un nemico muore.
     *
     * @param enemyModel il modello del nemico che ha generato il cibo
     */
    private void spawnFood(EnemyModel enemyModel) {
        if(!enemyModel.isFoodSpawned()) {
            foods.add(new FoodModel(enemyModel.getEnemyTileX() * TILES_SIZE, enemyModel.getEnemyTileY() * TILES_SIZE, (int) (18 * SCALE), (int) (18 * SCALE), random.nextInt(5)));
            enemyModel.setFoodSpawned(true);
        }
    }

    /**
     * Controlla se tutti i nemici sono stati sconfitti e carica il livello successivo se necessario.
     */
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

    private PlayerModel getPlayerModel() {
        return PlayerModel.getInstance();
    }

    public ArrayList<EnemyModel> getEnemies() {
        return enemies;
    }

    public ArrayList<FoodModel> getFoods() {
        return foods;
    }

    public void setTimeFrozen(boolean timeFrozen) {
        this.timeFrozen = timeFrozen;
    }
}

