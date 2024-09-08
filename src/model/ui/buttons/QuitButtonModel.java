package model.ui.buttons;

/**
 * Bottone per abbandonare la partita
 */
public class QuitButtonModel extends CustomButtonModel{

    public QuitButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void quit(){
        System.exit(0);
    }

}
