package model.entities.enemies;

import model.entities.PlayerModel;
import model.objects.InvaderLaserModel;
import model.objects.MaitaFireballModel;
import model.objects.ProjectileManagerModel;

import static model.utilz.Constants.Enemies.RUNNING;
import static model.utilz.Constants.Enemies.RUNNING_ANGRY;
import static model.utilz.Constants.GameConstants.SCALE;


public class InvaderModel extends EnemyModel{

    private boolean shooting = false;

    public InvaderModel(float x, float y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE;
        initHitbox(14, 16);
        shootingTimer = 500;
    }

    @Override
    public void update(){
        if (enemyState == RUNNING || enemyState == RUNNING_ANGRY){
            updatePos();
            updateShootingTimer();
            checkShooting();
        }
        updateEnemyState();
    }

    public void checkShooting() {
        if (shooting){
            ProjectileManagerModel.getInstance().addProjectile(new InvaderLaserModel(hitbox.x , hitbox.y + hitbox.height));
        }
    }

    protected void updateShootingTimer(){
        shooting = false;
        shootingTick++;
        if (shootingTick >= shootingTimer){
            shooting = true;
            shootingTick = 0;
        }
    }

    @Override
    public void updatePos(){
        isInAirCheck();
            if (inAir)
                fallingChecks(walkSpeed);
            else
                walkWithDifferentY();
    }
}
