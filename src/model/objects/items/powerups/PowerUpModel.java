package model.objects.items.powerups;

import model.objects.CustomObjectModel;

public abstract class PowerUpModel extends CustomObjectModel {

    protected int score;
    protected int type;
    protected boolean pickedUp;

    protected int despawnTick, despawnTimer = 1200;

    public PowerUpModel(float x, float y, int width, int height, int type, int score) {
        super(x, y, width, height);
        this.type = type;
        this.score = score;
    }

    @Override
    public void update() {
        despawnTick++;
        if (despawnTick >= despawnTimer) {
            despawnTick = 0;
            active = false;
        }
    }

    public abstract void applyEffect();

    public abstract void unapplyEffect();

    public int getType() {
        return type;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public int getScore() {
        return score;
    }
}
