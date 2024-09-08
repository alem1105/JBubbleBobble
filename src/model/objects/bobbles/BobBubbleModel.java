package model.objects.bobbles;

import model.utilz.UtilityMethods;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.SpecialBubbles.BOB_BUBBLE;
import static model.utilz.Gravity.*;

/**
 * Rappresenta una bolla lanciata dal player nel gioco.
 * Estende il modello di bolla base e gestisce il movimento della bolla
 * e la logica di collisione.
 */
public class BobBubbleModel extends BubbleModel {

    /**
     * Tempo di viaggio del proiettile.
     */
    private int projectileTravelTimes = 0;

    /**
     * Durata del viaggio del proiettile in frame.
     */
    private int projectTravelDuration = 60;

    /**
     * Indica se è già stata lanciata una bolla di fulmine.
     */
    private boolean alreadyShotLighting;

    /**
     * Costruttore della classe BobBubbleModel.
     *
     * @param x La coordinata x iniziale della bolla.
     * @param y La coordinata y iniziale della bolla.
     * @param width Larghezza della bolla.
     * @param height Altezza della bolla.
     * @param bubbleDirection La direzione in cui la bolla si muove.
     */
    public BobBubbleModel(float x, float y, int width, int height, int bubbleDirection) {
        super(x, y, width, height, BOB_BUBBLE);
        this.bubbleDirection = bubbleDirection;
        this.bubbleSpeed = 1.5F * SCALE;
        this.collision = false;
    }

    /**
     * Aggiorna lo stato della bolla in ogni frame.
     * Chiama il metodo per aggiornare la posizione della bolla.
     */
    @Override
    public void update() {
        updatePos();
    }

    /**
     * Aggiorna la posizione della bolla in base al tempo di viaggio del proiettile.
     * Se il proiettile ha viaggiato per un tempo inferiore o uguale alla durata
     * del viaggio esegue il movimento iniziale, altrimenti, gestisce la collisione.
     */
    protected void updatePos() {
        if (projectileTravelTimes <= projectTravelDuration) {
            firstShotMovement();
        } else {
            collision = true;
            afterShotMovement();
        }
    }

    /**
     * Gestisce il movimento iniziale della bolla in base alla direzione.
     * Muove la bolla a destra o a sinistra a seconda della direzione
     * impostata e verifica se può muoversi.
     */
    private void firstShotMovement() {
        if (bubbleDirection == RIGHT) {
            if (CanMoveHere(hitbox.x + bubbleSpeed, y, width, height, UtilityMethods.getLvlData())) {
                hitbox.x += bubbleSpeed;
            } else {
                hitbox.x = GetEntityXPosNextToWall(hitbox, bubbleSpeed);
            }
        } else {
            if (CanMoveHere(hitbox.x - bubbleSpeed, y, width, height, UtilityMethods.getLvlData())) {
                hitbox.x -= bubbleSpeed;
            } else {
                hitbox.x = GetEntityXPosNextToWall(hitbox, bubbleSpeed);
            }
        }
        projectileTravelTimes++;
    }

    /**
     * Ripristina i valori modificati dal powerup caramelle.
     */
    public void resetModifiedCandyValues() {
        projectileTravelTimes = 240;
        bubbleSpeed = 1.5f * SCALE;
        bubbleSpeedAfterShot = 0.3f * SCALE;
    }

    public void setProjectileTravelTimes(int projectileTravelTimes) {
        this.projectileTravelTimes = projectileTravelTimes;
    }

    public boolean isAlreadyShotLighting() {
        return alreadyShotLighting;
    }

    public void setAlreadyShotLighting(boolean alreadyShotLighting) {
        this.alreadyShotLighting = alreadyShotLighting;
    }
}
