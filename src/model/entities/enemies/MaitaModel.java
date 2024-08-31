package model.entities.enemies;

import model.entities.PlayerModel;
import model.objects.MaitaFireballModel;
import model.objects.ProjectileManagerModel;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.*;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.GameConstants.TILES_SIZE;

public class MaitaModel extends EnemyModel{

    public MaitaModel(float x, float y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE;
        initHitbox(14, 16);
        walkSpeed = 0.5f * SCALE;
    }

    @Override
    public void update() {
        if (enemyState == RUNNING || enemyState == RUNNING_ANGRY){
            if(still) {
                checKFireballShooting();
            }
            else {
                if(isMaitaOnPlayerY() && !PlayerModel.getInstance().isInvincible())
                    startShootingTimer();
                updatePos();
            }
        }
        updateEnemyState();
    }

    private void checKFireballShooting() {
        startStillTimer();
        if(!shot && !inAir) {
            if (getWalkDir() == RIGHT) {
                // TODO capire se si può togliere sto 18 * SCALE
                ProjectileManagerModel.getInstance().addProjectile(new MaitaFireballModel(hitbox.x + hitbox.width, hitbox.y - (Math.abs(hitbox.height - 18 * SCALE)), walkDir));
            } else {
                ProjectileManagerModel.getInstance().addProjectile(new MaitaFireballModel(hitbox.x - hitbox.width, hitbox.y - (Math.abs(hitbox.height - 18 * SCALE)), walkDir));
            }
            shot = true;
        }
    }


    private boolean isMaitaOnPlayerY() {
        return (int) (hitbox.y / TILES_SIZE) == (int) (PlayerModel.getInstance().getHitbox().y / TILES_SIZE);
    }

}
