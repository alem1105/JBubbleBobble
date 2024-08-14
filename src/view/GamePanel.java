package view;

import model.entities.PlayerModel;
import model.gamestate.Gamestate;
import view.entities.PlayerView;
import view.stateview.MenuView;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import static model.utilz.Constants.GameConstants.*;

public class GamePanel extends JPanel implements Observer {

    private PlayerView playerView;
    private MenuView menuView;

    public GamePanel() {
        setPanelSize();
        setFocusable(true);
        requestFocusInWindow();
        loadViews();
    }

    private void loadViews() {
        this.playerView = new PlayerView(PlayerModel.getInstance());
        this.menuView = new MenuView();
    }

    private void setPanelSize() {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch(Gamestate.state) {
            case PLAYING -> playerView.render(g);
            case MENU -> menuView.draw(g);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        switch(Gamestate.state) {
            case PLAYING -> playerView.updateAnimationTick(); // Ci va la playing
            case MENU -> System.out.println("ci va animation click");
        }
        repaint();
    }
}
