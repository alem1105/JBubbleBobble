package view;

import model.gamestate.Gamestate;
import view.stateview.LevelEditorView;
import view.stateview.LevelSelectorView;
import view.stateview.MenuView;
import view.stateview.PlayingView;
import view.utilz.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import static model.utilz.Constants.GameConstants.*;

public class GamePanel extends JPanel implements Observer {

    private PlayingView playingView;
    private MenuView menuView;
    private LevelEditorView levelEditorView;
    private LevelSelectorView levelSelectorView;

    public GamePanel() {
        LoadSave.loadCustomFont();
        setPanelSize();
        setFocusable(true);
        requestFocus();
        loadViews();
    }

    private void loadViews() {
        this.playingView = new PlayingView();
        this.menuView = MenuView.getInstance();
        this.levelEditorView = LevelEditorView.getInstance();
        this.levelSelectorView = LevelSelectorView.getInstance();
    }

    private void setPanelSize() {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch(Gamestate.state) {
            case PLAYING -> playingView.render(g);
            case MENU -> menuView.draw(g);
            case LEVEL_EDITOR -> levelEditorView.draw(g);
            case LEVEL_SELECTOR -> levelSelectorView.draw(g);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        switch(Gamestate.state) {
            case PLAYING -> playingView.update();
            case MENU -> menuView.update();
            case LEVEL_EDITOR -> levelEditorView.update();
            case LEVEL_SELECTOR -> levelSelectorView.update();
        }
        repaint();
    }
}
