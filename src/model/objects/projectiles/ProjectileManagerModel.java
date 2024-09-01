package model.objects.projectiles;

import model.entities.PlayerModel;

import java.util.ArrayList;

import static model.utilz.Constants.PlayerConstants.DEATH;

public class ProjectileManagerModel {

    private ArrayList<ProjectileModel> projectiles;
    private static ProjectileManagerModel instance;
    private PlayerModel playerModel;

    public static ProjectileManagerModel getInstance() {
        if (instance == null) {
            instance = new ProjectileManagerModel();
        }
        return instance;
    }

    private ProjectileManagerModel(){
        projectiles = new ArrayList<>();
        playerModel = PlayerModel.getInstance();
    }

    public void update() {
        for (ProjectileModel projectile : projectiles) {
            if(projectile.isActive()) {
                projectile.update();
                checkPlayerCollision(projectile);
            }
        }
    }

    private void checkPlayerCollision(ProjectileModel projectile) {
        if(playerModel.getHitbox().intersects(projectile.getHitbox()) && !playerModel.isInvincible())
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
