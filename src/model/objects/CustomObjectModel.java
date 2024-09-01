package model.objects;

import java.awt.geom.Rectangle2D;

public abstract class CustomObjectModel {

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected boolean active = true;

    public CustomObjectModel(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        initHitbox();
    }

    private void initHitbox() {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    public abstract void update();

    public boolean isActive() {
        return active;
    }

    public float getX() {
        return hitbox.x;
    }

    public float getY() {
        return hitbox.y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
