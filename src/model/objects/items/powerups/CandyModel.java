package model.objects.items.powerups;

import model.LevelManagerModel;
import model.objects.bobbles.BobBubbleModel;
import model.objects.bobbles.BubbleManagerModel;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PowerUps.*;

/**
 * La classe {@code CandyModel} rappresenta un power-up di tipo caramella nel gioco,
 * estendendo {@code PowerUpModel}.
 */
public class CandyModel extends PowerUpModel {

    /** Il livello in cui la caramella è stata inizialmente raccolta. */
    int startLevel;

    /**
     * Costruttore della classe {@code CandyModel}.
     *
     * @param x la coordinata X della caramella.
     * @param y la coordinata Y della caramella.
     * @param width la larghezza della caramella.
     * @param height l'altezza della caramella.
     * @param candyType il tipo di caramella, che ne determina l'effetto specifico.
     */
    public CandyModel(float x, float y, int width, int height, int candyType) {
        super(x, y, width, height, candyType, 100);
        startLevel = LevelManagerModel.getInstance().getLvlIndex();
    }

    /**
     * Aggiorna lo stato della caramella. Questo metodo controlla se il livello corrente è cambiato e in tal caso
     * la caramella viene disattivata e il suo effetto viene annullato.
     */
    @Override
    public void update() {
        super.update();
        int currentLevel = LevelManagerModel.getInstance().getLvlIndex();
        if (startLevel != currentLevel) {
            active = false;
            unapplyEffect();
        }
    }

    /**
     * Applica l'effetto della caramella. Gli effetti variano a seconda del tipo di caramella:
     * <ul>
     *     <li>{@code CANDY_PINK} aumenta la distanza di viaggio delle bolle di {@code BobBubbleModel}.</li>
     *     <li>{@code CANDY_BLUE} aumenta la velocità delle bolle di {@code BobBubbleModel}
     *     <li>{@code CANDY_YELLOW}le bolle di {@code BobBubbleModel} vengono sparate più rapidamente</li>
     * </ul>
     */
    @Override
    public void applyEffect() {
        pickedUp = true;
        for (BobBubbleModel bobBubbleModel : BubbleManagerModel.getInstance().getBobBubbles()) {
            switch (type) {
                case CANDY_PINK -> bobBubbleModel.setProjectileTravelTimes(360);
                case CANDY_BLUE -> bobBubbleModel.setBubbleSpeedAfterShot(0.7f * SCALE);
                case CANDY_YELLOW -> bobBubbleModel.setBubbleSpeed(2 * SCALE);
            }
        }
    }

    /**
     * Annulla l'effetto della caramella. Ripristina i valori originali delle bolle di {@code BobBubbleModel},
     * che sono stati modificati dall'effetto della caramella.
     */
    @Override
    public void unapplyEffect() {
        for (BobBubbleModel bobBubbleModel : BubbleManagerModel.getInstance().getBobBubbles()) {
            bobBubbleModel.resetModifiedCandyValues();
        }
    }
}

