package model.objects.items.powerups;

import model.objects.CustomObjectModel;

import java.awt.*;

public abstract class PowerUpModel extends CustomObjectModel {

    protected int type;

    public PowerUpModel(float x, float y, int width, int height, int type) {
        super(x, y, width, height);
        this.type = type;
    }

    public abstract void applyEffect();

    public abstract void unapplyEffect();

    public int getType() {
        return type;
    }
}
