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

                int rgb = lvlSprites[index - 1].getRGB(3, 3);
                if(!(y == TILES_IN_HEIGHT - 2)) {
                    g.setColor(getDarkenedColor(rgb));
                    for(int i = 0; i < 8; i++) {
                        g.fillRect(x * TILES_SIZE + i, y * TILES_SIZE + + i, TILES_SIZE, TILES_SIZE);
                    }
                } else {
                    g.fillRect(x * TILES_SIZE, y * TILES_SIZE, TILES_SIZE, TILES_SIZE);
                }
                g.drawImage(lvlSprites[index - 1], x * TILES_SIZE, y * TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
            }
        }
    }

    private Color getDarkenedColor(int rgb) {
        Color originalColor = new Color(rgb); // Colore RGB originale
        float darkeningFactor = 0.4f; // Riduce il colore a metà della sua intensità
        int red = (int) (originalColor.getRed() * darkeningFactor);
        int green = (int) (originalColor.getGreen() * darkeningFactor);
        int blue = (int) (originalColor.getBlue() * darkeningFactor);
        Color darkColor = new Color(red, green, blue);
        return darkColor;
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
