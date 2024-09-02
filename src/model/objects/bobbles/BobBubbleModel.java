package model.objects.bobbles;

import model.utilz.UtilityMethods;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.SpecialBubbles.BOB_BUBBLE;
import static model.utilz.Gravity.*;

public class BobBubbleModel extends BubbleModel {

    private int projectileTravelTimes = 0;

    public BobBubbleModel(float x, float y, int width, int height, int bubbleDirection) {
        super(x, y, width, height, BOB_BUBBLE);
        this.bubbleDirection = bubbleDirection;
        this.bubbleSpeed = 1.5F * SCALE;
        this.collision = false;
    }

    public void update() {
        updatePos();
    }

    //@Override
    protected void updatePos() {
        if(projectileTravelTimes <= 60) {
            firstShotMovement();
        }
        else {
            collision = true;
            afterShotMovement();
        }
    }

    private void firstShotMovement() {
        if (bubbleDirection == RIGHT) {
            if (CanMoveHere(hitbox.x + bubbleSpeed, y, width, height, UtilityMethods.getLvlData()))
                hitbox.x += bubbleSpeed;
            else {
                hitbox.x = GetEntityXPosNextToWall(hitbox, bubbleSpeed);
            }
        } else {
            if (CanMoveHere(hitbox.x - bubbleSpeed, y, width, height, UtilityMethods.getLvlData()))
                hitbox.x -= bubbleSpeed;
            else
                hitbox.x = GetEntityXPosNextToWall(hitbox, bubbleSpeed);
        }
        projectileTravelTimes++;
    }
}
