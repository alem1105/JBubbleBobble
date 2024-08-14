package model.gamestate;

public class Menu {

    private static Menu instance;

    public static Menu getInstance() {
        if (instance == null) {
            instance = new Menu();
        }
        return instance;
    }

    private Menu() {
    }

    public void update() {

    }

}
