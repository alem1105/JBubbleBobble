package view.ui.buttons;

import model.ui.buttons.EditButtonModel;
import view.utilz.LoadSave;

import java.awt.*;

public class EditButtonView extends CustomButtonView<EditButtonModel> {
    private Font font;

    public EditButtonView(EditButtonModel model) {
        super(model);
        font = LoadSave.CUSTOM_FONT;
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.EDIT_BUTTON, 1, 3, 35, 14);
    }
}
