package view.ui.buttons;

import model.ui.buttons.QuitButtonModel;
import view.utilz.LoadSave;

public class QuitButtonView extends CustomButtonView<QuitButtonModel> {

    public QuitButtonView(QuitButtonModel model) {
        super(model);
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.QUIT_BUTTON, 1, 3, 43);
    }
}

