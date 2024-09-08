package view.ui.buttons;

import model.ui.buttons.StartButtonModel;
import view.utilz.LoadSave;

public class StartButtonView extends CustomButtonView<StartButtonModel> {

    private boolean inPauseScreen;

    public StartButtonView(StartButtonModel model, boolean inPauseScreen) {
        super(model);
        this.inPauseScreen = inPauseScreen;
        loadSprites();
    }

    @Override
    protected void loadSprites() {
        if (inPauseScreen)
            sprites = LoadSave.loadAnimations(LoadSave.START_BUTTON2, 1, 3, 47, 14);
        else
            sprites = LoadSave.loadAnimations(LoadSave.START_BUTTON, 1, 3, 47, 14);
    }
}