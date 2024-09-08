package view.objects.projectiles;

import model.objects.projectiles.InvaderLaserModel;

import java.awt.image.BufferedImage;

public class InvaderLaserView extends ProjectileView<InvaderLaserModel>{

    private final int endingTimer = 120;
    private int endingTick = endingTimer;

    public InvaderLaserView(InvaderLaserModel objectModel, BufferedImage[][] sprites) {
        super(objectModel);
        spriteIndex = 0;
        this.sprites = sprites;
    }

    @Override
    protected void update(){
        super.update();
        setSprite();
        if (!objectModel.isActive()) {
            endingTimer();
        }
    }

    private void endingTimer() {
        if (endingTick == endingTimer)
            resetAniTick();
        endingTick--;
    }

    @Override
    public boolean conditionToDraw(){
        return (objectModel.isActive() || endingTick != 0);
    }

    @Override
    protected int getSpriteAmount() {
        return 2;
    }

    private void setSprite() {
        if (!objectModel.isActive()){
            spriteIndex = 1;}
        else
            spriteIndex = 0;
    }


}
