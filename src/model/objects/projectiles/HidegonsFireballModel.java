package model.objects.projectiles;

import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Gravity.canMoveHere;
import static model.utilz.UtilityMethods.getLvlData;

/**
 * Rappresenta un modello di una palla di fuoco lanciata da Hidegon nel gioco.
 * Estende la classe {@link ProjectileModel}.
 */
public class HidegonsFireballModel extends ProjectileModel {

    /** Velocità della palla di fuoco. */
    private float fireballSpeed = 1.5f * SCALE;

    /**
     * Crea un'istanza di HidegonsFireballModel.
     *
     * @param x         La coordinata X iniziale della palla di fuoco.
     * @param y         La coordinata Y iniziale della palla di fuoco.
     * @param direction La direzione iniziale della palla di fuoco.
     */
    public HidegonsFireballModel(float x, float y, int direction) {
        super(x, y, (int) (18 * SCALE), (int)(13 * SCALE), direction);
        checkDirection();
    }

    /**
     * Controlla la direzione della palla di fuoco e imposta la velocità appropriata.
     */
    private void checkDirection() {
        if (direction == LEFT)
            fireballSpeed = -fireballSpeed;
    }

    /**
     * Aggiorna la posizione della palla di fuoco.
     * Questo metodo viene chiamato per aggiornare lo stato della palla di fuoco nel gioco.
     */
    @Override
    public void update() {
        updatePos();
    }

    /**
     * Aggiorna la posizione della palla di fuoco in base alla sua velocità.
     * Se la palla di fuoco non può muoversi nella direzione desiderata, viene disattivata.
     */
    private void updatePos() {
        if (canMoveHere(hitbox.x + fireballSpeed, hitbox.y, hitbox.width, hitbox.height, getLvlData())) {
            hitbox.x += fireballSpeed;
        } else {
            active = false;
        }
    }
}

