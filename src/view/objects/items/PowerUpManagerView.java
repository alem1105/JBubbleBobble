package view.objects.items;

import model.objects.items.powerups.PowerUpModel;
import model.objects.items.powerups.PowerUpsManagerModel;

import java.awt.*;
import java.util.ArrayList;

public class PowerUpManagerView {

    private static PowerUpManagerView instance;

    private PowerUpsManagerModel powerUpsManagerModel;
    private ArrayList<PowerUpView> powerUpViews;
    private ArrayList<PowerUpModel> powerUpModels;

    public static PowerUpManagerView getInstance() {
        if (instance == null) {
            instance = new PowerUpManagerView();
        }
        return instance;
    }

    private PowerUpManagerView(){
        powerUpViews = new ArrayList<>();
        powerUpsManagerModel = PowerUpsManagerModel.getInstance();
    }

//    public void update() {
//
//    }

    public void draw(Graphics g) {
        getPowerupViewsArrays();
        for(PowerUpView powerUpView : powerUpViews)
            if(powerUpView.getPowerUpModel().isActive())
                powerUpView.draw(g);
    }

    private void getPowerupViewsArrays() {
        powerUpModels = powerUpsManagerModel.getPowerups();
        int modelLength = powerUpModels.size();
        int i = powerUpViews.size();
        if (i > modelLength) {
            //i = 0;
            powerUpViews.clear();
        }
        while (modelLength > powerUpViews.size()) {
            PowerUpModel powerUp = powerUpModels.get(i);
            powerUpViews.add(new PowerUpView(powerUp));
            i++;
        }
    }
}
