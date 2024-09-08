package view.objects.bobbles;

import model.objects.bobbles.BubbleModel;
import view.objects.CustomObjectView;
import view.utilz.LoadSave;

import java.util.ArrayList;

import static model.utilz.Constants.CustomObjects.*;

import static model.utilz.Constants.CustomObjects.BUBBLE_SPAWNED;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.SpecialBubbles.*;
import static view.utilz.AudioManager.*;

/**
 * Rappresenta la visualizzazione di una bolla nel gioco.
 * Questa classe gestisce lo stato della bolla e la sua animazione.
 *
 * @param <T> Il tipo del modello della bolla, che estende BubbleModel.
 */
public class BubbleView<T extends BubbleModel> extends CustomObjectView<T> {

    protected int bubbleState = BUBBLE_SPAWNED;
    protected ArrayList<WaterView> waterfallView;

    /**
     * Costruttore per la classe BubbleView.
     *
     * @param model Il modello della bolla associato a questa vista.
     */
    public BubbleView(T model) {
        super(model);
        setSprites();
        waterfallView = new ArrayList<>();
    }

    /**
     * Imposta le sprite in base al tipo di bolla.
     */
    private void setSprites() {
        switch (objectModel.getBubbleType()) {
            case BOB_BUBBLE ->
                    sprites = LoadSave.loadAnimations(LoadSave.BOB_BUBBLE_SPRITE, 3, 3, 16, 16);
            case EXTEND_BUBBLE ->
                    sprites = LoadSave.loadAnimations(LoadSave.EXTEND_SPRITE, 7, 2, 16, 16);
            case WATER_BUBBLE, LIGHTNING_BUBBLE, FIRE_BUBBLE ->
                    sprites = LoadSave.loadAnimations(LoadSave.SPECIAL_BUBBLE_SPRITE, 4, 2, 16, 16);
        }
    }

    /**
     * Aggiorna lo stato della bolla e le sue animazioni.
     */
    public void update() {
        updateBubbleState();
        setSpriteIndex();
        updateAnimationTick();
    }

    /**
     * Recupera il modello dell'acqua e lo aggiunge alla lista di visualizzazione
     */
    public void getWaterfallModelArray() {
        int modelLength = objectModel.getWaterfall().size();
        int i = waterfallView.size();
        if (i > modelLength) {
            waterfallView.clear();
            i = 0;
        }
        while (modelLength > waterfallView.size()) {
            waterfallView.add(new WaterView(objectModel.getWaterfall().get(i)));
            i++;
        }
    }

    /**
     * Aggiorna l'indice del frame dell'animazione da mostrare
     */
    @Override
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount()) {
                aniIndex = 0;
                if (bubbleState == BUBBLE_EXPLODING) {
                    checkBubbleSound();
                    aniIndex = 2; // uso questo index per riconoscere il fatto che è finita l'animazione dell'esplosione
                }
            }
        }
    }

    /**
     * Controlla e riproduce il suono associato alla bolla.
     */
    private void checkBubbleSound() {
        switch (objectModel.getBubbleType()) {
            case EXTEND_BUBBLE -> playPickupSound(LETTER_BUBBLE_POP);
            case WATER_BUBBLE -> playPickupSound(WATER_FLOW);
            default -> playPickupSound(POP_BUBBLE_SINGLE_ENEMY);
        }
    }

    /**
     * Aggiorna lo stato della bolla in base alla sua attività.
     * Cambia lo stato in esplosione se la bolla non è attiva.
     */
    protected void updateBubbleState() {
        int currentState = bubbleState;

        if (!objectModel.isActive()) {
            bubbleState = BUBBLE_EXPLODING;
        } else if (bubbleState == BUBBLE_SPAWNING && aniIndex == 2) {
            bubbleState = BUBBLE_SPAWNED;
        }

        if (bubbleState != currentState)
            resetAniTick();
    }

    /**
     * @return il numero massimo di sprite in base allo stato della bolla come intero
     */
    @Override
    protected int getSpriteAmount() {
        return switch (bubbleState) {
            case BUBBLE_EXPLODING -> 2;
            default -> 1;
        };
    }

    /**
     * Imposta l'indice (riga) della sprite da visualizzare in base allo stato e al tipo della bolla.
     */
    protected void setSpriteIndex() {
        if (bubbleState == BUBBLE_EXPLODING) {
            if (objectModel.getBubbleType() == EXTEND_BUBBLE)
                spriteIndex = 6;
            else spriteIndex = 3;
            return;
        }

        if (objectModel.getBubbleType() != EXTEND_BUBBLE)
            spriteIndex = objectModel.getBubbleType();
        else
            spriteIndex = "Extend".indexOf(objectModel.getExtendChar());
    }

    public BubbleModel getModel() {
        return objectModel;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public ArrayList<WaterView> getWaterfallView() {
        return waterfallView;
    }
}
