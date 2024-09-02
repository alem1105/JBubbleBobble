package view.objects.projectiles;

import model.objects.projectiles.InvaderLaserModel;
import view.utilz.LoadSave;

public class InvaderLaserView extends ProjectileView<InvaderLaserModel>{

    private final int endingTimer = 120;
    private int endingTick = endingTimer;

    public InvaderLaserView(InvaderLaserModel objectModel) {
        super(objectModel);
        spriteIndex = 0;
        sprites = LoadSave.loadAnimations(LoadSave.INVADER_LASER, 2, 2, 18, 18);
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
            System.out.println(objectModel.isActive());
            spriteIndex = 1;}
        else
            spriteIndex = 0;
    }


}
