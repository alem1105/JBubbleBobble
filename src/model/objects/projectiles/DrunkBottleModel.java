package model.objects.projectiles;

import model.entities.PlayerModel;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.GAME_WIDTH;
import static model.utilz.Gravity.canMoveHere;
import static model.utilz.UtilityMethods.getLvlData;

/**
 * Rappresenta un modello di bottiglia nel gioco (proiettile di Drunk).
 * Estende la classe {@link ProjectileModel}.
 */
public class DrunkBottleModel extends ProjectileModel {

    /** Velocità della bottiglia in direzione X. */
    private float xBottleSpeed = 0.8f * SCALE;

    /** Velocità della bottiglia in direzione Y. */
    private float yBottleSpeed = 0.8f * SCALE;

    /** Coordinata X iniziale della bottiglia. */
    private float startX;

    /** Numero di cambi di direzione della bottiglia. */
    private int dirChanged;

    /** Indica se la bottiglia è del boss. */
    private boolean superDrunk;

    /**
     * Crea un'istanza di DrunkBottleModel.
     *
     * @param x         La coordinata X iniziale della bottiglia.
     * @param y         La coordinata Y iniziale della bottiglia.
     * @param direction La direzione iniziale della bottiglia.
     */
    public DrunkBottleModel(float x, float y, int direction) {
        super(x, y, (int) (18 * SCALE), (int)(18 * SCALE), direction);
        startX = x;
        dirChanged = 0;
        checkDirection();
    }

    /**
     * Controlla la direzione della bottiglia in base alla direzione iniziale.
     */
    private void checkDirection() {
        switch(direction) {
            case RIGHT -> yBottleSpeed = 0;
            case LEFT -> {
                xBottleSpeed = -xBottleSpeed;
                yBottleSpeed = 0;
            }
            case UP_RIGHT -> yBottleSpeed = -yBottleSpeed;
            case UP_LEFT -> {
                xBottleSpeed = -xBottleSpeed;
                yBottleSpeed = -yBottleSpeed;
            }
            case DOWN_LEFT -> xBottleSpeed = -xBottleSpeed;
        }
    }

    /**
     * Aggiorna la posizione della bottiglia.
     * Questo metodo viene chiamato per aggiornare lo stato della bottiglia nel gioco.
     */
    @Override
    public void update() {
        updatePos();
    }

    /**
     * Aggiorna la posizione della bottiglia a seconda di chi appartiene (boss o nemico normale).
     */
    private void updatePos() {
        if (superDrunk)
            updateSuperDrunkPos();
        else
            updateDrunkPos();
    }

    /**
     * Aggiorna la posizione della bottiglia quando appartiene al boss.
     * Controlla anche se colpisce il giocatore.
     */
    private void updateSuperDrunkPos() {
        if (hitbox.intersects(PlayerModel.getInstance().getHitbox()) && !PlayerModel.getInstance().isInvincible()) {
            PlayerModel.getInstance().playerHasBeenHit();
        }

        if (canDrunkBottleMoveOnThisX(hitbox.x + xBottleSpeed))
            if (canDrunkBottleMoveOnThisY(hitbox.y + yBottleSpeed)) {
                hitbox.x += xBottleSpeed;
                hitbox.y += yBottleSpeed;
                return;
            }

        active = false;
    }

    /**
     * Controlla se la bottiglia può muoversi in Y.
     *
     * @param nextY La prossima coordinata Y.
     * @return true se può muoversi, false altrimenti.
     */
    private boolean canDrunkBottleMoveOnThisY(float nextY) {
        return !(nextY + hitbox.height >= GAME_HEIGHT - (2 * TILES_SIZE) || nextY <= TILES_SIZE);
    }

    /**
     * Controlla se la bottiglia può muoversi in X.
     *
     * @param nextX La prossima coordinata X.
     * @return true se può muoversi, false altrimenti.
     */
    private boolean canDrunkBottleMoveOnThisX(float nextX) {
        return !(nextX + hitbox.width >= GAME_WIDTH - TILES_SIZE || nextX <= TILES_SIZE);
    }

    /**
     * Aggiorna la posizione della bottiglia quando è di un nemico normale.
     * Controlla se deve scoppiare o invertire direzione.
     */
    private void updateDrunkPos() {
        // Controlla se scoppiare, un po' di scarto per sicurezza
        if (hitbox.x >= startX - (2 * SCALE) && hitbox.x <= startX + (2 * SCALE) && dirChanged > 0) {
            active = false;
        }

        // Controlla se invertire direzione
        if (Math.abs(hitbox.x - startX) >= TILES_SIZE * 4) {
            dirChanged++;
            xBottleSpeed = -xBottleSpeed;
        }

        // Controlla se puoi muoverti
        if (canMoveHere(hitbox.x + xBottleSpeed, hitbox.y, hitbox.width, hitbox.height, getLvlData())) {
            hitbox.x += xBottleSpeed;
        } else {
            dirChanged++;
            xBottleSpeed = -xBottleSpeed;
        }
    }

    public void setSuperDrunk(boolean superDrunk) {
        this.superDrunk = superDrunk;
    }
}