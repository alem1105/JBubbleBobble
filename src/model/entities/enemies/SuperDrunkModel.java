package model.entities.enemies;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.GameConstants.*;

public class SuperDrunkModel extends EnemyModel{
    private int lives = 60;
    private boolean hasBeenHit;

    public SuperDrunkModel(float x, float y, int width, int height) {
        super(x, y, width, height);
        initHitbox(width, height);
        this.walkSpeed = 0.55f * SCALE;
        walkDir = UP_RIGHT;
    }

    protected void updatePos() {
        switch (walkDir) {
            case UP_RIGHT -> {
                if (canSuperDrunkMoveOnThisX(hitbox.x + walkSpeed))
                    if (canSuperDrunkMoveOnThisY(hitbox.y - walkSpeed))
                        move(walkSpeed, -walkSpeed);
                    else
                        walkDir = DOWN_RIGHT;
                else
                    walkDir = UP_LEFT;
            }
            case UP_LEFT -> {
                if (canSuperDrunkMoveOnThisX(hitbox.x - walkSpeed))
                    if (canSuperDrunkMoveOnThisY(hitbox.y - walkSpeed))
                        move(-walkSpeed, -walkSpeed);
                    else
                        walkDir = DOWN_LEFT;
                else
                    walkDir = UP_RIGHT;
            }
            case DOWN_RIGHT -> {
                if (canSuperDrunkMoveOnThisX(hitbox.x + walkSpeed))
                    if (canSuperDrunkMoveOnThisY(hitbox.y + walkSpeed))
                        move(walkSpeed, walkSpeed);
                    else
                        walkDir = UP_RIGHT;
                else
                    walkDir = DOWN_LEFT;
            }
            case DOWN_LEFT -> {
                if (canSuperDrunkMoveOnThisX(hitbox.x - walkSpeed))
                    if (canSuperDrunkMoveOnThisY(hitbox.y + walkSpeed))
                        move(-walkSpeed, walkSpeed);
                    else
                        walkDir = UP_LEFT;
                else
                    walkDir = DOWN_RIGHT;
            }
        }
    }

    private void move(float xMovement, float yMovement) {
        hitbox.x += xMovement;
        hitbox.y += yMovement;
    }

    private boolean canSuperDrunkMoveOnThisY(float nextY) {
        return !(nextY + hitbox.height >= GAME_HEIGHT - (2 * TILES_SIZE) || nextY <= TILES_SIZE);
    }

    private boolean canSuperDrunkMoveOnThisX(float nextX) {
        return !(nextX + hitbox.width  >= GAME_WIDTH - TILES_SIZE || nextX <= TILES_SIZE);
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public boolean hasBeenHit() {
        return hasBeenHit;
    }

    public void setHasBeenHit(boolean hasBeenHit) {
        this.hasBeenHit = hasBeenHit;
    }

    public void decreaseLives() {
        this.lives--;
    }
}
