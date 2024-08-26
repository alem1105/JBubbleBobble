package model.entities.enemies;

import model.LevelManager;
import model.entities.EntityModel;

import java.awt.image.BufferedImage;
import java.util.Random;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.Enemies.*;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.GameConstants.TILES_SIZE;
import static model.utilz.Gravity.*;

public abstract class EnemyModel extends EntityModel {
    protected boolean active = true;
    protected boolean inBubble = false;
    protected boolean angry = false;
    protected boolean resetAniTick = false;

    protected int inBubbleTime;

    protected int enemyType;
    protected int enemyState = RUNNING;

    // movimenti e gravità
    //protected float fallSpeed;
    protected int walkDir = RIGHT;
    protected float walkSpeed;
    protected boolean goingUp = false;
    protected int targetYTile;
    private int upTick;

    public EnemyModel(float x, float y, int width, int height) {
        super(x, y - 1, width, height);
    }

    public void updateEnemyState() {
        int startAni = enemyState;

        if (inBubble) {
            inBubbleTime++;
            if (inBubbleTime >= 600) {
                angry = true;
                inBubble = false;
                inBubbleTime = 0;
            }
            if (angry)
                enemyState = CAPTURED_ANGRY;
            else
                enemyState = CAPTURED;
        }
        else if (angry) {
            enemyState = RUNNING_ANGRY;
            walkSpeed = 0.80f * SCALE;
        }

        if (startAni != enemyState)
            resetAniTick = true;
        else
            resetAniTick = false;
    }

    protected boolean checkUpSolid(int[][] lvlData) { // controlla se sopra il nemico si hanno almeno tre tile su cui saltare
        int currentTileY = (int) (hitbox.y / TILES_SIZE);
        int currentTileX = (int) (hitbox.x / TILES_SIZE);
        if(checkThreeYTilesSolid(currentTileY - 2, currentTileX, lvlData)) {
            targetYTile = currentTileY - 4;
            return true;
        } else if(checkThreeYTilesSolid(currentTileY - 1, currentTileX, lvlData)) {
            targetYTile = currentTileY - 3;
            return true;
        }else {
            return false;
        }
    }

    protected boolean checkThreeYTilesSolid(int yTile, int xTile, int[][] lvldata) {
        if (xTile - 2 >= 0 && yTile -2 >= 0 && xTile + 1 >= 0)
            return IsTileSolid(xTile - 2, yTile, lvldata) && IsTileSolid(xTile, yTile, lvldata) && IsTileSolid(xTile + 1, yTile, lvldata);
        return false;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public int getEnemyState() {
        return enemyState;
    }

    public void setEnemyState(int enemyState) {
        this.enemyState = enemyState;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setInBubble(boolean inBubble) {this.inBubble = inBubble;}

    public boolean isAngry() {
        return angry;
    }

    public int getWalkDir() {
        return walkDir;
    }

    public boolean isInBubble() {
        return inBubble;
    }

    public boolean isActive() {
        return active;
    }
}
