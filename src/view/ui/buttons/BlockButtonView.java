package view.ui.buttons;

import model.ui.buttons.BlockButtonModel;
import view.stateview.LevelEditorView;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BlockButtonView extends CustomButtonView<BlockButtonModel> {

    private BufferedImage imageButton;

    public BlockButtonView(BlockButtonModel blockButtonModel, BufferedImage image) {
        super(blockButtonModel);
        this.imageButton = image;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(imageButton,
                buttonModel.getX(), buttonModel.getY(),
                buttonModel.getWidth(), buttonModel.getHeight(), null);
        if (buttonModel.getIndex() == LevelEditorView.getInstance().getBlockIndex())
            drawSelectedBox(g);
    }

}
