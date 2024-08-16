package model;

import model.entities.PlayerModel;
import model.gamestate.Gamestate;
import model.gamestate.Menu;
import model.gamestate.Playing;

import java.util.Observable;

public class ModelManager extends Observable {

    private static ModelManager instance;

    private Playing playing;
    private Menu menu;

    private ModelManager() {
        playing = Playing.getInstance();
        menu = Menu.getInstance();
    }

    public static ModelManager getInstance() {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }

    public void update() {

        switch (Gamestate.state) {
            case PLAYING -> playing.update();
            case MENU -> menu.update();
        }
        setChanged();
        notifyObservers();
    }

}
