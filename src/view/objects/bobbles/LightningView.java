package view.objects.bobbles;

import model.objects.bobbles.LightningModel;
import view.objects.CustomObjectView;
import view.utilz.LoadSave;

public class LightningView extends CustomObjectView<LightningModel> {

    public LightningView(LightningModel objectModel) {
        super(objectModel);
        sprites = LoadSave.loadAnimations(LoadSave.LIGHTNING_SPRITE, 1, 1, 16, 16);
        aniIndex = 0;
    }

    @Override
    protected int getSpriteAmount() {
        return 1;
    }

    public LightningModel getObjectModel() {
        return objectModel;
    }
}
