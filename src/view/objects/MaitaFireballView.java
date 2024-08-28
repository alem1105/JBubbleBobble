package view.objects;

import model.objects.MaitaFireballModel;
import view.utilz.LoadSave;

public class MaitaFireballView extends ProjectileView<MaitaFireballModel> {

    private int exploding = 0;

    public MaitaFireballView(MaitaFireballModel objectModel) {
        super(objectModel);
        spriteIndex = 0;
        sprites = LoadSave.loadAnimations(LoadSave.MAITA_FIREBALL, 2, 3, 18, 18);
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
