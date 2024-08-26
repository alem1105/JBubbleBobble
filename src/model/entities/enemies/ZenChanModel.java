package model.entities.enemies;

import model.entities.PlayerModel;

import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.*;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Gravity.CanMoveHere;
import static model.utilz.Gravity.GetEntityXPosNextToWall;

public class ZenChanModel extends EnemyModel {

    private boolean hasEnemyHitTheWall;

    public ZenChanModel(int x, int y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE;
        initHitbox(14, 16);
    }

    public void update() {
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
        // se il player è sulla stessa riga del nemico allora usiamo il metodo walkWithSameY()
        if ((int) (PlayerModel.getInstance().getHitbox().y / TILES_SIZE) == (int) (hitbox.y / TILES_SIZE)) {
            walkWithSameY();
        } else {// significa che sono su y diverse
            // se il player è sopra rispetto al nemico dobbiamo andare verso sopra
            if ((int) (PlayerModel.getInstance().getHitbox().y / TILES_SIZE) <= (int) (hitbox.y / TILES_SIZE) && !inAir) {
                // se sopra abbiamo almeno 3 tile percorribili e il player non è in aria allora saliamo, altrimenti camminiamo per trovare tile sopra di noi
                if (checkUpSolid(getLvlData()) && !(PlayerModel.getInstance().isInAir()))
                    goingUp = true;
                else {
                    walkWithDifferentY();
                }
            } // se il player è sotto di noi, camminiamo fino a quando cadremo e lo troveremo con gli altri metodi
            else if ((int) (PlayerModel.getInstance().getHitbox().y / TILES_SIZE) >= (int) (hitbox.y / TILES_SIZE)) {
                walkWithDifferentY();
            }

        }
    }

    // se sono su y diverse il nemico cammina avanti e indietro
    private void walkWithDifferentY() {
        if (CanMoveHere(hitbox.x + walkSpeed, hitbox.y, hitbox.width, hitbox.height, getLvlData())) {
            hitbox.x += walkSpeed;
            checkDirectionForEnemy();
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, walkSpeed);
            if (walkDir == RIGHT) {
                walkDir = LEFT;
            } else if (walkDir == LEFT) {
                walkDir = RIGHT;
            }
            this.walkSpeed = -walkSpeed;
        }
    }

    private void checkDirectionForEnemy() {
        if (walkSpeed >= 0)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    // se sono sulla stessa riga, il nemico si muove solo orizzontalmente cercando di raggiungere il player
    private void walkWithSameY() {
        // player a sinistra del nemico
        if ((int) (PlayerModel.getInstance().getHitbox().x / TILES_SIZE) <= (int) (hitbox.x / TILES_SIZE)) {
            // se ci possiamo muovere a sinistra lo facciamo, altrimenti abbiamo sbattuto al bordo e dobbiamo cambiare direzione
            if (CanMoveHere(hitbox.x - walkSpeed, hitbox.y, hitbox.width, hitbox.height, getLvlData())) {
                hitbox.x += walkSpeed;
                checkDirectionForEnemy();
            } else if (hasEnemyHitTheWall) {
                walkDir = RIGHT;
                walkSpeed = -walkSpeed;
                hasEnemyHitTheWall = false;
            } else {
                hitbox.x = GetEntityXPosNextToWall(hitbox, walkSpeed);
                hasEnemyHitTheWall = true;
            }
        }
        //player a destra del nemico
        else if ((int) (PlayerModel.getInstance().getHitbox().x / TILES_SIZE) >= (int) (hitbox.x / TILES_SIZE)) {
            // se ci possiamo muovere a destra lo facciamo, altrimenti abbiamo sbattuto al bordo e dobbiamo cambiare direzione
            if (CanMoveHere(hitbox.x + walkSpeed, hitbox.y, hitbox.width, hitbox.height, getLvlData())) {
                hitbox.x += walkSpeed;
                checkDirectionForEnemy();
            } else if (hasEnemyHitTheWall) {
                walkDir = LEFT;
                walkSpeed = -walkSpeed;
                hasEnemyHitTheWall = false;
            } else {
                hitbox.x = GetEntityXPosNextToWall(hitbox, walkSpeed);
                hasEnemyHitTheWall = true;
            }
        }
    }

    public void changeDirAfterHitBorder() {
        hitbox.x = GetEntityXPosNextToWall(hitbox, walkSpeed);
        if (walkDir == RIGHT) {
            walkDir = LEFT;
        } else if (walkDir == LEFT) {
            walkDir = RIGHT;
        }
        this.walkSpeed = -walkSpeed;
    }

    public void updateEnemyState() {
        int startAni = enemyState;

        if (inBubble)
            if (angry)
                enemyState = CAPTURED_ANGRY;
            else
                enemyState = CAPTURED;
        else if (angry)
            enemyState = RUNNING_ANGRY;

        // manca DEAD

        if (startAni != enemyState)
            resetAniTick = true;
        else
            resetAniTick = false;
    }
}
