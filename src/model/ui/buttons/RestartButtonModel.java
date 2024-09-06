package model.ui.buttons;

import model.utilz.UtilityMethods;

public class RestartButtonModel extends CustomButtonModel{
    public RestartButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void restart(){
        UtilityMethods.resetAll();
    }
}