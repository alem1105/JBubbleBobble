package view.objects.bobbles;

import model.objects.bobbles.BubbleModel;
import view.objects.CustomObjectView;
import view.utilz.LoadSave;

import static model.utilz.Constants.CustomObjects.*;

import static model.utilz.Constants.CustomObjects.BUBBLE_SPAWNED;
import static model.utilz.Constants.GameConstants.*;

public class BubbleView<T extends BubbleModel> extends CustomObjectView<T> {

    protected int bubbleState = BUBBLE_SPAWNED;

    public BubbleView(T model) {
        super(model);
        switch (model.getBubbleType()) {
            case 3 -> sprites = LoadSave.loadAnimations(LoadSave.BOB_BUBBLE_SPRITE,3, 3, 16, 16);
            case 0 -> sprites = LoadSave.loadAnimations(LoadSave.SPECIAL_BUBBLE_SPRITE,4, 2, 16, 16);
        }
    }

    public void update() {
        updateBubbleState();
        setSpriteIndex();
        updateAnimationTick();
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
                    aniIndex = 2;
                }
            }
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
            spriteIndex = 3;
            return;
        }
        spriteIndex = objectModel.getBubbleType();
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

}
