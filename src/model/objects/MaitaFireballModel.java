package model.objects;

import model.entities.PlayerModel;
import model.entities.enemies.MaitaModel;

import static model.utilz.Constants.CustomObjects.*;
import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.GameConstants.TILES_SIZE;
import static model.utilz.Gravity.CanMoveHere;
import static model.utilz.Gravity.GetEntityXPosNextToWall;
import static model.utilz.UtilityMethods.getLvlData;

public class MaitaFireballModel extends ProjectileModel{

    private float fireballSpeed = 0.5f * SCALE;

    public MaitaFireballModel(float x, float y,int direction) {
        super(x, y, (int) (18 * SCALE), (int)(18* SCALE), direction);
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
            System.out.println("mi rompo");
            active = false;
        }
    }



}
