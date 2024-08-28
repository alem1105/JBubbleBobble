package view.ui;

import model.LevelManagerModel;
import model.LevelModel;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.awt.Color.*;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.SCALE;

public class NextLevelScreenView {

    private static NextLevelScreenView instance;
    private LevelManagerModel levelManager;
    private BufferedImage[] lvlSprites;
    private int time = 0;
    private int[][] prevLevelData;
    private int [][] nextLevelData;
    private int prevY = 0, nextY = GAME_HEIGHT - TILES_SIZE;

    public static NextLevelScreenView getInstance() {
        if (instance == null) {
            instance = new NextLevelScreenView();
        }
        return instance;
    }

    private NextLevelScreenView(){
        levelManager = LevelManagerModel.getInstance();
        lvlSprites = LoadSave.importSprites();
    }

    public void update(){
        if (nextY <= 0){
            resetNextScreenView();
            return;
        }
        if (time == 0)
            getData();
        else {
            nextY -= 4;
            prevY -= 4;
        }
        time++;
    }

    private void getData() {
        prevLevelData = levelManager.getLevels().get(levelManager.getLvlIndex() - 1).getLvlData();
        nextLevelData = levelManager.getLevels().get(levelManager.getLvlIndex()).getLvlData();
    }

    public void resetNextScreenView(){
        time = 0;
        nextY = GAME_HEIGHT - TILES_SIZE;
        prevY = 0;
        levelManager.setNextLevel(false);
    }

    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        renderData(g, prevLevelData, prevY);
        renderData(g, nextLevelData, nextY);
    }

    private void renderData(Graphics g, int[][] lvlData, int lvlY){
        for (int y = 0; y < TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < lvlData[0].length; x++) {
                int index = lvlData[y][x];
                if (index == 0 || index == 255) continue;
                g.drawImage(lvlSprites[index - 1], x * TILES_SIZE, (y * TILES_SIZE) + lvlY , TILES_SIZE, TILES_SIZE, null);
            }
        }
    }
}
