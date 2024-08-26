package view.ui.buttons;

import model.ui.buttons.XButtonModel;
import view.utilz.LoadSave;

public class XButtonView extends CustomButtonView<XButtonModel> {

    public XButtonView(XButtonModel XButton) {
        super(XButton);
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.X_BUTTON, 1, 3, 18, 18);
    }

}
