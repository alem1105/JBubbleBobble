package view.objects.items;

import model.objects.items.FoodModel;
import view.objects.CustomObjectView;
import view.utilz.LoadSave;

import java.awt.*;

import static model.utilz.Constants.Fruit.*;
import static model.utilz.Constants.GameConstants.SCALE;

public class FoodView extends CustomObjectView<FoodModel> {

    private int scoreDuration = 40, scoreTick = 0;

    public FoodView(FoodModel model) {
        super(model);
        aniIndex = 0;
        sprites = LoadSave.loadAnimations(LoadSave.FOOD_SPRITE, 109, 1, 18, 18);
        setSpriteIndex();
    }

    private void setSpriteIndex() {
        switch (objectModel.getFoodIndex()) {
            case ORANGE ->  spriteIndex = 0;
            case PEPPER -> spriteIndex = 1;
            case GRAPE -> spriteIndex = 2;
            case TOMATO -> spriteIndex = 3;
            case CHERRY -> spriteIndex = 4;
        }
    }

    @Override
    protected int getSpriteAmount() {
        return 0;
    }

    public void update(){
        if (scoreDuration >= scoreTick){
            if (!objectModel.isActive()){
                scoreTick++;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (objectModel.isActive())
            super.draw(g);
        else
            drawScoreAmount(g);
    }

    private void drawScoreAmount(Graphics g) {
        g.setColor(Color.GREEN);
        Font font = (LoadSave.BUBBLE_BOBBLE_FONT).deriveFont(10 * SCALE);
        g.setFont(font);
        g.drawString(String.valueOf(objectModel.getGivenScoreAmount()), (int)objectModel.getX(), (int)objectModel.getY());
    }

    public int getScoreDuration() {
        return scoreDuration;
    }

    public int getScoreTick() {
        return scoreTick;
    }
}
