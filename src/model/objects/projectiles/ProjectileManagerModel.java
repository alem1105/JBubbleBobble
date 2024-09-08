package model.objects.projectiles;

import model.entities.PlayerModel;

import java.util.ArrayList;

import static model.utilz.Constants.PlayerConstants.DEATH;

/**
 * Classe che gestisce i proiettili nel gioco.
 * Questa classe utilizza il pattern Singleton per garantire un'unica istanza.
 */
public class ProjectileManagerModel {

    /**
     * Lista dei proiettili attivi nel gioco.
     */
    private ArrayList<ProjectileModel> projectiles;

    /**
     * Istanza unica della classe ProjectileManagerModel.
     */
    private static ProjectileManagerModel instance;

    /**
     * Modello del giocatore per gestire le collisioni con i proiettili.
     */
    private PlayerModel playerModel;

    /**
     * Restituisce l'istanza unica di ProjectileManagerModel.
     * Se l'istanza non esiste, ne crea una nuova.
     *
     * @return l'istanza unica di ProjectileManagerModel
     */
    public static ProjectileManagerModel getInstance() {
        if (instance == null) {
            instance = new ProjectileManagerModel();
        }
        return instance;
    }

    /**
     * Costruttore privato per inizializzare la lista dei proiettili e ottenere il modello del giocatore.
     */
    private ProjectileManagerModel() {
        projectiles = new ArrayList<>();
        playerModel = PlayerModel.getInstance();
    }

    /**
     * Aggiorna tutti i proiettili attivi.
     * Per ogni proiettile attivo, chiama il metodo update e controlla le collisioni con il giocatore.
     */
    public void update() {
        for (ProjectileModel projectile : projectiles) {
            if (projectile.isActive()) {
                projectile.update();
                checkPlayerCollision(projectile);
            }
        }
    }

    /**
     * Controlla se il proiettile ha colpito il giocatore.
     * Se il giocatore non è invincibile e non è in stato di morte, chiama il metodo
     * per gestire il colpo subito dal giocatore.
     *
     * @param projectile il proiettile da controllare
     */
    private void checkPlayerCollision(ProjectileModel projectile) {
        if (playerModel.getHitbox().intersects(projectile.getHitbox()) && !playerModel.isInvincible())
            if (playerModel.getPlayerAction() != DEATH)
                playerModel.playerHasBeenHit();
    }

    public void addProjectile(ProjectileModel projectile) {
        projectiles.add(projectile);
    }

    public void resetProjectiles() {
        projectiles.clear();
    }

    public ArrayList<ProjectileModel> getProjectiles() {
        return projectiles;
    }
}
