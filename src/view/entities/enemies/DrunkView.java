package view.entities.enemies;

import model.entities.enemies.DrunkModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import static view.utilz.LoadSave.loadAnimations;

/**
 * La classe {@code DrunkView} estende {@code EnemyView} e rappresenta la visualizzazione per l'entità nemica "Drunk".
 * Gestisce l'animazione e la visualizzazione del nemico Drunk nel gioco.
 */
public class DrunkView extends EnemyView<DrunkModel> {

    /** Quante righe sono presenti nelle sprite */
    private static final int ROW_INDEX = 5;

    /** Quante colonne sono presenti nelle sprite */
    private static final int COL_INDEX = 6;

    /**
     * Costruisce una nuova istanza di {@code DrunkView} associata al modello specificato.
     *
     * @param enemy Il modello {@code DrunkModel} associato a questa visualizzazione.
     */
    public DrunkView(DrunkModel enemy) {
        super(enemy);
        animations = loadAnimations(LoadSave.DRUNK_SPRITE, ROW_INDEX, COL_INDEX, 18, 18);
        xDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
        yDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
    }

}
