package model.objects.bobbles;

import model.objects.CustomObjectModel;
import model.utilz.Fallable;

import java.util.ArrayList;

import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.TILES_SIZE;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

public class WaterModel extends CustomObjectModel implements Fallable {

    private boolean inAir = true;
    private float airSpeed;
    private float waterSpeed = 0.65f * SCALE;
    private int direction;
    private boolean specificTrappedPlayer = false;

    public WaterModel(float x, float y, int width, int height, int direction){
        super(x, y, width, height);
        this.direction = direction;
    }

    public void update() {
        updatePos();
    }

    private void updatePos() {
        isInAirCheck();

        if (inAir)
            fallingChecks(waterSpeed);
        else
            updateXPos(waterSpeed);
    }

    @Override
    public void isInAirCheck() {
        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, getLvlData())) {
                inAir = true;
            }
        }
    }

    @Override
    public void fallingChecks(float xSpeed) {
        // Caduta normale
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed,
                hitbox.width, hitbox.height,
                getLvlData())) {
            airSpeed = 0.65f * SCALE; // Discesa lenta
            hitbox.y += airSpeed;
            //updateXPos(xSpeed);
            checkOutOfMap();
        }
        // Finita Caduta
        else {
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            if (airSpeed > 0) {
                resetInAir();
            }
            updateXPos(xSpeed);
        }
    }

    @Override
    public void checkOutOfMap() {
        int currentTileY = (int) (hitbox.y / TILES_SIZE);
        if(currentTileY == TILES_IN_HEIGHT - 1)
            hitbox.y = -TILES_SIZE;
    }

    @Override
    public void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    @Override
    public void updateXPos(float xSpeed) {
        xSpeed = (direction == RIGHT) ? xSpeed : -xSpeed;

        if(canWaterMoveHere(hitbox.x + xSpeed))
            hitbox.x += xSpeed;
        else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
            direction = (direction == RIGHT) ? LEFT : RIGHT;
        }
    }

    private boolean canWaterMoveHere(float nextX) {
        return CanMoveHere(nextX, hitbox.y, hitbox.width, hitbox.height, getLvlData());
    }

    @Override
    public String toString() {
        return "WaterModel{" +
                "x=" + hitbox.x +
                ", y=" + hitbox.y +
                ", width=" + hitbox.width +
                ", height=" + hitbox.height +
                ", direction=" + direction +
                ", active=" + active +
                '}';
    }

    public boolean isInAir() {
        return inAir;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isSpecificTrappedPlayer() {
        return specificTrappedPlayer;
    }

    public void setSpecificTrappedPlayer(boolean trappedPlayer) {
        this.specificTrappedPlayer = trappedPlayer;
    }
}
