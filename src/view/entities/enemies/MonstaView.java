package view.entities.enemies;

import model.entities.enemies.MonstaModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import static model.utilz.Constants.Directions.DOWN_RIGHT;
import static model.utilz.Constants.Directions.UP_RIGHT;
import static model.utilz.Constants.Enemies.ZEN_CHAN_WIDTH;
import static view.utilz.LoadSave.loadAnimations;
/**
 * La classe {@code MonstaView} rappresenta la visualizzazione del nemico Monsta nel gioco.
 * Estende la classe generica {@link EnemyView} per gestire il rendering e impostare le animazioni
 * specifiche del nemico Monsta.
 */
public class MonstaView extends EnemyView<MonstaModel> {

    private static final int ROW_INDEX = 5;
    private static final int COL_INDEX = 6;

    public MonstaView(MonstaModel enemy) {
        super(enemy);
        animations = loadAnimations(LoadSave.MONSTA_SPRITE, ROW_INDEX, COL_INDEX, 18, 18);
        xDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
        yDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
    }

    /**
     * questi metodi servono per lo specchiamento del nemico in base alla direzione nella
     * quale sta viaggiando.
     */
    @Override
    public void flipX() {
        if (enemy.getWalkDir() == UP_RIGHT || enemy.getWalkDir() == DOWN_RIGHT)
            flipX = ZEN_CHAN_WIDTH;
        else
            flipX = 0;
    }

    @Override
    public void flipW() {
        if (enemy.getWalkDir() == UP_RIGHT || enemy.getWalkDir() == DOWN_RIGHT)
            flipW = -1;
        else
            flipW = 1;
    }
}
