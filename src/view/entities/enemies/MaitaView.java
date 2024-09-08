package view.entities.enemies;

import model.entities.enemies.MaitaModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import static view.utilz.LoadSave.loadAnimations;

/**
 * La classe {@code MaitaView} rappresenta la visualizzazione del nemico Maita nel gioco.
 * Estende la classe generica {@link EnemyView} per gestire il rendering e impostare le animazioni
 * specifiche del nemico Maita.
 */
public class MaitaView extends EnemyView<MaitaModel>{

    public MaitaView(MaitaModel maita) {
        super(maita);
        animations = loadAnimations(LoadSave.MAITA_SPRITE, ROW_INDEX, COL_INDEX, 18, 18);
        xDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
        yDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
    }

}
