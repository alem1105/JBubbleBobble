package view;

import model.LevelModel;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import model.LevelManager;

import static model.utilz.Constants.GameConstants.*;

public class LevelView {

    private BufferedImage[] lvlSprites;
    private ArrayList<LevelModel> levels;
    private LevelManager lvlManager;

    public LevelView() {
        importSprites();
        lvlManager = LevelManager.getInstance();
        levels = lvlManager.getLevels();
    }

    private void importSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_SPRITE);
        int index = 0;
        lvlSprites = new BufferedImage[230];
        for (int j = 0; j < 46; j++) {
            for (int i = 0; i < 5; i++) {
                lvlSprites[index] = img.getSubimage(i * 16, j * 16, 16, 16);
                index++;
            }
        }
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




}
