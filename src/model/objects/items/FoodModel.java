package model.objects.items;

import model.objects.CustomObjectModel;

import static model.utilz.Constants.Fruit.*;

/**
 * La classe {@code FoodModel} rappresenta un oggetto (estende {@code CustomObjectModel} )
 * di cibo nel gioco che può fornire un punteggio al giocatore quando raccolto.
 * Include informazioni sul tipo di cibo e sul punteggio da fornire.
 */
public class FoodModel extends CustomObjectModel {

    /** L'ammontare di punteggio fornito da questo cibo. */
    private int givenScoreAmount;

    /** L'indice del cibo, che determina il tipo di cibo. */
    private int foodIndex;

    /**
     * Costruttore della classe {@code FoodModel}.
     *
     * @param x la coordinata X del cibo.
     * @param y la coordinata Y del cibo.
     * @param width la larghezza del cibo.
     * @param height l'altezza del cibo.
     * @param foodIndex l'indice del cibo, che rappresenta il tipo di cibo.
     */
    public FoodModel(float x, float y, int width, int height, int foodIndex) {
        super(x, y, width, height);
        this.foodIndex = foodIndex;
        setFoodValues();
    }

    @Override
    public void update() {
    }

    /**
     * Imposta il valore del punteggio fornito dal cibo in base al tipo.
     */
    private void setFoodValues() {
        switch (foodIndex) {
            case ORANGE -> givenScoreAmount = 100;
            case PEPPER -> givenScoreAmount = 200;
            case GRAPE -> givenScoreAmount = 300;
            case TOMATO -> givenScoreAmount = 400;
            case CHERRY -> givenScoreAmount = 500;
        }
    }

    public int getGivenScoreAmount() {
        return givenScoreAmount;
    }

    public int getFoodIndex() {
        return foodIndex;
    }
}

