package view.ui.buttons;

import model.ui.buttons.EraserButtonModel;
import view.utilz.LoadSave;

public class EraserButtonView extends CustomButtonView<EraserButtonModel> {

    public EraserButtonView(EraserButtonModel buttonModel) {
        super(buttonModel);
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.ERASER_BUTTON, 1, 3, 18, 18);
    }

    @Override
    public EraserButtonModel getButtonModel() {
        return buttonModel;
    }
}
