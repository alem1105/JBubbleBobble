package view.objects.bobbles;

import model.entities.PlayerModel;
import model.objects.bobbles.BobBubbleModel;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.CustomObjects.*;

public class BobBubbleView extends BubbleView<BobBubbleModel>{

    private BufferedImage[][] lightningBobBubble;

    public BobBubbleView(BobBubbleModel model) {
        super(model);
        bubbleState = BUBBLE_SPAWNING;
        lightningBobBubble = LoadSave.loadAnimations(LoadSave.SPECIAL_BUBBLE_SPRITE, 4, 2, 16, 16);
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

    private BufferedImage[][] typeOfBubbleToDraw() {
        if(bubbleState == BUBBLE_SPAWNED && PlayerModel.getInstance().isShootingLightningBubble())
            return lightningBobBubble;
        return sprites;
    }

    @Override
    public void draw(Graphics g){
        g.drawImage(typeOfBubbleToDraw()[spriteIndex][aniIndex],
                (int) objectModel.getX(),
                (int) objectModel.getY(),
                objectModel.getWidth(),
                objectModel.getHeight(),
                null);
    }
}
