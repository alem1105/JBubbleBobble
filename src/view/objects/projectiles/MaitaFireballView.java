package view.objects.projectiles;

import model.objects.projectiles.MaitaFireballModel;

import java.awt.image.BufferedImage;

public class MaitaFireballView extends ProjectileView<MaitaFireballModel> {

    private int exploding = 0;

    public MaitaFireballView(MaitaFireballModel objectModel, BufferedImage[][] sprites) {
        super(objectModel);
        spriteIndex = 0;
        this.sprites = sprites;
    }

    @Override
    protected int getSpriteAmount() {
        return 3;
    }

    @Override
    protected void update(){
        super.update();
        updateAnimation();
    }

    @Override
    public boolean conditionToDraw(){
        return !(!objectModel.isActive() && exploding >= 2);
    }

    private void updateAnimation(){
        if (!objectModel.isActive()){
            spriteIndex = 1;
            if (!objectModel.isActive() && exploding == 0){
                resetAniTick();
            }
            exploding ++;
        }
    }

}
