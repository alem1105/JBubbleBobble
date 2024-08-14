package model;

import model.entities.PlayerModel;
import model.gamestate.Gamestate;
import model.gamestate.Menu;

import java.util.Observable;

public class ModelManager extends Observable {
    private PlayerModel player;
    private static ModelManager instance;
    private Menu menu;

    private ModelManager() {
        player = PlayerModel.getInstance();
        menu = Menu.getInstance();
    }

    public static ModelManager getInstance() {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }

    public void update() {
        if (menu == null) {
            menu = Menu.getInstance();
        }

        switch (Gamestate.state) {
            case PLAYING -> player.update();
            case MENU -> menu.update();
        }
        setChanged();
        notifyObservers();
    }

    public PlayerModel getPlayer() {
        return player;
    }
}
