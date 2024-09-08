package model.objects.bobbles;

import model.objects.CustomObjectModel;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import static model.utilz.Constants.SpecialBubbles.EXTEND_BUBBLE;
import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;

/**
 * Rappresenta un modello di bolla nel gioco.
 * Estende la classe {@link CustomObjectModel}.
 */
public class BubbleModel extends CustomObjectModel {

    /** Velocità della bolla. */
    protected float bubbleSpeed = 0.2F * SCALE;

    /** Timer di vita della bolla. */
    protected int lifeTimer = 0; // tempo della vita dopo di che esplode

    /** Indica se la bolla è scaduta. */
    protected boolean timeOut;

    /** Durata della vita della bolla. */
    protected int lifeTime = 3000;

    /** Tipo di bolla. */
    protected int bubbleType;

    /** Carattere di estensione della bolla. */
    protected char extendChar;

    /** Velocità della bolla dopo il tiro. */
    protected float bubbleSpeedAfterShot = 0.3F * SCALE;

    /** Durata del percorso della bolla. */
    protected int pathDuration = 240;

    /** Tick del percorso della bolla. */
    protected int pathTick = 0;

    /** Direzione della bolla. */
    protected int bubbleDirection;

    /** Indica se c'è una collisione. */
    protected boolean collision = true;

    /** Indica se la bolla è bloccata. */
    protected boolean stuck = false;

    /** Generatore casuale per la bolla. */
    protected Random random;

    /** Lista di modelli di acqua associati alla bolla. */
    private ArrayList<WaterModel> waterfall;

    /** Indica se tutte le acque sono state generate. */
    private boolean hasSpawnedAllWaters = false;

    /** Coordinata Y quando la bolla esplode. */
    private float yWhenPopped;

    /** Coordinata X quando la bolla esplode. */
    private float xWhenPopped;

    /**
     * Crea un'istanza di BubbleModel.
     *
     * @param x        La coordinata X della bolla.
     * @param y        La coordinata Y della bolla.
     * @param width    La larghezza della bolla.
     * @param height   L'altezza della bolla.
     * @param bubbleType Il tipo di bolla.
     */
    public BubbleModel(float x, float y, int width, int height, int bubbleType) {
        super(x, y, width, height);
        this.bubbleType = bubbleType;
        random = new Random();
        extendChar = (bubbleType == EXTEND_BUBBLE) ? "Extend".charAt(random.nextInt(6)) : 0;
        checkDirection();
        waterfall = new ArrayList<>();
    }

    /**
     * Controlla la direzione della bolla.
     * Imposta {@code bubbleDirection} a destra o a sinistra
     * a seconda della posizione della bolla.
     */
    private void checkDirection() {
        if ((int) hitbox.x / TILES_SIZE < TILES_IN_WIDTH / 2) {
            bubbleDirection = RIGHT;
        } else {
            bubbleDirection = LEFT;
        }
    }

    /**
     * Aggiorna lo stato della bolla.
     * Controlla il timer della vita e gestisce il movimento dopo lo sparo.
     */
    @Override
    public void update() {
        checkLifeTimer();
        afterShotMovement();
    }

    /**
     * Genera una cascata di acqua quando la bolla d'acqua esplode.
     */
    public void spawnWaterFall() {
        if (!hasSpawnedAllWaters && !active) {
            int direction = (xWhenPopped < (float) GAME_WIDTH / 2) ? RIGHT : LEFT;

            if (waterfall.size() >= 10) {
                hasSpawnedAllWaters = true;
            }

            if (waterfall.isEmpty())
                waterfall.add(new WaterModel(xWhenPopped, yWhenPopped, (int) (8 * SCALE), (int) (8 * SCALE), direction));
            else {
                Rectangle2D.Float lastWaterHitbox = waterfall.getLast().getHitbox();
                if (hasWaterMovedFromStartPoint(lastWaterHitbox)) {
                    waterfall.add(new WaterModel(xWhenPopped, yWhenPopped, (int) (8 * SCALE), (int) (8 * SCALE), direction));
                }
            }
        }
    }

    /**
     * Controlla se il "cubetto" d'acqua si è spostata dal punto di partenza.
     *
     * @param lastWaterHitbox L'area di collisione dell'ultima acqua.
     * @return true se l'acqua si è spostata, false altrimenti.
     */
    private boolean hasWaterMovedFromStartPoint(Rectangle2D.Float lastWaterHitbox) {
        return (Math.abs(Math.abs(lastWaterHitbox.y) - Math.abs(yWhenPopped))) >= lastWaterHitbox.getHeight() - 1;
    }

    /**
     * Gestisce il movimento della bolla dopo essere stata sparata.
     */
    protected void afterShotMovement() {
        if (!stuck) {
            if (getBubbleTileY() > 2) {
                hitbox.y -= bubbleSpeedAfterShot;
            } else {
                if (isBubbleInXRange()) {
                    stuck = true;
                } else {
                    checkBubbleDirection();
                }
            }
        } else {
            startFloating();
        }
    }

    /**
     * Controlla se la bolla è all'interno dell'intervallo orizzontale in cui fluttuare.
     *
     * @return true se la bolla è nell'intervallo, false altrimenti.
     */
    private boolean isBubbleInXRange() {
        return TILES_IN_WIDTH / 2 - 2 <= getBubbleTileX() && getBubbleTileX() <= TILES_IN_WIDTH / 2 + 1;
    }

    /**
     * Controlla la direzione della bolla in base alla sua posizione.
     */
    protected void checkBubbleDirection() {
        if (getBubbleTileX() < TILES_IN_WIDTH / 2 - 2) {
            hitbox.x += bubbleSpeedAfterShot;
        } else {
            hitbox.x -= bubbleSpeedAfterShot;
        }
    }

    /**
     * Inizia a far fluttuare la bolla dopo che è entrata nell'area in cui può fluttuare.
     */
    protected void startFloating() {
        if (pathTick <= pathDuration / 2) {
            hitbox.y -= bubbleSpeedAfterShot;
        } else if (pathTick > pathDuration / 2 && pathTick <= pathDuration) {
            hitbox.y += bubbleSpeedAfterShot;
        } else {
            pathTick = 0;
        }
        pathTick++;
    }

    /**
     * Controlla il timer di vita della bolla.
     * Se la vita supera la durata massima, la bolla esplode.
     */
    private void checkLifeTimer() {
        if (active) {
            lifeTimer++;
            if (lifeTimer >= lifeTime) { // dopo 25 secondi esplode
                active = false;
                timeOut = true;
            }
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isTimeOut() {
        return timeOut;
    }

    protected int getBubbleTileY() {
        return (int) (hitbox.y / TILES_SIZE);
    }

    private int getBubbleTileX() {
        return (int) (hitbox.x / TILES_SIZE);
    }

    public boolean isCollision() {
        return collision;
    }

    public int getBubbleType() {
        return bubbleType;
    }

    public void setTimeout(boolean timeOut) {
        this.timeOut = timeOut;
    }

    public void setBubbleSpeed(float bubbleSpeed) {
        this.bubbleSpeed = bubbleSpeed;
    }

    public void setBubbleSpeedAfterShot(float bubbleSpeedAfterShot) {
        this.bubbleSpeedAfterShot = bubbleSpeedAfterShot;
    }

    public ArrayList<WaterModel> getWaterfall() {
        return waterfall;
    }

    public void setyWhenPopped(float yWhenPopped) {
        this.yWhenPopped = yWhenPopped;
    }

    public void setxWhenPopped(float xWhenPopped) {
        this.xWhenPopped = xWhenPopped;
    }

    public char getExtendChar() {
        return extendChar;
    }
}

