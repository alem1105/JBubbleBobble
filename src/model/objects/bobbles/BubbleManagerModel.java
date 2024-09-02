package model.objects.bobbles;

import model.entities.PlayerModel;
import model.entities.enemies.EnemyManagerModel;
import model.entities.enemies.EnemyModel;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import static model.utilz.Constants.GameConstants.*;

public class BubbleManagerModel {

    private static BubbleManagerModel instance;

    private ArrayList<BobBubbleModel> bobBubbles;
    private ArrayList<BubbleModel> bubbles;

    private Random rand;
    int spawnBubbleTick = 0;
    int spawnBubbleDuration = 360;

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
    }

    public void update(){
        updateRandomBubbleSpawn();

        for( BobBubbleModel bubble : bobBubbles ){
            if (bubble.isActive()) {
                bubble.update();
                checkPlayerHit(bubble);
                checkEnemyHasBeenHit(bubble);
                checkCollisionWithOtherBubbles(bubble);
            }
        }
        for (BubbleModel bubble : bubbles){
            if (bubble.isActive()) {
                //checkPlayerHit(bubble);
                bubble.update();
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
        if (getPlayerHitbox().getY() <= bubble.getHitbox().getMaxY() + (int) (1 * SCALE) && getPlayerHitbox().getY() >= bubble.getHitbox().getMaxY() - (int)(1 * SCALE)
                && getPlayerHitbox().getX() <= bubble.getHitbox().getMaxX() + (int) (2 * SCALE) && getPlayerHitbox().getX() >= bubble.getHitbox().getX() - (int) (2 * SCALE)
                && PlayerModel.getInstance().getAirSpeed() < 0) {
            if (bubble.getHitbox().intersects(PlayerModel.getInstance().getHitbox()) && bubble.isCollision()) {

                bubble.setActive(false);
                bubble.setTimeout(true);

                // Con queste tutte quelle che si toccano
                checkIntersects(bubble);

            }
        }
    }

    private void checkIntersects(BubbleModel bubble) {
        for (BobBubbleModel bob : bobBubbles) {
            if (bob.isActive()) {
                if (bob.getHitbox().intersects(bubble.getHitbox())) {
                    bob.setActive(false);
                    bubble.setTimeout(true);
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
}
