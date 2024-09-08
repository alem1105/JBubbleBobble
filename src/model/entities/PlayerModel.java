package model.entities;

import model.UserModel;
import model.gamestate.UserStateModel;
import model.objects.bobbles.BobBubbleModel;
import model.objects.bobbles.BubbleManagerModel;
import model.utilz.Constants;

import java.awt.*;

import static model.utilz.Constants.CustomObjects.BUBBLE_SIZE;
import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PlayerConstants.*;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

public class PlayerModel extends EntityModel {

    private static PlayerModel instance;

    private int playerAction = IDLE;
    private boolean left, right, jump, resetAniTick;
    private boolean moving = false;
    private float playerSpeed = 1.0f * SCALE;

    // Gravity
    private float jumpSpeed = -2.25f * SCALE;

    private int lives = 3;

    // segnala che ha perso tutte le vite
    private boolean gameOver = false;

    private boolean attack, attackingClick;
    private int facing = RIGHT;

    // invincibilità
    private boolean invincible = true;
    private int invincibleDuration = 600;
    private int invincibleTick = 0;

    // per i power up
    private int blowedBubbles, poppedBubbles, poppedLightingBubbles, poppedFireBubbles = 1,  poppedWaterBubbles = 1;
    private int jumpedTimes, eatenPinkCandies, eatenYellowCandies, runDistanceAmount, reachedFinalLevel;
    private int scoreForJump = 0;

    private boolean shootingLightningBubble;

    private BubbleManagerModel bubbleManagerModel = BubbleManagerModel.getInstance();

    public static PlayerModel getInstance() {
        if (instance == null) {
            instance = new PlayerModel(100, 100, (int) (18 * SCALE), (int) (18 * SCALE));
        }
        return instance;
    }

    private PlayerModel(float x, float y, int width, int height) {
        super(x, y, width, height);
        this.x = getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getPlayerSpawn().x;
        this.y = getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getPlayerSpawn().y;
        initHitbox(13, 14);
    }

    public void moveToSpawn(){
        hitbox.x = getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getPlayerSpawn().x;
        hitbox.y = getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getPlayerSpawn().y;
        inAir = true;
    }

    public void playerHasBeenHit() {
        lives--;
        invincible = true;
        hitbox.x = getPlayerSpawn().x;
        hitbox.y = getPlayerSpawn().y;
        inAir = true;
        playerAction = DEATH;
    }

    private void checkAttack() {
        if (attack && !attackingClick) {
            blowedBubbles++;
            attackingClick = true;
            float bubbleX = hitbox.x;
            if (right)
                bubbleX += hitbox.width;
            bubbleManagerModel.addBobBubbles(new BobBubbleModel(bubbleX, hitbox.y - (BUBBLE_SIZE - hitbox.height), BUBBLE_SIZE, BUBBLE_SIZE, facing));
        }
    }

    public void update() {
        updatePos();
        checkRidingABubble();
        checkAttack();
        setPlayerAction();
        updateInvincibleStatus();
        if(lives <= 0) {
            gameOver = true;
            UserModel user = UserStateModel.getInstance().getCurrentUserModel();
            user.updateLevelScore();
            user.incrementLosses();
            user.incrementMatches();
            user.setMaxScore();
            user.serialize("res/users/" + user.getNickname() + ".bb");
        }
    }

    private void updateInvincibleStatus() {
        if (invincible) {
            invincibleTick++;
            if (invincibleTick >= invincibleDuration) {
                invincible = false;
                invincibleTick = 0;
            }
        }
    }

