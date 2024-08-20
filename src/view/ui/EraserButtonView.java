package view.ui;

import model.ui.EraserButtonModel;
import view.utilz.LoadSave;

import java.awt.*;
import java.time.chrono.Era;

public class EraserButtonView extends CustomButtonView<EraserButtonModel> {

    public EraserButtonView(EraserButtonModel buttonModel) {
        super(buttonModel);
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.ERASER_BUTTON, 1, 3);
    }

    public EraserButtonModel getButtonModel() {
        return buttonModel;
    }
}
