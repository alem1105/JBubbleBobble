package view.stateview;

import model.LevelManagerModel;
import model.UserModel;
import model.gamestate.PlayingModel;
import model.gamestate.UserStateModel;
import view.LevelView;
import view.entities.PlayerView;
import view.entities.enemies.EnemiesManagerView;
import view.objects.items.PowerUpManagerView;
import view.objects.projectiles.ProjectileManagerView;
import view.objects.bobbles.BubbleManagerView;
import view.ui.DeathScreenView;
import view.ui.GamePausedScreenView;
import view.ui.NextLevelScreenView;
import view.utilz.AudioManager;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import static model.utilz.Constants.GameConstants.*;
import static view.utilz.AudioManager.*;

public class PlayingView {

    private static PlayingView instance;

    private PlayerView playerView;
    private LevelView levelView;
    private EnemiesManagerView enemiesManagerView;
    private DeathScreenView deathScreenView;
    private BufferedImage heartLifeImage;
    private BubbleManagerView bubbleManagerView;
    private ProjectileManagerView projectileManagerView;
    private NextLevelScreenView nextLevelScreenView;
    private PowerUpManagerView powerUpManagerView;
    private GamePausedScreenView gamePausedScreenView;

    private DecimalFormat decFormat = new DecimalFormat("00");

    public static PlayingView getInstance() {
        if (instance == null) {
            instance = new PlayingView();
        }
        return instance;
    }

    private PlayingView() {
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
        gamePausedScreenView = GamePausedScreenView.getInstance();
    }

    public void render(Graphics g) {

        if (LevelManagerModel.getInstance().isNextLevel()){
            nextLevelScreenView.render(g);
            return;
        }

        levelView.render(g);
        bubbleManagerView.draw(g);
        projectileManagerView.updateAndDraw(g);
        enemiesManagerView.render(g);
        powerUpManagerView.draw(g);
        playerView.render(g);
        drawLifeHearts(g);
        drawStats(g);

        if (playerView.getPlayerModel().isGameOver())
            deathScreenView.render(g);
        else if (PlayingModel.getInstance().isPaused())
            gamePausedScreenView.render(g);

    }

    public void update(){
        if(playerView.getPlayerModel().isGameOver()) {
            AudioManager.getInstance().continuousSoundPlay(GAME_OVER_INDEX);
            deathScreenView.update();
            return;
        }

        if (LevelManagerModel.getInstance().isNextLevel()){
            nextLevelScreenView.update();
            return;
        }

        if (PlayingModel.getInstance().isPaused()){
            gamePausedScreenView.update();
            return;
        }

        if (!gamePausedScreenView.isJustEnteredInPauseScreen())
            gamePausedScreenView.resetPauseScreen();

        chooseTrackToPlayBasedOnLevel();

        playerView.update();
        bubbleManagerView.update();
        enemiesManagerView.update();
        powerUpManagerView.update();
    }

    private void chooseTrackToPlayBasedOnLevel() {
        if (LevelManagerModel.getInstance().getLvlIndex() + 1 == LevelManagerModel.getInstance().getLevels().size())
            AudioManager.getInstance().continuousSoundPlay(SUPER_DRUNK_INDEX);
        else
            AudioManager.getInstance().continuousSoundPlay(MAIN_THEME_INDEX);
    }

    private void drawLifeHearts(Graphics g) {
        for(int live = 0; live < playerView.getPlayerModel().getLives(); live++)
            g.drawImage(heartLifeImage, live * TILES_SIZE, GAME_HEIGHT - TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
    }

    private void drawStats(Graphics g) {
        UserModel currentUser = UserStateModel.getInstance().getCurrentUserModel();
        g.setColor(Color.WHITE);
        g.setFont(LoadSave.NES_FONT);
        FontMetrics measures = g.getFontMetrics();
        g.drawString(String.valueOf(currentUser.getTempScore()), GAME_WIDTH - TILES_SIZE * 10 - measures.stringWidth(String.valueOf(currentUser.getTempScore())), GAME_HEIGHT - (int) (5 * SCALE));
        String levelStat = decFormat.format(LevelManagerModel.getInstance().getLvlIndex() + 1);
        g.drawString(levelStat, GAME_WIDTH - TILES_SIZE * 7 - measures.stringWidth(levelStat), GAME_HEIGHT - (int) (5 * SCALE));
        g.setColor(Color.RED);
        g.drawString(String.valueOf(currentUser.getMaxScore()), GAME_WIDTH - measures.stringWidth(String.valueOf(currentUser.getMaxScore())) - TILES_SIZE, GAME_HEIGHT - (int) (5 * SCALE));
    }
}
