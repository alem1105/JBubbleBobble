package view;

import model.LevelModel;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import model.LevelManagerModel;

import static model.utilz.Constants.GameConstants.*;

public class LevelView {

    private BufferedImage[] lvlSprites;
    private ArrayList<LevelModel> levels;
    private LevelManagerModel lvlManager;

    public LevelView() {
        lvlSprites = LoadSave.importSprites();
        lvlManager = LevelManagerModel.getInstance();
        levels = lvlManager.getLevels();
    }

    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        for (int y = 0; y < TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < levels.get(lvlManager.getLvlIndex()).getLvlData()[0].length; x++) {
                int index = levels.get(lvlManager.getLvlIndex()).getSpriteIndex(x, y);
                if (index == 0 || index == 255) continue;
                g.drawImage(lvlSprites[index - 1], x * TILES_SIZE, y * TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
            }
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.RED);
        for (int x = 0; x <= GAME_WIDTH; x += TILES_SIZE) {
            g.drawLine(x, 0, x, GAME_HEIGHT);
        }
        for (int y = 0; y <= GAME_HEIGHT; y += TILES_SIZE) {
            g.drawLine(0, y, GAME_WIDTH, y);
        }
    }


}
