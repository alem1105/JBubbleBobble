package model.objects.items.powerups;

import model.entities.enemies.EnemyManagerModel;

import static model.utilz.Constants.PowerUps.CLOCK;

public class ClockModel extends PowerUpModel{

    public ClockModel(float x, float y, int width, int height) {
        super(x, y, width, height, CLOCK);
    }

    @Override
    public void applyEffect() {
        EnemyManagerModel.getInstance().setTimeFrozen(true);
    }

    @Override
    public void unapplyEffect() {
        EnemyManagerModel.getInstance().setTimeFrozen(false);
    }

    @Override
    public void update() {

    }
}
