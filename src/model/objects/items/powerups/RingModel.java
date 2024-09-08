package model.objects.items.powerups;

import model.entities.PlayerModel;
import model.objects.bobbles.BubbleManagerModel;

import static model.utilz.Constants.PowerUps.*;

/**
 * La classe {@code RingModel} estende {@code PowerUpModel} e rappresenta un power-up di tipo anello che
 * modifica il punteggio guadagnato in base al tipo di anello raccolto.
 */
public class RingModel extends PowerUpModel {

    /** Il contatore per il tempo dell'effetto. */
    private int effectTick;

    /** Il tempo totale per cui l'effetto rimane attivo. */
    private int effectTimer = 600;

    /**
     * Costruttore della classe {@code RingModel}.
     *
     * @param x la coordinata X dell'anello.
     * @param y la coordinata Y dell'anello.
     * @param width la larghezza dell'anello.
     * @param height l'altezza dell'anello.
     * @param ringType il tipo di anello che determina l'effetto.
     */
    public RingModel(float x, float y, int width, int height, int ringType) {
        super(x, y, width, height, ringType, 1000);
    }

    /**
     * Aggiorna lo stato dell'anello, gestisce il tempo dell'effetto e disabilita
     * l'effetto quando il tempo scade.
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
     * Applica l'effetto dell'anello. A seconda del tipo di anello:
     * <ul>
     *  <li>{@code RING_PINK} aumenta il punteggio raccolto per i salti.</li>
     *  <li>{@code RING_RED} aumenta il punteggio raccolto per bolla scoppiata
     *</ul>
     */
    @Override
    public void applyEffect() {
        pickedUp = true;
        switch (type) {
            case RING_PINK -> PlayerModel.getInstance().setScoreForJump(500);
            case RING_RED -> BubbleManagerModel.getInstance().setScoreForPop(100);
        }
    }

    /**
     * Annulla l'effetto dell'anello e ripristina i valori predefiniti per il punteggio.
     */
    @Override
    public void unapplyEffect() {
        switch (type) {
            case RING_PINK -> PlayerModel.getInstance().setScoreForJump(0);
            case RING_RED -> BubbleManagerModel.getInstance().setScoreForPop(10);
        }
    }
}

