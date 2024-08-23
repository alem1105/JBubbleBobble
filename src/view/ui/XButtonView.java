package view.ui;

import model.ui.XButtonModel;
import view.utilz.LoadSave;

import java.awt.*;

public class XButtonView extends CustomButtonView<XButtonModel> {

    public XButtonView(XButtonModel XButton) {
        super(XButton);
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.X_BUTTON, 1, 3);
    }

}
