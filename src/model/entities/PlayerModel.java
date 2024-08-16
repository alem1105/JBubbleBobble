package model.entities;

import model.LevelManager;
import model.utilz.Constants;

import java.awt.geom.Rectangle2D;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PlayerConstants.*;
import static model.utilz.Gravity.*;

public class PlayerModel extends EntityModel {

    private static PlayerModel instance;

    private LevelManager levelManager;

    private int playerAction = IDLE;
    private boolean left, right, jump, resetAniTick;
    private boolean moving = false;
    private float playerSpeed = 1.0f * SCALE;

    // Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * SCALE, jumpSpeed = -2.25f * SCALE;
    private float fallSpeedAfterCollision = 0.5f * SCALE;
    private boolean inAir = true;


    public static PlayerModel getInstance() {
        if (instance == null) {
            instance = new PlayerModel(100, 100, (int) (18 * SCALE), (int) (18 * SCALE));
        }
        return instance;
    }

    private PlayerModel(float x, float y, int width, int height) {
        super(x, y, width, height);
        levelManager = LevelManager.getInstance();
        this.x = levelManager.getLevels().get(levelManager.getLvlIndex()).getPlayerSpawn().x;
        this.y = levelManager.getLevels().get(levelManager.getLvlIndex()).getPlayerSpawn().y;
        initHitbox((int) (13 * SCALE), (int) (14 * SCALE));
    }

    public void update() {
        updatePos();
        setPlayerAction();
        updatePlayerAction();
    }

    private void setPlayerAction() {
        int startAni = playerAction;

        if (moving) playerAction = RUNNING;
        else playerAction = IDLE;

        if (inAir) {
            if (airSpeed < 0) playerAction = JUMP;
            else playerAction = FALL;
        }

        if (startAni != playerAction)
            resetAniTick = true;
        else
            resetAniTick = false;
    }

    private void updatePos() {
        moving = false;

        if (jump) {
            jump();
        }

        if (!inAir) {
            if ((!left && !right) || (right && left)) {
                return;
            }
        }

        float xSpeed = 0;

        if (left) {
            xSpeed -= playerSpeed;
        }
        if (right) {
            xSpeed += playerSpeed;
        }

        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, levelManager.getLevels().get(levelManager.getLvlIndex()).getLvlData())) {
                inAir = true;
            }
        }

        if (inAir) {
            // In aria
            if (airSpeed < 0) {
                // Stiamo saltando
                hitbox.y += airSpeed; // Controllo y
                airSpeed += gravity;
                if (canJumpHere(xSpeed)) hitbox.x += xSpeed;
            }
            else {
                // Stiamo cadendo
                // Bloccati dentro un muro
                if (!CanMoveHere(hitbox.x, hitbox.y, hitbox.width, hitbox.height,levelManager.getLevels().get(levelManager.getLvlIndex()).getLvlData())) {
                    hitbox.y += airSpeed;
                    airSpeed = 0.65f * SCALE;
                }
                // Caduta normale
                else if (CanMoveHere(hitbox.x, hitbox.y + airSpeed,
                    hitbox.width, hitbox.height,
                    levelManager.getLevels().get(levelManager.getLvlIndex()).getLvlData())) {
                        airSpeed = 0.65f * SCALE; // Discesa lenta
                        hitbox.y += airSpeed;
                        updateXPos(xSpeed);
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
        }
        else {
            updateXPos(xSpeed);
        }
        moving = true;
    }

    private boolean canJumpHere(float xSpeed) {

        if (hitbox.x + xSpeed > Constants.GameConstants.TILES_SIZE && hitbox.x + xSpeed + hitbox.width < Constants.GameConstants.GAME_WIDTH - Constants.GameConstants.TILES_SIZE)
            return true;
        return false;
    }

    private void updatePlayerAction() {
        int startAni = playerAction;

        if (moving) playerAction = RUNNING;
        else playerAction = IDLE;

        if (inAir) {
            if (airSpeed < 0) playerAction = JUMP;
            else playerAction = FALL;
        }

        if (startAni != playerAction) resetAniTick = true;
    }


    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height,
                levelManager.getLevels().get(levelManager.getLvlIndex()).getLvlData()))
            hitbox.x += xSpeed;
        else
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
    }

    public int getPlayerAction() {
        return playerAction;
    }

    public void setRight(boolean b) {
        this.right = b;
    }

    public void setLeft(boolean b) {
        this.left = b;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isResetAniTick() {
        return resetAniTick;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}
