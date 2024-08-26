package view.ui.buttons;

import model.ui.buttons.SaveButtonModel;
import view.utilz.LoadSave;

public class SaveButtonView extends CustomButtonView<SaveButtonModel> {

    public SaveButtonView(SaveButtonModel saveButton){
        super(saveButton);
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.SAVE_BUTTON, 1, 3, 18, 18);
    }

}
