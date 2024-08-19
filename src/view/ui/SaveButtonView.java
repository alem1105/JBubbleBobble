package view.ui;

import model.ui.CustomButtonModel;
import model.ui.SaveButtonModel;
import view.utilz.LoadSave;

import java.awt.*;

public class SaveButtonView extends CustomButtonView {

    private int spriteIndex;
    private SaveButtonModel buttonModel;

    public SaveButtonView(SaveButtonModel saveButton){
        super();
        this.buttonModel = saveButton;
        loadSprites();
    }

    public void update() {
        // aggiorna animazione
        if (buttonModel.isHover()) spriteIndex = 1;
        else spriteIndex = 0;
        if (buttonModel.isPressed()) spriteIndex = 2;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(sprites[0][spriteIndex],
                buttonModel.getX(), buttonModel.getY(),
                buttonModel.getWidth(), buttonModel.getHeight(), null);
    }

    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.SAVE_BUTTON, 1, 3);
    }

    public SaveButtonModel getButtonModel() {
        return buttonModel;
    }
}
