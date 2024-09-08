package view.objects.items;

import model.objects.items.powerups.PowerUpModel;
import view.objects.CustomObjectView;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PowerUps.*;
import static view.utilz.AudioManager.ITEM_PICKUP;

public class PowerUpView extends CustomObjectView<PowerUpModel> {

    private int xDrawOffset, yDrawOffset;
    private int type;
    private int pointsDuration = 40, pointsTick = 0;

    public PowerUpView(PowerUpModel powerUpModel, BufferedImage[][] sprites) {
        super(powerUpModel);
        aniIndex = 0;
        type = powerUpModel.getType();
        spriteIndex = type;
        this.sprites = sprites;
        setDrawOffset();
    }

    public void update(){
            if (objectModel.isPickedUp()){
                playPickupSound(ITEM_PICKUP);
                pointsTick++;
            }
    }

    @Override
    public void draw(Graphics g){
        if (objectModel.isPickedUp()){
            drawPoints(g);
        }
        else
            g.drawImage(sprites[spriteIndex][aniIndex],
                            (int) objectModel.getX() - xDrawOffset,
                            (int) objectModel.getY() - yDrawOffset,
                            (int) (18 * SCALE),
                            (int) (18 * SCALE),
                            null);

    }

    @Override
    protected int getSpriteAmount(){
        return 1;
    }

    private void setDrawOffset(){
        switch (type){
            case CANDY_PINK, CANDY_BLUE, CANDY_YELLOW -> {
                xDrawOffset = (int) (1 * SCALE);
                yDrawOffset = (int) (4 * SCALE);
            }
            case UMBRELLA_ORANGE, UMBRELLA_PINK, UMBRELLA_RED -> {
                xDrawOffset = (int) (2 * SCALE);
                yDrawOffset = 0;
            }
            case RING_PINK, RING_RED -> {
                xDrawOffset = (int) (5 * SCALE);
                yDrawOffset = (int) (2 * SCALE);
            }
            case SNEAKER -> {
                xDrawOffset = (int) (8 * SCALE);
                yDrawOffset = (int) (2 * SCALE);
            }
            case CLOCK, BOMB -> {
                xDrawOffset = (int) (3 * SCALE);
                yDrawOffset = (int) (3 * SCALE);
            }
            case POTION_LIGHTNING -> {
                xDrawOffset = (int) (1 * SCALE);
                yDrawOffset = (int) (1 * SCALE);
            }
        }
    }

    private void drawPoints(Graphics g) {
        g.setColor(Color.GREEN);
        Font font = (LoadSave.NES_FONT).deriveFont(8 * SCALE);
        g.setFont(font);
        g.drawString(String.valueOf(objectModel.getScore()), (int) objectModel.getX(), (int) objectModel.getY());
    }

    public PowerUpModel getPowerUpModel(){
        return objectModel;
    }

    public int getPointsDuration() {
        return pointsDuration;
    }

    public int getPointsTick() {
        return pointsTick;
    }
}
