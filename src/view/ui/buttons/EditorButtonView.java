package view.ui.buttons;

import model.ui.buttons.EditorButtonModel;
import model.ui.buttons.RestartButtonModel;
import view.utilz.LoadSave;

public class EditorButtonView extends CustomButtonView<EditorButtonModel> {

    public EditorButtonView(EditorButtonModel model) {
        super(model);
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.EDITOR_BUTTON, 1, 3, 47, 14);
    }
}
