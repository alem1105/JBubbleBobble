package view.ui.buttons;

import model.ui.buttons.QuitButtonModel;
import view.utilz.LoadSave;

/**
 * Classe che indica la vista del bottone ussato per abbandonare la partita
 */
public class QuitButtonView extends CustomButtonView<QuitButtonModel> {

    private boolean inPauseScreen;

    public QuitButtonView(QuitButtonModel model, boolean inPauseScreen) {
        super(model);
        this.inPauseScreen = inPauseScreen;
        loadSprites();
    }

    @Override
    protected void loadSprites() {
        if (inPauseScreen) {
            sprites = LoadSave.loadAnimations(LoadSave.QUIT_BUTTON2, 1, 3, 47, 14);
        }
        else
            sprites = LoadSave.loadAnimations(LoadSave.QUIT_BUTTON, 1, 3, 47, 14);
    }
}

