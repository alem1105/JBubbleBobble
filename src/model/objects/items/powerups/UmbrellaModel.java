package model.objects.items.powerups;

import model.LevelManagerModel;
import model.objects.CustomObjectModel;

import static model.utilz.Constants.PowerUps.*;

public class UmbrellaModel extends PowerUpModel {

    private LevelManagerModel levelManager;

    public UmbrellaModel(float x, float y, int width, int height, int umbrellaType) {
        super(x, y, width, height, umbrellaType);
        levelManager = LevelManagerModel.getInstance();
    }

    @Override
    public void update() {
    }

    public void applyEffect() {
        switch (type) {
            case UMBRELLA_ORANGE -> levelManager.setLvlIndex(levelManager.getLvlIndex() + 2);
            case UMBRELLA_RED -> levelManager.setLvlIndex(levelManager.getLvlIndex() + 4);
            case UMBRELLA_PINK -> levelManager.setLvlIndex(levelManager.getLvlIndex() + 6);
        }
        LevelManagerModel.getInstance().loadNextLevel();
    }

    @Override
    public void unapplyEffect() {

    }

}
