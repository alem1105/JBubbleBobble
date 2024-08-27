package model.objects;

import static model.utilz.Constants.GameConstants.SCALE;

public class BubbleModel extends CustomObjectModel {
    protected int bubbleDirection;
    protected boolean active = true;
    protected float bubbleSpeed = 1.5F * SCALE;
    protected int lifeTimer = 0; // tempo della vita dopo di che esplode
    protected boolean timeOut;
    protected int lifeTime = 3000;


    public BubbleModel(float x, float y, int width, int height, int bubbleDirection){
        super(x, y, width, height);
        this.bubbleDirection = bubbleDirection;
    }

    public void update(){
        if (active){
            lifeTimer++;
            if (lifeTimer >= lifeTime){ //dopo 25 secondi esplode
                active = false;
                timeOut = true;
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isTimeOut() {
        return timeOut;
    }

}

