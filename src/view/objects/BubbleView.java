package view.objects;

import model.objects.BubbleModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import static model.utilz.Constants.CustomObjects.*;

import static model.utilz.Constants.CustomObjects.BUBBLE_SPAWNED;
import static model.utilz.Constants.GameConstants.ANI_SPEED;

public class BubbleView<T extends BubbleModel> extends CustomObjectView {

    protected BufferedImage[][] sprites;
    protected T bubbleModel;
    protected int spriteIndex, aniIndex, aniTick;
    protected boolean exploding = false;
    protected int bubbleState = BUBBLE_SPAWNED;

    public BubbleView(T model) {
        this.bubbleModel = model;
    }

    public void draw(Graphics g){
        g.drawImage(sprites[spriteIndex][aniIndex],
                (int) bubbleModel.getX(),
                (int) bubbleModel.getY(),
                bubbleModel.getWidth(),
                bubbleModel.getHeight(),
                null);
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount()) {
                aniIndex = 0;
            }
        }
    }

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

    protected void resetAniTick(){
        aniTick= 0;
        aniIndex = 0;
    }
}
