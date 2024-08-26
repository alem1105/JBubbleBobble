package model.entities.enemies;

import model.entities.PlayerModel;
import model.utilz.UtilityMethods;

import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.*;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Gravity.CanMoveHere;
import static model.utilz.Gravity.GetEntityXPosNextToWall;
import static model.utilz.UtilityMethods.getLvlData;
import static model.utilz.UtilityMethods.getPlayer;

public class ZenChanModel extends EnemyModel {

    private boolean hasEnemyHitTheWall;

    public ZenChanModel(int x, int y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE;
        initHitbox(14, 16);
    }

    public void update() {
        if (enemyState == RUNNING || enemyState == RUNNING_ANGRY)
            updatePos();
        updateEnemyState();
    }

    private void updatePos() {
        isInAirCheck();

        if (!goingUp) {
            if (inAir)
                fallingChecks(walkSpeed);
            else
                updateXPos(walkSpeed);
        } else {
            if ((int) (hitbox.y / TILES_SIZE) != targetYTile) {
                hitbox.y -= 1.5F;
            } else {
                goingUp = false;
            }
        }
    }

    @Override
    protected void updateXPos(float walkSpeed) {
        if (playerAndEnemyAreOnTheSameRow() && !getPlayer().isInvincible()) {
            walkwithSameY();
        } else {
            if(isPlayerOnTopOfTheEnemy() && !(getPlayer().isInAir())) {
                if(checkUpSolid(getLvlData())) {
                    goingUp = true;
                } else {
                    walkWithDifferentY();
                }
            } else {
                walkWithDifferentY();
            }
        }
    }

    private void walkwithSameY() {
        if(isPlayerToLeftOfEnemy()) {
            walkDir = LEFT;
            walkSpeed = -Math.abs(walkSpeed);
            checkEnemyMovement();
        } else if(isPlayerToRightOfEnemy()) {
            walkDir = RIGHT;
            walkSpeed = Math.abs(walkSpeed);
            checkEnemyMovement();
        } else {
            walkWithDifferentY();
        }
    }

    private void walkWithDifferentY() {
        if (walkDir == RIGHT) {
            walkSpeed = Math.abs(walkSpeed);
            if (canEnemyMoveHere()) {
                hitbox.x += walkSpeed;
            } else {
                hitbox.x = GetEntityXPosNextToWall(hitbox, walkSpeed);
                walkDir = LEFT;
            }
        } else {
            walkSpeed = -Math.abs(walkSpeed);
            if (canEnemyMoveHere()) {
                hitbox.x += walkSpeed;
            } else {
                hitbox.x = GetEntityXPosNextToWall(hitbox, walkSpeed);
                walkDir = RIGHT;
            }
        }
    }

    private void checkEnemyMovement() {
        if(canEnemyMoveHere()) {
            hitbox.x += walkSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, walkSpeed);
        }
    }

    private boolean canEnemyMoveHere() {
        return CanMoveHere(hitbox.x + walkSpeed, hitbox.y, hitbox.width, hitbox.height, getLvlData());
    }

    private boolean isPlayerToRightOfEnemy() {
        return getPlayerTileX() > getEnemyTileX();
    }

    private boolean isPlayerToLeftOfEnemy() {
        return getPlayerTileX() < getEnemyTileX();
    }

    private boolean playerAndEnemyAreOnTheSameRow() {
        return getPlayerTileY() == getEnemyTileY();
    }

    private boolean isPlayerOnTopOfTheEnemy() {
        return getPlayerTileY() < getEnemyTileY();
    }

    private int getPlayerTileY() {
        return (int) (getPlayer().getHitbox().y / TILES_SIZE);
    }

    private int getEnemyTileY() {
        return (int) (hitbox.y / TILES_SIZE);
    }

    private int getPlayerTileX() {
        return (int) (getPlayer().getHitbox().x / TILES_SIZE);
    }

    private int getEnemyTileX() {
        return (int) (hitbox.x / TILES_SIZE);
    }

}
