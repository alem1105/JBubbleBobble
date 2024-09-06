package view.objects.bobbles;

import model.objects.bobbles.FireModel;
import view.objects.CustomObjectView;
import view.utilz.LoadSave;

import java.awt.*;

import static model.utilz.Constants.GameConstants.SCALE;
import static view.utilz.LoadSave.loadAnimations;

public class FireView extends CustomObjectView<FireModel> {

    private int xDrawOffset = 4, yDrawOffset = 2;
    private int disappearingTimer = 120, disappearingTick = 0;
    public FireView(FireModel objectModel) {
        super(objectModel);
        sprites = loadAnimations(LoadSave.FIRE_SPRITE, 2, 3, 16, 16);
        spriteIndex = 0;
    }

    public void update(){
        setSpriteIndex();
        updateAnimationTick();
        if (!objectModel.isActive())
            disappearingTick ++;
    }

    public boolean canDrawFire(){
        return disappearingTick <= disappearingTimer;
    }

    private void setSpriteIndex() {
        if(!objectModel.isActive())
            spriteIndex = 1;
    }

    @Override
    protected int getSpriteAmount() {
        if (!objectModel.isActive())
            return 3;
        else
            return 2;
    }

    @Override
    public void draw(Graphics g){
        g.drawImage(sprites[spriteIndex][aniIndex],
                (int) objectModel.getX() - xDrawOffset,
                (int) objectModel.getY() - yDrawOffset,
                (int) (16 * SCALE),
                (int) (16 * SCALE),
                null);
    }

}
