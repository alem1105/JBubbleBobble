package view.entities.enemies;

import model.entities.enemies.EnemyModel;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * La classe astratta {@code EnemyView} rappresenta la visualizzazione di un nemico nel gioco.
 * Fornisce metodi per gestire l'animazione e la renderizzazione del nemico.
 *
 * @param <T> Il tipo di modello del nemico, che estende {@code EnemyModel}.
 */
public abstract class EnemyView<T extends EnemyModel> {

    /** righe presenti nello sprite del nemico. */
    protected static final int ROW_INDEX = 5;

    /** colonne presenti nello sprite del nemico. */
    protected static final int COL_INDEX = 6;

    /** L'offset di disegno sull'asse X. */
    protected int xDrawOffset;

    /** L'offset di disegno sull'asse Y. */
    protected int yDrawOffset;

    /** L'indice dell'animazione corrente. */
    protected int aniIndex;

    /** Il tick dell'animazione che determina quando passare all'indice dopo. */
    protected int aniTick;

    /** La velocità dell'animazione (in ticks). */
    protected int aniSpeed = 25;

    /** gli sprites del nemico. */
    protected BufferedImage[][] animations;

    /** Il modello del nemico associato. */
    protected T enemy;

    /** La larghezza per l'animazione in base al flip . */
    protected int flipW = 1;

    /** La posizione sull'asse X del nemico in base al flip. */
    protected int flipX;

    /** Contatore per l'esplosione della bolla del nemico. */
    protected int exploding = 0;

    /**
     * Costruisce una nuova istanza di {@code EnemyView} con il modello del nemico specificato.
     *
     * @param enemy Il modello del nemico da visualizzare.
     */
    public EnemyView(T enemy) {
        this.enemy = enemy;
    }

    /**
     * Aggiorna l'animazione del nemico.
     * Incrementa il tick dell'animazione e gestisce la logica per il cambiamento di stato dell'animazione.
     */
    public void update() {
        updateAnimationTick();
        flipX();
        flipW();
    }

    /**
     * Aggiorna il tick dell'animazione. Gestisce la logica per cambiare il frame dell'animazione.
     */
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            if (!enemy.isActive()) {
                exploding++;
            }
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount()) {
                aniIndex = 0;
                if (enemy.getEnemyState() == DEAD && exploding > 2) {
                    aniIndex = 2;
                }
            }
        }

        if (enemy.isResetAniTick()) {
            aniIndex = 0;
            aniTick = 0;
            enemy.setResetAniTick(false);
        }
    }

    /**
     * Imposta la quantità da aggiungere se bisogna effettuare il flip orizzontale in base alla direzione di camminata del nemico.
     */
    public void flipX() {
        if (enemy.getWalkDir() == RIGHT) {
            flipX = ZEN_CHAN_WIDTH;
        } else {
            flipX = 0;
        }
    }

    /**
     * Imposta il flip per l'animazione in base alla direzione di camminata del nemico :
     * se è negativo lo disegna flippato.
     */
    public void flipW() {
        if (enemy.getWalkDir() == RIGHT) {
            flipW = -1;
        } else {
            flipW = 1;
        }
    }

    /**
     * Restituisce il numero di sprite da visualizzare in base allo stato del nemico.
     *
     * @return Il numero di sprite da visualizzare.
     */
    public int getSpriteAmount() {
        return switch (enemy.getEnemyState()) {
            case DEAD -> 6;
            default -> 2;
        };
    }

    /**
     * Renderizza l'immagine del nemico sul grafico specificato.
     *
     * @param g L'oggetto {@code Graphics} su cui disegnare il nemico.
     */
    public void render(Graphics g) {
        g.drawImage(animations[enemy.getEnemyState()][aniIndex],
                (int) (enemy.getHitbox().x - xDrawOffset) + flipX,
                (int) (enemy.getHitbox().y - yDrawOffset),
                enemy.getWidth() * flipW,
                enemy.getHeight(),
                null);
    }

    public T getEnemy(){
        return enemy;
    }
}
