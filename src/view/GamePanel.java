package view;

import model.gamestate.Gamestate;
import view.stateview.*;
import view.utilz.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import static model.utilz.Constants.GameConstants.*;

@SuppressWarnings("Deprecated")
public class GamePanel extends JPanel implements Observer {

    private PlayingView playingView;
    private MenuView menuView;
    private LevelEditorView levelEditorView;
    private LevelSelectorView levelSelectorView;
    private UserStateView userStateView;

    public GamePanel() {
        LoadSave.loadCustomFont();
        setPanelSize();
        setFocusable(true);
        requestFocus();
        loadViews();
    }

    private void loadViews() {
        this.playingView = PlayingView.getInstance();
        this.menuView = MenuView.getInstance();
        this.levelEditorView = LevelEditorView.getInstance();
        this.levelSelectorView = LevelSelectorView.getInstance();
        this.userStateView = UserStateView.getInstance();
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
            case USER -> userStateView.draw(g);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        switch(Gamestate.state) {
            case PLAYING -> playingView.update();
            case MENU -> menuView.update();
            case LEVEL_EDITOR -> levelEditorView.update();
            case LEVEL_SELECTOR -> levelSelectorView.update();
            case USER -> userStateView.update();
        }
        repaint();
    }
}
