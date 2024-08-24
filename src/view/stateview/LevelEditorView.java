package view.stateview;

import model.LevelManager;
import model.ui.buttons.*;
import view.ui.buttons.*;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static model.utilz.Constants.GameConstants.*;

public class LevelEditorView {
    private static LevelEditorView instance;

    private LevelManager levelManager;
    private int drawOffset = (int)(5 * SCALE);

    private int levelIndex;
    private int blockIndex = 1;
    private int enemyIndex = 0;

    private BlockButtonView[] buttons;
    private EnemyButtonView[] enemies;
    private SaveButtonView saveButton;
    private XButtonView XButton;
    private EraserButtonView eraserButton;
    private PlayerButtonView playerButton;

    private int levelHeight = TILES_IN_HEIGHT * (TILES_SIZE - drawOffset);
    private int levelWidth;

    private BufferedImage[] blocksImages; // contiene le immagini di tutte le tile/blocchi
    private BufferedImage[] enemiesImages; // contiene immagini dei nemici (una riga)

    private LevelEditorView() {
        levelManager = LevelManager.getInstance();
        levelWidth = levelManager.getLevels().get(levelIndex).getLvlData()[0].length * (TILES_SIZE - drawOffset);
        initButtons();
        XButton.getButtonModel().updateData(levelIndex);
    }

    public static LevelEditorView getInstance() {
        if (instance == null) {
            instance = new LevelEditorView();
        }
        return instance;
    }

    private void initButtons() {
        initTileButtons();
        playerButton = PlayerButtonView.getInstance(
                PlayerButtonModel.getInstance(
                        levelWidth + (int) (10 * SCALE) + (int) (30 * SCALE),
                        (int)(90 * SCALE) + (int)(25 * SCALE),
                        (int) (24 * SCALE),
                        (int) (24 * SCALE)
                ), LoadSave.GetSpriteAtlas(LoadSave.PLAYER_BUTTON)
        );
        saveButton = new SaveButtonView(new SaveButtonModel(levelWidth + (int) (58 * SCALE),
                levelHeight + (int) (46 * SCALE),
                (int) (32 * SCALE),
                (int) (32 * SCALE)));
        XButton = new XButtonView(new XButtonModel(levelWidth + (int) (23 * SCALE),
                levelHeight + (int) (46 * SCALE),
                (int) (32 * SCALE),
                (int) (32 * SCALE)));
        eraserButton = new EraserButtonView(new EraserButtonModel(levelWidth + (int) (58 * SCALE),
                levelHeight + (int) (11 * SCALE),
                (int) (32 * SCALE),
                (int) (32 * SCALE)));
        initEnemyButtons();
    }

    private void initEnemyButtons() {
        BufferedImage[][] enemiesImagesMatrix = LoadSave.loadAnimations(LoadSave.ENEMIES_BUTTON, 1, 6, 18);
        enemiesImages = enemiesImagesMatrix[0];
        enemies = new EnemyButtonView[6];
        int y = 0;
        int x = 0;
        for (int i = 0; i < enemies.length; i++) {
            if(x >= 3) {
                x = 0;
                y++;
            }
            enemies[i] = new EnemyButtonView(new EnemyButtonModel(levelWidth + (int)(x++ * (30 * SCALE)) + (int)(10 * SCALE),
                    (int)(30 * y * SCALE) + (int) (25 * SCALE),
                    (int)(24 * SCALE),
                    (int)(24 * SCALE), 255 - i), enemiesImages[i]);
        }
    }

    public void initTileButtons(){
        blocksImages = LoadSave.importSprites();
        blocksImages = Arrays.copyOf(blocksImages, 27);
        buttons = new BlockButtonView[27];
        int y = 0;
        int x = 0;

        for (int i = 0; i < blocksImages.length; i++) {
            if(x >= 9) {
                x = 0;
                y++;
            }
            // TODO SISTEMARE
            buttons[i] = new BlockButtonView(new BlockButtonModel(10 + x++ * (int)(24 * SCALE),
                    levelHeight + 10 + ((int)(18 * y * SCALE)),
                    (int)(16 * SCALE),
                    (int)(16 * SCALE), i + 1), blocksImages[i]);
        }
    }

