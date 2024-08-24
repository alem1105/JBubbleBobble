package model.ui.buttons;

import java.awt.geom.Rectangle2D;

public abstract class CustomButtonModel {
    protected int x, y, width, height;
    protected Rectangle2D.Float bounds;
    protected boolean mouseHover, mousePressed;

    public CustomButtonModel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        bounds = new Rectangle2D.Float(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle2D.Float getBounds() {
        return bounds;
    }

    public void setHover(boolean hover) {
        mouseHover = hover;
    }

    public void setPressed(boolean pressed) {
        mousePressed = pressed;
    }

    public boolean isHover() {
        return mouseHover;
    }

    public boolean isPressed() {
        return mousePressed;
    }
}
