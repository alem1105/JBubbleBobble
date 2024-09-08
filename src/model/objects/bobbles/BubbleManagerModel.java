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

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.PlayerConstants.DEATH;
import static model.utilz.Constants.SpecialBubbles.*;
import static model.utilz.Gravity.CanMoveHere;
import static model.utilz.Gravity.IsTileSolid;
import static model.utilz.UtilityMethods.getLvlData;

public class BubbleManagerModel {

    private static BubbleManagerModel instance;

    private ArrayList<BobBubbleModel> bobBubbles;
    private ArrayList<BubbleModel> bubbles;
    private ArrayList<FireModel> fires;
    private ArrayList<LightningModel> lightnings;

    private Random rand;
    int spawnBubbleTick = 0;
    int spawnBubbleDuration = 360;

    // Power Up
    private int scoreForPop = 0;
    private HashMap<Character, Boolean> extend;

    private boolean generalTrappedPlayer;
    private boolean playerInTheFireCarpet;

    public static BubbleManagerModel getInstance() {
        if (instance == null) {
            instance = new BubbleManagerModel();
        }
        return instance;
    }

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

    public void update(){
        updateRandomBubbleSpawn();
        updateBubbles();
        updateBobBubble();
        updateExplodedBubbles();
        checkExtend();
        createFireCarpet();
    }

    private void checkExtend() {
// TODO decidere se mettere gli stream !!!
//        if (extend.values().stream().anyMatch(b -> !b)) {
//            return;
//        };

        for (Boolean b : extend.values())
            if(!b)
                return;
        PlayerModel.getInstance().incrementLives();
        extend.keySet().forEach(k -> extend.put(k, false));
    }

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
            if (lightningModel.isActive())
                lightningModel.update();
            checkObjectHitEnemy(lightningModel);
        }
    }

    private void checkObjectHitEnemy(CustomObjectModel objectModel) {
        for(EnemyModel enemy : EnemyManagerModel.getInstance().getEnemies()) {
            if(objectModel.getHitbox().intersects(enemy.getHitbox())) {

                if(objectModel instanceof LightningModel && enemy instanceof SuperDrunkModel)
                    handleSuperDrunkLives((SuperDrunkModel) enemy);

                else if (enemy.isActive()) {
                    //enemy.setEnemyState(DEATH);
                    enemy.setActive(false);
                }
            }
        }
    }

    private void handleSuperDrunkLives(SuperDrunkModel superDrunk) {
        if(superDrunk.getLives() <= 0)
            superDrunk.setInBubble(true);
        else{
            superDrunk.decreaseLives();
            superDrunk.setHasBeenHit(true);
        }
    }

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
                    checkIfWaterfallHitAPlayer(currentWater, waterfallArray, bubble);
                }
            }
        }
    }

    private void checkIfWaterFellOffTheMap(WaterModel waterModel) {
        if((int) (waterModel.getHitbox().getY() / TILES_SIZE) != -1)
            return;

        waterModel.setActive(false);
        waterModel.setSpecificTrappedPlayer(false);
        generalTrappedPlayer = false;
    }

    private void checkIfWaterfallHitAPlayer(WaterModel currentWater, ArrayList<WaterModel> waterfallArray, BubbleModel bubble) {
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

    private void checkIfWaterfallHitAnEnemy(int i, ArrayList<WaterModel> waterfallArray, WaterModel currentWater, ArrayList<EnemyModel> enemies) {
        if(i != 0 && i != waterfallArray.size() - 1)
            return;

        for (EnemyModel enemy : enemies)
            if (currentWater.getHitbox().intersects(enemy.getHitbox()))
                enemy.setActive(false);
    }

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

    private void checkLightingBubbleSpawnAfterPowerUpPickup(BobBubbleModel bubble) {
        if(!PlayerModel.getInstance().isShootingLightningBubble())
            return;

        if(bubble.isAlreadyShotLighting())
            return;

        lightnings.add(new LightningModel(bubble.getHitbox().x, bubble.getHitbox().y, (int) (16 * SCALE), (int) (16 * SCALE), PlayerModel.getInstance().getFacing()));
        bubble.setAlreadyShotLighting(true);
    }

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

    private boolean floorDoesNotHaveHoles() {
        int yTile = TILES_IN_HEIGHT - 2;

        for(int x = 0; x < TILES_IN_WIDTH; x++)
            if(!IsTileSolid(x, yTile, getLvlData()))
                return false;

        return true;
    }

    private void checkPlayerHit(BubbleModel bubble) {
        if (intersectBubbleFromBelow()) {

            if (bubble.getHitbox().intersects(PlayerModel.getInstance().getHitbox()) && bubble.isCollision()) {

                if(CanMoveHere(bubble.getHitbox().x, bubble.getHitbox().y, bubble.getHitbox().width, bubble.getHitbox().height, getLvlData())) {
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

    private void checkIntersectsFireCarpet(FireModel fireModel) {
        if (fireModel.getHitbox().intersects(PlayerModel.getInstance().getHitbox())) {
            PlayerModel.getInstance().setPlayerSpeed(0.35f * SCALE);
            playerInTheFireCarpet = true;
        }
    }


    private boolean intersectBubbleFromBelow(){
//        return getPlayerHitbox().getY() <= bubble.getHitbox().getMaxY() + (int) (1 * SCALE)
//                && getPlayerHitbox().getY() >= bubble.getHitbox().getMaxY() - (int)(1 * SCALE)
//                && getPlayerHitbox().getX() <= bubble.getHitbox().getMaxX() + (int) (2 * SCALE)
//                && getPlayerHitbox().getX() >= bubble.getHitbox().getX() - (int) (2 * SCALE)
        return !PlayerModel.getInstance().getJump() && PlayerModel.getInstance().getAirSpeed() != 0;
    }

    private void checkIntersects(BubbleModel bubble) {
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
    }

    private void checkCollisionWithOtherBubbles(BobBubbleModel bubble1) {
        for( BobBubbleModel bubble2 : bobBubbles ){
            if(bubble1.getHitbox().intersects(bubble2.getHitbox()) && bubble1.isActive() && bubble2.isActive()){
                Random random = new Random();
                bobBubblesRandomMovements(random, bubble1, bubble2);
            }
        }
    }

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

    private void createFireCarpet(){
        ArrayList<FireModel> tempArray = new ArrayList<>();
        for (FireModel fireModel: fires) {
            if (fireModel.isCreatingCarpet()) {
                fireModel.setCreatingCarpet(false);
                int i = 1;
                float currentX = fireModel.getX();
                while (!CanMoveHere( currentX + fireModel.getWidth(), fireModel.getY() + fireModel.getHeight(), fireModel.getWidth(), fireModel.getHeight(), getLvlData())
                && CanMoveHere(currentX + fireModel.getWidth(), fireModel.getY(), fireModel.getWidth(), fireModel.getHeight(), getLvlData())) {
                    currentX = fireModel.getX() + fireModel.getWidth() * i;
                    tempArray.add(new FireModel(currentX, fireModel.getY(),true));
                    i++;
                }
            }
        }
        fires.addAll(tempArray);
    }

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

    public void resetBubbles() {
        bobBubbles.clear();
        bubbles.clear();
        fires.clear();
        lightnings.clear();
    }

    public void addBobBubbles(BobBubbleModel bobBubble) {
        bobBubbles.add(bobBubble);
        //bubbles.add(bobBubble);
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
