package view;

import model.LevelManager;
import model.ui.BlockButtonModel;
import model.ui.SaveButtonModel;
import model.ui.XButtonModel;
import view.ui.BlockButtonView;
import view.ui.SaveButtonView;
import view.ui.XButtonView;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static model.utilz.Constants.GameConstants.*;

public class LevelEditorView {
    private static LevelEditorView instance;

    private LevelManager levelManager;
    private int drawOffset = 10;
    private BlockButtonView[] buttons;
    private int blockIndex = 0;
    private SaveButtonView saveButton;
    private XButtonView XButton;

    private int levelHeight = TILES_IN_HEIGHT * (TILES_SIZE - drawOffset);
    private int levelWidth;

    private LevelEditorView() {
        levelManager = LevelManager.getInstance();
        levelWidth = levelManager.getLevels().get(levelManager.getLvlIndex()).getLvlData()[0].length * (TILES_SIZE - drawOffset);
        initButtons();
    }

    public static LevelEditorView getInstance() {
        if (instance == null) {
            instance = new LevelEditorView();
        }
        return instance;
    }

    private void initButtons() {
        initTileButtons();
        saveButton = new SaveButtonView(new SaveButtonModel(levelWidth, levelHeight + 10, (int) (18 * SCALE), (int) (18 * SCALE)));
        XButton = new XButtonView(new XButtonModel(levelWidth + 10 + (int) (18 * SCALE), levelHeight + 10, (int) (18 * SCALE), (int) (18 * SCALE)));
    }

    public void initTileButtons(){
        BufferedImage[] blocksImages = LoadSave.importSprites();
        blocksImages = Arrays.copyOf(blocksImages, 27);
        buttons = new BlockButtonView[27];
        int y = 0;
        int i = 0;
        int x = 0;

        for (BufferedImage blocksImage : blocksImages) {
            if(x >= 9) {
                x = 0;
                y++;
            }
            // TODO SISTEMARE
            buttons[i] = new BlockButtonView(new BlockButtonModel(10 + x++ * (int)(24 * SCALE),
                    levelHeight + 10 + ((int)(18 * y * SCALE)),
                    (int)(16 * SCALE),
                    (int)(16 * SCALE), i), blocksImages[i]);
            i++;
        }
    }

    public void update() {
        saveButton.update();
        XButton.update();
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
        saveButton.draw(g);
        XButton.draw(g);
    }

    public void drawLevel(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        for (int y = 0; y < TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < levelManager.getLevels().get(levelManager.getLvlIndex()).getLvlData()[0].length; x++) {
                int index = levelManager.getLevels().get(levelManager.getLvlIndex()).getSpriteIndex(x, y);
                if (index == 0 || index == 255) continue;

                g.drawImage(LoadSave.importSprites()[index - 1],
                        x * (TILES_SIZE - drawOffset),
                        y * (TILES_SIZE - drawOffset),
                        TILES_SIZE - drawOffset, TILES_SIZE - drawOffset, null);
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
}