    public void update() {
        playerButton.update();
        saveButton.update();
        XButton.update();
        eraserButton.update();
    }

    public void draw(Graphics g) {
        drawLevel(g);
        drawGrid(g);
        drawButtons(g);
    }

    private void drawButtons(Graphics g) {
        for(BlockButtonView button : buttons) {
            button.draw(g);
        }
        playerButton.draw(g);
        saveButton.draw(g);
        XButton.draw(g);
        eraserButton.draw(g);
        for (EnemyButtonView button : enemies) {
            button.draw(g);
        }
    }

    public void drawLevel(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        drawTilesAndEnemies(g);
        drawPlayer(g);
    }

    public void drawPlayer(Graphics g) {
        Point playerSpawn = LevelManager.getInstance().getLevels().get(levelIndex).getPlayerSpawn();
        g.drawImage(playerButton.getImageButton(),
                (playerSpawn.x / TILES_SIZE) * (TILES_SIZE - drawOffset),
                (playerSpawn.y / TILES_SIZE) * (TILES_SIZE - drawOffset),
                TILES_SIZE - drawOffset,
                TILES_SIZE - drawOffset,
                null);
    }

    public void drawTilesAndEnemies(Graphics g) {
        for (int y = 0; y < TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < levelManager.getLevels().get(levelIndex).getLvlData()[0].length; x++) {
                int blockIndex = levelManager.getLevels().get(levelIndex).getSpriteIndex(x, y);
                int enemyIndex = levelManager.getLevels().get(levelIndex).getEnemyIndex(x, y);

                if (blockIndex > 0 && blockIndex < 255) {
                    g.drawImage(blocksImages[blockIndex - 1],
                            x * (TILES_SIZE - drawOffset),
                            y * (TILES_SIZE - drawOffset),
                            TILES_SIZE - drawOffset,
                            TILES_SIZE - drawOffset,
                            null);
                    continue;
                }

                if (enemyIndex >= 250) {
                    g.drawImage(enemiesImages[255 - enemyIndex],
                            x * (TILES_SIZE - drawOffset),
                            y * (TILES_SIZE - drawOffset),
                            TILES_SIZE - drawOffset,
                            TILES_SIZE - drawOffset,
                            null);
                }

            }
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.RED);

        for (int x = 0; x <= levelWidth; x += (TILES_SIZE - drawOffset)) {
            g.drawLine(x, 0, x, levelHeight);
        }

        for (int y = 0; y <= levelHeight; y += (TILES_SIZE - drawOffset)) {
            g.drawLine(0, y, levelWidth, y);
        }
    }

    public int getDrawOffset() {
        return drawOffset;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public BlockButtonView[] getButtons() {
        return buttons;
    }

    public SaveButtonView getSaveButtonView() {
        return saveButton;
    }

    public XButtonView getXButtonView() {
        return XButton;
    }

    public void setBlockIndex(int blockIndex) {
        this.blockIndex = blockIndex;
    }

    public EraserButtonView getEraserButtonView() {
        return eraserButton;
    }

    public int getEnemyIndex() {
        return enemyIndex;
    }

    public EnemyButtonView[] getEnemies() {
        return enemies;
    }

    public void setEnemyIndex(int enemyIndex) {
        this.enemyIndex = enemyIndex;
    }

    public PlayerButtonView getPlayerButtonView() {
        return playerButton;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public int getLevelHeight() {
        return levelHeight;
    }

    public int getLevelWidth() {
        return levelWidth;
    }
    
    public void setLevelIndex(int index){
        levelIndex = index;
    }
}
