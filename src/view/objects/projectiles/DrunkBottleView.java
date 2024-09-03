package view.objects.projectiles;

import model.objects.projectiles.DrunkBottleModel;
import view.utilz.LoadSave;

import java.awt.image.BufferedImage;

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