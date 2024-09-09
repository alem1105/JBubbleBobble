package model.objects.bobbles;

import model.LevelManagerModel;
import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;
import model.entities.enemies.EnemyModel;
import model.entities.enemies.SuperDrunkModel;
import model.gamestate.UserStateModel;
import model.objects.CustomObjectModel;

import java.awt.geom.Rectangle2D;
import java.util.*;

import static model.utilz.Constants.Enemies.DEAD;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.PlayerConstants.DEATH;
import static model.utilz.Constants.SpecialBubbles.*;
import static model.utilz.Gravity.canMoveHere;
import static model.utilz.Gravity.isTileSolid;
import static model.utilz.UtilityMethods.getLvlData;

/**
 * Questa classe gestisce la logica delle bolle, inclusi gli aggiornamenti e la creazione di bolle,
 * effetti speciali e interazioni con il giocatore e i nemici.
 */
public class BubbleManagerModel {

    /**
     * L'istanza singleton della classe BubbleManagerModel.
     */
    private static BubbleManagerModel instance;

    /**
     * La lista delle bolle di Bob attive nel gioco.
     */
    private ArrayList<BobBubbleModel> bobBubbles;

    /**
     * La lista delle bolle attive nel gioco.
     */
    private ArrayList<BubbleModel> bubbles;

    /**
     * La lista dei modelli di fuoco attivi nel gioco.
     */
    private ArrayList<FireModel> fires;

    /**
     * La lista dei modelli di fulmine attivi nel gioco.
     */
    private ArrayList<LightningModel> lightnings;

    /**
     * Generatore casuale utilizzato per spawnare bolle casualmente.
     */
    private Random rand;

    /**
     * Contatore per il tempo trascorso dall'ultimo spawn di una bolla.
     */
    int spawnBubbleTick = 0;

    /**
     * Durata tra lo spawn di una bolla e un'altra.
     */
    int spawnBubbleDuration = 1200;

    /**
     * Il punteggio guadagnato per ogni bolla esplosa.
     */
    private int scoreForPop = 10;

    /**
     * Mappa che tiene traccia degli estensioni dei potenziamenti attivi.
     * La chiave rappresenta un tipo di potenziamento, il valore indica se è attivo o meno.
     */
    private HashMap<Character, Boolean> extend;

    /**
     * Indica se il giocatore è stato intrappolato dall'acqua.
     */
    private boolean generalTrappedPlayer;

    /**
     * Indica se il giocatore si trova su un tappeto di fuoco.
     */
    private boolean playerInTheFireCarpet;


    /**
     * Restituisce l'istanza singleton della classe BubbleManagerModel.
     *
     * @return L'istanza singleton di BubbleManagerModel.
     */
    public static BubbleManagerModel getInstance() {
        if (instance == null) {
            instance = new BubbleManagerModel();
        }
        return instance;
    }

    /**
     * Costruttore privato per inizializzare le variabili della classe.
     */
    private BubbleManagerModel() {
        bobBubbles = new ArrayList<>();
        bubbles = new ArrayList<>();
        rand = new Random();
        fires = new ArrayList<>();
        lightnings = new ArrayList<>();
        extend = new HashMap<>() {{
            put('E', false);
            put('x', false);
            put('t', false);
            put('e', false);
            put('n', false);
            put('d', false);
        }};
    }

    /**
     * Aggiorna lo stato delle bolle e gestisce le interazioni.
     */
    public void update(){
        updateRandomBubbleSpawn();
        updateBubbles();
        updateBobBubble();
        updateExplodedBubbles();
        checkExtend();
        createFireCarpet();
    }

    /**
     * Controlla se attivare extend e incrementa le vite del giocatore.
     */
    private void checkExtend() {
        if (extend.values().stream().anyMatch(b -> !b))
            return;

        PlayerModel.getInstance().incrementLives();
        extend.keySet().forEach(k -> extend.put(k, false));
    }

