package model.objects.items.powerups;

import model.entities.PlayerModel;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PowerUps.SNEAKER;

/**
 * La classe {@code SneakerModel} estende {@code PowerUpModel} e rappresenta un potenziamento di tipo scarpa che modifica
 * le proprietà del giocatore come la velocità e la capacità di salto.
 */
public class SneakerModel extends PowerUpModel {

    /** Il contatore per il tempo dell'effetto. */
    private int effectTick;

    /** Il tempo totale per cui l'effetto rimane attivo. */
    private int effectTimer = 1200;

    /**
     * Costruttore della classe {@code SneakerModel}.
     *
     * @param x la coordinata X del potenziamento.
     * @param y la coordinata Y del potenziamento.
     * @param width la larghezza del potenziamento.
     * @param height l'altezza del potenziamento.
     */
    public SneakerModel(float x, float y, int width, int height) {
        super(x, y, width, height, SNEAKER, 100);
    }

    /**
     * Aggiorna lo stato del potenziamento. Questo metodo gestisce il tempo dell'effetto e disabilita
     * l'effetto quando il tempo scade.
     */
    @Override
    public void update() {
        super.update();
        effectTick++;
        if (effectTick >= effectTimer) {
            effectTick = 0;
            unapplyEffect();
        }
    }

    /**
     * Applica l'effetto del potenziamento: modifica la velocità del giocatore, la velocità di salto
     * e la velocità di caduta.
     */
    @Override
    public void applyEffect() {
        pickedUp = true;
        PlayerModel.getInstance().setPlayerSpeed(1.8f * SCALE);
        PlayerModel.getInstance().setJumpSpeed(-2.75f * SCALE);
        PlayerModel.getInstance().setFallingSpeed(1.50f * SCALE);
    }

    /**
     * Annulla l'effetto del potenziamento e ripristina i valori predefiniti cambiati
     */
    @Override
    public void unapplyEffect() {
        PlayerModel.getInstance().setPlayerSpeed(1.0f * SCALE);
        PlayerModel.getInstance().setJumpSpeed(-2.25f * SCALE);
        PlayerModel.getInstance().setFallingSpeed(0.65f * SCALE);
        active = false;
    }
}
