package view.objects.projectiles;

import model.objects.projectiles.DrunkBottleModel;
import view.utilz.LoadSave;

public class DrunkBottleView extends ProjectileView<DrunkBottleModel> {

    public DrunkBottleView(DrunkBottleModel objectModel) {
        super(objectModel);
        spriteIndex = 0;
        sprites = LoadSave.loadAnimations(LoadSave.DRUNK_BOTTLE, 1, 4, 18, 18);
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