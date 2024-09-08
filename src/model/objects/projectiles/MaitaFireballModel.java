package model.objects.projectiles;

import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Gravity.canMoveHere;
import static model.utilz.UtilityMethods.getLvlData;

/**
 * Rappresenta un modello di una fireball lanciata da Maita nel gioco.
 * Estende la classe {@link ProjectileModel}.
 */
public class MaitaFireballModel extends ProjectileModel {

    /** Velocità della fireball. */
    private float fireballSpeed = 0.5f * SCALE;

    /**
     * Crea un'istanza di MaitaFireballModel.
     *
     * @param x La coordinata X iniziale della fireball.
     * @param y La coordinata Y iniziale della fireball.
     * @param direction La direzione iniziale della fireball.
     */
    public MaitaFireballModel(float x, float y, int direction) {
        super(x, y, (int) (18 * SCALE), (int)(18 * SCALE), direction);
        checkDirection();
    }

    /**
     * Controlla la direzione della fireball e inverte la velocità se necessario.
     */
    private void checkDirection() {
        if (direction == LEFT) {
            fireballSpeed = -fireballSpeed;
        }
    }

    /**
     * Aggiorna la posizione della fireball.
     * Se la fireball può muoversi nella direzione attuale, si sposta.
     * Altrimenti, la fireball viene disattivata.
     */
    @Override
    public void update() {
        updatePos();
    }

    /**
     * Aggiorna la posizione della fireball in base alla sua velocità.
     * Verifica se la fireball può muoversi nella nuova posizione.
     */
    private void updatePos() {
        if (canMoveHere(hitbox.x + fireballSpeed, hitbox.y, hitbox.width, hitbox.height, getLvlData())) {
            hitbox.x += fireballSpeed;
        } else {
            active = false;
        }
    }
}