    /**
     * Aggiorna le bolle esplose e gestisce gli effetti del fuoco e del fulmine.
     */
    private void updateExplodedBubbles() {
        playerInTheFireCarpet = false;
        for (FireModel fireModel : fires){
            if (fireModel.isActive()) {
                fireModel.update();
                if (fireModel.isPartOfTheCarpet()){
                    checkObjectHitEnemy(fireModel);
                    checkIntersectsFireCarpet(fireModel);
                }

            }
        }

        if (!playerInTheFireCarpet)
            PlayerModel.getInstance().setPlayerSpeed(1.0f * SCALE);

        for (LightningModel lightningModel : lightnings) {
            if (lightningModel.isActive()) {
                lightningModel.update();
                checkObjectHitEnemy(lightningModel);
            }
        }
    }

    /**
     * Controlla se un fulmine o il fuoco hanno colpito un nemico.
     *
     * @param objectModel L'oggetto da controllare.
     */
    private void checkObjectHitEnemy(CustomObjectModel objectModel) {
        for(EnemyModel enemy : EnemyManagerModel.getInstance().getEnemies()) {
            if(objectModel.getHitbox().intersects(enemy.getHitbox())) {

                if(objectModel instanceof LightningModel && enemy instanceof SuperDrunkModel)
                    handleSuperDrunkLives((SuperDrunkModel) enemy, (LightningModel) objectModel);
                else if (enemy.isActive())
                    enemy.setActive(false);

            }
        }
    }

    /**
     * Gestisce la logica delle vite del boss SuperDrunk se colpito da un fulmine.
     *
     * @param superDrunk Il nemico SuperDrunk colpito.
     * @param lightningModel Il fulmine che ha colpito il nemico.
     */
    private void handleSuperDrunkLives(SuperDrunkModel superDrunk, LightningModel lightningModel) {
        if(lightningModel.isAlreadyHitBoss())
            return;

        if(superDrunk.getLives() <= 0)
            superDrunk.setInBubble(true);
        else{
            superDrunk.decreaseLives();
            superDrunk.setHasBeenHit(true);
        }

        lightningModel.setAlreadyHitBoss(true);
    }

    /**
     * Aggiorna lo stato delle bolle attive e della cascata d'acqua.
     */
    private void updateBubbles() {
        for (BubbleModel bubble : bubbles){
            if (bubble.isActive()) {
                checkPlayerHit(bubble);
                bubble.update();
            } else {
                createWaterfallAndUpdateIt(bubble);
            }
        }
    }

    /**
     * Crea e aggiorna una cascata d'acqua associata a una bolla.
     *
     * @param bubble La bolla da controllare.
     */
    private void createWaterfallAndUpdateIt(BubbleModel bubble) {
        // controlla se il tipo di bolla è una bolla d'acqua
        if (bubble.getBubbleType() == WATER_BUBBLE) {
            // genera la cascata d'acqua
            bubble.spawnWaterFall();
            ArrayList<WaterModel> waterfallArray = bubble.getWaterfall();
            ArrayList<EnemyModel> enemies = EnemyManagerModel.getInstance().getEnemies();

            // itera attraverso la cascata d'acqua
            for (int i = 0; i < waterfallArray.size(); i++) {
                WaterModel currentWater = waterfallArray.get(i);
                if(currentWater.isActive()) {
                    currentWater.update();
                    checkIfWaterFellOffTheMap(currentWater);
                    checkIfWaterfallHitAnEnemy(i, waterfallArray, currentWater, enemies);
                    checkIfWaterfallHitAPlayer(currentWater, waterfallArray);
                }
            }
        }
    }

    /**
     * Controlla se l'acqua è uscita dalla mappa.
     *
     * @param waterModel Il modello dell'acqua da controllare.
     */
    private void checkIfWaterFellOffTheMap(WaterModel waterModel) {
        if(waterModel.getHitbox().getY() >= GAME_HEIGHT - TILES_SIZE) {
            waterModel.setActive(false);
            waterModel.setSpecificTrappedPlayer(false);
            generalTrappedPlayer = false;
            PlayerModel.getInstance().setInAir(true);
        }
    }

