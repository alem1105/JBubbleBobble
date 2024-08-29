package view.objects.bobbles;

import model.objects.bobbles.BubbleModel;
import view.objects.CustomObjectView;

import java.awt.image.BufferedImage;

import static model.utilz.Constants.CustomObjects.*;

import static model.utilz.Constants.CustomObjects.BUBBLE_SPAWNED;
import static model.utilz.Constants.GameConstants.*;

public class BubbleView<T extends BubbleModel> extends CustomObjectView<T> {
    protected BufferedImage sprite;

    protected int bubbleState = BUBBLE_SPAWNED;

    public BubbleView(T model) {
        super(model);
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

    @Override
    protected int getSpriteAmount(){
        switch (bubbleState){
            case BUBBLE_SPAWNED -> {
                return 1;
            }
            case BUBBLE_EXPLODING -> {
                return 2;
            }
        }
        return 1;
    }

    protected void setSpriteIndex(){
        switch (bubbleState){
            case BUBBLE_SPAWNED-> {
                spriteIndex = 0;
            }
            case BUBBLE_EXPLODING -> {
                spriteIndex = 1;
            }
        }
    }


    public BubbleModel getBubbleModel() {
        return objectModel;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getAniTick() {
        return aniTick;
    }

    public BufferedImage getSprite() {
        return sprite;
    }
}
