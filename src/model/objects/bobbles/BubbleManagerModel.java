package model.objects.bobbles;

import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;
import model.entities.enemies.EnemyModel;
import model.gamestate.UserStateModel;
import model.objects.CustomObjectModel;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.SpecialBubbles.*;
import static model.utilz.Gravity.CanMoveHere;
import static model.utilz.UtilityMethods.getLvlData;

public class BubbleManagerModel {

    private static BubbleManagerModel instance;

    private ArrayList<BobBubbleModel> bobBubbles;
    private ArrayList<BubbleModel> bubbles;
    private ArrayList<WaterModel> waters;
    private ArrayList<FireModel> fires;
    private ArrayList<LightningModel> lightnings;

    private Random rand;
    int spawnBubbleTick = 0;
    int spawnBubbleDuration = 360;

    // Power Up
    private int scoreForPop = 0;
    private HashMap<Character, Boolean> extend;

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
        waters = new ArrayList<>();
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
        for (WaterModel waterModel : waters) {
            if(waterModel.isActive())
                waterModel.update();
        }
        for (FireModel fireModel : fires){
            if (fireModel.isActive()) {
                fireModel.update();
                if (fireModel.isPartOfTheCarpet())
                    checkObjectHitEnemy(fireModel);
            }
        }
        for (LightningModel lightningModel : lightnings) {
            if (lightningModel.isActive()){
                lightningModel.update();
                checkObjectHitEnemy(lightningModel);
            }
        }
    }

    private void checkObjectHitEnemy(CustomObjectModel objectModel) {
        for(EnemyModel enemy : EnemyManagerModel.getInstance().getEnemies()) {
            if(objectModel.getHitbox().intersects(enemy.getHitbox())) {
                if (enemy.isActive()) {
                    //enemy.setEnemyState(DEATH);
                    enemy.setActive(false);
                }
            }
        }
    }

    private void updateBubbles() {
        for (BubbleModel bubble : bubbles){
            if (bubble.isActive()) {
                checkPlayerHit(bubble);
                bubble.update();
            } else {
                if(bubble.getBubbleType() == WATER_BUBBLE) {
                    bubble.spawnWaterFall();
                    for(WaterModel water : bubble.getWaterfall())
                        water.update();
                }
            }
        }
    }

    private void updateBobBubble() {
        for( BobBubbleModel bubble : bobBubbles ){
            if (bubble.isActive()) {
                bubble.update();
                checkPlayerHit(bubble);
                checkEnemyHasBeenHit(bubble);
                checkCollisionWithOtherBubbles(bubble);
            }
        }
    }

    private void updateRandomBubbleSpawn() {
        spawnBubbleTick++;
        if (spawnBubbleTick >= spawnBubbleDuration) {
            spawnBubbleTick = 0;
            int bubbleType = rand.nextInt(3); //2;

            int max = (TILES_IN_WIDTH - 1) * TILES_SIZE;
            int min = TILES_SIZE;

            int randomX = rand.nextInt(max - min + 1) + min;
            int y = GAME_HEIGHT;
            bubbles.add(new BubbleModel(randomX, y,(int) (14 * SCALE), (int) (16 * SCALE), bubbleType));
        }
    }

    private void checkPlayerHit(BubbleModel bubble) {
        if (intersectBubbleFromBelow()) {

            if (bubble.getHitbox().intersects(PlayerModel.getInstance().getHitbox()) && bubble.isCollision()) {

                if (CanMoveHere(bubble.getHitbox().x, bubble.getHitbox().y, bubble.getHitbox().width, bubble.getHitbox().height, getLvlData())) {
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
                if (enemy.isActive()) {
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

    public ArrayList<WaterModel> getWaters() {
        return waters;
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
