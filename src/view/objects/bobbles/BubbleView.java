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

public class BubbleView<T extends BubbleModel> extends CustomObjectView<T> {

    protected int bubbleState = BUBBLE_SPAWNED;
    protected ArrayList<WaterView> waterfallView;

    public BubbleView(T model) {
        super(model);
        setSprites();
        waterfallView = new ArrayList<>();
    }

    private void setSprites(){
        switch (objectModel.getBubbleType()) {
            case BOB_BUBBLE ->
                sprites = LoadSave.loadAnimations(LoadSave.BOB_BUBBLE_SPRITE,3, 3, 16, 16);
            case EXTEND_BUBBLE ->
                    sprites = LoadSave.loadAnimations(LoadSave.EXTEND_SPRITE,7, 2, 16, 16);
            case WATER_BUBBLE, LIGHTNING_BUBBLE, FIRE_BUBBLE ->
                    sprites = LoadSave.loadAnimations(LoadSave.SPECIAL_BUBBLE_SPRITE,4, 2, 16, 16);
        }
    }

    public void update() {
        updateBubbleState();
        setSpriteIndex();
        updateAnimationTick();
    }

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

    @Override
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount()) {
                aniIndex = 0;
                if (bubbleState == BUBBLE_EXPLODING){
                    checkBubbleSound();
                    aniIndex = 2; // uso questo index per riconoscere il fatto che è finita l'animazione dell'esplosione
                }
            }
        }
    }

    private void checkBubbleSound() {
        switch (objectModel.getBubbleType()) {
            case EXTEND_BUBBLE -> playPickupSound(LETTER_BUBBLE_POP);
            case WATER_BUBBLE -> playPickupSound(WATER_FLOW);
            default -> playPickupSound(POP_BUBBLE_SINGLE_ENEMY);
        }
    }

    protected void updateBubbleState(){
        int currentState = bubbleState;

        if (!objectModel.isActive()) {
            bubbleState = BUBBLE_EXPLODING;
        }
        else if (bubbleState == BUBBLE_SPAWNING && aniIndex == 2){
            bubbleState = BUBBLE_SPAWNED;
        }

        if (bubbleState != currentState)
            resetAniTick();
    }

    @Override
    protected int getSpriteAmount(){
        return switch(bubbleState) {
            case BUBBLE_EXPLODING -> 2;
            default -> 1;
        };
    }

    protected void setSpriteIndex(){
        if (bubbleState == BUBBLE_EXPLODING){
            if (objectModel.getBubbleType() == EXTEND_BUBBLE)
                spriteIndex = 6;
            else spriteIndex = 3;
            return;
        }

        if(objectModel.getBubbleType() != EXTEND_BUBBLE)
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

    public int getAniTick() {
        return aniTick;
    }

    public ArrayList<WaterView> getWaterfallView() {
        return waterfallView;
    }
}
