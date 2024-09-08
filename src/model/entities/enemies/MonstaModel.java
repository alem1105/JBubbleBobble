package model.entities.enemies;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

/**
 * Classe che rappresenta il modello di un nemico di tipo Monsta.
 * Estende la classe {@link EnemyModel} e gestisce il comportamento di movimento
 * di questo tipo di nemico.
 */
public class MonstaModel extends EnemyModel {

    /**
     * Costruttore per inizializzare il modello Monsta.
     *
     * @param x La coordinata X iniziale del nemico.
     * @param y La coordinata Y iniziale del nemico.
     */
    public MonstaModel(float x, float y) {
        super(x, y - 1, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE;
        initHitbox(14, 16);
        walkDir = UP_RIGHT; // Direzione di camminata iniziale
    }

    /**
     * Aggiorna la posizione del nemico in base alla direzione in cui si sta muovendo.
     * Gestisce il movimento del nemico in base alla direzione corrente.
     */
    @Override
    protected void updatePos() {
        if(inBubble) {
            inBubbleMovement();
            return;
        }

        switch (walkDir) {
            case UP_RIGHT -> {
                if (canMonstaMoveOnThisX(hitbox.x + walkSpeed))
                    if (canMonstaMoveOnThisY(hitbox.y - walkSpeed))
                        move(walkSpeed, -walkSpeed);
                    else
                        walkDir = DOWN_RIGHT;
                else
                    walkDir = UP_LEFT;
            }
            case UP_LEFT -> {
                if (canMonstaMoveOnThisX(hitbox.x - walkSpeed))
                    if (canMonstaMoveOnThisY(hitbox.y - walkSpeed))
                        move(-walkSpeed, -walkSpeed);
                    else
                        walkDir = DOWN_LEFT;
                else
                    walkDir = UP_RIGHT;
            }
            case DOWN_RIGHT -> {
                if (canMonstaMoveOnThisX(hitbox.x + walkSpeed))
                    if (canMonstaMoveOnThisY(hitbox.y + walkSpeed))
                        move(walkSpeed, walkSpeed);
                    else
                        walkDir = UP_RIGHT;
                else
                    walkDir = DOWN_LEFT;
            }
            case DOWN_LEFT -> {
                if (canMonstaMoveOnThisX(hitbox.x - walkSpeed))
                    if (canMonstaMoveOnThisY(hitbox.y + walkSpeed))
                        move(-walkSpeed, walkSpeed);
                    else
                        walkDir = UP_LEFT;
                else
                    walkDir = DOWN_RIGHT;
            }
        }
    }

    /**
     * Muove il nemico di una certa quantità nelle direzioni specificate.
     *
     * @param xMovement La velocità di movimento lungo l'asse X.
     * @param yMovement La velocità di movimento lungo l'asse Y.
     */
    private void move(float xMovement, float yMovement) {
        hitbox.x += xMovement;
        hitbox.y += yMovement;
    }

    /**
     * Controlla se il nemico può muoversi lungo l'asse Y alla coordinata specificata.
     *
     * @param nextY La prossima coordinata Y da controllare.
     * @return true se il nemico può muoversi, false altrimenti.
     */
    private boolean canMonstaMoveOnThisY(float nextY) {
        return canMoveHere(hitbox.x, nextY, hitbox.width, hitbox.height, getLvlData());
    }

    /**
     * Controlla se il nemico può muoversi lungo l'asse X alla coordinata specificata.
     *
     * @param nextX La prossima coordinata X da controllare.
     * @return true se il nemico può muoversi, false altrimenti.
     */
    private boolean canMonstaMoveOnThisX(float nextX) {
        return canMoveHere(nextX, hitbox.y, hitbox.width, hitbox.height, getLvlData());
    }
}

