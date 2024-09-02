package model.objects.projectiles;

import model.objects.CustomObjectModel;

public abstract class ProjectileModel extends CustomObjectModel {

    protected int direction;

    public ProjectileModel(float x, float y, int width, int height, int direction) {
        super(x, y, width, height);
        this.direction = direction;
    }

    public abstract void update();

    public int getDirection() {
        return direction;
    }
}
