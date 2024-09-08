package view.objects.bobbles;

import model.entities.PlayerModel;
import model.objects.bobbles.BobBubbleModel;

import java.awt.image.BufferedImage;

import static model.utilz.Constants.CustomObjects.*;

import java.awt.Graphics;

/**
 * Rappresenta la visualizzazione di una bolla del giocatore nel gioco.
 * Questa classe estende BubbleView e gestisce le animazioni e lo stato della bolla.
 */
public class BobBubbleView extends BubbleView<BobBubbleModel> {

    private BufferedImage[][] lightningBobBubble;

    /**
     * Costruttore per la classe BobBubbleView.
     *
     * @param model Il modello della bolla da visualizzare.
     */
    public BobBubbleView(BobBubbleModel model) {
        super(model);
        bubbleState = BUBBLE_SPAWNING;
        lightningBobBubble = BubbleManagerView.getInstance().getLightningBobBubble();
    }

    /**
     * Restituisce il numero di sprite da utilizzare in base allo stato attuale della bolla.
     *
     * @return Il numero di sprite disponibili per lo stato attuale della bolla.
     */
    @Override
    protected int getSpriteAmount() {
        switch (bubbleState) {
            case BUBBLE_SPAWNING -> {
                return 3;
            }
            case BUBBLE_EXPLODING -> {
                return 2;
            }
            case BUBBLE_SPAWNED -> {
                return 1;
            }
        }
        return 1;
    }

    /**
     * Imposta l'indice dello sprite da utilizzare in base allo stato attuale della bolla.
     */
    @Override
    protected void setSpriteIndex() {
        switch (bubbleState) {
            case BUBBLE_SPAWNING -> {
                spriteIndex = 0;
            }
            case BUBBLE_SPAWNED -> {
                spriteIndex = 1;
            }
            case BUBBLE_EXPLODING -> {
                spriteIndex = 2;
            }
        }
    }

    /**
     * Restituisce le immagini della bolla da disegnare in base allo stato attuale
     * e' attivo il potenziamento delle bolle di fulmine.
     *
     * @return La matrice di immagini da disegnare.
     */
    private BufferedImage[][] typeOfBubbleToDraw() {
        if (bubbleState == BUBBLE_SPAWNED && PlayerModel.getInstance().isShootingLightningBubble())
            return lightningBobBubble;
        return sprites;
    }

    /**
     * Disegna la bolla.
     *
     * @param g .
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(typeOfBubbleToDraw()[spriteIndex][aniIndex],
                (int) objectModel.getX(),
                (int) objectModel.getY(),
                objectModel.getWidth(),
                objectModel.getHeight(),
                null);
    }
}

