package view.objects.bobbles;


import model.objects.bobbles.WaterModel;
import view.objects.CustomObjectView;
import view.utilz.LoadSave;

import java.awt.*;

import static model.utilz.Constants.CustomObjects.WATER_FALLING;
import static model.utilz.Constants.CustomObjects.WATER_WALKING;
import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;

public class WaterView extends CustomObjectView<WaterModel> {

    private int waterState;
    private int flipW = 1, flipX = 0;
    private int index;

    public WaterView(WaterModel objectModel) {
        super(objectModel);
        sprites = LoadSave.loadAnimations(LoadSave.WATER_SPRITE, 5, 1, 8, 8);
        aniIndex = 0;
    }

    public void update() {
        updateWaterState();
        setSpriteIndex();
        updateSpriteBasedOnDirection();
    }

    private void updateWaterState() {
        waterState = (objectModel.isInAir()) ? WATER_FALLING : WATER_WALKING;
    }

    private void setSpriteIndex() {
        spriteIndex = (waterState == WATER_FALLING) ? 1 : 0;
        // se è l'ultimo "cubetto" o il primo ha un'immagine diversa, quindi aggiorniamo lo spriteIndex
        if(index == 1 || index == 10) {
            if(waterState == WATER_FALLING) {
                if(index == 1)
                    spriteIndex = 4;
                else
                    spriteIndex = 3;
            } else {
                spriteIndex = 2;
            }
        }
    }

    @Override
    protected int getSpriteAmount() {
        return 1;
    }

    private void updateSpriteBasedOnDirection() {
        if(index == 0)
            return;

        if ((objectModel.getDirection() == RIGHT && index == 10) || (objectModel.getDirection() == LEFT && index == 1)) {
            flipX = objectModel.getWidth();
            flipW = -1;
        } else {
            flipX = 0;
            flipW = 1;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(sprites[spriteIndex][0],
                (int) (objectModel.getHitbox().x) + flipX, (int) (objectModel.getHitbox().y),
                objectModel.getWidth() * flipW, objectModel.getHeight(), null);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
