package view;

import model.gamestate.Gamestate;
import view.stateview.*;
import view.utilz.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import static model.utilz.Constants.GameConstants.*;

/**
 * Rappresenta il pannello principale del gioco, gestendo la visualizzazione
 * delle diverse schermate come il gioco, il menu, l'editor di livelli,
 * il selettore di livelli e lo stato dell'utente.
 */
@SuppressWarnings("Deprecated")
public class GamePanel extends JPanel implements Observer {

    // Vista del gioco in corso
    private PlayingView playingView;

    // Vista del menu
    private MenuView menuView;

    // Vista dell'editor di livelli
    private LevelEditorView levelEditorView;

    // Vista del selettore di livelli
    private LevelSelectorView levelSelectorView;

    // Vista dello stato dell'utente
    private UserStateView userStateView;

    /**
     * Costruttore della classe GamePanel.
     * Inizializza il pannello, carica il font personalizzato e le viste necessarie.
     */
    public GamePanel() {
        LoadSave.loadCustomFont();
        setPanelSize();
        setFocusable(true);
        requestFocus();
        loadViews();
    }

    /**
     * Carica le varie viste utilizzate nel gioco.
     */
    private void loadViews() {
        this.playingView = PlayingView.getInstance();
        this.menuView = MenuView.getInstance();
        this.levelEditorView = LevelEditorView.getInstance();
        this.levelSelectorView = LevelSelectorView.getInstance();
        this.userStateView = UserStateView.getInstance();
    }

    /**
     * Imposta le dimensioni del pannello.
     */
    private void setPanelSize() {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
    }

    /**
     * Disegna il contenuto del pannello in base allo stato del gioco corrente.
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (Gamestate.state) {
            case PLAYING -> playingView.render(g);
            case MENU -> menuView.draw(g);
            case LEVEL_EDITOR -> levelEditorView.draw(g);
            case LEVEL_SELECTOR -> levelSelectorView.draw(g);
            case USER -> userStateView.draw(g);
        }
    }

    /**
     * Aggiorna la vista in base alle modifiche e ridisegna il pannello.
     *
     * @param o l'oggetto osservato
     * @param arg argomento dell'aggiornamento
     */
    @Override
    public void update(Observable o, Object arg) {
        switch (Gamestate.state) {
            case PLAYING -> playingView.update();
            case MENU -> menuView.update();
            case LEVEL_EDITOR -> levelEditorView.update();
            case LEVEL_SELECTOR -> levelSelectorView.update();
            case USER -> userStateView.update();
        }
        repaint();
    }
}

