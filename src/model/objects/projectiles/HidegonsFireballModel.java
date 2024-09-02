package model.objects.projectiles;

import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Gravity.CanMoveHere;
import static model.utilz.UtilityMethods.getLvlData;

public class HidegonsFireballModel extends ProjectileModel{

    private float fireballSpeed = 1.5f * SCALE;

    public HidegonsFireballModel(float x, float y,int direction) {
        super(x, y, (int) (18 * SCALE), (int)(13* SCALE), direction);
        checkDirection();
    }

    private void checkDirection() {
        if(direction == LEFT)
            fireballSpeed = -fireballSpeed;
    }

    @Override
    public void update(){
        updatePos();
    }

    private void updatePos() {
        if(CanMoveHere(hitbox.x + fireballSpeed, hitbox.y, hitbox.width, hitbox.height, getLvlData())) {
            hitbox.x += fireballSpeed;
        } else {
            active = false;
        }
    }
}
