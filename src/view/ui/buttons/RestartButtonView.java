package view.ui.buttons;

import model.ui.buttons.RestartButtonModel;
import view.utilz.LoadSave;

/**
 * Classe che indica il bottone usato per riavviare la partita
 */
public class RestartButtonView extends CustomButtonView<RestartButtonModel> {

    public RestartButtonView(RestartButtonModel model) {
        super(model);
    }

    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.RESTART_BUTTON, 1, 3, 47, 14);
    }
}