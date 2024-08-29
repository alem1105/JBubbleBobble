package view.ui.buttons;

import model.ui.buttons.ChangePageButtonModel;
import view.utilz.LoadSave;

import java.awt.*;

public class ChangePageButtonView extends CustomButtonView<ChangePageButtonModel> {

    int flipX;
    int flipW;

    public ChangePageButtonView(ChangePageButtonModel model) {
        super(model);
        setDirection(model);
    }

    private void setDirection(ChangePageButtonModel model) {
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
        sprites = LoadSave.loadAnimations(LoadSave.CHANGE_LVL_BUTTON, 1, 3, 18, 18);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(sprites[0][spriteIndex],
                buttonModel.getX() + flipX, buttonModel.getY(),
                buttonModel.getWidth() * flipW, buttonModel.getHeight(), null);
    }

}
