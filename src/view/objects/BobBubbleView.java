package view.objects;

import model.objects.BobBubbleModel;

import static model.utilz.Constants.CustomObjects.*;
import static view.utilz.LoadSave.*;

public class BobBubbleView extends BubbleView<BobBubbleModel>{

    public BobBubbleView(BobBubbleModel model) {
        super(model);
        sprites = loadAnimations(BOB_BUBBLE_SPRITE, 3, 3, 16, 16);
        bubbleState = BUBBLE_SPAWNING;
    }

    public void update() {
        updateBubbleState();
        setSpriteIndex();
        updateAnimationTick();
    }

    private void updateBubbleState(){
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
        switch (bubbleState){
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

    @Override
    protected void setSpriteIndex(){
        switch (bubbleState){
            case BUBBLE_SPAWNING -> {
                spriteIndex = 0;
            }
            case BUBBLE_SPAWNED-> {
                spriteIndex = 1;
            }
            case BUBBLE_EXPLODING -> {
                spriteIndex = 2;
            }
        }
    }
}
