package model.ui;

import model.entities.PlayerModel;

import javax.swing.*;

public class PlayerButtonModel extends CustomButtonModel {

    private boolean alreadyThere;
    private boolean selected;

    public PlayerButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
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
