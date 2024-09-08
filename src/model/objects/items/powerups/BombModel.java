package model.objects.items.powerups;

import model.entities.enemies.EnemyManagerModel;

import static model.utilz.Constants.PowerUps.BOMB;

/**
 * La classe {@code BombModel} rappresenta un power-up di tipo bomba.
 * Estende {@code PowerUpModel} e implementa il comportamento specifico del potenziamento bomba,
 * inclusa l'attivazione dell'effetto e la morte di tutti i nemici ancora liberi.
 */
public class BombModel extends PowerUpModel {

    /**
     * Costruttore della classe {@code BombModel}.
     *
     * @param x la coordinata X della bomba.
     * @param y la coordinata Y della bomba.
     * @param width la larghezza della bomba.
     * @param height l'altezza della bomba.
     */
    public BombModel(float x, float y, int width, int height) {
        super(x, y, width, height, BOMB, 200);
    }

    /**
     * Applica l'effetto della bomba al gioco. Quando il potenziamento bomba viene raccolto:
     * <ul>
     *     <li>Il potenziamento viene disattivato .</li>
     *     <li>Uccide tutti i nemici.</li>
     *     <li>Il flag per l'esplosione della bomba viene impostato su {@code true}.</li>
     * </ul>
     */
    @Override
    public void applyEffect() {
        active = false;
        pickedUp = false;
        EnemyManagerModel.getInstance().getEnemies().forEach(e -> e.setActive(false));
        PowerUpsManagerModel.getInstance().setBombExploding(true);
    }

    /**
     * Rimuove l'effetto della bomba. Questo metodo non è implementato per la classe {@code BombModel}.
     * Il metodo rimane vuoto poiché l'effetto della bomba non necessita di essere annullato.
     */
    @Override
    public void unapplyEffect() {
    }
}