    private void setPlayerAction() {
        int startAni = playerAction;

        if (attack) {
            playerAction = ATTACK;
        }
        else {
            if (moving)
                playerAction = RUNNING;
            else if (playerAction != DEATH)
                playerAction = IDLE;

            if (inAir) {
                if (airSpeed < 0) playerAction = JUMP;
                else playerAction = FALL;
            }
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
            facing = LEFT;
            xSpeed -= playerSpeed;
        }
        if (right) {
            facing = RIGHT;
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


    private void checkRidingABubble() {
        if (airSpeed > 0) {
            bubbleManagerModel.getBobBubbles().stream()
                    .filter(BobBubbleModel::isActive)
                    .filter(BobBubbleModel::isCollision)
                    .filter(bobBubble -> hitbox.getMaxY() >= bobBubble.getHitbox().getY() - (5 * SCALE) // Controlla se il player è precisamente sopra la bolla
                            && hitbox.getMaxY() <= bobBubble.getHitbox().getY() + (5 * SCALE)
                            && hitbox.x >= bobBubble.getHitbox().getX()
                            && hitbox.x <= bobBubble.getHitbox().getMaxX()
                            && jump)
                    .findFirst() // si ferma alla prima bolla trovata
                    .ifPresent(bobBubble -> {
                        inAir = true;
                        airSpeed = jumpSpeed;
                    });
        }
    }


    @ Override
    public void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height,
                getLvlData())) {
            hitbox.x += xSpeed;
            runDistanceAmount += (int) Math.abs(xSpeed);
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    private boolean canJumpHere(float xSpeed) {
        if (hitbox.x + xSpeed > Constants.GameConstants.TILES_SIZE && hitbox.x + xSpeed + hitbox.width < Constants.GameConstants.GAME_WIDTH - Constants.GameConstants.TILES_SIZE)
            return true;
        return false;
    }

    private void jump() {
        if (inAir)
            return;
        UserStateModel.getInstance().getCurrentUserModel().incrementTempScore(scoreForJump);
        jumpedTimes++;
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

    public void setPlayerAction(int playerAction) {
        this.playerAction = playerAction;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public void setAttack(boolean attack) {
        this.attack = attack;
    }

    public boolean isAttack() {
        return attack;
    }

    public void setAttackingClick(boolean attackingClick) {
        this.attackingClick = attackingClick;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public float getAirSpeed() {
        return airSpeed;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean getJump() {
        return jump;
    }

    public int getJumpedTimes() {
        return jumpedTimes;
    }

    public void setScoreForJump(int value) {
        scoreForJump = value;
    }

    public int getPoppedWaterBubbles() {
        return poppedWaterBubbles;
    }

    public int getBlowedBubbles() {
        return blowedBubbles;
    }

    public void setBlowedBubbles(int blowedBubbles) {
        this.blowedBubbles = blowedBubbles;
    }

    public int getPoppedBubbles() {
        return poppedBubbles;
    }

    public void setPoppedBubbles(int poppedBubbles) {
        this.poppedBubbles = poppedBubbles;
    }

    public int incrementPoppedBubbles() {
        return poppedBubbles++;
    }

    public int getPoppedLightingBubbles() {
        return poppedLightingBubbles;
    }

    public void incrementPoppedLightingBubbles() {
        poppedLightingBubbles++;
    }

    public int getPoppedFireBubbles() {
        return poppedFireBubbles;
    }

    public void incrementPoppedFireBubbles() {
        poppedFireBubbles++;
    }

    public void incrementPoppedWaterBubbles() {
        poppedWaterBubbles ++;
    }

    public void setJumpedTimes(int jumpedTimes) {
        this.jumpedTimes = jumpedTimes;
    }

    public int getEatenPinkCandies() {
        return eatenPinkCandies;
    }

    public void setEatenPinkCandies(int eatenPinkCandies) {
        this.eatenPinkCandies = eatenPinkCandies;
    }

    public int getEatenYellowCandies() {
        return eatenYellowCandies;
    }

    public void setEatenYellowCandies(int eatenYellowCandies) {
        this.eatenYellowCandies = eatenYellowCandies;
    }

    public int getRunDistanceAmount() {
        return runDistanceAmount;
    }

    public void setRunDistanceAmount(int runDistanceAmount) {
        this.runDistanceAmount = runDistanceAmount;
    }

    public void setPlayerSpeed(float value) {
        this.playerSpeed = value;
    }

    public void setJumpSpeed(float value) {
        this.jumpSpeed = value;
    }

    public void setGravity(float value) {
        this.gravity = value;
    }

    public void setPoppedLightingBubbles(int poppedLightingBubbles) {
        this.poppedLightingBubbles = poppedLightingBubbles;
    }

    public void incrementLives() {
        this.lives++;
    }

    public int getFacing() {
        return facing;
    }

    public boolean isShootingLightningBubble() {
        return shootingLightningBubble;
    }

    public void setShootingLightningBubble(boolean shootingLightningBubble) {
        this.shootingLightningBubble = shootingLightningBubble;
    }

    public Point getPlayerSpawn() {
        return getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getPlayerSpawn();
    }
}
