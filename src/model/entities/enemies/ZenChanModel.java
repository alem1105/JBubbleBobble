package model.entities.enemies;

import static model.utilz.Constants.GameConstants.*;

/**
 * Classe che rappresenta il modello di un nemico di tipo ZenChan.
 * Estende la classe {@link EnemyModel} e gestisce il comportamento di questo tipo di nemico.
 */
public class ZenChanModel extends EnemyModel {

    /**
     * Costruttore per inizializzare il modello ZenChan.
     *
     * @param x La coordinata X iniziale del nemico.
     * @param y La coordinata Y iniziale del nemico.
     */
    public ZenChanModel(int x, int y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE; // Imposta la velocità di camminata del nemico
        initHitbox(14, 16); // Inizializza la hitbox del nemico
    }
}

