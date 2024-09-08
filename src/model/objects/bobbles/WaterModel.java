package model.objects.bobbles;

import model.entities.PlayerModel;
import model.objects.CustomObjectModel;
import model.utilz.Fallable;

import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.TILES_SIZE;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

/**
 * Rappresenta un oggetto d'acqua nel gioco, che può cadere e muoversi orizzontalmente.
 * Implementa l'interfaccia {@link Fallable} per gestire la caduta dell'acqua.
 */
public class WaterModel extends CustomObjectModel implements Fallable {

    private boolean inAir = true;
    private float airSpeed;
    private float waterSpeed = 0.65f * SCALE;
    private int direction;
    private boolean specificTrappedPlayer = false;

    /**
     * Costruttore per creare un'istanza di {@code WaterModel}.
     *
     * @param x la coordinata x iniziale
     * @param y la coordinata y iniziale
     * @param width la larghezza dell'oggetto
     * @param height l'altezza dell'oggetto
     * @param direction la direzione in cui l'acqua si muove (LEFT o RIGHT)
     */
    public WaterModel(float x, float y, int width, int height, int direction){
        super(x, y, width, height);
        this.direction = direction;
    }

    /**
     * Aggiorna la posizione dell'oggetto d'acqua.
     */
    public void update() {
        updatePos();
    }

    /**
     * Aggiorna la posizione dell'oggetto d'acqua in base al fatto che sia in aria o meno.
     */
    private void updatePos() {
        isInAirCheck();

        if (inAir)
            fallingChecks(waterSpeed);
        else
            updateXPos(waterSpeed);
    }

    /**
     * Controlla se l'oggetto d'acqua è in aria. Se non è in aria e non è sul pavimento, viene reimpostato in aria.
     */
    @Override
    public void isInAirCheck() {
        if (!inAir) {
            if (!isEntityOnFloor(hitbox, getLvlData())) {
                inAir = true;
            }
        }
    }

    /**
     * Gestisce la caduta dell'oggetto d'acqua, aggiornando la sua posizione verticale.
     * Se l'acqua raggiunge il pavimento, viene riposizionata sopra di esso.
     *
     * @param xSpeed la velocità orizzontale dell'acqua
     */
    @Override
    public void fallingChecks(float xSpeed) {
        // Caduta normale
        if (canMoveHere(hitbox.x, hitbox.y + airSpeed,
                hitbox.width, hitbox.height,
                getLvlData())) {
            airSpeed = 0.65f * SCALE; // Discesa lenta
            hitbox.y += airSpeed;
        }
        // Finita Caduta
        else {
            hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            if (airSpeed > 0) {
                resetInAir();
            }
            updateXPos(xSpeed);
        }
    }


    @Override
    public void checkOutOfMap() {}

    /**
     * Resetta lo stato dell'oggetto d'acqua indicando che non è più in aria e "ferma" la sua velocità verticale.
     */
    @Override
    public void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    /**
     * Aggiorna la posizione orizzontale dell'acqua.
     * Cambia direzione se l'acqua non può muoversi ulteriormente nella direzione attuale.
     *
     * @param xSpeed la velocità orizzontale dell'acqua
     */
    @Override
    public void updateXPos(float xSpeed) {
        xSpeed = (direction == RIGHT) ? xSpeed : -xSpeed;

        if(canWaterMoveHere(hitbox.x + xSpeed))
            hitbox.x += xSpeed;
        else {
            hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
            direction = (direction == RIGHT) ? LEFT : RIGHT;
        }
    }

    /**
     * Verifica se l'acqua può muoversi alla nuova posizione orizzontale.
     *
     * @param nextX la coordinata x successiva
     * @return true se l'acqua può muoversi, false altrimenti
     */
    private boolean canWaterMoveHere(float nextX) {
        return canMoveHere(nextX, hitbox.y, hitbox.width, hitbox.height, getLvlData());
    }

    /**
     * Restituisce una rappresentazione testuale dell'acqua.
     *
     * @return una stringa che rappresenta l'oggetto d'acqua
     */
    @Override
    public String toString() {
        return "WaterModel{" +
                "x=" + hitbox.x +
                ", y=" + hitbox.y +
                ", width=" + hitbox.width +
                ", height=" + hitbox.height +
                ", direction=" + direction +
                ", active=" + active +
                '}';
    }

    public boolean isInAir() {
        return inAir;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isSpecificTrappedPlayer() {
        return specificTrappedPlayer;
    }

    public void setSpecificTrappedPlayer(boolean trappedPlayer) {
        this.specificTrappedPlayer = trappedPlayer;
    }
}
