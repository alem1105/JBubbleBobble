package model;

import model.entities.enemies.EnemyModel;
import model.entities.enemies.MaitaModel;
import model.entities.enemies.MonstaModel;
import model.entities.enemies.ZenChanModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import static model.utilz.Constants.GameConstants.TILES_SIZE;

public class LevelModel {
    private int[][] lvlData, enemiesData;
    private BufferedImage lvlImg;
    private ArrayList<ZenChanModel> zenChans;
    private ArrayList<MaitaModel> maitas;
    private ArrayList<MonstaModel> monstas;
    private Point playerSpawn;

    public LevelModel(BufferedImage lvlImg) {
        this.lvlImg = lvlImg;
        loadLvlData();
        loadEnemies();
        loadPlayerSpawn();
    }

    public void loadEnemies() {
        enemiesData = new int[lvlImg.getHeight()][lvlImg.getWidth()];
        zenChans = new ArrayList<>();
        maitas = new ArrayList<>();
        monstas = new ArrayList<>();
        for(int y = 0; y < lvlImg.getHeight(); y++) {
            for(int x = 0; x < lvlImg.getWidth(); x++) {
                Color color = new Color(lvlImg.getRGB(x, y));
                if (color.getRed() != 255 && color.getBlue() != 255) {
                    switch (color.getGreen()) {
                        case 255 -> {
                            zenChans.add(new ZenChanModel(x * TILES_SIZE, y * TILES_SIZE));
                            enemiesData[y][x] = 255;
                        }
                        case 254 -> {
                            maitas.add(new MaitaModel(x * TILES_SIZE, y * TILES_SIZE));
                            enemiesData[y][x] = 254;
                        }
                        case 253 -> {
                            monstas.add(new MonstaModel(x * TILES_SIZE, y * TILES_SIZE));
                            enemiesData[y][x] = 253;
                        }
                    }
                }
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
                    playerSpawn = new Point(x * TILES_SIZE, y * TILES_SIZE);
            }
        }
    }

    public int[][] getEnemiesData() {
        return enemiesData;
    }

    public int getEnemyIndex(int x, int y) {
        return enemiesData[y][x];
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

    public ArrayList<ZenChanModel> getZenChans() {
        return zenChans;
    }

    public ArrayList<MaitaModel> getMaitas() {
        return maitas;
    }

    public ArrayList<MonstaModel> getMonstas() {
        return monstas;
    }

    public void setPlayerSpawn(Point playerSpawn) {
        this.playerSpawn = playerSpawn;
    }

    public void setLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    public void setEnemiesData(int[][] enemiesData){
        this.enemiesData = enemiesData;
    }
}
