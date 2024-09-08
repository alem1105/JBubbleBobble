package model.ui.buttons;

import javax.imageio.ImageIO;

import static model.utilz.Constants.GameConstants.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.awt.Color;
import java.awt.Point;

/**
 * Classe che rappresenta un modello di pulsante per salvare un'immagine di un nuovo livello.
 * Questa classe estende {@link CustomButtonModel} e fornisce funzionalità per salvare i dati di un livello in formato immagine.
 */
public class SaveButtonModel extends CustomButtonModel {

    /**
     * Costruttore per inizializzare un pulsante di salvataggio con le coordinate e le dimensioni specificate.
     *
     * @param x      Coordinata X iniziale del pulsante.
     * @param y      Coordinata Y iniziale del pulsante.
     * @param width  Larghezza del pulsante.
     * @param height Altezza del pulsante.
     */
    public SaveButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Salva un'immagine del nuovo livello utilizzando i dati forniti.
     *
     * @param lvlData      I dati del livello rappresentati come una matrice di interi.
     * @param enemiesData  I dati dei nemici rappresentati come una matrice di interi.
     * @param playerSpawn  La posizione di spawn del giocatore, rappresentata come un punto.
     * @param levelIndex   L'indice del livello da salvare.
     */
    public void saveNewLevelImage(int[][] lvlData, int[][] enemiesData, Point playerSpawn, int levelIndex) {
        BufferedImage image = new BufferedImage(TILES_IN_WIDTH, TILES_IN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < TILES_IN_HEIGHT; i++) {
            for (int j = 0; j < TILES_IN_WIDTH; j++) {
                Color color = getColor(lvlData, enemiesData, playerSpawn, i, j);
                image.setRGB(j, i, color.getRGB());
            }
        }

        try {
            File outputfile = new File("./res/lvls/" + (levelIndex + 1) + ".png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            System.out.println("ERRORE NEL SALVATAGGIO IMMAGINE");
        }
    }

    /**
     * Ottiene il colore da impostare a un determinato pixel nell'immagine
     *
     * <ul>
     *     <li>
     *         Il rosso indica un nemico
     *     </li>
     *     <li>
     *         Il verde indica un nemico
     *     </li>
     * </ul>
     *
     * Se il pixel corrisponde allo spawn del player imposta soltanto il blu, mentre se una tile e' vuota imposta tutti i colori al massimo
     *
     * @param lvlData      I dati del livello rappresentati come una matrice di interi.
     * @param enemiesData  I dati dei nemici rappresentati come una matrice di interi.
     * @param playerSpawn  La posizione di spawn del giocatore, rappresentata come un punto.
     * @param y            La coordinata Y del pixel.
     * @param x            La coordinata X del pixel.
     * @return Il colore corrispondente per il pixel specificato.
     */
    private Color getColor(int[][] lvlData, int[][] enemiesData, Point playerSpawn, int y, int x) {
        int r = lvlData[y][x];
        int g = enemiesData[y][x];
        int b = 0;
        if (x == (playerSpawn.x / TILES_SIZE) && y == (playerSpawn.y / TILES_SIZE)) {
            r = 0;
            g = 0;
            b = 255;
        } else if ((r == 255 || r == 0) && g == 0 && b == 0) {
            r = 255;
            g = 255;
            b = 255;
        }
        Color color = new Color(r, g, b);
        return color;
    }
}

