package view.stateview;

import model.entities.PlayerModel;
import view.LevelView;
import view.entities.PlayerView;
import view.entities.enemies.EnemiesManagerView;
import view.objects.ProjectileManagerView;
import view.objects.bobbles.BubbleManagerView;
import view.ui.DeathScreenView;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.GameConstants.*;

public class PlayingView {

    private PlayerView playerView;
    private LevelView levelView;
    private EnemiesManagerView enemiesManagerView;
    private DeathScreenView deathScreenView;
    private BufferedImage heartLifeImage;
    private BubbleManagerView bubbleManagerView;
    private ProjectileManagerView projectileManagerView;

    public PlayingView() {
        initViews();
        heartLifeImage = LoadSave.GetSpriteAtlas(LoadSave.HEART_LIFE_BUTTON);
    }

    private void initViews() {
        playerView = new PlayerView(PlayerModel.getInstance());
        levelView = new LevelView();
        enemiesManagerView = new EnemiesManagerView();
        deathScreenView = DeathScreenView.getInstance();
        bubbleManagerView = BubbleManagerView.getInstance();
        projectileManagerView = ProjectileManagerView.getInstance();
    }

    public void render(Graphics g) {
        levelView.render(g);
        enemiesManagerView.render(g);
        playerView.render(g);
        bubbleManagerView.draw(g);
        drawLifeHearts(g);
        if(playerView.getPlayerModel().isGameOver()) {
            deathScreenView.render(g);
        }
        projectileManagerView.updateAndDraw(g);
    }

    public void update(){
        if(!(playerView.getPlayerModel().isGameOver())) {
            playerView.update();
            bubbleManagerView.update();
            enemiesManagerView.update();
        }
        else {
            deathScreenView.update();
        }
    }

    private void drawLifeHearts(Graphics g) {
        for(int live = 0; live < playerView.getPlayerModel().getLives(); live++)
            g.drawImage(heartLifeImage, live * TILES_SIZE, GAME_HEIGHT - TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
    }

}
