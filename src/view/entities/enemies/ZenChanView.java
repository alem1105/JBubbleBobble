package view.entities.enemies;

import model.entities.enemies.ZenChanModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import java.awt.*;

import static model.utilz.Constants.Enemies.*;
import static view.utilz.LoadSave.*;

public class ZenChanView extends EnemyView {

    private static final int ROW_INDEX = 5;
    private static final int COL_INDEX = 6;

    public ZenChanView(ZenChanModel zenChan) {
        super(zenChan);
        animations = loadAnimations(LoadSave.ZEN_CHAN_SPRITE, ROW_INDEX, COL_INDEX, 18, 18);
        xDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
        yDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
    }

    @Override
    public int getSpriteAmount() {
        return switch (enemy.getEnemyState()) {
            case DEAD -> 6;
            default -> 2;
        };
    }
}
