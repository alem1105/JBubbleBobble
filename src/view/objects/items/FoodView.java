package view.objects.items;

import model.objects.items.FoodModel;
import view.objects.CustomObjectView;
import view.utilz.LoadSave;

import static model.utilz.Constants.Fruit.*;
import static model.utilz.Constants.GameConstants.SCALE;
import static view.utilz.AudioManager.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Rappresenta la visualizzazione del cibo nel gioco.
 * Questa classe gestisce l'animazione e la logica di visualizzazione del cibo,
 * incluso il punteggio associato al cibo raccolto.
 */
public class FoodView extends CustomObjectView<FoodModel> {

    // Durata del punteggio visualizzato e contatore dei tick
    private int scoreDuration = 40, scoreTick = 0;

    /**
     * Costruttore per la classe FoodView.
     *
     * @param model Il modello del cibo associato a questa vista.
     */
    public FoodView(FoodModel model) {
        super(model);
        aniIndex = 0;
        sprites = LoadSave.loadAnimations(LoadSave.FOOD_SPRITE, 109, 1, 18, 18);
        setSpriteIndex();
    }

    /**
     * Imposta l'indice della sprite in base al tipo di cibo.
     */
    private void setSpriteIndex() {
        switch (objectModel.getFoodIndex()) {
            case ORANGE ->  spriteIndex = 0;
            case PEPPER -> spriteIndex = 1;
            case GRAPE -> spriteIndex = 2;
            case TOMATO -> spriteIndex = 3;
            case CHERRY -> spriteIndex = 4;
        }
    }

    /**
     * Restituisce il numero di sprite da visualizzare.
     *
     * @return sempre 0, poiché non ci sono animazioni da visualizzare per il cibo.
     */
    @Override
    protected int getSpriteAmount() {
        return 0;
    }

    /**
     * Aggiorna la logica del punteggio visualizzato.
     */
    public void update() {
        if (scoreDuration >= scoreTick) {
            if (!objectModel.isActive()) {
                scoreTick++;
            }
        }
    }

    /**
     * Disegna la vista del cibo.
     *
     * @param g
     */
    @Override
    public void draw(Graphics g) {
        if (objectModel.isActive()) {
            super.draw(g);
        } else {
            playPickupSound(FOOD_PICKUP);
            drawScoreAmount(g);
        }
    }

    /**
     * Disegna la quantità di punteggio associata al cibo raccolto.
     *
     * @param g
     */
    private void drawScoreAmount(Graphics g) {
        g.setColor(Color.GREEN);
        Font font = (LoadSave.NES_FONT).deriveFont(8 * SCALE);
        g.setFont(font);
        g.drawString(String.valueOf(objectModel.getGivenScoreAmount()), (int) objectModel.getX(), (int) objectModel.getY());
    }

    public int getScoreDuration() {
        return scoreDuration;
    }

    public int getScoreTick() {
        return scoreTick;
    }
}
