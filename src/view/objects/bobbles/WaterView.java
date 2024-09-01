package view.objects.bobbles;


import model.objects.bobbles.WaterModel;
import view.objects.CustomObjectView;
import view.utilz.LoadSave;

import static model.utilz.Constants.CustomObjects.WATER_FALLING;
import static model.utilz.Constants.CustomObjects.WATER_WALKING;

public class WaterView extends CustomObjectView<WaterModel> {

    protected int waterState;

    public WaterView(WaterModel objectModel) {
        super(objectModel);
        sprites = LoadSave.loadAnimations(LoadSave.WATER_SPRITE, 2, 1, 8, 8);
        aniIndex = 0;
    }

    public void update() {
        updateWaterState();
        setSpriteIndex();
    }

    private void updateWaterState() {
        waterState = (objectModel.isInAir()) ? WATER_FALLING : WATER_WALKING;
    }

    private void setSpriteIndex() {
        spriteIndex = (waterState == WATER_FALLING) ? 1 : 0;
    }

    @Override
    protected int getSpriteAmount() {
        return 1;
    }
}
