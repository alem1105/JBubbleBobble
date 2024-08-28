package model.objects;

import model.objects.bobbles.BubbleManagerModel;

import java.util.ArrayList;

public class ProjectileManagerModel {

    private ArrayList<ProjectileModel> projectiles;
    private static ProjectileManagerModel instance;

    public static ProjectileManagerModel getInstance() {
        if (instance == null) {
            instance = new ProjectileManagerModel();
        }
        return instance;
    }

    private ProjectileManagerModel(){
        projectiles = new ArrayList<>();
    }

    public void update() {
        for (ProjectileModel projectile : projectiles) {
            projectile.update();
        }
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
