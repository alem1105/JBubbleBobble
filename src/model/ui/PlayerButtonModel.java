package model.ui;

public class PlayerButtonModel extends CustomButtonModel {

    private boolean alreadyThere;
    private boolean selected;

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


    public void setAlreadyThere(boolean alreadyThere) {
        this.alreadyThere = alreadyThere;
    }

    public boolean isAlreadyThere() {
        return alreadyThere;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
