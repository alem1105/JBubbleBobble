package model.objects.items.powerups;

import model.entities.PlayerModel;
import model.gamestate.UserStateModel;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PowerUps.SNEAKER;

public class SneakerModel extends PowerUpModel{

    private int effectTick, effectTimer = 1200;

    public SneakerModel(float x, float y, int width, int height) {
        super(x, y, width, height,  SNEAKER, 100);
    }

    @Override
    public void update() {
        super.update();
        effectTick++;
        if (effectTick >= effectTimer) {
            effectTick = 0;

            unapplyEffect();
        }
    }

    @Override
    public void applyEffect() {
        pickedUp = true;
        PlayerModel.getInstance().setPlayerSpeed(1.8f * SCALE);
        PlayerModel.getInstance().setJumpSpeed(-2.75f * SCALE);
        PlayerModel.getInstance().setFallingSpeed(1.50f * SCALE);
    }

    @Override
    public void unapplyEffect() {
        PlayerModel.getInstance().setPlayerSpeed(1.0f * SCALE);
        PlayerModel.getInstance().setJumpSpeed(-2.25f * SCALE);
        PlayerModel.getInstance().setFallingSpeed(0.65f * SCALE);
        active = false;
    }

}
