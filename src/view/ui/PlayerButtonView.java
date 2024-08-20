package view.ui;

import model.ui.PlayerButtonModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerButtonView extends CustomButtonView<PlayerButtonModel> {

    private BufferedImage imageButton;
    private static PlayerButtonView instance;

    private PlayerButtonView(PlayerButtonModel playerButtonModel, BufferedImage imageButton) {
        super(playerButtonModel);
        this.imageButton = imageButton;
    }

    public static PlayerButtonView getInstance(PlayerButtonModel playerButtonModel, BufferedImage imageButton) {
        if (instance == null) {
            instance = new PlayerButtonView(playerButtonModel, imageButton);
        }
        return instance;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(imageButton,
                buttonModel.getX(), buttonModel.getY(),
                buttonModel.getWidth(), buttonModel.getHeight(), null);
        if (buttonModel.isPressed()) {
            drawSelectedBox(g);
        }
    }

    public BufferedImage getImageButton() {
        return imageButton;
    }
}
