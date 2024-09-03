package model.objects.bobbles;

import model.objects.CustomObjectModel;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import static model.utilz.Constants.SpecialBubbles.EXTEND_BUBBLE;
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
    protected char extendChar;

    protected float bubbleSpeedAfterShot = 0.3F * SCALE;
    protected int pathDuration = 240;
    protected int pathTick = 0;

    protected int bubbleDirection;

    protected boolean collision = true;
    protected boolean stuck = false;
    protected Random random;

    // waterfall variables
    private ArrayList<WaterModel> waterfall;
    private boolean hasSpawnedAllWaters = false;
    private float yWhenPopped;
    private float xWhenPopped;

    public BubbleModel(float x, float y, int width, int height, int bubbleType) {
        super(x, y, width, height);
        this.bubbleType = bubbleType;
        random = new Random();
        extendChar = (bubbleType == EXTEND_BUBBLE) ? "Extend".charAt(random.nextInt(6)) : 0;
        checkDirection();
        waterfall = new ArrayList<>();
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

    public void spawnWaterFall() {
        if(!hasSpawnedAllWaters && !active) {
            int direction = (xWhenPopped < (float) GAME_WIDTH / 2) ? RIGHT : LEFT;

            if(waterfall.size() >= 10) {
                hasSpawnedAllWaters = true;
            }

            if(waterfall.isEmpty())
                waterfall.add(new WaterModel(xWhenPopped, yWhenPopped, (int) (8 * SCALE), (int) (8 * SCALE), direction));
            else {
                Rectangle2D.Float lastWaterHitbox = waterfall.getLast().getHitbox();
                if(hasWaterMovedFromStartPoint(lastWaterHitbox)) {
                    waterfall.add(new WaterModel(xWhenPopped, yWhenPopped, (int) (8 * SCALE), (int) (8 * SCALE), direction));
                }
            }
        }
    }

    private boolean hasWaterMovedFromStartPoint(Rectangle2D.Float lastWaterHitbox) {
        return lastWaterHitbox.y >= yWhenPopped + lastWaterHitbox.height
                || lastWaterHitbox.x >= xWhenPopped + lastWaterHitbox.width
                || lastWaterHitbox.x <= xWhenPopped - lastWaterHitbox.width;
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

    public void setBubbleSpeed(float bubbleSpeed) {
        this.bubbleSpeed = bubbleSpeed;
    }

    public void setBubbleSpeedAfterShot(float bubbleSpeedAfterShot) {
        this.bubbleSpeedAfterShot = bubbleSpeedAfterShot;
    }

    public ArrayList<WaterModel> getWaterfall() {
        return waterfall;
    }

    public void setyWhenPopped(float yWhenPopped) {
        this.yWhenPopped = yWhenPopped;
    }

    public void setxWhenPopped(float xWhenPopped) {
        this.xWhenPopped = xWhenPopped;
    }

    public char getExtendChar() {
        return extendChar;
    }
}

