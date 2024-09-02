package model.entities.enemies;

import model.entities.PlayerModel;
import model.objects.projectiles.DrunkBottleModel;
import model.objects.projectiles.ProjectileManagerModel;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.RUNNING;
import static model.utilz.Constants.Enemies.RUNNING_ANGRY;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.GameConstants.TILES_SIZE;

public class DrunkModel extends EnemyModel{

    public DrunkModel(float x, float y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE;
        initHitbox(14, 16);
    }

    @Override
    public void update() {
        if (enemyState == RUNNING || enemyState == RUNNING_ANGRY){
            if(still) {
                checKBottleShooting();
            }
            else {
                if(isEnemyOnPlayerY() && !PlayerModel.getInstance().isInvincible())
                    startShootingTimer();
                updatePos();
            }
        }
        updateEnemyState();
    }

    private void checKBottleShooting() {
        startStillTimer();
        if(!shot && !inAir) {

            float offsetX = (getWalkDir() == RIGHT) ? hitbox.width : -hitbox.width;
            float projectileX = hitbox.x + offsetX;
            float projectileY = hitbox.y - Math.abs(hitbox.height - 18 * SCALE); // TODO capire se si può togliere sto 18 * SCALE

            ProjectileManagerModel.getInstance().addProjectile(new DrunkBottleModel(projectileX, projectileY, walkDir));

            shot = true;
        }
    }
}