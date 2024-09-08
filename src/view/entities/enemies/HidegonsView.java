package view.entities.enemies;

import model.entities.enemies.HidegonsModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import static view.utilz.LoadSave.loadAnimations;

/**
 * La classe {@code HidegonsView} rappresenta la visualizzazione del nemico Hidegons nel gioco.
 * Estende la classe generica {@link EnemyView} per gestire il rendering e impostare le animazioni
 * specifiche del nemico Hidegons.
 */
public class HidegonsView extends EnemyView<HidegonsModel> {
    public HidegonsView(HidegonsModel enemy) {
        super(enemy);
        animations = loadAnimations(LoadSave.HIDEGONS_SPRITE, ROW_INDEX, COL_INDEX, 18, 18);
        xDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
        yDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
    }
}
