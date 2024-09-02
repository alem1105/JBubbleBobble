package model.objects.bobbles;

import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;
import model.entities.enemies.EnemyModel;
import model.gamestate.UserStateModel;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.SpecialBubbles.*;

public class BubbleManagerModel {

    private static BubbleManagerModel instance;

    private ArrayList<BobBubbleModel> bobBubbles;
    private ArrayList<BubbleModel> bubbles;
    private ArrayList<WaterModel> waters;

    private Random rand;
    int spawnBubbleTick = 0;
    int spawnBubbleDuration = 360;

    // Power Up
    private int scoreForPop = 0;

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
    }

    public void update(){
        updateRandomBubbleSpawn();
        updateBubbles();
        updateBobBubble();
        updateExplodedBubbles();
    }

    private void updateExplodedBubbles() {
        for (WaterModel waterModel : waters) {
            if(waterModel.isActive())
                waterModel.update();
        }
    }

    private void updateBubbles() {
        for (BubbleModel bubble : bubbles){
            if (bubble.isActive()) {
                checkPlayerHit(bubble);
                bubble.update();
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
            int bubbleType = 0;//rand.nextInt(2);

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

                UserStateModel.getInstance().getCurrentUserModel().incrementTempScore(scoreForPop);
                PlayerModel.getInstance().incrementPoppedBubbles();

                bubble.setActive(false);
                bubble.setTimeout(true);

                switch (bubble.getBubbleType()) {
                    case WATER_BUBBLE -> {
                        spawnWaterWaterfall(bubble);
                        PlayerModel.getInstance().incrementPoppedWaterBubbles();
                    }
                    case LIGHTNING_BUBBLE -> PlayerModel.getInstance().incrementPoppedLightingBubbles();
                    case FIRE_BUBBLE -> PlayerModel.getInstance().incrementPoppedFireBubbles();
                }

                checkIntersects(bubble);

            }
        }
    }

    private void spawnWaterWaterfall(BubbleModel bubble) {
        int direction = (bubble.getHitbox().x < (float) GAME_WIDTH / 2) ? RIGHT : LEFT;
        for (int i = 0; i < 10; i++) {
            waters.add(new WaterModel(bubble.getHitbox().x, bubble.getHitbox().y + (i * (int) (7 * SCALE)), (int) (8 * SCALE), (int) (8 * SCALE), direction));
        }
    }

    private boolean intersectBubbleFromBelow(){
//        return getPlayerHitbox().getY() <= bubble.getHitbox().getMaxY() + (int) (1 * SCALE)
//                && getPlayerHitbox().getY() >= bubble.getHitbox().getMaxY() - (int)(1 * SCALE)
//                && getPlayerHitbox().getX() <= bubble.getHitbox().getMaxX() + (int) (2 * SCALE)
//                && getPlayerHitbox().getX() >= bubble.getHitbox().getX() - (int) (2 * SCALE)
               return !PlayerModel.getInstance().getJump() && PlayerModel.getInstance().getAirSpeed() < 0;
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
}
