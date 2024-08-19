package view.ui;

import model.ui.BlockButtonModel;
import model.ui.CustomButtonModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class CustomButtonView {

    protected BufferedImage[][] sprites;

    public CustomButtonView() {

    }

    public abstract void draw(Graphics g);

}
