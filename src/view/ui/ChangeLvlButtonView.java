package view.ui;

import model.ui.ChangeLvlButtonModel;
import view.utilz.LoadSave;

import java.awt.*;

public class ChangeLvlButtonView extends CustomButtonView<ChangeLvlButtonModel> {

    int flipX;
    int flipW;

    public ChangeLvlButtonView(ChangeLvlButtonModel model) {
        super(model);
        setDirection(model);
    }

    private void setDirection(ChangeLvlButtonModel model) {
        if (model.getDirection() == 2) {
            flipX = 0;
            flipW = 1;
        } else {
            flipX = model.getWidth();
            flipW = -1;
        }
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.CHANGE_LVL_BUTTON, 1, 3);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(sprites[0][spriteIndex],
                buttonModel.getX() + flipX, buttonModel.getY(),
                buttonModel.getWidth() * flipW, buttonModel.getHeight(), null);
    }

}
