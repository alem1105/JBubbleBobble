package model.entities.enemies;

import model.entities.PlayerModel;
import model.objects.MaitaFireballModel;
import model.objects.ProjectileManagerModel;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.*;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.GameConstants.TILES_SIZE;

public class MaitaModel extends EnemyModel{
    protected int shootingTimer = 240;
    protected int shootingTick = 0;

    private boolean still = false;
    private boolean shot = false;
    private int stillTimer = 30;
    private int stillTick = 0;

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

    private void startShootingTimer() {
        if(shootingTick >= shootingTimer && !inAir) {
            still = true;
            shootingTick = 0;
            shot = false;
        }
        shootingTick++;
    }

    private void startStillTimer() {
        if (stillTick >= stillTimer)  {
            stillTick = 0;
            still = false;
            shootingTick = 0;
        }
        stillTick++;
    }

    private boolean isMaitaOnPlayerY() {
        return (int) (hitbox.y / TILES_SIZE) == (int) (PlayerModel.getInstance().getHitbox().y / TILES_SIZE);
    }

}
