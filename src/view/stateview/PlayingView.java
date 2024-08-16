package view.stateview;

import model.entities.PlayerModel;
import view.LevelView;
import view.entities.PlayerView;

import java.awt.*;

public class PlayingView {

    private PlayerView playerView;
    private LevelView levelView;

    public PlayingView() {
        initViews();
    }

    private void initViews() {
        playerView = new PlayerView(PlayerModel.getInstance());
        levelView = new LevelView();
    }

    public void render(Graphics g) {
        levelView.render(g);
        playerView.render(g);
    }

    public void update(){
        playerView.update();
    }

}
