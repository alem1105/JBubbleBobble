package view.stateview;

import java.util.Random;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.SCALE;

public class TwinkleView {

    private float x, y;
    private int aniIndex, aniTick;
    private float ySpeed = 0.1f * SCALE;
    private float xSpeed = 0.2f * SCALE;
    private Random random;

    public TwinkleView(float x, float y, int direction) {
        this.x = x;
        this.y = y;
        if (direction == LEFT)
            xSpeed = -xSpeed;
        else if (direction == UP)
            xSpeed = 0;

        random = new Random();
        aniIndex = random.nextInt(4);
    }

    public void update() {
        updatePos();
        updateAnimation();
    }

    private void updateAnimation() {
        aniTick++;
        if (aniTick > ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 4)
                aniIndex = 0;
        }
    }

    private void updatePos() {
        if (x > GAME_WIDTH + 10)
            x = - 5;
        else if (x < -10)
            x = GAME_WIDTH;

        if (y <= -10)
            y = GAME_HEIGHT;

        x += xSpeed;
        y -= ySpeed;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
