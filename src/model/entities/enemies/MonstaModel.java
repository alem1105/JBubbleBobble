package model.entities.enemies;

import static model.utilz.Constants.GameConstants.SCALE;

public class MonstaModel extends EnemyModel{

    public MonstaModel(float x, float y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE;
        initHitbox(14, 16);
    }

}
