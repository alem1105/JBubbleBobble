package model.ui.buttons;

/**
 * Bottone per selezionare le tile nel Level Editor
 */
public class BlockButtonModel extends CustomButtonModel{
    private int index;

    public BlockButtonModel(int x, int y, int width, int height, int index) {
        super(x, y, width, height);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
