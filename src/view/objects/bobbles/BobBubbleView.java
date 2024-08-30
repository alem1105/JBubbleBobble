package view.objects.bobbles;

import model.objects.bobbles.BobBubbleModel;

import static model.utilz.Constants.CustomObjects.*;
import static view.utilz.LoadSave.*;

public class BobBubbleView extends BubbleView<BobBubbleModel>{

    public BobBubbleView(BobBubbleModel model) {
        super(model);
        //sprites = loadAnimations(BOB_BUBBLE_SPRITE, 3, 3, 16, 16);
        bubbleState = BUBBLE_SPAWNING;
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

    @ Override
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
