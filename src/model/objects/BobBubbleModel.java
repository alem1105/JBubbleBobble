package model.objects;

import model.entities.PlayerModel;
import model.utilz.UtilityMethods;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.GameConstants.TILES_IN_WIDTH;
import static model.utilz.Constants.GameConstants.TILES_SIZE;
import static model.utilz.Gravity.*;

public class BobBubbleModel extends BubbleModel {
    private int projectileTravelTimes = 0;
    private int targetTileY = 2;

    public BobBubbleModel(float x, float y, int width, int height, int bubbleDirection) {
        super(x, y, width, height, bubbleDirection);
    }

    public void update() {
        updatePos();
    }

    private void updatePos() {
        if(projectileTravelTimes <= 60) {
            firstShotMovement();
        }
        else
            afterShotMovement();
    }

    private void afterShotMovement() {

    }

    private void firstShotMovement() {
        if (bubbleDirection == RIGHT) {
            if (CanMoveHere(hitbox.x + bubbleSpeed, y, width, height, UtilityMethods.getLvlData()))
                hitbox.x += bubbleSpeed;
            else {
                hitbox.x = GetEntityXPosNextToWall(hitbox, bubbleSpeed);
                System.out.println("blocco");
            }
        } else {
            if (CanMoveHere(hitbox.x - bubbleSpeed, y, width, height, UtilityMethods.getLvlData()))
                hitbox.x -= bubbleSpeed;
            else
                hitbox.x = GetEntityXPosNextToWall(hitbox, bubbleSpeed);
        }
        projectileTravelTimes++;
    }

    private void changeDir() {
        if(bubbleDirection == RIGHT)
            bubbleDirection = LEFT;
        else
            bubbleDirection = RIGHT;
        bubbleSpeed = -bubbleSpeed;
    }
}
