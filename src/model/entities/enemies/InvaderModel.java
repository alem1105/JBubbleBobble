package model.entities.enemies;

import model.objects.projectiles.InvaderLaserModel;
import model.objects.projectiles.ProjectileManagerModel;

import static model.utilz.Constants.Enemies.RUNNING;
import static model.utilz.Constants.Enemies.RUNNING_ANGRY;
import static model.utilz.Constants.GameConstants.SCALE;

/**
 * Classe che rappresenta il modello di un nemico di tipo Invader.
 * Estende la classe {@link EnemyModel} e implementa le specifiche
 * per il comportamento di questo tipo di nemico.
 */
public class InvaderModel extends EnemyModel {

    /**
     * Indica se il nemico sta attualmente sparando.
     */
    private boolean shooting = false;

    /**
     * Costruttore per inizializzare il modello Invader.
     *
     * @param x La coordinata X iniziale del nemico.
     * @param y La coordinata Y iniziale del nemico.
     */
    public InvaderModel(float x, float y) {
        super(x, y, (int) (18 * SCALE), (int) (18 * SCALE));
        this.walkSpeed = 0.55f * SCALE;
        initHitbox(14, 16);
        shootingTimer = 500; // Imposta il timer per lo sparo
    }

    /**
     * Aggiorna lo stato del nemico in base al suo stato attuale.
     * Aggiorna la posizione del nemico, il timer di sparo e controlla se deve sparare.
     */
    @Override
    public void update() {
        if (enemyState == RUNNING || enemyState == RUNNING_ANGRY) {
            updatePos();
            updateShootingTimer();
            checkShooting();
        }
        updateEnemyState();
    }

    /**
     * Controlla se il nemico deve sparare un laser.
     * Se il nemico sta sparando, aggiunge un nuovo proiettile di tipo {@link InvaderLaserModel}.
     */
    public void checkShooting() {
        if (shooting) {
            ProjectileManagerModel.getInstance().addProjectile(new InvaderLaserModel(hitbox.x, hitbox.y + hitbox.height));
        }
    }

    /**
     * Aggiorna il timer per lo sparo.
     * Se il timer raggiunge il limite, il nemico può sparare nuovamente.
     */
    protected void updateShootingTimer() {
        shooting = false;
        shootingTick++;
        if (shootingTick >= shootingTimer) {
            shooting = true;
            shootingTick = 0;
        }
    }

    /**
     * Aggiorna la posizione del nemico.
     * Controlla se il nemico è in aria e gestisce la sua posizione di conseguenza.
     */
    @Override
    public void updatePos() {
        isInAirCheck();
        if (inAir) {
            fallingChecks(walkSpeed);
        } else {
            walkWithDifferentY();
        }
    }
}

