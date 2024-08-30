package model.entities.enemies;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.GameConstants.TILES_SIZE;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

public class MonstaModel extends EnemyModel{

    private float xSpeed = (float) (0.55 * SCALE);
    private float ySpeed = (float) (0.55 * SCALE);

    public MonstaModel(float x, float y) {
        super(x, y-1, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE;
        initHitbox(14, 16);
        walkDir = UP_RIGHT;
    }

    public void update() {
        updatePos();
        updateEnemyState();
    }

    protected void updatePos() {
        switch (walkDir) {
            case UP_RIGHT -> {
                if (canMonstaMoveOnThisX(hitbox.x + walkSpeed))
                    if (canMonstaMoveOnThisY(hitbox.y - walkSpeed))
                        move(walkSpeed, -walkSpeed);
                    else
                        walkDir = DOWN_RIGHT;
                else
                    walkDir = UP_LEFT;
            }
            case UP_LEFT -> {
                if (canMonstaMoveOnThisX(hitbox.x - walkSpeed))
                    if (canMonstaMoveOnThisY(hitbox.y - walkSpeed))
                        move(-walkSpeed, -walkSpeed);
                    else
                        walkDir = DOWN_LEFT;
                else
                    walkDir = UP_RIGHT;
            }
            case DOWN_RIGHT -> {
                if (canMonstaMoveOnThisX(hitbox.x + walkSpeed))
                    if (canMonstaMoveOnThisY(hitbox.y + walkSpeed))
                        move(walkSpeed, walkSpeed);
                    else
                        walkDir = UP_RIGHT;
                else
                    walkDir = DOWN_LEFT;
            }
            case DOWN_LEFT -> {
                if (canMonstaMoveOnThisX(hitbox.x - walkSpeed))
                    if (canMonstaMoveOnThisY(hitbox.y + walkSpeed))
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

    private boolean canMonstaMoveOnThisY(float nextY) {
        return CanMoveHere(hitbox.x, nextY, hitbox.width, hitbox.height, getLvlData());
    }

    private boolean canMonstaMoveOnThisX(float nextX) {
        return CanMoveHere(nextX, hitbox.y, hitbox.width, hitbox.height, getLvlData());
    }

}
