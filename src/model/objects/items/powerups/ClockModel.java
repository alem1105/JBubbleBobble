package model.objects.items.powerups;

import model.entities.enemies.EnemyManagerModel;

import static model.utilz.Constants.PowerUps.CLOCK;

public class ClockModel extends PowerUpModel{

    private int effectTick, effectTimer = 600;

    public ClockModel(float x, float y, int width, int height) {
        super(x, y, width, height, CLOCK, 200);
    }

    @Override
    public void update() {
        super.update();
        effectTick++;
        if (effectTick >= effectTimer) {
            effectTick = 0;
            active = false;
            unapplyEffect();
        }
    }

    @Override
    public void applyEffect() {
        pickedUp = true;
        EnemyManagerModel.getInstance().setTimeFrozen(true);
    }

    @Override
    public void unapplyEffect() {
        EnemyManagerModel.getInstance().setTimeFrozen(false);
    }

}
