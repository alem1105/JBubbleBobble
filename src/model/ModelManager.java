package model;

import model.gamestate.Gamestate;
import model.gamestate.PlayingModel;
import model.gamestate.UserStateModel;

import java.util.Observable;

/**
 * La classe {@code ModelManager} è un singleton che gestisce l'aggiornamento dei modelli di gioco.
 * Estende {@code Observable} per permettere la notifica agli osservatori (la view) quando lo stato cambia.
 */
public class ModelManager extends Observable {

    /** L'istanza singleton della classe {@code ModelManager}. */
    private static ModelManager instance;

    /** Il modello di gioco in esecuzione. */
    private PlayingModel playingModel;

    /**
     * Costruttore privato della classe {@code ModelManager}.
     * Inizializza {@code UserStateModel} e {@code PlayingModel}.
     */
    private ModelManager() {
        UserStateModel.getInstance();
        playingModel = PlayingModel.getInstance();
    }

    /**
     * Restituisce l'istanza singleton della classe {@code ModelManager}.
     *
     * @return l'istanza singleton di {@code ModelManager}.
     */
    public static ModelManager getInstance() {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }

    /**
     * Aggiorna il modello di gioco.
     * Notifica tutti gli osservatori che lo stato è cambiato.
     */
    public void update() {
        if (Gamestate.state == Gamestate.PLAYING)
            playingModel.update();
        setChanged();
        notifyObservers();
    }
}
