package model;

import model.gamestate.Gamestate;
import model.gamestate.MenuModel;
import model.gamestate.PlayingModel;
import model.gamestate.UserStateModel;

import java.util.Observable;

@SuppressWarnings("Deprecated")
public class ModelManager extends Observable {

    private static ModelManager instance;

    private PlayingModel playingModel;
    private MenuModel menuModel;

    private ModelManager() {
        UserStateModel.getInstance();
        playingModel = PlayingModel.getInstance();
        menuModel = MenuModel.getInstance();
    }

    public static ModelManager getInstance() {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }

    public void update() {
        switch (Gamestate.state) {
            case PLAYING -> {
                if (!playingModel.getPlayer().isGameOver())
                    playingModel.update();
            }
            case MENU -> menuModel.update();
        }
        setChanged();
        notifyObservers();
    }

}
