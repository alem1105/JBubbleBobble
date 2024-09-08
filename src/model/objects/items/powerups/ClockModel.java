package model.objects.items.powerups;

import model.entities.enemies.EnemyManagerModel;

import static model.utilz.Constants.PowerUps.CLOCK;

/**
 * La classe {@code ClockModel} estende {@code PowerUpModel} rappresenta un power-up di tipo orologio.
 */
public class ClockModel extends PowerUpModel {

    /** Il contatore per il tempo trascorso dal momento dell'attivazione dell'effetto. */
    private int effectTick;

    /** Il tempo massimo in tick per la durata dell'effetto. */
    private final int effectTimer = 600;

    /**
     * Costruttore della classe {@code ClockModel}.
     *
     * @param x la coordinata X dell'orologio.
     * @param y la coordinata Y dell'orologio.
     * @param width la larghezza dell'orologio.
     * @param height l'altezza dell'orologio.
     */
    public ClockModel(float x, float y, int width, int height) {
        super(x, y, width, height, CLOCK, 200);
    }

    /**
     * Aggiorna lo stato dell'orologio. Questo metodo gestisce la durata dell'effetto:
     * incrementa il contatore {@code effectTick} e annulla l'effetto dell'orologio se
     * il tempo dell'effetto è scaduto.
     */
    @Override
    public void update() {
        super.update();
        effectTick++;
        if (effectTick >= effectTimer) {
            effectTick = 0;
            active = false;
            unapplyEffect();
        }
    }

    /**
     * Applica l'effetto dell'orologio: congela il tempo nel gioco per tutta la durata dell'effetto.
     * L'effetto viene applicato impostando il flag di congelamento del tempo nel {@code EnemyManagerModel}.
     */
    @Override
    public void applyEffect() {
        pickedUp = true;
        EnemyManagerModel.getInstance().setTimeFrozen(true);
    }

    /**
     * Annulla l'effetto dell'orologio. Ripristina il normale flusso del tempo nel gioco.
     * L'effetto viene annullato impostando il flag di congelamento del tempo nel {@code EnemyManagerModel} a false.
     */
    @Override
    public void unapplyEffect() {
        EnemyManagerModel.getInstance().setTimeFrozen(false);
    }
}

