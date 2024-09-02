package model.objects.items.powerups;

import model.entities.PlayerModel;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PowerUps.SNEAKER;

public class SneakerModel extends PowerUpModel{

    public SneakerModel(float x, float y, int width, int height) {
        super(x, y, width, height,  SNEAKER);
    }

    @Override
    public void update() {

    }

    @Override
    public void applyEffect() {
        PlayerModel.getInstance().setPlayerSpeed(1.5f * SCALE);
        PlayerModel.getInstance().setJumpSpeed(-2.75f * SCALE);
        PlayerModel.getInstance().setGravity(0.08f * SCALE);
    }

    @Override
    public void unapplyEffect() {
        PlayerModel.getInstance().setPlayerSpeed(1.0f * SCALE);
        PlayerModel.getInstance().setJumpSpeed(-2.25f * SCALE);
        PlayerModel.getInstance().setGravity(0.04f * SCALE);
    }

}
