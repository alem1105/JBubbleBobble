package model.entities.enemies;

import static model.utilz.Constants.GameConstants.*;

public class ZenChanModel extends EnemyModel {

    public ZenChanModel(int x, int y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE;
        initHitbox(14, 16);
    }

}
