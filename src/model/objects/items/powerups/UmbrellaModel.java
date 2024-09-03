package model.objects.items.powerups;

import model.LevelManagerModel;
import model.objects.CustomObjectModel;

import static model.utilz.Constants.PowerUps.*;

public class UmbrellaModel extends PowerUpModel {

    private LevelManagerModel levelManager;
    private int levelsSkipped;

    public UmbrellaModel(float x, float y, int width, int height, int umbrellaType) {
        super(x, y, width, height, umbrellaType, 200);
        levelManager = LevelManagerModel.getInstance();
        setLevelSkipped();
    }

    private void setLevelSkipped() {
        switch (type) {
            case UMBRELLA_ORANGE -> levelsSkipped = 3;
            case UMBRELLA_RED -> levelsSkipped = 5;
            case UMBRELLA_PINK -> levelsSkipped = 7;
        }
    }

    public void applyEffect() {
        active = false;
        pickedUp = false;
        levelManager.setLvlIndex(levelManager.getLvlIndex() + levelsSkipped -1);
        LevelManagerModel.getInstance().setLevelSkipped(levelsSkipped);
        LevelManagerModel.getInstance().loadNextLevel();
    }

    @Override
    public void unapplyEffect() {

    }

    public int getLevelsSkipped() {
        return levelsSkipped;
    }
}
