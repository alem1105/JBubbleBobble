package model.ui.buttons;

import controller.GameController;

public class RestartButtonModel extends CustomButtonModel{
    public RestartButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void restart(){
        new GameController();
        System.exit(0);
    }
}