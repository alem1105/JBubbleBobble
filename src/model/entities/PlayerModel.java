package model.entities;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PlayerConstants.*;

public class PlayerModel extends EntityModel {

    private static PlayerModel instance;

    private int playerAction = IDLE;
    private boolean left, right;
    private boolean moving = false;
    private float playerSpeed = 1.0f * SCALE;

    public static PlayerModel getInstance() {
        if (instance == null) {
            instance = new PlayerModel(100, 100, (int) (18 * SCALE), (int) (18 * SCALE));
        }
        return instance;
    }

    private PlayerModel(float x, float y, int width, int height) {
        super(x, y, width, height);
    }

    public void update() {
        updatePos();
    }

    private void updatePos() {
        moving = false;

        //float xSpeed = 0; da usare con gravità collisioni
        if(left) {
            x -= playerSpeed;
        }
        if (right) {
            x += playerSpeed;
        }

    }

    public int getPlayerAction() {
        return playerAction;
    }

    public void setRight(boolean b) {
        this.right = b;
    }
    public void setLeft(boolean b) {
        this.left = b;
    }


}
