package model.objects;

import java.awt.geom.Rectangle2D;

/**
 * Classe astratta che rappresenta un oggetto personalizzato nel gioco.
 * Questa classe fornisce una struttura base per tutti gli oggetti nel gioco,
 * inclusi le loro coordinate, dimensioni e hitbox.
 */
public abstract class CustomObjectModel {

    /**
     * Coordinata X dell'oggetto.
     */
    protected float x;

    /**
     * Coordinata Y dell'oggetto.
     */
    protected float y;

    /**
     * Larghezza dell'oggetto.
     */
    protected int width;

    /**
     * Altezza dell'oggetto.
     */
    protected int height;

    /**
     * Hitbox dell'oggetto, utilizzata per rilevare le collisioni.
     */
    protected Rectangle2D.Float hitbox;

    /**
     * Stato attivo dell'oggetto.
     */
    protected boolean active = true;

    /**
     * Costruttore per inizializzare un oggetto personalizzato con le coordinate e le dimensioni specificate.
     *
     * @param x      Coordinata X iniziale dell'oggetto.
     * @param y      Coordinata Y iniziale dell'oggetto.
     * @param width  Larghezza dell'oggetto.
     * @param height Altezza dell'oggetto.
     */
    public CustomObjectModel(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        initHitbox();
    }

    /**
     * Inizializza la hitbox dell'oggetto in base alle coordinate e dimensioni specificate.
     */
    private void initHitbox() {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    /**
     * Metodo astratto che deve essere implementato dalle sottoclassi per aggiornare lo stato dell'oggetto.
     */
    public abstract void update();

    public boolean isActive() {
        return active;
    }

    public float getX() {
        return hitbox.x;
    }

    public float getY() {
        return hitbox.y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
