package view.objects;

import model.objects.CustomObjectModel;
import view.utilz.AudioManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import static model.utilz.Constants.GameConstants.ANI_SPEED;
import static view.utilz.AudioManager.ITEM_PICKUP;

public abstract class CustomObjectView<T extends CustomObjectModel> {

    protected T objectModel;
    protected int spriteIndex, aniIndex, aniTick;
    protected BufferedImage[][] sprites;

    protected boolean playedPickupSound;

    public CustomObjectView(T objectModel) {
        this.objectModel = objectModel;
    }

    public void draw(Graphics g){
        g.drawImage(sprites[spriteIndex][aniIndex],
                (int) objectModel.getX(),
                (int) objectModel.getY(),
                objectModel.getWidth(),
                objectModel.getHeight(),
                null);
    }

    public void drawHitbox(Graphics g){
        g.setColor(Color.PINK);
        g.drawRect((int) (objectModel.getHitbox().x), (int) (objectModel.getHitbox().y),
                (int) objectModel.getHitbox().width,
                (int) objectModel.getHitbox().height);
    }

    protected void playPickupSound(String path) {
        if(playedPickupSound)
            return;

        AudioManager.getInstance().oneTimePlay(path);
        playedPickupSound = true;
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount()) {
                resetAniTick();
            }
        }
    }

    protected void resetAniTick(){
        aniTick= 0;
        aniIndex = 0;
    }

    protected abstract int getSpriteAmount();

}
