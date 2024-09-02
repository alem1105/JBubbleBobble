package model.objects.projectiles;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.*;

public class InvaderLaserModel extends ProjectileModel {

    private float laserSpeed = 0.5f * SCALE;

    public InvaderLaserModel(float x, float y) {
        super(x, y, (int) (18 * SCALE), (int)(18* SCALE), RIGHT);
    }

    @Override
    public void update() {
        System.out.println(active);
        if (!AtTheLastTile())
            hitbox.y += laserSpeed;
        else
            active = false;
    }

    private boolean AtTheLastTile() {
        return (hitbox.y + hitbox.height) >= GAME_HEIGHT - TILES_SIZE; // TODO controllare se fa difetto
    }


}
