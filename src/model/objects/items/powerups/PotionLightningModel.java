package model.objects.items.powerups;

import model.entities.PlayerModel;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PowerUps.POTION_LIGHTNING;

/**
 * La classe {@code PotionLightningModel} esetende {@code PowerUpModel} e rappresenta un potenziamento di tipo pozione che conferisce
 * al giocatore la capacità di sparare bolle di fulmine e aumenta la velocità e il salto del giocatore.
 */
public class PotionLightningModel extends PowerUpModel {

    /** Il numero di vite del giocatore al momento dell'acquisizione della pozione. */
    private int startLives;

    /**
     * Costruttore della classe {@code PotionLightningModel}.
     *
     * @param x la coordinata X della pozione.
     * @param y la coordinata Y della pozione.
     * @param width la larghezza della pozione.
     * @param height l'altezza della pozione.
     */
    public PotionLightningModel(float x, float y, int width, int height) {
        super(x, y, width, height, POTION_LIGHTNING, 100);
        startLives = PlayerModel.getInstance().getLives();
    }

    /**
     * Annulla l'effetto se il numero di vite del giocatore cambia.
     */
    @Override
    public void update() {
        if (PlayerModel.getInstance().getLives() != startLives) {
            unapplyEffect();
        }
    }

    /**
     * Applica l'effetto della pozione. Abilita la capacità di sparare bolle di fulmine
     * e modifica la velocità di movimento, la velocità di salto e la velocità di caduta.
     */
    @Override
    public void applyEffect() {
        pickedUp = true;
        PlayerModel.getInstance().setShootingLightningBubble(true);
        PlayerModel.getInstance().setPlayerSpeed(1.5f * SCALE);
        PlayerModel.getInstance().setJumpSpeed(-2.5f * SCALE);
        PlayerModel.getInstance().setFallingSpeed(1.2f * SCALE);
    }

    /**
     * Annulla l'effetto della pozione.
     * Ripristina i valori del giocatore a quelli predefiniti e disabilita la capacità di sparare bolle di fulmine.
     */
    @Override
    public void unapplyEffect() {
        PlayerModel.getInstance().setShootingLightningBubble(false);
        PlayerModel.getInstance().setPlayerSpeed(1.0f * SCALE);
        PlayerModel.getInstance().setJumpSpeed(-2.25f * SCALE);
        PlayerModel.getInstance().setFallingSpeed(0.65f * SCALE);
        active = false;
    }
}
