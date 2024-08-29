package view.ui.buttons;

import model.ui.buttons.CreateButtonModel;
import model.ui.buttons.EditButtonModel;
import view.utilz.LoadSave;

public class CreateButtonView extends CustomButtonView<CreateButtonModel> {

    public CreateButtonView(CreateButtonModel model) {
        super(model);
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.CREATE_BUTTON, 1, 3, 47, 14);
    }
}
