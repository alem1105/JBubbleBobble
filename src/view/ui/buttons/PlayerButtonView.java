package view.ui.buttons;

import model.ui.buttons.PlayerButtonModel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Classe che indica la vista del bottone usato per lo spawn del Player nel Level Editor
 */
public class PlayerButtonView extends CustomButtonView<PlayerButtonModel> {

    private BufferedImage imageButton;

    public PlayerButtonView(PlayerButtonModel playerButtonModel, BufferedImage imageButton) {
        super(playerButtonModel);
        this.imageButton = imageButton;
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