    /**
     * Controlla se una cascata d'acqua ha colpito il giocatore.
     *
     * @param currentWater L'acqua corrente da controllare.
     * @param waterfallArray L'array delle cascate d'acqua.
     */
    private void checkIfWaterfallHitAPlayer(WaterModel currentWater, ArrayList<WaterModel> waterfallArray) {
        if(currentWater.getHitbox().intersects(getPlayerHitbox()) && !generalTrappedPlayer && waterfallArray.size() == 10) {
            generalTrappedPlayer = true;
            currentWater.setSpecificTrappedPlayer(true);
        }

        if(PlayerModel.getInstance().getJump() || PlayerModel.getInstance().getPlayerAction() == DEATH) {
            generalTrappedPlayer = false;
            if(currentWater.isSpecificTrappedPlayer())
                currentWater.setSpecificTrappedPlayer(false);
        }

        if(currentWater.isSpecificTrappedPlayer()) {
            PlayerModel.getInstance().getHitbox().x = currentWater.getHitbox().x;
            PlayerModel.getInstance().getHitbox().y = currentWater.getHitbox().y - (PlayerModel.getInstance().getHitbox().height - currentWater.getHitbox().height);
        }
    }

    /**
     * Controlla se una cascata d'acqua ha colpito un nemico.
     *
     * @param i Indice della cascata d'acqua.
     * @param waterfallArray L'array delle cascate d'acqua.
     * @param currentWater L'acqua corrente.
     * @param enemies L'elenco dei nemici.
     */
    private void checkIfWaterfallHitAnEnemy(int i, ArrayList<WaterModel> waterfallArray, WaterModel currentWater, ArrayList<EnemyModel> enemies) {
        if(i != 0 && i != waterfallArray.size() - 1)
            return;

        for (EnemyModel enemy : enemies)
            if (currentWater.getHitbox().intersects(enemy.getHitbox()))
                enemy.setActive(false);
    }

    /**
     * Aggiorna lo stato delle bolle di Bob.
     */
    private void updateBobBubble() {
        for( BobBubbleModel bubble : bobBubbles ){
            if (bubble.isActive()) {
                bubble.update();
                checkPlayerHit(bubble);
                checkEnemyHasBeenHit(bubble);
                checkCollisionWithOtherBubbles(bubble);
            } else {
                checkLightingBubbleSpawnAfterPowerUpPickup(bubble);
            }
        }
    }

    /**
     * Controlla se una bolla di Bob ha generato un fulmine dopo il pickup di un potenziamento.
     *
     * @param bubble La bolla di Bob da controllare.
     */
    private void checkLightingBubbleSpawnAfterPowerUpPickup(BobBubbleModel bubble) {
        if(!PlayerModel.getInstance().isShootingLightningBubble())
            return;

        if(bubble.isAlreadyShotLighting())
            return;

        lightnings.add(new LightningModel(bubble.getHitbox().x, bubble.getHitbox().y, (int) (16 * SCALE), (int) (16 * SCALE), PlayerModel.getInstance().getFacing()));
        bubble.setAlreadyShotLighting(true);
    }

    /**
     * Fa lo spawn casuale delle bolle speciali (acqua, fuoco, fulmini, EXTEND)
     */
    private void updateRandomBubbleSpawn() {
        spawnBubbleTick++;
        if (spawnBubbleTick >= spawnBubbleDuration) {
            spawnBubbleTick = 0;
            int randomBubbleType = rand.nextInt(3); //2;

            int[] excludedFromSpawningBubbles = new int[4];
            checkWhichBubbleCanSpawn(excludedFromSpawningBubbles);
            while(excludedFromSpawningBubbles[randomBubbleType] == 1)
                randomBubbleType = rand.nextInt(4);


            int max = (TILES_IN_WIDTH - 1) * TILES_SIZE;
            int min = TILES_SIZE;

            int randomX = rand.nextInt(max - min + 1) + min;
            int y = GAME_HEIGHT;
            bubbles.add(new BubbleModel(randomX, y,(int) (14 * SCALE), (int) (16 * SCALE), randomBubbleType));
        }
    }

    /**
     * Genera le bolle speciali in base al livello in cui ci troviamo
     * @param excludedFromSpawningBubbles Array che indica quali bolle speciale possono spawnare in base al livello
     */
    private void checkWhichBubbleCanSpawn(int[] excludedFromSpawningBubbles) {
        LevelManagerModel levelManagerModel = LevelManagerModel.getInstance();

        if(levelManagerModel.getLvlIndex() + 1 == levelManagerModel.getLevels().size()) {
            excludedFromSpawningBubbles[WATER_BUBBLE] = 1;
            excludedFromSpawningBubbles[FIRE_BUBBLE] = 1;
            excludedFromSpawningBubbles[EXTEND_BUBBLE] = 1;
        }

        if(floorDoesNotHaveHoles())
            excludedFromSpawningBubbles[WATER_BUBBLE] = 1;
    }

