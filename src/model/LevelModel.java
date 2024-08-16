package model;

import model.entities.EnemyModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.GameConstants.TILES_SIZE;

public class LevelModel {
    private int[][] lvlData;
    private BufferedImage lvlImg;
    private ArrayList<EnemyModel> enemies;
    private Point playerSpawn;

    public LevelModel(BufferedImage lvlImg) {
        this.lvlImg = lvlImg;
        loadLvlData();
        loadEnemies();
        loadPlayerSpawn();
    }

    private void loadEnemies() {
        enemies = new ArrayList<>();

        for(int y = 0; y < lvlImg.getHeight(); y++) {
            for(int x = 0; x < lvlImg.getWidth(); x++) {
                Color color = new Color(lvlImg.getRGB(x, y));
                if(color.getGreen() == 255)
                    System.out.println("nemico trovato");
            }
        }
    }

    private void loadLvlData() {
        lvlData = new int[lvlImg.getHeight()][lvlImg.getWidth()];

        for(int y = 0; y < lvlImg.getHeight(); y++) {
            for(int x = 0; x < lvlImg.getWidth(); x++) {
                Color color = new Color(lvlImg.getRGB(x, y));
                lvlData[y][x] = color.getRed();
            }
        }
    }

    private void loadPlayerSpawn() {
        for(int y = 0; y < lvlImg.getHeight(); y++) {
            for(int x = 0; x < lvlImg.getWidth(); x++) {
                Color color = new Color(lvlImg.getRGB(x, y));
                if(color.getBlue() == 255 && color.getRed() != 255 && color.getGreen() != 255)
                    playerSpawn = new Point((int) (x * TILES_SIZE), (int) (y * TILES_SIZE));
            }
        }
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public BufferedImage getLvlImg() {
        return lvlImg;
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }
}
