package model.objects;

import model.entities.enemies.EnemyManagerModel;
import model.entities.enemies.EnemyModel;

import java.util.ArrayList;
import java.util.Random;

public class BubbleManagerModel {

    private static BubbleManagerModel instance;

    private ArrayList<BobBubbleModel> bobBubbles;

    public static BubbleManagerModel getInstance() {
        if (instance == null) {
            instance = new BubbleManagerModel();
        }
        return instance;
    }

    private BubbleManagerModel() {
        bobBubbles = new ArrayList<>();
    }

    public void update(){
        for( BobBubbleModel bubble : bobBubbles ){
            if (bubble.isActive()) {
                bubble.update();
                checkEnemyHasBeenHit(bubble);
                checkCollisionWithOtherBubbles(bubble);
            }
        }
    }

    private void checkCollisionWithOtherBubbles(BobBubbleModel bubble1) {
        for( BobBubbleModel bubble2 : bobBubbles ){
            if(bubble1.getHitbox().intersects(bubble2.getHitbox())){
                Random random = new Random();
                bobBubblesRandomMovements(random, bubble1, bubble2);
            }
        }
    }

    private void checkEnemyHasBeenHit(BubbleModel bubble) {
        for(EnemyModel enemy : EnemyManagerModel.getInstance().getEnemies()) {
            if(bubble.getHitbox().intersects(enemy.getHitbox())) {
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
        bubble1.getHitbox().y += randomNumberY;
        bubble2.getHitbox().x -= randomNumberX;
        bubble2.getHitbox().y -= randomNumberY;
    }

    public void addBobBubbles(BobBubbleModel bobBubble) {
        bobBubbles.add(bobBubble);
    }

    public ArrayList<BobBubbleModel> getBobBubbles() {
        return bobBubbles;
    }
}
