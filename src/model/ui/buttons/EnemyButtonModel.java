package model.ui.buttons;

/**
 * Bottone per selezionare il nemico da inserire nel livello nel level editor, utilizza un index per scegliere il nemico
 */
public class EnemyButtonModel extends CustomButtonModel{

    private int index;

    public EnemyButtonModel(int x, int y, int width, int height, int index){
        super(x, y, width, height);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
