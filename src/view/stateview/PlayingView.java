package view.stateview;

import model.LevelManagerModel;
import model.UserModel;
import model.gamestate.UserStateModel;
import view.LevelView;
import view.entities.PlayerView;
import view.entities.enemies.EnemiesManagerView;
import view.objects.PowerUpManagerView;
import view.objects.PowerUpView;
import view.objects.projectiles.ProjectileManagerView;
import view.objects.bobbles.BubbleManagerView;
import view.ui.DeathScreenView;
import view.ui.NextLevelScreenView;
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
    private NextLevelScreenView nextLevelScreenView;
    private PowerUpManagerView powerUpManagerView;

    public PlayingView() {
        initViews();
        heartLifeImage = LoadSave.GetSpriteAtlas(LoadSave.HEART_LIFE_BUTTON);
    }

    private void initViews() {
        playerView = PlayerView.getInstance();
        levelView = new LevelView();
        enemiesManagerView = EnemiesManagerView.getInstance();
        deathScreenView = DeathScreenView.getInstance();
        bubbleManagerView = BubbleManagerView.getInstance();
        projectileManagerView = ProjectileManagerView.getInstance();
        nextLevelScreenView = NextLevelScreenView.getInstance();
        powerUpManagerView = PowerUpManagerView.getInstance();
    }

    public void render(Graphics g) {
        if (playerView.getPlayerModel().isGameOver()){
            deathScreenView.render(g);
        } else {
            if (LevelManagerModel.getInstance().isNextLevel()){
                nextLevelScreenView.render(g);
            }
            else {
                levelView.render(g);
                playerView.render(g);
                bubbleManagerView.draw(g);
                projectileManagerView.updateAndDraw(g);
                enemiesManagerView.render(g);
                powerUpManagerView.draw(g);
                drawLifeHearts(g);
                drawStats(g);
            }
        }
    }

    public void update(){
        if(playerView.getPlayerModel().isGameOver()) {
            deathScreenView.update();
        }
        else {
            if (LevelManagerModel.getInstance().isNextLevel()){
                nextLevelScreenView.update();
            }
            playerView.update();
            bubbleManagerView.update();
            enemiesManagerView.update();
        }
    }

    private void drawLifeHearts(Graphics g) {
        for(int live = 0; live < playerView.getPlayerModel().getLives(); live++)
            g.drawImage(heartLifeImage, live * TILES_SIZE, GAME_HEIGHT - TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
    }

    private void drawStats(Graphics g) {
        UserModel currentUser = UserStateModel.getInstance().getCurrentUserModel();
        g.setColor(Color.WHITE);
        g.setFont(LoadSave.BUBBLE_BOBBLE_FONT);
        g.drawString(String.valueOf(currentUser.getTempScore()), TILES_SIZE * 5, GAME_HEIGHT);
        g.setColor(Color.RED);
        g.drawString(String.valueOf(currentUser.getMaxScore()), TILES_SIZE * 13, GAME_HEIGHT);
    }


}
