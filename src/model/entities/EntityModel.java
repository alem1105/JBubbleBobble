package model.entities;

import model.LevelManager;

import java.awt.geom.Rectangle2D;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Gravity.*;

public abstract class EntityModel {

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;

    // gravity
    protected float airSpeed = 0f;
    protected float gravity = 0.04f * SCALE;
    protected float fallSpeedAfterCollision = 0.5f * SCALE;
    protected boolean inAir = true;

    public EntityModel(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, width * SCALE, height * SCALE);
    }

    protected void isInAirCheck() {
        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getLvlData())) {
                inAir = true;
            }
        }
    }

    protected void fallingChecks(float xSpeed){
        // Stiamo cadendo
        // Bloccati dentro un muro
        if (!CanMoveHere(hitbox.x, hitbox.y, hitbox.width, hitbox.height,getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getLvlData())) {
            hitbox.y += airSpeed;
            airSpeed = 0.65f * SCALE;
        }
        // Caduta normale
        else if (CanMoveHere(hitbox.x, hitbox.y + airSpeed,
                hitbox.width, hitbox.height,
                getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getLvlData())) {
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
            } else {
                airSpeed = fallSpeedAfterCollision;
            }
            updateXPos(xSpeed);
        }
    }

    private void checkOutOfMap() {
        int currentTileY = (int) (hitbox.y / TILES_SIZE);
        if(currentTileY == TILES_IN_HEIGHT - 1)
            hitbox.y = -TILES_SIZE;
    }

    protected void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    protected abstract void updateXPos(float xSpeed);

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public LevelManager getLevelManager() {
        return LevelManager.getInstance();
    }

    public boolean isInAir() {
        return inAir;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }
}


