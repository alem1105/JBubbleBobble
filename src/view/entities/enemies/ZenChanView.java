package view.entities.enemies;

import model.entities.enemies.ZenChanModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import static view.utilz.LoadSave.*;

/**
 * La classe {@code ZenChanView} rappresenta la visualizzazione del nemico ZenChan nel gioco.
 * Estende la classe generica {@link EnemyView} per gestire il rendering e impostare le animazioni
 * specifiche del nemico ZenChan.
 */
public class ZenChanView extends EnemyView<ZenChanModel> {

    public ZenChanView(ZenChanModel zenChan) {
        super(zenChan);
        animations = loadAnimations(LoadSave.ZEN_CHAN_SPRITE, ROW_INDEX, COL_INDEX, 18, 18);
        xDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
        yDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
    }

}
