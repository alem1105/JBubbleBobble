package model.objects.items;

import model.objects.CustomObjectModel;

import static model.utilz.Constants.Fruit.*;

public class FoodModel extends CustomObjectModel {

    private int givenScoreAmount, foodIndex;

    public FoodModel(float x, float y, int width, int height, int foodIndex) {
        super(x, y, width, height);
        this.foodIndex = foodIndex;
        setFoodValues();
    }

    @Override
    public void update() {}

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
