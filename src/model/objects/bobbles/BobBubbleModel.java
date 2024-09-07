package model.objects.bobbles;

import model.utilz.UtilityMethods;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.SpecialBubbles.BOB_BUBBLE;
import static model.utilz.Gravity.*;

public class BobBubbleModel extends BubbleModel {

    private int projectileTravelTimes = 0;
    private int projectTravelDuration = 60;

    private boolean alreadyShotLighting;

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
        if(projectileTravelTimes <= projectTravelDuration) {
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

    public void resetModifiedCandyValues() {
        projectileTravelTimes = 240;
        bubbleSpeed = 1.5f * SCALE;
        bubbleSpeedAfterShot = 0.3f * SCALE;
    }

    public void setProjectileTravelTimes(int projectileTravelTimes) {
        this.projectileTravelTimes = projectileTravelTimes;
    }

    public boolean isAlreadyShotLighting() {
        return alreadyShotLighting;
    }

    public void setAlreadyShotLighting(boolean alreadyShotLighting) {
        this.alreadyShotLighting = alreadyShotLighting;
    }
}
