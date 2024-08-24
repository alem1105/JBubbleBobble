package model.entities;

import model.utilz.Constants;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PlayerConstants.*;
import static model.utilz.Gravity.*;

public class PlayerModel extends EntityModel {

    private static PlayerModel instance;

    private int playerAction = IDLE;
    private boolean left, right, jump, resetAniTick;
    private boolean moving = false;
    private float playerSpeed = 1.0f * SCALE;

    // Gravity
    private float jumpSpeed = -2.25f * SCALE;

    public static PlayerModel getInstance() {
        if (instance == null) {
            instance = new PlayerModel(100, 100, (int) (18 * SCALE), (int) (18 * SCALE));
        }
        return instance;
    }

    private PlayerModel(float x, float y, int width, int height) {
        super(x, y, width, height);
        //getLevelManager() = getLevelManager().getInstance();
        this.x = getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getPlayerSpawn().x;
        this.y = getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getPlayerSpawn().y;
        initHitbox(13, 14);
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

        isInAirCheck();

        if (inAir) {
            // In aria
            if (airSpeed < 0) {
                // Stiamo saltando
                hitbox.y += airSpeed; // Controllo y
                airSpeed += gravity;
                if (canJumpHere(xSpeed)) hitbox.x += xSpeed;
            }
            else {
                fallingChecks(xSpeed);
            }
        }
        else {
            updateXPos(xSpeed);
        }
        moving = true;
    }

    @ Override
    protected void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height,
                getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getLvlData()))
            hitbox.x += xSpeed;
        else
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
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
