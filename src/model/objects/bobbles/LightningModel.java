package model.objects.bobbles;

import model.objects.CustomObjectModel;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;

/**
 * Rappresenta un modello di fulmine nel gioco.
 * Estende la classe {@link CustomObjectModel}.
 */
public class LightningModel extends CustomObjectModel {

    /** Velocità del fulmine. */
    private float speed = 2f * SCALE;

    /** Indica se il fulmine ha già colpito il boss. */
    private boolean alreadyHitBoss;

    /**
     * Crea un'istanza di LightningModel.
     *
     * @param x         La coordinata X del fulmine.
     * @param y         La coordinata Y del fulmine.
     * @param width     La larghezza del fulmine.
     * @param height    L'altezza del fulmine.
     * @param direction La direzione in cui si muove il fulmine.
     */
    public LightningModel(float x, float y, int width, int height, int direction) {
        super(x, y, width, height);
        if (direction == RIGHT)
            speed = -speed;
    }

    /**
     * Aggiorna la posizione del fulmine.
     * Questo metodo viene chiamato per aggiornare lo stato del fulmine nel gioco.
     */
    @Override
    public void update() {
        updatePos();
    }

    /**
     * Aggiorna la posizione del fulmine.
     * Se il fulmine è all'interno dei confini, la sua posizione viene aggiornata;
     * altrimenti, viene disattivato.
     */
    private void updatePos() {
        if (checkInBorder())
            hitbox.x += speed;
        else
            active = false;
    }

    /**
     * Controlla se il fulmine è all'interno dei confini del gioco.
     *
     * @return true se il fulmine è all'interno dei confini, false altrimenti.
     */
    private boolean checkInBorder() {
        int currentX = (int) (hitbox.x / TILES_SIZE);
        return 0 < currentX && currentX <= TILES_IN_WIDTH;
    }

    public void setAlreadyHitBoss(boolean alreadyHitBoss) {
        this.alreadyHitBoss = alreadyHitBoss;
    }

    public boolean isAlreadyHitBoss() {
        return alreadyHitBoss;
    }
}

