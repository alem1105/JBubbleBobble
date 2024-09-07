package view.objects.bobbles;

import model.entities.PlayerModel;
import model.objects.bobbles.BobBubbleModel;
import view.utilz.LoadSave;

import static model.utilz.Constants.CustomObjects.*;

public class BobBubbleView extends BubbleView<BobBubbleModel>{

    public BobBubbleView(BobBubbleModel model) {
        super(model);
        if (PlayerModel.getInstance().isShootingLightningBubble())
            sprites = LoadSave.loadAnimations(LoadSave.SPECIAL_BUBBLE_SPRITE,4, 2, 16, 16);
        bubbleState = BUBBLE_SPAWNING;
    }


    @Override
    protected int getSpriteAmount(){
        if (PlayerModel.getInstance().isShootingLightningBubble() && !(bubbleState == BUBBLE_EXPLODING))
            return 1;

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
        if (PlayerModel.getInstance().isShootingLightningBubble()){
            spriteIndex = 1;
            if (bubbleState == BUBBLE_EXPLODING)
                spriteIndex = 3;
        return;
        }

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
