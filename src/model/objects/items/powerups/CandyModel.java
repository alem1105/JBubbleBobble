package model.objects.items.powerups;

import model.LevelManagerModel;
import model.objects.bobbles.BobBubbleModel;
import model.objects.bobbles.BubbleManagerModel;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PowerUps.*;

public class CandyModel extends PowerUpModel {

    int startLevel;

    public CandyModel(float x, float y, int width, int height, int candyType) {
        super(x, y, width, height, candyType, 100);
        startLevel = LevelManagerModel.getInstance().getLvlIndex();
    }

    @Override
    public void update() {
        super.update();
        int currentLevel = LevelManagerModel.getInstance().getLvlIndex();
        if (startLevel != currentLevel) {
            active = false;
            unapplyEffect();
        }
    }

    @Override
    public void applyEffect() {
        pickedUp = true;
        for(BobBubbleModel bobBubbleModel : BubbleManagerModel.getInstance().getBobBubbles())
            switch (type) {
                case CANDY_PINK -> bobBubbleModel.setProjectileTravelTimes(360);
                case CANDY_BLUE -> bobBubbleModel.setBubbleSpeedAfterShot(0.7f * SCALE);
                case CANDY_YELLOW -> bobBubbleModel.setBubbleSpeed(2 * SCALE);
            }
    }

    @Override
    public void unapplyEffect() {
        for (BobBubbleModel bobBubbleModel : BubbleManagerModel.getInstance().getBobBubbles())
            bobBubbleModel.resetModifiedCandyValues();
    }
}
