package view.stateview;

import java.util.Random;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.SCALE;

/**
 * Rappresenta una stella nel menu del gioco. La classe gestisce
 * la posizione e l'animazione della stella
 */
public class TwinkleView {

    private float x, y; // Posizione della stella
    private int aniIndex, aniTick; // Indici per l'animazione
    private float ySpeed = 0.1f * SCALE; // Velocità di movimento verticale
    private float xSpeed = 0.2f * SCALE; // Velocità di movimento orizzontale
    private Random random;

    /**
     * Costruttore per inizializzare una nuova stella.
     *
     * @param x        La posizione orizzontale iniziale della stella.
     * @param y        La posizione verticale iniziale della stella.
     * @param direction La direzione in cui la stella si muove.
     */
    public TwinkleView(float x, float y, int direction) {
        this.x = x;
        this.y = y;
        if (direction == LEFT)
            xSpeed = -xSpeed;
        else if (direction == UP)
            xSpeed = 0;

        random = new Random();
        aniIndex = random.nextInt(4);
    }

    /**
     * Aggiorna la posizione e l'animazione della stella.
     */
    public void update() {
        updatePos();
        updateAnimation();
    }

    /**
     * Aggiorna l'animazione della stella, cambiando il frame dell'animazione
     * in base al tempo.
     */
    private void updateAnimation() {
        aniTick++;
        if (aniTick > ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 4)
                aniIndex = 0; // Resetta l'indice se supera il numero di frames
        }
    }

    /**
     * Aggiorna la posizione della stella. Gestisce il movimento orizzontale
     * e verticale della stella.
     */
    private void updatePos() {
        if (!MenuView.getInstance().getLogoFalling()) {
            if (x > GAME_WIDTH + 10)
                x = -5;
            else if (x < -10)
                x = GAME_WIDTH;

            x += xSpeed;
        }

        if (y <= -10)
            y = GAME_HEIGHT;

        y -= ySpeed;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
