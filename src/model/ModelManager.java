package model;

import model.gamestate.Gamestate;
import model.gamestate.PlayingModel;
import model.gamestate.UserStateModel;

import java.util.Observable;

import static model.gamestate.Gamestate.PLAYING;

@SuppressWarnings("Deprecated")
public class ModelManager extends Observable {

    private static ModelManager instance;

    private PlayingModel playingModel;

    private ModelManager() {
        UserStateModel.getInstance();
        playingModel = PlayingModel.getInstance();
    }

    public static ModelManager getInstance() {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }

    public void update() {
        if (Gamestate.state == PLAYING) {
            playingModel.update();
        }
        setChanged();
        notifyObservers();
    }

}
