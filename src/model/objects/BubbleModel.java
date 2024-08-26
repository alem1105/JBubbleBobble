package model.objects;

import static model.utilz.Constants.GameConstants.SCALE;

public class BubbleModel extends CustomObjectModel {
    protected int bubbleDirection;
    protected boolean active = true;
    protected float bubbleSpeed = 1.5F * SCALE;

    public BubbleModel(float x, float y, int width, int height, int bubbleDirection){
        super(x, y, width, height);
        this.bubbleDirection = bubbleDirection;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

