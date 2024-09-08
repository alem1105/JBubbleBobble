package model.objects.items.powerups;

import model.entities.PlayerModel;
import model.objects.bobbles.BubbleManagerModel;

import static model.utilz.Constants.PowerUps.*;

public class RingModel extends PowerUpModel {

    private int effectTick, effectTimer = 600;

    public RingModel(float x, float y, int width, int height, int ringType) {
        super(x, y, width, height, ringType, 1000);
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
        switch (type) {
            case RING_PINK -> PlayerModel.getInstance().setScoreForJump(500);
            case RING_RED -> BubbleManagerModel.getInstance().setScoreForPop(100);
        }
    }

    @Override
    public void unapplyEffect() {
        switch (type) {
            case RING_PINK -> PlayerModel.getInstance().setScoreForJump(0);
            case RING_RED -> BubbleManagerModel.getInstance().setScoreForPop(0);
        }
    }
}
