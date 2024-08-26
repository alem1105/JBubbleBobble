package model.ui.buttons;

public class QuitButtonModel extends CustomButtonModel{

    public QuitButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void quit(){
        System.exit(0);
    }

}
