package view.objects;

import model.objects.CustomObjectModel;
import model.objects.items.powerups.PowerUpModel;
import view.utilz.LoadSave;

import java.awt.*;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PowerUps.*;

public class PowerUpView extends CustomObjectView<PowerUpModel> {

    private int xDrawOffset, yDrawOffset;
    private int type;

    public PowerUpView(PowerUpModel powerUpModel) {
        super(powerUpModel);
        aniIndex = 0;
        type = powerUpModel.getType();
        spriteIndex = type;
        sprites = LoadSave.loadAnimations(LoadSave.POWERUP_SPRITE, 12, 1, 18, 18);
    }

    @Override
    public void draw(Graphics g){
        g.drawImage(sprites[spriteIndex][aniIndex],
                (int) objectModel.getX() - xDrawOffset,
                (int) objectModel.getY() - yDrawOffset,
                (int) (18 * SCALE),
                (int) (18 * SCALE),
                null);
    }

    protected int getSpriteAmount(){
        return 1;
    }

    private void setDrawOffset(){
        switch (type){
            case CANDY_PINK, CANDY_BLUE, CANDY_YELLOW -> {
                xDrawOffset = (int) (1 * SCALE);
                yDrawOffset = (int) (1 * SCALE);
            }
        }
    }

    public PowerUpModel getPowerUpModel(){
        return objectModel;
    }

}
