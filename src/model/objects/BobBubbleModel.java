package model.objects;

import model.entities.PlayerModel;
import model.utilz.UtilityMethods;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

public class BobBubbleModel extends BubbleModel {
    private int projectileTravelTimes = 0;
    private int targetTileY = 2;
    private boolean floatingArea = false;
    private boolean touchedRoof = false;
    private float bubbleSpeedAfterShot = 0.3F * SCALE;

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
        if(!floatingArea) {
            if(CanMoveHere(hitbox.x, hitbox.y - bubbleSpeedAfterShot, hitbox.width, hitbox.height, getLvlData()))
                hitbox.y -= bubbleSpeedAfterShot;
            else {
                if(touchedRoof) {
                    if(!(bubbleTileYInBubbleArea())) {
                        checkFloatingAreaPos();
                    }
                }
                else {
                    hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, bubbleSpeedAfterShot);
                    touchedRoof = true;
                }
            }
        }else {
            System.out.println("sto nella floating area");
        }
    }

    private void checkFloatingAreaPos() {
    }

    private boolean bubbleTileYInBubbleArea() {
        //if()
        return false;
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

    private void changeDir() {
        if(bubbleDirection == RIGHT)
            bubbleDirection = LEFT;
        else
            bubbleDirection = RIGHT;
        bubbleSpeed = -bubbleSpeed;
    }

    private int getBubbleTileY() {
        return (int) (hitbox.y / TILES_SIZE);
    }

    private int getBubbleTileX() {
        return (int) (hitbox.x / TILES_SIZE);
    }

}
