package view.level;

import model.level.LevelModel;
import model.entities.enemies.EnemyManagerModel;
import model.entities.enemies.EnemyModel;
import model.entities.enemies.SuperDrunkModel;
import model.objects.items.powerups.PowerUpsManagerModel;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

import model.level.LevelManagerModel;

import static model.utilz.Constants.GameConstants.*;

/**
 * Rappresenta la vista del livello nel gioco
 */
public class LevelView {

    /**
     * Array di sprite dei livelli, usato per disegnare i vari tiles del livello.
     */
    private BufferedImage[] lvlSprites;

    /**
     * Elenco dei modelli di livello. Contiene tutti i livelli disponibili nel gioco.
     */
    private ArrayList<LevelModel> levels;

    /**
     * Gestore dei livelli, responsabile della gestione e del caricamento dei livelli.
     */
    private LevelManagerModel lvlManager;

    /**
     * Tick per la gestione del tempo delle esplosioni delle bombe.
     * Incrementato ad ogni ciclo di rendering per controllare l'animazione delle esplosioni.
     */
    private int bombTick = 0;

    /**
     * Timer per la durata delle esplosioni.
     * Definisce quanto a lungo l'effetto bomba sarà visibile prima di essere rimosso.
     */
    private int bombTimer = 360;

    /**
     * Colori per l'effetto bomba.
     * Utilizzati per rappresentare visivamente le esplosioni delle bombe nel gioco.
     */
    private Color[] colors = {Color.RED, null, Color.YELLOW};

    /**
     * Indice del colore corrente nell'array colors.
     * Usato per cicli di colore durante l'animazione delle esplosioni.
     */
    private int colorIndex = 0;

    private DecimalFormat decFormat = new DecimalFormat("00"); // Formattazione per le vite

    /**
     * Costruttore della classe LevelView.
     * Inizializza gli sprite dei livelli e carica i livelli dal gestore.
     */
    public LevelView() {
        lvlSprites = LoadSave.importSprites();
        lvlManager = LevelManagerModel.getInstance();
        levels = lvlManager.getLevels();
    }

    /**
     * Disegna il livello attuale
     *
     * @param g
     */
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        checkAndUpdateBombDrawing(g);
        drawBossLives(g);

        // Disegna i tiles del livello
        for (int y = 0; y < TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < levels.get(lvlManager.getLvlIndex()).getLvlData()[0].length; x++) {
                int index = levels.get(lvlManager.getLvlIndex()).getSpriteIndex(x, y);
                if (index == 0 || index == 255) continue; // Salta i tiles non validi

                int rgb = lvlSprites[index - 1].getRGB(3, 3); // Ottiene il colore dello sprite
                if (!(y == TILES_IN_HEIGHT - 2)) {
                    g.setColor(LoadSave.getDarkenedColor(rgb));
                    for (int i = 0; i < 8; i++)
                        g.fillRect(x * TILES_SIZE + i, y * TILES_SIZE + +i, TILES_SIZE, TILES_SIZE);
                } else {
                    g.fillRect(x * TILES_SIZE, y * TILES_SIZE, TILES_SIZE, TILES_SIZE);
                }
                g.drawImage(lvlSprites[index - 1], x * TILES_SIZE, y * TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
            }
        }
    }

    /**
     * Disegna le vite del boss nel livello attuale se il livello è l'ultimo.
     *
     * @param g
     */
    private void drawBossLives(Graphics g) {
        if (lvlManager.getLvlIndex() == 24) {
            SuperDrunkModel superDrunk = getSuperDrunkModelFromEnemiesArray();
            g.setFont(LoadSave.NES_FONT);
            g.setColor(Color.WHITE);
            String superDrunkLives = decFormat.format(superDrunk.getLives());
            g.drawString(superDrunkLives, 9 * TILES_SIZE + TILES_SIZE / 2, TILES_SIZE - TILES_SIZE / 4);
        }
    }

    /**
     * Ottiene il modello SuperDrunk dall'array di nemici.
     *
     * @return il modello SuperDrunk trovato, o null se non esiste
     */
    private SuperDrunkModel getSuperDrunkModelFromEnemiesArray() {
        for (EnemyModel enemyModel : EnemyManagerModel.getInstance().getEnemies()) {
            if (enemyModel instanceof SuperDrunkModel)
                return (SuperDrunkModel) enemyModel;
        }
        return null;
    }

    /**
     * Controlla e aggiorna il disegno delle bombe.
     *
     * @param g
     */
    private void checkAndUpdateBombDrawing(Graphics g) {
        if (PowerUpsManagerModel.getInstance().getBombExploding()) {
            if (bombTimer >= 0) {
                if (bombTick >= 15) {
                    bombTimer -= bombTick;
                    bombTick = 0;
                    colorIndex = (colorIndex == colors.length - 1) ? 0 : colorIndex + 1; // Cambia colore
                } else {
                    if (colors[colorIndex] != null) {
                        g.setColor(colors[colorIndex]);
                        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT); // Disegna il colore dell'esplosione
                    }
                }
            } else {
                bombTimer = 360; // Resetta il timer della bomba
                PowerUpsManagerModel.getInstance().setBombExploding(false); // Ferma l'esplosione
            }
            bombTick++;
        }
    }

// For debugging
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
