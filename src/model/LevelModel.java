package model;

import model.entities.enemies.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.utilz.Constants.GameConstants.TILES_SIZE;

import java.awt.Color;
import java.awt.Point;

/**
 * Classe che rappresenta un livello del gioco, contenente dati sul livello,
 * dati sui nemici e la posizione di spawn del giocatore.
 */
public class LevelModel {
    private int[][] lvlData, enemiesData;
    private BufferedImage lvlImg;
    private ArrayList<ZenChanModel> zenChans;
    private ArrayList<MaitaModel> maitas;
    private ArrayList<MonstaModel> monstas;
    private ArrayList<DrunkModel> drunks;
    private ArrayList<InvaderModel> invaders;
    private ArrayList<HidegonsModel> hidegons;
    private Point playerSpawn;

    /**
     * Costruttore della classe LevelModel.
     *
     * @param lvlImg L'immagine del livello da caricare.
     */
    public LevelModel(BufferedImage lvlImg) {
        this.lvlImg = lvlImg;
        loadLvlData();
        loadEnemies();
        loadPlayerSpawn();
    }

    /**
     * Carica i dati dei nemici dal livello.
     * Crea e aggiunge i nemici alle rispettive liste in base al colore dei pixel.
     */
    public void loadEnemies() {
        enemiesData = new int[lvlImg.getHeight()][lvlImg.getWidth()];
        zenChans = new ArrayList<>();
        maitas = new ArrayList<>();
        monstas = new ArrayList<>();
        drunks = new ArrayList<>();
        invaders = new ArrayList<>();
        hidegons = new ArrayList<>();

        for (int y = 0; y < lvlImg.getHeight(); y++) {
            for (int x = 0; x < lvlImg.getWidth(); x++) {
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
                        case 252 -> {
                            invaders.add(new InvaderModel(x * TILES_SIZE, y * TILES_SIZE));
                            enemiesData[y][x] = 252;
                        }
                        case 251 -> {
                            hidegons.add(new HidegonsModel(x * TILES_SIZE, y * TILES_SIZE));
                            enemiesData[y][x] = 251;
                        }
                        case 250 -> {
                            drunks.add(new DrunkModel(x * TILES_SIZE, y * TILES_SIZE));
                            enemiesData[y][x] = 250;
                        }
                    }
                }
            }
        }
    }

    /**
     * Carica i dati del livello dall'immagine.
     * Ogni pixel rosso dell'immagine rappresenta una tile del livello.
     */
    private void loadLvlData() {
        lvlData = new int[lvlImg.getHeight()][lvlImg.getWidth()];

        for (int y = 0; y < lvlImg.getHeight(); y++) {
            for (int x = 0; x < lvlImg.getWidth(); x++) {
                Color color = new Color(lvlImg.getRGB(x, y));
                lvlData[y][x] = color.getRed();
            }
        }
    }

    /**
     * Carica la posizione di spawn del giocatore dal livello.
     * La posizione di spawn è determinata dal pixel blu dell'immagine.
     */
    private void loadPlayerSpawn() {
        playerSpawn = new Point(lvlImg.getWidth() / 2, lvlImg.getHeight() / 2);

        for (int y = 0; y < lvlImg.getHeight(); y++) {
            for (int x = 0; x < lvlImg.getWidth(); x++) {
                Color color = new Color(lvlImg.getRGB(x, y));
                if (color.getBlue() == 255 && color.getRed() != 255 && color.getGreen() != 255)
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

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public ArrayList<ZenChanModel> getZenChans() {
        return zenChans;
    }

    public ArrayList<MaitaModel> getMaitas() {
        return maitas;
    }

    public ArrayList<InvaderModel> getInvaders() {
        return invaders;
    }

    public ArrayList<MonstaModel> getMonstas() {
        return monstas;
    }

    public ArrayList<DrunkModel> getDrunks() {
        return drunks;
    }

    public ArrayList<HidegonsModel> getHidegons() {
        return hidegons;
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
