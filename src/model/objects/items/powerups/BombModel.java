package model.objects.items.powerups;

import model.entities.enemies.EnemyManagerModel;
import model.gamestate.UserStateModel;

import static model.utilz.Constants.PowerUps.BOMB;

public class BombModel extends PowerUpModel{

    public BombModel(float x, float y, int width, int height) {
        super(x, y, width, height, BOMB, 200);
    }

    @Override
    public void applyEffect() {
        active = false;
        pickedUp = false;
        EnemyManagerModel.getInstance().getEnemies().forEach(e -> e.setActive(false));
        PowerUpsManagerModel.getInstance().setBombExploding(true);
    }

    @Override
    public void unapplyEffect() {

    }

}
