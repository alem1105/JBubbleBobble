package model.objects.bobbles;

import model.objects.CustomObjectModel;
import model.utilz.Fallable;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.TILES_SIZE;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

public class WaterModel extends CustomObjectModel implements Fallable {

    private boolean inAir = true;
    private float airSpeed;

    public WaterModel(float x, float y, int width, int height){
        super(x, y, width, height);
    }

    public void update() {

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
            updateXPos(xSpeed);
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

    }

}