    /**
     * Controlla se il pavimento non ha buchi
     * @return boolean
     */
    private boolean floorDoesNotHaveHoles() {
        int yTile = TILES_IN_HEIGHT - 2;

        for(int x = 0; x < TILES_IN_WIDTH; x++)
            if(!isTileSolid(x, yTile, getLvlData()))
                return false;

        return true;
    }

    /**
     * Controlla se un giocatore è stato colpito da una bolla.
     *
     * @param bubble La bolla da controllare.
     */
    private void checkPlayerHit(BubbleModel bubble) {
        if (intersectBubbleFromBelow()) {

            if (bubble.getHitbox().intersects(PlayerModel.getInstance().getHitbox()) && bubble.isCollision()) {

                if(canMoveHere(bubble.getHitbox().x, bubble.getHitbox().y, bubble.getHitbox().width, bubble.getHitbox().height, getLvlData())) {
                    UserStateModel.getInstance().getCurrentUserModel().incrementTempScore(scoreForPop);
                    PlayerModel.getInstance().incrementPoppedBubbles();
                    bubble.setActive(false);
                    bubble.setTimeout(true);

                switch (bubble.getBubbleType()) {
                    case WATER_BUBBLE -> {
                        bubble.setyWhenPopped(bubble.getHitbox().y);
                        bubble.setxWhenPopped(bubble.getHitbox().x);
                        PlayerModel.getInstance().incrementPoppedWaterBubbles();
                    }
                    case LIGHTNING_BUBBLE -> {
                        lightnings.add(new LightningModel(bubble.getHitbox().x, bubble.getHitbox().y, (int) (16 * SCALE), (int) (16 * SCALE), PlayerModel.getInstance().getFacing()));
                        PlayerModel.getInstance().incrementPoppedLightingBubbles();
                    }
                    case FIRE_BUBBLE -> {
                        fires.add(new FireModel(bubble.getHitbox().x, bubble.getHitbox().y, false));
                        PlayerModel.getInstance().incrementPoppedFireBubbles();
                    }
                    case EXTEND_BUBBLE -> extend.put(bubble.getExtendChar(), true);
                }

                checkIntersects(bubble);
                }
            }
        }
    }

    /**
     * Controlla se il player ha toccato del fuoco, lo rallenta se succede
     * @param fireModel il modello del fuoco
     */
    private void checkIntersectsFireCarpet(FireModel fireModel) {
        if (fireModel.getHitbox().intersects(PlayerModel.getInstance().getHitbox())) {
            PlayerModel.getInstance().setPlayerSpeed(0.35f * SCALE);
            playerInTheFireCarpet = true;
        }
    }

    /**
     * Controlla se il player sta toccando una bolla con la testa
     * @return boolean
     */
    private boolean intersectBubbleFromBelow(){
        return !PlayerModel.getInstance().getJump() && PlayerModel.getInstance().getAirSpeed() != 0;
    }

    /**
     * Controlla le intersezioni della bolla con il player e con le altre bolle per farle scoppiare tutte insieme
     * @param bubble la bolla da controllare
     */
    public void checkIntersects(BubbleModel bubble) {
        for (BobBubbleModel bob : bobBubbles) {
            if (bob.isActive()) {
                if (bob.getHitbox().intersects(bubble.getHitbox())) {
                    bob.setActive(false);
                    bubble.setTimeout(true);
                    UserStateModel.getInstance().getCurrentUserModel().incrementTempScore(scoreForPop);
                    PlayerModel.getInstance().incrementPoppedBubbles();
                    checkIntersects(bob);
                }
            }
        }
        for (EnemyModel enemy : EnemyManagerModel.getInstance().getEnemies())
            if (enemy.isActive() && enemy.isInBubble()) {
                if (bubble.getHitbox().intersects(enemy.getHitbox())) {
                    enemy.setEnemyState(DEAD);
                    enemy.setActive(false);
                    EnemyManagerModel.getInstance().checkCollisionWithOtherBubbles(enemy);
                }
            }
    }

