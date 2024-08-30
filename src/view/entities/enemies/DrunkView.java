package view.entities.enemies;

import model.entities.enemies.EnemyModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import static view.utilz.LoadSave.loadAnimations;

public class DrunkView extends EnemyView {

    private static final int ROW_INDEX = 5;
    private static final int COL_INDEX = 6;

    public DrunkView(EnemyModel enemy) {
        super(enemy);
        animations = loadAnimations(LoadSave.DRUNK_SPRITE, ROW_INDEX, COL_INDEX, 18, 18);
        xDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
        yDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
    }

}