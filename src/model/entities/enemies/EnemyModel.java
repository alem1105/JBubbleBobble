package model.entities.enemies;

import model.entities.EntityModel;

import java.util.Random;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.Enemies.*;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.TILES_IN_WIDTH;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;
import static model.utilz.UtilityMethods.getPlayer;

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
    
    private boolean deathMovement = false;
    private boolean invertDeathMovement = false;
    private int parableTick = 0;
    private boolean alreadyDidParable = false;

    public EnemyModel(float x, float y, int width, int height) {
        super(x, y - 1, width, height);
    }

    public void update() {
        if (enemyState == RUNNING || enemyState == RUNNING_ANGRY)
            updatePos();
        updateEnemyState();
    }

    // metoto "default" per nemici tipo Zen Chan

    protected void updatePos() {
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

    protected void updateXPos(float walkSpeed) {
        if (playerAndEnemyAreOnTheSameRow() && !getPlayer().isInvincible()) {
            walkwithSameY();
        } else {
            if(isPlayerOnTopOfTheEnemy() && !(getPlayer().isInAir())) {
                if(checkUpSolid(getLvlData()) && !inAir) {
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

    public void doDeathMovement(EnemyModel enemyModel) {
        if(!(deathMovement))
            parableMovement(enemyModel);

//        if(!(enemyModel.getEnemyTileY() >= TILES_IN_HEIGHT - 3)) {
//            parableMovement(enemyModel);
//        } else {
//            enemyModel.setDeathMovement(true);
//        }
    }
    
    private void parableMovement(EnemyModel enemyModel) {
        if (invertDeathMovement) {
            hitbox.y += 2;
            hitbox.x -= 0.8F;
            if(checkIfEnemyIsOnTheFloorBorder(enemyModel))
                deathMovement = true;
        } else {
            checkIfEnemyHitASideBorder(enemyModel);
            if (alreadyDidParable)
                deathMovement = true;
            else
                doParableMovement(enemyModel);
        }
    }

    private void checkIfEnemyHitASideBorder(EnemyModel enemyModel) {
        if((enemyModel.getEnemyTileX() >= TILES_IN_WIDTH - 1 || enemyModel.getEnemyTileX() <= 1))
            invertDeathMovement = true;
    }

    private void doParableMovement(EnemyModel enemyModel) {
        if(parableTick >= 80) {
            if(checkIfEnemyIsOnTheFloorBorder(enemyModel)){
                alreadyDidParable = true;
            }
            else {
                hitbox.y += 2;
                hitbox.x += 0.8F;
            }
        } else if (parableTick >= 60) {
            hitbox.y += 4;
            hitbox.x += 0.8F;
        } else if (parableTick >= 40) {
            hitbox.y -= 0.7F;
            hitbox.x += 2;
        } else if (parableTick >= 20) {
            hitbox.y -= 4.5F;
            hitbox.x += 1;
        }
        parableTick++;
    }

    private boolean checkIfEnemyIsOnTheFloorBorder(EnemyModel enemyModel) {
        return enemyModel.getEnemyTileY() >= TILES_IN_HEIGHT - 3;
    }


//        System.out.println("-----------------------------");
//        if(enemyModel.isInvertDeathMovement()) {
//            enemyModel.getHitbox().x -= 0.1;
//            enemyModel.getHitbox().y += 1.5;
//        } else {
//            if((enemyModel.getEnemyTileX() >= TILES_IN_WIDTH - 1 || enemyModel.getEnemyTileX() <= 1)) {
//                enemyModel.setInvertDeathMovement(true);
//            } else {
//                if(alreadyDidParable) {
//                    hitbox.y += 2;
//                    hitbox.x += 0.8;
//                } else {
//                    doParableMovement();
//                }
//            }
//        }

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

    protected boolean isPlayerToRightOfEnemy() {
        return getPlayerTileX() > getEnemyTileX();
    }

    protected boolean isPlayerToLeftOfEnemy() {
        return getPlayerTileX() < getEnemyTileX();
    }

    protected boolean playerAndEnemyAreOnTheSameRow() {
        return getPlayerTileY() == getEnemyTileY();
    }

    protected boolean isPlayerOnTopOfTheEnemy() {
        return getPlayerTileY() < getEnemyTileY();
    }

    protected int getPlayerTileY() {
        return (int) (getPlayer().getHitbox().y / TILES_SIZE);
    }

    protected int getEnemyTileY() {
        return (int) (hitbox.y / TILES_SIZE);
    }

    protected int getPlayerTileX() {
        return (int) (getPlayer().getHitbox().x / TILES_SIZE);
    }

    protected int getEnemyTileX() {
        return (int) (hitbox.x / TILES_SIZE);
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

    public void setResetAniTick(boolean resetAniTick) {
        this.resetAniTick = resetAniTick;
    }

    public boolean isResetAniTick() {
        return resetAniTick;
    }

    public boolean isDeathMovement() {
        return deathMovement;
    }

    public void setDeathMovement(boolean deathMovement) {
        this.deathMovement = deathMovement;
    }

    public boolean isInvertDeathMovement() {
        return invertDeathMovement;
    }

    public boolean isAlreadyDidParable() {
        return alreadyDidParable;
    }

    public void setInvertDeathMovement(boolean invertDeathMovement) {
        this.invertDeathMovement = invertDeathMovement;
    }

}
