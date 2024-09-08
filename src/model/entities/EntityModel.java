package model.entities;

import model.LevelManagerModel;
import model.utilz.Fallable;

import java.awt.geom.Rectangle2D;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

/**
 * La classe astratta {@code EntityModel} rappresenta un'entità nel gioco che può
 * essere soggetta alla gravità e al movimento. Implementa l'interfaccia {@code Fallable}
 * per gestire il comportamento relativo alla caduta e salto.
 */
public abstract class EntityModel implements Fallable {

    /**
     * Le coordinate X e Y dell'entità.
     */
    protected float x, y;

    /**
     * Larghezza e altezza dell'entità.
     */
    protected int width, height;

    /**
     * Rettangolo che rappresenta la hitbox dell'entità.
     */
    protected Rectangle2D.Float hitbox;

    /**
     * La velocità dell'entità mentre è in aria.
     */
    protected float airSpeed = 0f;

    /**
     * La forza di gravità applicata all'entità.
     */
    protected float gravity = 0.04f * SCALE;

    /**
     * La velocità di caduta quando l'entità collide con un ostacolo.
     */
    protected float fallSpeedAfterCollision = 0.5f * SCALE;

    /**
     * Indica se l'entità è attualmente in aria.
     */
    protected boolean inAir = true;

    /**
     * Velocità di caduta dell'entità.
     */
    protected float fallingSpeed = 0.65f * SCALE;

    /**
     * Costruttore della classe {@code EntityModel}. Inizializza la posizione e le dimensioni
     * dell'entità.
     *
     * @param x La posizione X iniziale dell'entità.
     * @param y La posizione Y iniziale dell'entità.
     * @param width La larghezza dell'entità.
     * @param height L'altezza dell'entità.
     */
    public EntityModel(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Inizializza la hitbox dell'entità in base alle dimensioni specificate.
     *
     * @param width La larghezza della hitbox.
     * @param height L'altezza della hitbox.
     */
    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, width * SCALE, height * SCALE);
    }

    /**
     * Controlla se l'entità si trova in aria. Se non è in aria, verifica se l'entità
     * ha lasciato il pavimento e imposta lo stato in aria se necessario.
     */
    @Override
    public void isInAirCheck() {
        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, getLvlData())) {
                inAir = true;
            }
        }
    }

    /**
     * Gestisce i controlli di caduta per l'entità. Questo metodo determina come l'entità
     * reagisce quando si trova in aria o quando collide con il pavimento o con un muro.
     *
     * @param xSpeed La velocità di movimento lungo l'asse X dell'entità.
     */
    @Override
    public void fallingChecks(float xSpeed) {
        // Stiamo cadendo
        if (!CanMoveHere(hitbox.x, hitbox.y, hitbox.width, hitbox.height, getLvlData())) {
            hitbox.y += airSpeed;
            airSpeed = fallingSpeed;
        }
        // Caduta normale
        else if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, getLvlData())) {
            airSpeed = fallingSpeed; // Discesa lenta
            hitbox.y += airSpeed;
            updateXPos(xSpeed);
            checkOutOfMap();
        }
        // Fine della caduta
        else {
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            if (airSpeed > 0) {
                resetInAir();
            } else {
                airSpeed = fallSpeedAfterCollision;
            }
            updateXPos(xSpeed);
        }
    }

    /**
     * Controlla se l'entità è uscita dalla mappa. Se l'entità cade fuori mappa,
     * viene riposizionata nella parte superiore della mappa.
     */
    @Override
    public void checkOutOfMap() {
        int currentTileY = (int) (hitbox.y / TILES_SIZE);
        if (currentTileY == TILES_IN_HEIGHT - 1) {
            hitbox.y = -TILES_SIZE;
        }
    }

    /**
     * Resetta lo stato dell'entità in aria, indicando che ha toccato il suolo.
     * La velocità dell'aria viene impostata a zero.
     */
    @Override
    public void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    /**
     * Aggiorna la posizione lungo l'asse X dell'entità in base alla velocità specificata.
     * Questo metodo è astratto e deve essere implementato nelle sottoclassi.
     *
     * @param xSpeed La velocità di movimento lungo l'asse X.
     */
    @Override
    public abstract void updateXPos(float xSpeed);

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public float getX() {
        return hitbox.x;
    }

    public float getY() {
        return hitbox.y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public LevelManagerModel getLevelManager() {
        return LevelManagerModel.getInstance();
    }

    public boolean isInAir() {
        return inAir;
    }

    public void setFallingSpeed(float fallingSpeed) {
        this.fallingSpeed = fallingSpeed;
    }
}


