package model.objects;

import model.entities.PlayerModel;
import model.utilz.UtilityMethods;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.GameConstants.TILES_IN_WIDTH;
import static model.utilz.Constants.GameConstants.TILES_SIZE;
import static model.utilz.Gravity.*;

public class BobBubbleModel extends BubbleModel {
    private boolean justShot = true;
    private int targetTileY = 2;

    public BobBubbleModel(float x, float y, int width, int height, int bubbleDirection) {
        super(x, y, width, height, bubbleDirection);
    }

    public void update() {
        updatePos();
    }

    private void updatePos() {
//        if(justShot)
//            firstShotMovement();
//        else
//            afterShotMovement();
        if (bubbleDirection == RIGHT) {
            hitbox.x+=bubbleSpeed;
        }
        else {
            hitbox.x-=bubbleSpeed;
        }
    }

    private void afterShotMovement() {

    }

    private void firstShotMovement() {
        for(int i = 0; i < (TILES_SIZE * 3) / bubbleSpeed; i++) {
            if(bubbleDirection == RIGHT) {
                hitbox.x += bubbleSpeed;
            } else if(bubbleDirection == LEFT) {
                hitbox.x -= bubbleSpeed;
            }
            /*
            if (bubbleDirection == RIGHT) {
                if (CanMoveHere(x + bubbleSpeed, y, width, height, UtilityMethods.getLvlData()))
                    hitbox.x += bubbleSpeed;
                else
                    hitbox.x = GetEntityXPosNextToWall(hitbox, bubbleSpeed);
            }else {
                if (CanMoveHere(x - bubbleSpeed, y, width, height, UtilityMethods.getLvlData()))
                    hitbox.x -= bubbleSpeed;
                else
                    hitbox.x = GetEntityXPosNextToWall(hitbox, bubbleSpeed);
            }
            System.out.println(hitbox.x);
             */
        }
        justShot = false;
    }

    private void changeDir() {
        if(bubbleDirection == RIGHT)
            bubbleDirection = LEFT;
        else
            bubbleDirection = RIGHT;
        bubbleSpeed = -bubbleSpeed;
    }
}
