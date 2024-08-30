package model.objects.bobbles;

import model.objects.CustomObjectModel;

import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.*;
import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;

public class BubbleModel extends CustomObjectModel {

    protected float bubbleSpeed = 0.2F * SCALE;
    protected int lifeTimer = 0; // tempo della vita dopo di che esplode
    protected boolean timeOut;
    protected int lifeTime = 3000;
    protected int bubbleType;

    protected float bubbleSpeedAfterShot = 0.3F * SCALE;
    protected int pathDuration = 240;
    protected int pathTick = 0;

    protected int bubbleDirection;

    protected boolean collision = false;
    protected boolean stuck = false;

    public BubbleModel(float x, float y, int width, int height, int bubbleType) {
        super(x, y, width, height);
        this.bubbleType = bubbleType;
        checkDirection();
    }

    private void checkDirection() {
        if ((int) hitbox.x / TILES_SIZE < TILES_IN_WIDTH / 2) {
            bubbleDirection = RIGHT;
        } else {
            bubbleDirection = LEFT;
        }
    }

    public void update(){
        checkLifeTimer();
        afterShotMovement();
    }

//    protected void updatePos() {
//        if (bubbleDirection == RIGHT) {
//            bubbleSpeed = -Math.abs(bubbleSpeed);
//            checkBubbleMovement();
//        } else {
//            bubbleSpeed = Math.abs(bubbleSpeed);
//            checkBubbleMovement();
//        }
//    }

    protected void afterShotMovement() {
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

    protected void checkBubbleDirection() {
        if(getBubbleTileX() < TILES_IN_WIDTH / 2 - 2) {
            hitbox.x += bubbleSpeedAfterShot;
        }else {
            hitbox.x -= bubbleSpeedAfterShot;
        }
    }

    protected void startFloating() {
        if(pathTick <= pathDuration / 2 ) {
            hitbox.y -= bubbleSpeedAfterShot;
        }else if(pathTick > pathDuration / 2 && pathTick <= pathDuration) {
            hitbox.y += bubbleSpeedAfterShot;
        }else {
            pathTick = 0;
        }
        pathTick++;
    }

    protected void checkBubbleMovement() {
        if (canBubbleMoveHere()) {
            hitbox.x += bubbleSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, bubbleSpeed);
        }
    }

    private boolean canBubbleMoveHere() {
        return CanMoveHere(hitbox.x, hitbox.y, hitbox.width, hitbox.height, getLvlData());
    }

    private void checkLifeTimer() {
        if (active){
            lifeTimer++;
            if (lifeTimer >= lifeTime){ //dopo 25 secondi esplode
                active = false;
                timeOut = true;
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isTimeOut() {
        return timeOut;
    }

    protected int getBubbleTileY() {
        return (int) (hitbox.y / TILES_SIZE);
    }

    private int getBubbleTileX() {
        return (int) (hitbox.x / TILES_SIZE);
    }

    public boolean isCollision() {
        return collision;
    }

    public int getBubbleType() {
        return bubbleType;
    }

    public void setTimeout(boolean timeOut) {
        this.timeOut = timeOut;
    }
}

