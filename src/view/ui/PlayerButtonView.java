package view.ui;

import model.ui.PlayerButtonModel;
import view.LevelEditorView;

import java.awt.*;
import java.awt.image.BufferedImage;

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
        if (buttonModel.isSelected()) {
            drawSelectedBox(g);
        }
    }
}
