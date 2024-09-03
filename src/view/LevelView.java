package view;

import model.LevelModel;
import model.objects.items.powerups.PowerUpsManagerModel;
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

    private int bombTick = 0;
    private int bombTimer = 360;
    private Color[] colors = {Color.RED, null, Color.YELLOW};
    private int colorIndex = 0;

    public LevelView() {
        lvlSprites = LoadSave.importSprites();
        lvlManager = LevelManagerModel.getInstance();
        levels = lvlManager.getLevels();
    }

    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        checkAndUpdateBombDrawing(g);
        for (int y = 0; y < TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < levels.get(lvlManager.getLvlIndex()).getLvlData()[0].length; x++) {
                int index = levels.get(lvlManager.getLvlIndex()).getSpriteIndex(x, y);
                if (index == 0 || index == 255) continue;

                int rgb = lvlSprites[index - 1].getRGB(3, 3);
                if(!(y == TILES_IN_HEIGHT - 2)) {
                    g.setColor(LoadSave.getDarkenedColor(rgb));
                    for (int i = 0; i < 8; i++)
                        g.fillRect(x * TILES_SIZE + i, y * TILES_SIZE + +i, TILES_SIZE, TILES_SIZE);
                }
                else {
                    g.fillRect(x * TILES_SIZE, y * TILES_SIZE, TILES_SIZE, TILES_SIZE);
                }
                g.drawImage(lvlSprites[index - 1], x * TILES_SIZE, y * TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
            }
        }
    }

    private void checkAndUpdateBombDrawing(Graphics g) {
        if(PowerUpsManagerModel.getInstance().getBombExploding()) {
            if (bombTimer >= 0) {
                if (bombTick >= 15) {
                    bombTimer -= bombTick;
                    bombTick = 0;
                    colorIndex = (colorIndex == colors.length - 1) ? 0 : colorIndex + 1;
                } else {
                    if(colors[colorIndex] != null) {
                        g.setColor(colors[colorIndex]);
                        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
                    }
                }
            } else {
                bombTimer = 360;
                PowerUpsManagerModel.getInstance().setBombExploding(false);
            }
            bombTick++;
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
