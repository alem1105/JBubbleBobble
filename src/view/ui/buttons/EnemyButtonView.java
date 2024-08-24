package view.ui.buttons;

import model.ui.buttons.EnemyButtonModel;
import view.stateview.LevelEditorView;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EnemyButtonView extends CustomButtonView<EnemyButtonModel>{

    private BufferedImage imageButton;

    public EnemyButtonView(EnemyButtonModel buttonModel, BufferedImage imageButton) {
        super(buttonModel);
        this.buttonModel = buttonModel;
        this.imageButton = imageButton;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(imageButton,
                buttonModel.getX(), buttonModel.getY(),
                buttonModel.getWidth(), buttonModel.getHeight(), null);
        if (buttonModel.getIndex() == LevelEditorView.getInstance().getEnemyIndex())
            drawSelectedBox(g);
    }
}
