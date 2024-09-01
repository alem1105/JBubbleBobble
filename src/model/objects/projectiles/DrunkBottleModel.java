package model.objects.projectiles;

import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.GameConstants.TILES_SIZE;
import static model.utilz.Gravity.CanMoveHere;
import static model.utilz.UtilityMethods.getLvlData;

public class DrunkBottleModel extends ProjectileModel{

    private float bottleSpeed = 0.5f * SCALE;
    private float startX;
    private int dirChanged;

    public DrunkBottleModel(float x, float y,int direction) {
        super(x, y, (int) (18 * SCALE), (int)(18* SCALE), direction);
        startX = x;
        dirChanged = 0;
        checkDirection();
    }

    private void checkDirection() {
        if(direction == LEFT)
            bottleSpeed = -bottleSpeed;
    }

    @Override
    public void update(){
        updatePos();
    }

    private void updatePos() {

        // Controlla se scoppiare, un po' di scarto per sicurezza
        if (hitbox.x >= startX - (2 * SCALE) && hitbox.x <= startX + (2 * SCALE) && dirChanged > 0) {
            active = false;
        }

        // Controlla se invertire direzione
        if (Math.abs(hitbox.x - startX) >= TILES_SIZE * 4) {
            dirChanged++;
            bottleSpeed = -bottleSpeed;
        }

        // Controlla se puoi muoverti (passa attraverso i muri?)
        if(CanMoveHere(hitbox.x + bottleSpeed, hitbox.y, hitbox.width, hitbox.height, getLvlData())) {
            hitbox.x += bottleSpeed;
        } else {
            dirChanged++;
            bottleSpeed = -bottleSpeed;
        }

    }

}