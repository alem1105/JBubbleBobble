package model.entities.enemies;

import model.entities.PlayerModel;
import model.objects.projectiles.HidegonsFireballModel;
import model.objects.projectiles.ProjectileManagerModel;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.RUNNING;
import static model.utilz.Constants.Enemies.RUNNING_ANGRY;
import static model.utilz.Constants.GameConstants.SCALE;

public class HidegonsModel extends EnemyModel{

    public HidegonsModel(float x, float y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.75f * SCALE;
        initHitbox(14, 16);
    }

    @Override
    public void update() {
        if (enemyState == RUNNING || enemyState == RUNNING_ANGRY){
            if(still) {
                checKFireballShooting();
            }
            else {
                if(isEnemyOnPlayerY() && !PlayerModel.getInstance().isInvincible())
                    startShootingTimer();
                updatePos();
            }
        }
        updateEnemyState();
    }

    private void checKFireballShooting() {
        startStillTimer();
        if(!shot && !inAir) {

            float offsetX = (getWalkDir() == RIGHT) ? hitbox.width : -hitbox.width;
            float projectileX = hitbox.x + offsetX;
            float projectileY = hitbox.y - Math.abs(hitbox.height - 18 * SCALE);

            ProjectileManagerModel.getInstance().addProjectile(new HidegonsFireballModel(projectileX, projectileY, walkDir));
            shot = true;
        }
    }

}
