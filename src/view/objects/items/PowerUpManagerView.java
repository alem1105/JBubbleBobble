package view.objects.items;

import model.objects.items.powerups.PowerUpModel;
import model.objects.items.powerups.PowerUpsManagerModel;
import view.utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import java.awt.Graphics;

/**
 * Rappresenta la gestione della visualizzazione dei potenziamenti nel gioco.
 * Questa classe gestisce l'aggiornamento e il disegno dei potenziamenti attivi.
 */
public class PowerUpManagerView {

    private static PowerUpManagerView instance;

    private PowerUpsManagerModel powerUpsManagerModel;
    private ArrayList<PowerUpView> powerUpViews;
    private ArrayList<PowerUpModel> powerUpModels;

    private BufferedImage[][] sprites;

    /**
     * Restituisce l'istanza singleton della classe PowerUpManagerView.
     *
     * @return L'istanza di PowerUpManagerView.
     */
    public static PowerUpManagerView getInstance() {
        if (instance == null) {
            instance = new PowerUpManagerView();
        }
        return instance;
    }

    /**
     * Costruttore privato per inizializzare la classe PowerUpManagerView.
     */
    private PowerUpManagerView() {
        powerUpViews = new ArrayList<>();
        powerUpsManagerModel = PowerUpsManagerModel.getInstance();
        sprites = LoadSave.loadAnimations(LoadSave.POWERUP_SPRITE, 12, 1, 18, 18);
    }

    /**
     * Aggiorna lo stato dei potenziamenti attivi.
     */
    public void update() {
        for (PowerUpView powerUpView : powerUpViews)
            if (powerUpView.getPowerUpModel().isActive())
                if (powerUpView.getPointsTick() <= powerUpView.getPointsDuration()) {
                    powerUpView.update();
                }
    }

    /**
     * Disegna i potenziamenti attivi
     *
     * @param g
     */
    public void draw(Graphics g) {
        getPowerupViewsArrays();
        for (PowerUpView powerUpView : powerUpViews)
            if (powerUpView.getPowerUpModel().isActive())
                if (powerUpView.getPointsTick() <= powerUpView.getPointsDuration()) {
                    powerUpView.draw(g);
                }
    }

    /**
     * Aggiorna l'elenco delle visualizzazioni dei potenziamenti in base al modello.
     */
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
