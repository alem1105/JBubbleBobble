package model.objects.bobbles;

import model.utilz.UtilityMethods;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Gravity.*;

public class BobBubbleModel extends BubbleModel {
    private int projectileTravelTimes = 0;
    private int targetTileY = 2;
    private boolean floatingArea = false;
    private boolean stuck = false;
    private float bubbleSpeedAfterShot = 0.3F * SCALE;
    private boolean collision = false;

    private int pathDuration = 240;
    private int pathTick = 0;


    public BobBubbleModel(float x, float y, int width, int height, int bubbleDirection) {
        super(x, y, width, height);
    }

    public void update() {
        super.update();
        updatePos();
    }

    @Override
    protected void updatePos() {
        if(projectileTravelTimes <= 60) {
            firstShotMovement();
        }
        else {
            collision = true;
            afterShotMovement();
        }
    }

    private void afterShotMovement() {
        if(!stuck) {
            if(getBubbleTileY() > 2) {
                hitbox.y -= bubbleSpeedAfterShot;
            } else {
                if(isBubbleInXRange()) {
                    stuck = true;
                }else {
                    checkBubbleDirection();
                }
            }
        } else {
            startFloating();
        }
    }

    private boolean isBubbleInXRange() {
        return TILES_IN_WIDTH / 2 - 2 <= getBubbleTileX() && getBubbleTileX() <= TILES_IN_WIDTH / 2 + 1;
    }

    private void checkBubbleDirection() {
        if(getBubbleTileX() < TILES_IN_WIDTH / 2 - 2) {
            hitbox.x += bubbleSpeedAfterShot;
        }else {
            hitbox.x -= bubbleSpeedAfterShot;
        }
    }

    private void startFloating() {
        if(pathTick <= pathDuration / 2 ) {
            hitbox.y -= bubbleSpeedAfterShot;
        }else if(pathTick > pathDuration / 2 && pathTick <= pathDuration) {
            hitbox.y += bubbleSpeedAfterShot;
        }else {
            pathTick = 0;
        }
        pathTick++;
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

    private int getBubbleTileY() {
        return (int) (hitbox.y / TILES_SIZE);
    }

    private int getBubbleTileX() {
        return (int) (hitbox.x / TILES_SIZE);
    }

    public boolean isCollision() {
        return collision;
    }
}
