package model.entities.enemies;

import model.entities.PlayerModel;
import model.objects.projectiles.HidegonsFireballModel;
import model.objects.projectiles.ProjectileManagerModel;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.RUNNING;
import static model.utilz.Constants.Enemies.RUNNING_ANGRY;
import static model.utilz.Constants.GameConstants.SCALE;

/**
 * Classe che rappresenta il modello di un nemico di tipo Hidegons.
 * Estende la classe {@link EnemyModel} e implementa le specifiche
 * per il comportamento di questo tipo di nemico.
 */
public class HidegonsModel extends EnemyModel {

    /**
     * Costruttore per inizializzare il modello Hidegons.
     *
     * @param x La coordinata X iniziale del nemico.
     * @param y La coordinata Y iniziale del nemico.
     */
    public HidegonsModel(float x, float y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.75f * SCALE;
        initHitbox(14, 16);
    }

    /**
     * Aggiorna lo stato del nemico in base al suo stato attuale.
     * Se il nemico è in movimento, controlla se deve sparare o aggiornare la sua posizione.
     */
    @Override
    public void update() {
        if (still) {
            checKFireballShooting();
        } else {
            if (isEnemyOnPlayerY() && !PlayerModel.getInstance().isInvincible())
                startShootingTimer();
            updatePos();
        }
        updateEnemyState();
    }

    /**
     * Controlla se il nemico deve sparare una palla di fuoco.
     * Avvia il timer per rimanere fermo e spara una palla di fuoco se le condizioni sono soddisfatte.
     */
    private void checKFireballShooting() {
        startStillTimer();
        if (!shot && !inAir) {
            float offsetX = (getWalkDir() == RIGHT) ? hitbox.width : -hitbox.width;
            float projectileX = hitbox.x + offsetX;
            float projectileY = hitbox.y - Math.abs(hitbox.height - 18 * SCALE);

            ProjectileManagerModel.getInstance().addProjectile(new HidegonsFireballModel(projectileX, projectileY, walkDir));
            shot = true;
        }
    }
}
