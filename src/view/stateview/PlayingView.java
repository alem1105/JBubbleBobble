package view.stateview;

import model.entities.PlayerModel;
import view.LevelView;
import view.entities.PlayerView;
import view.entities.enemies.EnemiesManagerView;

import java.awt.*;

public class PlayingView {

    private PlayerView playerView;
    private LevelView levelView;
    private EnemiesManagerView enemiesManagerView;

    public PlayingView() {
        initViews();
    }

    private void initViews() {
        playerView = new PlayerView(PlayerModel.getInstance());
        levelView = new LevelView();
        enemiesManagerView = new EnemiesManagerView();
    }

    public void render(Graphics g) {
        levelView.render(g);
        enemiesManagerView.render(g);
        playerView.render(g);
    }

    public void update(){
        playerView.update();
        enemiesManagerView.update();
    }

}
