package model.ui.buttons;

/**
 * Bottone per impostare lo spawn del Player nel Level Editor
 */
public class PlayerButtonModel extends CustomButtonModel {

    private static PlayerButtonModel instance;

    private PlayerButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public static PlayerButtonModel getInstance(int x, int y, int width, int height) {
        if (instance == null) {
            instance = new PlayerButtonModel(x, y, width, height);
        }
        return instance;
    }
}
