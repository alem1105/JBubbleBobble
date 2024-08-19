package view.ui;

import model.ui.BlockButtonModel;
import view.LevelEditorView;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BlockButtonView extends CustomButtonView {

    private BlockButtonModel buttonModel;
    private BufferedImage imageButton;

    public BlockButtonView(BlockButtonModel blockButtonModel, BufferedImage image) {
        super();
        this.imageButton = image;
        this.buttonModel = blockButtonModel;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(imageButton,
                buttonModel.getX(), buttonModel.getY(),
                buttonModel.getWidth(), buttonModel.getHeight(), null);
        if (buttonModel.getIndex() == LevelEditorView.getInstance().getBlockIndex())
            drawSelectedBox(g);
    }

    private void drawSelectedBox(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawRect(buttonModel.getX() - 1, buttonModel.getY() - 1,
                buttonModel.getWidth() + 1, buttonModel.getHeight() + 1);
    }

    public BlockButtonModel getButtonModel() {
        return buttonModel;
    }
}
