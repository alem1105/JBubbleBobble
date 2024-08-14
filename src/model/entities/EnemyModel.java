package model.entities;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.Directions.*;

public abstract class EnemyModel extends EntityModel {
    protected int aniIndex, enemyState, enemyType;
    protected int aniTick, aniSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir;
    protected float fallSpeed;
    protected float gravity = 0.04f * SCALE;
    protected float walkSpeed = 0.35f * SCALE;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = TILES_SIZE;
    protected int maxHealth;
    protected int currentHealth;
    protected boolean active = true;
    protected boolean attackChecked;

    public EnemyModel(float x, float y, int width, int height) {
        super(x, y, width, height);
    }
}
