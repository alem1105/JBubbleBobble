package model.ui.buttons;

import java.awt.geom.Rectangle2D;

/**
 * Classe astratta che rappresenta un modello di pulsante personalizzato.
 * Questa classe fornisce la base per la creazione di pulsanti con coordinate,
 * dimensioni e stato in base alle azioni del cursore del mouse
 */
public abstract class CustomButtonModel {

    /**
     * Coordinata X del pulsante.
     */
    protected int x;

    /**
     * Coordinata Y del pulsante.
     */
    protected int y;

    /**
     * Larghezza del pulsante.
     */
    protected int width;

    /**
     * Altezza del pulsante.
     */
    protected int height;

    /**
     * Area di interazione del pulsante, utilizzata per rilevare il passaggio del mouse e i click.
     */
    protected Rectangle2D.Float bounds;

    /**
     * Indica se il mouse si trova sopra il pulsante.
     */
    protected boolean mouseHover;

    /**
     * Indica se il pulsante è stato premuto.
     */
    protected boolean mousePressed;

    /**
     * Costruttore per inizializzare un pulsante personalizzato con le coordinate e le dimensioni specificate.
     *
     * @param x      Coordinata X iniziale del pulsante.
     * @param y      Coordinata Y iniziale del pulsante.
     * @param width  Larghezza del pulsante.
     * @param height Altezza del pulsante.
     */
    public CustomButtonModel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        bounds = new Rectangle2D.Float(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle2D.Float getBounds() {
        return bounds;
    }

    public void setHover(boolean hover) {
        mouseHover = hover;
    }

    public void setPressed(boolean pressed) {
        mousePressed = pressed;
    }

    public boolean isHover() {
        return mouseHover;
    }

    public boolean isPressed() {
        return mousePressed;
    }
}
