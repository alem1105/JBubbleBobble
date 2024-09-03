package model.objects.bobbles;

import model.objects.CustomObjectModel;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;

public class LightningModel extends CustomObjectModel {

    private float speed = 2f * SCALE;

    public LightningModel(float x, float y, int width, int height, int direction) {
        super(x, y, width, height);
        if (direction == RIGHT)
            speed = -speed;
    }

    @Override
    public void update() {
        updatePos();
    }

    private void updatePos() {
        if (checkInBorder())
            hitbox.x += speed;
        else
            active = false;
    }

    private boolean checkInBorder() {
        int currentX = (int) (hitbox.x / TILES_SIZE);
        return 0 < currentX && currentX <= TILES_IN_WIDTH ;
    }

}
