package model;

import model.entities.EnemyModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelModel {
    private int[][] lvlData;
    private BufferedImage lvlImg;
    private ArrayList<EnemyModel> enemies;
    private Point playerSpawn;

    public LevelModel(BufferedImage lvlImg) {
        this.lvlImg = lvlImg;
        loadLvlData();
        loadEnemies();
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
