package model.objects.bobbles;

import model.objects.CustomObjectModel;
import model.utilz.Fallable;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.TILES_SIZE;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

public class FireModel extends CustomObjectModel implements Fallable {

    private boolean inAir = true;
    private float airSpeed = 0.65f * SCALE;

    private boolean partOfTheCarpet, creatingCarpet = false;

    private int carpetDuration = 300, carpetTick;

    public FireModel(float x, float y, boolean partOfTheCarpet) {
        super(x, y, (int) (8 * SCALE), (int) (12 * SCALE));
        this.partOfTheCarpet = partOfTheCarpet;
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
        // Bloccati dentro un muro
        if (!CanMoveHere(hitbox.x, hitbox.y, hitbox.width, hitbox.height,getLvlData())) {
            hitbox.y += airSpeed;
        }
        // Caduta normale
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed,
                hitbox.width, hitbox.height,
                getLvlData())) {
            hitbox.y += airSpeed;
            checkOutOfMap();
        }
        // Finita Caduta
        else {
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            resetInAir();
            partOfTheCarpet = true;
            creatingCarpet = true;
        }
    }

    @Override
    public void checkOutOfMap() {
        // serve per quando va fuori dalla schermata
        int currentTileY = (int) (hitbox.y / TILES_SIZE);
        if(currentTileY == TILES_IN_HEIGHT - 1)
            hitbox.y = -TILES_SIZE;
    }

    @Override
    public void resetInAir() {
        inAir = false;
    }

    @Override
    public void updateXPos(float xSpeed) {

    }

    @Override
    public void update() {
        if (inAir && !partOfTheCarpet)
            fallingChecks(airSpeed);
        else if (partOfTheCarpet)
            carpetTimer();
    }

    private void carpetTimer() {
        if (carpetTick <= carpetDuration)
            carpetTick ++;
        else
            active = false;
    }

    public boolean isPartOfTheCarpet() {
        return partOfTheCarpet;
    }

    public void setCreatingCarpet(boolean creatingCarpet) {
        this.creatingCarpet = creatingCarpet;
    }

    public boolean isCreatingCarpet() {
        return creatingCarpet;
    }
}
