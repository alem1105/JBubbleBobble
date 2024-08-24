package model.ui.buttons;

public class ChangeLvlButtonModel extends CustomButtonModel {

    private int direction;

    public ChangeLvlButtonModel(int x, int y, int width, int height, int direction) {
        super(x, y, width, height);
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

}
