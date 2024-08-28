package model.gamestate;

public class MenuModel {

    private static MenuModel instance;

    public static MenuModel getInstance() {
        if (instance == null) {
            instance = new MenuModel();
        }
        return instance;
    }

    private MenuModel() {
    }

    public void update() {

    }

}
