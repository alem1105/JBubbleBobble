package view.ui;

import model.ui.CustomButtonModel;
import model.ui.SaveButtonModel;
import view.utilz.LoadSave;

import java.awt.*;

public class SaveButtonView extends CustomButtonView<SaveButtonModel> {

    public SaveButtonView(SaveButtonModel saveButton){
        super(saveButton);
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.SAVE_BUTTON, 1, 3);
    }

}
