package model.ui;

import model.LevelManager;

import javax.imageio.ImageIO;

import static model.utilz.Constants.GameConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SaveButtonModel extends CustomButtonModel {

    public SaveButtonModel(int x, int y, int width, int height) {
        super (x, y, width, height);
    }

    public void saveNewLevelImage(int[][] lvlData, int[][] enemiesData, Point playerSpawn, int levelIndex) {
        BufferedImage image = new BufferedImage(TILES_IN_WIDTH, TILES_IN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < TILES_IN_HEIGHT; i++) {
            for(int j = 0; j < TILES_IN_WIDTH; j++) {
                Color color = getColor(lvlData, enemiesData, playerSpawn, i, j);
                image.setRGB(j, i, color.getRGB());
            }
        }

        try {
            File outputfile = new File("./res/lvls/" + levelIndex + ".png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            System.out.println("ERRORE NEL SALVATAGGIO IMMAGINE");
        }
    }

    private Color getColor(int[][] lvlData, int[][] enemiesData, Point playerSpawn, int y, int x) {
        int r = lvlData[y][x];
        int g = enemiesData[y][x];
        int b = 0;
        if(x == (playerSpawn.x / TILES_SIZE) && y == (playerSpawn.y / TILES_SIZE)) {
            r = 0;
            g = 0;
            b = 255;
        }
        else if ((r == 255 || r == 0) && g == 0 && b == 0) {
            r = 255;
            g = 255;
            b = 255;
        }
        Color color = new Color(r,g,b);
        return color;
    }

}
