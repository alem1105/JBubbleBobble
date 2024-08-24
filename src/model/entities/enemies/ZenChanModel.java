package model.entities.enemies;

import model.LevelManager;

import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.*;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Gravity.CanMoveHere;
import static model.utilz.Gravity.GetEntityXPosNextToWall;


public class ZenChanModel extends EnemyModel{
    
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

        if (inAir)
            fallingChecks(walkSpeed);
        else {
            if (dirChangedTimes == 2) {
                checkUpSolid(getLvlData());
            }
            updateXPos(walkSpeed);
        }
    }

    @Override
    protected void updateXPos(float walkSpeed) {
        if (CanMoveHere(hitbox.x + walkSpeed, hitbox.y, hitbox.width, hitbox.height,
                getLvlData()))
            hitbox.x += walkSpeed;
        else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, walkSpeed);
            if(walkDir == RIGHT) {
                walkDir = LEFT;
            }
            else if (walkDir == LEFT) {
                walkDir = RIGHT;
            }
            this.walkSpeed = -walkSpeed;
            dirChangedTimes++;
        }
    }

    public void updateEnemyState() {
        int startAni = enemyState;

        if (inBubble)
            if (angry)
                enemyState = CAPTURED_ANGRY;
            else
                enemyState = CAPTURED;
        else
            if (angry)
                enemyState = RUNNING_ANGRY;

            // manca DEAD

        if (startAni != enemyState)
            resetAniTick = true;
        else
            resetAniTick = false;
    }
}
