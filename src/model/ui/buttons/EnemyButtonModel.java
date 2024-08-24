package model.ui.buttons;

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
