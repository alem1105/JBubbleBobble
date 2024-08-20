package view.ui;

import model.ui.BlockButtonModel;
import model.ui.CustomButtonModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CustomButtonView<T extends CustomButtonModel> {

    protected BufferedImage[][] sprites;
    protected T buttonModel;
    protected int spriteIndex;

    public CustomButtonView(T buttonModel) {
        this.buttonModel = buttonModel;
        loadSprites();
    }

    protected void loadSprites() {}

    public void draw(Graphics g) {
        g.drawImage(sprites[0][spriteIndex],
                buttonModel.getX(), buttonModel.getY(),
                buttonModel.getWidth(), buttonModel.getHeight(), null);
    }

    protected void drawSelectedBox(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawRect(buttonModel.getX() - 1, buttonModel.getY() - 1,
                buttonModel.getWidth() + 1, buttonModel.getHeight() + 1);
    }

    public void update() {
        if (buttonModel.isHover()) spriteIndex = 1;
        else spriteIndex = 0;
        if (buttonModel.isPressed()) spriteIndex = 2;
    }

    public T getButtonModel() {
        return buttonModel;
    }

}
