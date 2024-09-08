package model.ui.buttons;

/**
 * Bottone usato per cambiare pagina in varie schermate ha una direzione per la freccia
 */
public class ChangePageButtonModel extends CustomButtonModel {

    private int direction;

    public ChangePageButtonModel(int x, int y, int width, int height, int direction) {
        super(x, y, width, height);
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

}
