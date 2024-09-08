package model.objects.projectiles;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;

/**
 * Rappresenta un modello di un laser lanciato da un Invader nel gioco.
 * Estende la classe {@link ProjectileModel}.
 */
public class InvaderLaserModel extends ProjectileModel {

    /** Velocità del laser. */
    private float laserSpeed = 0.5f * SCALE;

    /**
     * Crea un'istanza di InvaderLaserModel.
     *
     * @param x La coordinata X iniziale del laser.
     * @param y La coordinata Y iniziale del laser.
     */
    public InvaderLaserModel(float x, float y) {
        super(x, y, (int) (18 * SCALE), (int)(18 * SCALE), RIGHT);
    }

    /**
     * Aggiorna la posizione del laser.
     * Se il laser non ha raggiunto l'ultima tile, si sposta verso il basso.
     * Altrimenti, il laser viene disattivato.
     */
    @Override
    public void update() {
        if (!atTheLastTile())
            hitbox.y += laserSpeed;
        else
            active = false;
    }

    /**
     * Controlla se il laser ha raggiunto l'ultima tile.
     *
     * @return {@code true} se il laser ha raggiunto l'ultima tile; {@code false} altrimenti.
     */
    private boolean atTheLastTile() {
        return (hitbox.y + hitbox.height) >= GAME_HEIGHT - TILES_SIZE;
    }
}

