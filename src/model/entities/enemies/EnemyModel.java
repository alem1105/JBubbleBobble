package model.entities.enemies;

import model.LevelManager;
import model.entities.EntityModel;
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

    protected int enemyType;
    protected int enemyState = RUNNING;

    // movimenti e gravità
    //protected float fallSpeed;
    protected int walkDir = RIGHT;
    protected float walkSpeed;
    protected int dirChangedTimes = 0;
    protected boolean goUp = false;
    protected int targetY;

    public EnemyModel(float x, float y, int width, int height) {
        super(x, y - 1, width, height);
    }

    protected void checkUpSolid(int[][] lvlData) { // controlla se sopra il nemico si hanno almeno tre tile su cui saltare
        for (int y = lvlData.length - 1; y > 1; y--) { // Scorro al contrario per trovare la più vicina
            if (y < hitbox.y / TILES_SIZE) {
                for (int x = 0; x < lvlData[y].length; x++) {
                    if (x == (int) (hitbox.x / TILES_SIZE)) {
                        // Siamo nella stessa x del nemico, controlliamo se la y è percorribile
                        if (IsTileSolid(x, y, lvlData) && IsTileSolid(x - 1, y, lvlData) && IsTileSolid(x + 1, y, lvlData)) {
                            goUp = true;
                            targetY = y;
                            return;
                        }
                    }
                }
            }
        }
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

    public int[][] getLvlData() {
        return getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getLvlData();
    }
}
