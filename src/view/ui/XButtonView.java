package view.ui;

import model.ui.XButtonModel;
import view.utilz.LoadSave;

import java.awt.*;

public class XButtonView extends CustomButtonView {

    private XButtonModel buttonModel;
    private int spriteIndex;

    public XButtonView(XButtonModel XButton) {
        super();
        loadSprites();
        this.buttonModel = XButton;
    }

    private void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.X_BUTTON, 1, 3);
    }

    @Override
    public void draw(Graphics g){
        g.drawImage(sprites[0][spriteIndex],
                buttonModel.getX(), buttonModel.getY(),
                buttonModel.getWidth(), buttonModel.getHeight(), null);
    }

    public void update() {
        // aggiorna animazione
        if (buttonModel.isHover()) spriteIndex = 1;
        else spriteIndex = 0;
        if (buttonModel.isPressed()) spriteIndex = 2;
    }

    public XButtonModel getButtonModel() {
        return buttonModel;
    }
}
