package model.entities.enemies;

import model.entities.EntityModel;
import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.Enemies.*;

import static model.utilz.Constants.GameConstants.SCALE;

public abstract class EnemyModel extends EntityModel {
    protected boolean active = true;
    protected boolean inBubble = false;
    protected boolean angry = false;
    protected boolean resetAniTick = false;

    protected int enemyType;
    protected int enemyState = RUNNING;

    // movimenti e gravità
    protected boolean inAir;
    protected float gravity = 0.04f * SCALE;
    protected float fallSpeed;
    protected int walkDir = RIGHT;
    protected float walkSpeed = 0.35f * SCALE;

    public EnemyModel(float x, float y, int width, int height) {
        super(x, y, width, height);
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
}
