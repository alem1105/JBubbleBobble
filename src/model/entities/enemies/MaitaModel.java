package model.entities.enemies;

import model.entities.PlayerModel;
import model.objects.MaitaFireballModel;
import model.objects.ProjectileManagerModel;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.*;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.GameConstants.TILES_SIZE;

public class MaitaModel extends EnemyModel{
    protected int shootingTimer = 360;
    protected int shootingTick = 0;

    private boolean still = false;
    private int stillTimer = 60;
    private int stillTick = 0;

    public MaitaModel(float x, float y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE;
        initHitbox(14, 16);
    }

    public void update() {
        if (enemyState == RUNNING || enemyState == RUNNING_ANGRY){
            if(still)
                checkShooting();
            else
                updatePos();
            updateFireball();
        }
    }

    private void checkShooting() {
        if (stillTick >= stillTimer)  {
            stillTick = 0;
            still = false;
        }
        stillTick++;
    }

    protected void updateFireball() {
        if(isMaitaOnPlayerY()) {

            if(shootingTick >= shootingTimer) {
                still = true;
                if(getWalkDir() == RIGHT) {
                    ProjectileManagerModel.getInstance().addProjectile(new MaitaFireballModel(hitbox.x + hitbox.width, hitbox.y, walkDir));
                } else {
                    ProjectileManagerModel.getInstance().addProjectile(new MaitaFireballModel(hitbox.x, hitbox.y, walkDir));
                }

                shootingTick = 0;
            }
            shootingTick++;
        }
    }

    private boolean isMaitaOnPlayerY() {
        return (int) (hitbox.y / TILES_SIZE) == (int) (PlayerModel.getInstance().getHitbox().y / TILES_SIZE);
    }

}
