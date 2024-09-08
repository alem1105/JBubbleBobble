package view.objects;

import model.objects.CustomObjectModel;
import view.utilz.AudioManager;

import java.awt.image.BufferedImage;
import static model.utilz.Constants.GameConstants.ANI_SPEED;

import java.awt.Graphics;

/**
 * Rappresenta una vista generica per un modello di oggetto personalizzato nel gioco.
 * Questa classe fornisce metodi per disegnare l'oggetto e gestire l'animazione.
 *
 * @param <T> Il tipo di modello associato a questa vista.
 */
public abstract class CustomObjectView<T extends CustomObjectModel> {

    /** Modello dell'oggetto associato a questa vista. */
    protected T objectModel;
    /** Indici per la gestione delle sprite e delle animazioni. */
    protected int spriteIndex, aniIndex, aniTick;
    /** Sprite dell'oggetto. */
    protected BufferedImage[][] sprites;
    /** Indica se è stato riprodotto il suono di raccolta. */
    protected boolean playedPickupSound;

    /**
     * Costruttore per inizializzare la vista dell'oggetto.
     *
     * @param objectModel Il modello dell'oggetto associato a questa vista.
     */
    public CustomObjectView(T objectModel) {
        this.objectModel = objectModel;
    }

    /**
     * Disegna l'oggetto
     *
     * @param g
     */
    public void draw(Graphics g) {
        g.drawImage(sprites[spriteIndex][aniIndex],
                (int) objectModel.getX(),
                (int) objectModel.getY(),
                objectModel.getWidth(),
                objectModel.getHeight(),
                null);
    }

    // For Debugging
    /*
    public void drawHitbox(Graphics g) {
        g.setColor(Color.PINK);
        g.drawRect((int) (objectModel.getHitbox().x), (int) (objectModel.getHitbox().y),
                (int) objectModel.getHitbox().width,
                (int) objectModel.getHitbox().height);
    }
    */

    /**
     * Riproduce un suono di raccolta se non è già stato riprodotto.
     *
     * @param path Il percorso del suono da riprodurre.
     */
    protected void playPickupSound(String path) {
        if (playedPickupSound)
            return;

        AudioManager.getInstance().oneTimePlay(path);
        playedPickupSound = true;
    }

    /**
     * Aggiorna il tick dell'animazione per gestire il cambio dell'animazione.
     */
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount()) {
                resetAniTick();
            }
        }
    }

    /**
     * Ripristina il tick e l'indice dell'animazione.
     */
    protected void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    /**
     * Restituisce il numero di sprite associati all'oggetto.
     * Questo metodo deve essere implementato dalle classi derivate.
     *
     * @return Il numero di sprite.
     */
    protected abstract int getSpriteAmount();
}

