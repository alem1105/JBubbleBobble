package model.objects.bobbles;

import model.objects.CustomObjectModel;

import static model.utilz.UtilityMethods.*;
import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Gravity.CanMoveHere;
import static model.utilz.Gravity.GetEntityXPosNextToWall;

public class BubbleModel extends CustomObjectModel {

    protected float bubbleSpeed = 1.5F * SCALE;
    protected int lifeTimer = 0; // tempo della vita dopo di che esplode
    protected boolean timeOut;
    protected int lifeTime = 3000;

    protected int bubbleDirection;

    public BubbleModel(float x, float y, int width, int height) {
        super(x, y, width, height);
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
        updatePos();
    }

    protected void updatePos() {
        if (bubbleDirection == RIGHT) {
            bubbleSpeed = -Math.abs(bubbleSpeed);
            checkEnemyMovement();
        } else {
            bubbleSpeed = Math.abs(bubbleSpeed);
            checkEnemyMovement();
        }
    }

    private void checkEnemyMovement() {
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

}

