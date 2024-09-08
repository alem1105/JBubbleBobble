package model.entities.enemies;

import model.entities.PlayerModel;
import model.objects.projectiles.DrunkBottleModel;
import model.objects.projectiles.ProjectileManagerModel;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.Enemies.RUNNING;
import static model.utilz.Constants.Enemies.RUNNING_ANGRY;
import static model.utilz.Constants.GameConstants.SCALE;

/**
 * La classe {@code DrunkModel} rappresenta il nemico Drunk.
 * Estende la classe {@code EnemyModel} e gestisce il movimento e l'attacco.
 */
public class DrunkModel extends EnemyModel{

    /**
     * Crea un nuovo modello di Drunk con le coordinate specificate.
     *
     * @param x la coordinata X iniziale
     * @param y la coordinata Y iniziale
     */
    public DrunkModel(float x, float y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE;
        initHitbox(14, 16);
    }

    /**
     * Aggiorna lo stato del nemico.
     * Se il nemico è in corsa o in corsa arrabbiato, gestisce il movimento e
     * l'attacco a seconda della sua condizione attuale.
     */
    @Override
    public void update() {
            if(still) {
                checKBottleShooting();
            }
            else {
                if(isEnemyOnPlayerY() && !PlayerModel.getInstance().isInvincible())
                    startShootingTimer();
                updatePos();
        }
        updateEnemyState();
    }

    /**
     * Gestisce il lancio delle bottiglie.
     * Se è fermo e non ha già sparato, crea una nuova bottiglia e la lancia.
     */
    private void checKBottleShooting() {
        startStillTimer();
        if(!shot && !inAir) {

            float offsetX = (getWalkDir() == RIGHT) ? hitbox.width : -hitbox.width;
            float projectileX = hitbox.x + offsetX;
            float projectileY = hitbox.y - Math.abs(hitbox.height - 18 * SCALE);

            ProjectileManagerModel.getInstance().addProjectile(new DrunkBottleModel(projectileX, projectileY, walkDir));

            shot = true;
        }
    }
}
