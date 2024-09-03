package view.objects.projectiles;

import model.objects.projectiles.HidegonsFireballModel;
import model.objects.projectiles.MaitaFireballModel;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;

public class HidegonsFireballView extends ProjectileView<HidegonsFireballModel> {

    private int flipW = 1, flipX = 0;

    public HidegonsFireballView(HidegonsFireballModel objectModel, BufferedImage[][] sprites) {
        super(objectModel);
        spriteIndex = 0;
        aniIndex = 0;
        this.sprites = sprites;
    }

    @Override
    protected int getSpriteAmount() {
        return 3;
    }

    @Override
    protected void update(){
        updateDirections();
    }

    @Override
    public boolean conditionToDraw(){
        return objectModel.isActive();
    }

    private void updateDirections() {
        if (objectModel.getDirection() == RIGHT) {
            flipX = objectModel.getWidth();
            flipW = -1;
        }
        if (objectModel.getDirection() == LEFT) {
            flipX = 0;
            flipW = 1;
        }
    }

    @Override
    public void draw(Graphics g){
        g.drawImage(sprites[spriteIndex][aniIndex],
                (int) objectModel.getX() + flipX,
                (int) objectModel.getY(),
                objectModel.getWidth() * flipW,
                objectModel.getHeight(),
                null);
    }

}
