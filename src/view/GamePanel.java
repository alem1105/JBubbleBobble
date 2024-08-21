package view;

import model.gamestate.Gamestate;
import view.stateview.MenuView;
import view.stateview.PlayingView;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import static model.utilz.Constants.GameConstants.*;

public class GamePanel extends JPanel implements Observer {

    private PlayingView playingView;
    private MenuView menuView;
    private LevelEditorView levelEditorView;

    public GamePanel() {
        setPanelSize();
        setFocusable(true);
        requestFocus();
        loadViews();
    }

    private void loadViews() {
        this.playingView = new PlayingView();
        this.menuView = new MenuView();
        this.levelEditorView = LevelEditorView.getInstance();
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
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        switch(Gamestate.state) {
            case PLAYING -> playingView.update();
            case MENU -> System.out.println("Sto in GamePanel al posto di menu.update()");
            case LEVEL_EDITOR -> levelEditorView.update();
        }
        repaint();
    }
}
