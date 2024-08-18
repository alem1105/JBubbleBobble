package model.entities.enemies;

import static model.utilz.Constants.Enemies.*;
import static model.utilz.Constants.GameConstants.*;


public class ZenChanModel extends EnemyModel{
    
    public ZenChanModel(int x, int y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        initHitbox(14, 16);
    }
    
    public void update() {
        updateEnemyState();
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
