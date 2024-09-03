package model.entities;

import model.LevelManagerModel;
import model.utilz.Fallable;

import java.awt.geom.Rectangle2D;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

public abstract class EntityModel implements Fallable {

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;

    // gravity
    protected float airSpeed = 0f;
    protected float gravity = 0.04f * SCALE;
    protected float fallSpeedAfterCollision = 0.5f * SCALE;
    protected boolean inAir = true;
    protected float fallingSpeed = 0.65f * SCALE;

    public EntityModel(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, width * SCALE, height * SCALE);
    }

    public void isInAirCheck() {
        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, getLvlData())) {
                inAir = true;
            }
        }
    }

    public void fallingChecks(float xSpeed){
        // Stiamo cadendo
        // Bloccati dentro un muro
        if (!CanMoveHere(hitbox.x, hitbox.y, hitbox.width, hitbox.height,getLvlData())) {
            hitbox.y += airSpeed;
            airSpeed = fallingSpeed;
        }
        // Caduta normale
        else if (CanMoveHere(hitbox.x, hitbox.y + airSpeed,
                hitbox.width, hitbox.height,
                getLvlData())) {
            airSpeed = fallingSpeed; // Discesa lenta
            hitbox.y += airSpeed;
            updateXPos(xSpeed);
            checkOutOfMap();
        }
        // Finita Caduta
        else {
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            if (airSpeed > 0) {
                resetInAir();
            } else {
                airSpeed = fallSpeedAfterCollision;
            }
            updateXPos(xSpeed);
        }
    }

    public void checkOutOfMap() {
        int currentTileY = (int) (hitbox.y / TILES_SIZE);
        if(currentTileY == TILES_IN_HEIGHT - 1)
            hitbox.y = -TILES_SIZE;
    }

    public void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    public abstract void updateXPos(float xSpeed);

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public float getX() {
        return hitbox.x;
    }

    public float getY() {
        return hitbox.y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public LevelManagerModel getLevelManager() {
        return LevelManagerModel.getInstance();
    }

    public boolean isInAir() {
        return inAir;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public void setFallingSpeed(float fallingSpeed) {
        this.fallingSpeed = fallingSpeed;
    }
}


