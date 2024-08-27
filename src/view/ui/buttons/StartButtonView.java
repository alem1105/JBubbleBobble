package view.ui.buttons;

import model.ui.buttons.RestartButtonModel;
import model.ui.buttons.StartButtonModel;
import view.utilz.LoadSave;

public class StartButtonView extends CustomButtonView<StartButtonModel> {

    public StartButtonView(StartButtonModel model) {
        super(model);
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.START_BUTTON, 1, 3, 47, 14);
    }
}