package view.entities.enemies;

import model.entities.enemies.InvaderModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import static view.utilz.LoadSave.loadAnimations;

/**
 * La classe {@code InvaderView} rappresenta la visualizzazione del nemico Invader nel gioco.
 * Estende la classe generica {@link EnemyView} per gestire il rendering e le animazioni
 * specifiche del nemico Invader.
 */
public class InvaderView extends EnemyView<InvaderModel> {
    public InvaderView(InvaderModel enemy) {
        super(enemy);
        animations = loadAnimations(LoadSave.INVADER_SPRITE, ROW_INDEX, COL_INDEX, 18, 18);
        xDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
        yDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
    }
}
