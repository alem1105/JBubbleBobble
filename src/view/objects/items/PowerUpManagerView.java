package view.objects.items;

import model.objects.items.powerups.PowerUpModel;
import model.objects.items.powerups.PowerUpsManagerModel;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PowerUpManagerView {

    private static PowerUpManagerView instance;

    private PowerUpsManagerModel powerUpsManagerModel;
    private ArrayList<PowerUpView> powerUpViews;
    private ArrayList<PowerUpModel> powerUpModels;

    private BufferedImage[][] sprites;

    public static PowerUpManagerView getInstance() {
        if (instance == null) {
            instance = new PowerUpManagerView();
        }
        return instance;
    }

    private PowerUpManagerView(){
        powerUpViews = new ArrayList<>();
        powerUpsManagerModel = PowerUpsManagerModel.getInstance();
        sprites = LoadSave.loadAnimations(LoadSave.POWERUP_SPRITE, 12, 1, 18, 18);
    }

    public void update() {
        for(PowerUpView powerUpView : powerUpViews)
            if (powerUpView.getPowerUpModel().isActive())
                if (powerUpView.getPointsTick() <= powerUpView.getPointsDuration()) {
                    powerUpView.update();
                }
    }

    public void draw(Graphics g) {
        getPowerupViewsArrays();
        for(PowerUpView powerUpView : powerUpViews)
            if (powerUpView.getPowerUpModel().isActive())
                if(powerUpView.getPointsTick() <= powerUpView.getPointsDuration()) {
                    powerUpView.draw(g);
                }
    }

    private void getPowerupViewsArrays() {
        powerUpModels = powerUpsManagerModel.getPowerups();
        int modelLength = powerUpModels.size();
        int i = powerUpViews.size();
        if (i > modelLength) {
            powerUpViews.clear();
        }
        while (modelLength > powerUpViews.size()) {
            PowerUpModel powerUp = powerUpModels.get(i);
            powerUpViews.add(new PowerUpView(powerUp, sprites));
            i++;
        }
    }
}
