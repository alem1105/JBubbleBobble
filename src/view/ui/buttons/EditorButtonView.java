package view.ui.buttons;

import model.ui.buttons.EditorButtonModel;
import view.utilz.LoadSave;

/**
 * Classe che indica la vista del bottone usato per accedere al Level Editor dal menu'
 */
public class EditorButtonView extends CustomButtonView<EditorButtonModel> {

    public EditorButtonView(EditorButtonModel model) {
        super(model);
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.EDITOR_BUTTON, 1, 3, 47, 14);
    }
}