    /**
     * Controlla se ci sono collisioni tra le bolle.
     *
     * @param bubble1 La bolla da controllare.
     */
    private void checkCollisionWithOtherBubbles(BobBubbleModel bubble1) {
        for( BobBubbleModel bubble2 : bobBubbles ){
            if(bubble1.getHitbox().intersects(bubble2.getHitbox()) && bubble1.isActive() && bubble2.isActive()){
                Random random = new Random();
                bobBubblesRandomMovements(random, bubble1, bubble2);
            }
        }
    }

    /**
     * Controlla se un nemico è stato colpito da una bolla di Bob,
     * e in tal caso lo intappola in una bolla.
     *
     * @param bubble La bolla di Bob da controllare.
     */
    private void checkEnemyHasBeenHit(BobBubbleModel bubble) {
        for(EnemyModel enemy : EnemyManagerModel.getInstance().getEnemies()) {
            if(bubble.getHitbox().intersects(enemy.getHitbox()) && !(bubble.isCollision())) {
                if (enemy.isActive() && !(enemy instanceof SuperDrunkModel)) {
                    bubble.setActive(false);
                    enemy.setInBubble(true);
                }
            }
        }
    }

    /**
     * Crea un tappeto di fuoco, a seguito dello scoppio della bolla di fuoco.
     */
    private void createFireCarpet(){
        ArrayList<FireModel> tempArray = new ArrayList<>();
        for (FireModel fireModel: fires) {
            if (fireModel.isCreatingCarpet()) {
                fireModel.setCreatingCarpet(false);
                int i = 1;
                float currentX = fireModel.getX();
                while (!canMoveHere( currentX + fireModel.getWidth(), fireModel.getY() + fireModel.getHeight(), fireModel.getWidth(), fireModel.getHeight(), getLvlData())
                && canMoveHere(currentX + fireModel.getWidth(), fireModel.getY(), fireModel.getWidth(), fireModel.getHeight(), getLvlData())) {
                    currentX = fireModel.getX() + fireModel.getWidth() * i;
                    tempArray.add(new FireModel(currentX, fireModel.getY(),true));
                    i++;
                }
            }
        }
        fires.addAll(tempArray);
    }

    /**
     * Quando due bolle si toccano le fa muovere in modo random
     * @param random
     * @param bubble1 prima bolla
     * @param bubble2 seconda bolla
     */
    private void bobBubblesRandomMovements(Random random, BobBubbleModel bubble1, BobBubbleModel bubble2) {
        float randomNumberX = 0.3f + random.nextFloat() * (1.5f - 0.3f);
        float randomNumberY = 0.3f + random.nextFloat() * (1.5f - 0.3f);
        bubble1.getHitbox().x += randomNumberX;
        bubble2.getHitbox().x -= randomNumberX;

        if( (int) (bubble1.getHitbox().y + randomNumberY) / TILES_SIZE < 1)
            bubble1.getHitbox().y = TILES_SIZE;
        else
            bubble1.getHitbox().y += randomNumberY;

        if( (int) (bubble2.getHitbox().y - randomNumberY) / TILES_SIZE < 1)
            bubble2.getHitbox().y = TILES_SIZE;
        else
            bubble2.getHitbox().y -= randomNumberY;
    }

    /**
     * Resetta tutti gli array delle bolle e degli oggetti associati
     */
    public void resetBubbles() {
        bobBubbles.clear();
        bubbles.clear();
        fires.clear();
        lightnings.clear();
    }

    public void addBobBubbles(BobBubbleModel bobBubble) {
        bobBubbles.add(bobBubble);
    }

    public ArrayList<BobBubbleModel> getBobBubbles() {
        return bobBubbles;
    }

    public ArrayList<BubbleModel> getBubbles() {
        return bubbles;
    }

    private Rectangle2D.Float getPlayerHitbox() {
        return PlayerModel.getInstance().getHitbox();
    }

    public void setScoreForPop(int value) {
        this.scoreForPop = value;
    }

    public ArrayList<FireModel> getFires() {
        return fires;
    }

    public ArrayList<LightningModel> getLightnings() {
        return lightnings;
    }

    public HashMap<Character, Boolean> getExtend() {
        return extend;
    }
}
