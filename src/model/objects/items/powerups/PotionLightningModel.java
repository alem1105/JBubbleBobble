package model.objects.items.powerups;

import model.entities.PlayerModel;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PowerUps.POTION_LIGHTNING;

public class PotionLightningModel extends PowerUpModel{

    private int startLives;

    public PotionLightningModel(float x, float y, int width, int height) {
        super(x, y, width, height, POTION_LIGHTNING, 100);
        startLives = PlayerModel.getInstance().getLives();
    }

    @Override
    public void update() {
        if (PlayerModel.getInstance().getLives() != startLives)
            unapplyEffect();
    }

    @Override
    public void applyEffect() {
        pickedUp = true;
        PlayerModel.getInstance().setShootingLightningBubble(true);
        PlayerModel.getInstance().setPlayerSpeed(1.5f * SCALE);
        PlayerModel.getInstance().setJumpSpeed(-2.5f * SCALE);
        PlayerModel.getInstance().setFallingSpeed(1.2f * SCALE);
    }

    @Override
    public void unapplyEffect() {
        PlayerModel.getInstance().setShootingLightningBubble(false);
        PlayerModel.getInstance().setPlayerSpeed(1.0f * SCALE);
        PlayerModel.getInstance().setJumpSpeed(-2.25f * SCALE);
        PlayerModel.getInstance().setFallingSpeed(0.65f * SCALE);
        active = false;
    }

}
