package view.objects.projectiles;

import model.objects.projectiles.DrunkBottleModel;

import java.awt.image.BufferedImage;

/**
 * Classe che indica la vista del proiettile di Drunk
 */
public class DrunkBottleView extends ProjectileView<DrunkBottleModel> {

    public DrunkBottleView(DrunkBottleModel objectModel, BufferedImage[][] sprites) {
        super(objectModel);
        spriteIndex = 0;
        this.sprites = sprites;
    }

    @Override
    protected int getSpriteAmount() {
        return 4;
    }

    @Override
    protected void update(){
        super.update();
    }

}