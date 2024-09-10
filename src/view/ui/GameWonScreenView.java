package view.ui;

import model.LevelManagerModel;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static model.utilz.Constants.GameConstants.*;
import static view.utilz.LoadSave.HEART_SPRITE;
import static view.utilz.LoadSave.PARENTS_HUGGING_SPRITE;
import static view.utilz.LoadSave.HAPPY_END;
import static view.utilz.LoadSave.CHARACTER_KISSING;

/**
 * Rappresenta la vista della schermata di vittoria.
 * Gestisce l'animazione e la visualizzazione degli elementi
 * quando il gioco è stato vinto.
 */
public class GameWonScreenView {

    // Istanze della classe
    private static GameWonScreenView instance;

    // Sprite e dati di animazione
    private BufferedImage[][] heartSprites;
    private BufferedImage parentsHuggingSprites;
    private BufferedImage[][] characterKissingSprites;
    private BufferedImage happyEndWritingSprite;
    private BufferedImage[] lvlSprites;
    private int[][] lastLevelData;
    private int aniTick, aniIndexHeart, aniIndexKiss, aniSpeed = 25;
    private float blackScreenY = -GAME_HEIGHT;
    private boolean blackScreenFallingOver = false;

    // Campi per le stelle
    private Random random;
    private Color[] starColors;
    private ArrayList<Rectangle2D.Float> stars;

    private int durationTick, durationTimer = 10920;

    /**
     * Restituisce l'istanza singleton di GameWonScreenView.
     *
     * @return l'istanza di GameWonScreenView
     */
    public static GameWonScreenView getInstance() {
        if (instance == null) {
            instance = new GameWonScreenView();
        }
        return instance;
    }

    /**
     * Costruttore privato per inizializzare i dati necessari.
     */
    private GameWonScreenView() {
        heartSprites = LoadSave.loadAnimations(HEART_SPRITE, 1, 3, 50, 50);
        parentsHuggingSprites = LoadSave.getSpriteAtlas(PARENTS_HUGGING_SPRITE);
        happyEndWritingSprite = LoadSave.getSpriteAtlas(HAPPY_END);
        characterKissingSprites = LoadSave.loadAnimations(CHARACTER_KISSING, 2, 5, 37, 16);
        lastLevelData = LevelManagerModel.getInstance().getLevels().getLast().getLvlData();
        lvlSprites = LoadSave.importSprites();
        random = new Random();
        stars = new ArrayList<>();
        starColors = new Color[]{
                new Color(66, 81, 108),
                new Color(57, 56, 69),
                new Color(46, 90, 128),
                new Color(79, 79, 102)
        };
    }

    /**
     * Aggiorna lo stato della vista ad ogni frame.
     */
    public void update() {
        checkDuration();
        updateBlackScreen();
        updateAnimationTickHeart();
        if (blackScreenFallingOver)
            updateAnimationTickKiss();
        createStars();
    }

    /**
     * Controlla la durata dello schermo di vittoria e reimposta
     * il gioco se la durata è scaduta.
     */
    private void checkDuration() {
        durationTick++;
        if (durationTick >= durationTimer) {
            durationTick = 0;
            LevelManagerModel.getInstance().setGameWon(false);
        }
    }

    /**
     * Aggiorna la posizione dello schermo nero.
     */
    private void updateBlackScreen() {
        if (blackScreenY < 0) {
            blackScreenY += 0.5f;
            return;
        }
        blackScreenFallingOver = true;
    }

    /**
     * Aggiorna il tick dell'animazione del cuore.
     */
    private void updateAnimationTickHeart() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndexHeart++;
            if (aniIndexHeart >= 3) {
                aniIndexHeart = 0;
            }
        }
    }

    /**
     * Aggiorna il tick dell'animazione del bacio.
     */
    private void updateAnimationTickKiss() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndexKiss++;
            if (aniIndexKiss >= 5) {
                aniIndexKiss = 4;
            }
        }
    }

    /**
     * Disegna gli elementi.
     *
     * @param g
     */
    public void draw(Graphics g) {
        if (!blackScreenFallingOver)
            drawLevelBehind(g);
        drawBlackScreen(g);
        if (blackScreenFallingOver)
            drawStars(g);

        drawHeart(g, (int) (70 * SCALE), (int) (70 * SCALE), GAME_WIDTH / 2 - (int) (35 * SCALE), GAME_HEIGHT / 2 - (int) (35 * SCALE));
        drawHeart(g, (int) (25 * SCALE), (int) (25 * SCALE), (int) (63 * SCALE), GAME_HEIGHT - TILES_SIZE * 2 - (int) (59 * SCALE));
        drawHeart(g, (int) (25 * SCALE), (int) (25 * SCALE), GAME_WIDTH - (int) (82 * SCALE), GAME_HEIGHT - TILES_SIZE * 2 - (int) (59 * SCALE));
        drawParents(g);
        drawHappyEnd(g, GAME_WIDTH - (int) (102 * SCALE), GAME_HEIGHT - (int) (150 * SCALE));
        drawHappyEnd(g, (int) (41 * SCALE), GAME_HEIGHT - (int) (150 * SCALE));
        drawKiss(g, (int) (52 * SCALE), GAME_HEIGHT - TILES_SIZE * 2 - (int) (24 * SCALE), 0);
        drawKiss(g, GAME_WIDTH - (int) (97 * SCALE), GAME_HEIGHT - TILES_SIZE * 2 - (int) (24 * SCALE), 1);
        drawPoints(g);
    }

    /**
     * Disegna lo schermo nero
     *
     * @param g
     */
    private void drawBlackScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, (int) blackScreenY, GAME_WIDTH, GAME_HEIGHT);
    }

    /**
     * Disegna il livello dietro gli elementi
     *
     * @param g
     */
    private void drawLevelBehind(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        for (int y = 0; y < TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < lastLevelData[0].length; x++) {
                int index = lastLevelData[y][x];
                if (index == 0 || index == 255) continue;

                int rgb = lvlSprites[index - 1].getRGB(3, 3);
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
     * Crea stelle casuali nello schermo
     */
    private void createStars() {
        if (stars.size() != GAME_HEIGHT * GAME_WIDTH) {
            Rectangle2D.Float pixel = new Rectangle2D.Float(random.nextFloat(GAME_WIDTH), random.nextFloat(GAME_HEIGHT), (int) (1 * SCALE), (int) (1 * SCALE));
            stars.add(pixel);
        }
    }

    /**
     * Disegna le stelle sullo schermo
     *
     * @param g
     */
    private void drawStars(Graphics g) {
        for (int i = 0; i < stars.size(); i++) {
            int randomColorIndex = random.nextInt(starColors.length);
            Color randomColor = starColors[randomColorIndex];
            if (i % 6 == 0)
                g.setColor(randomColor);
            else
                g.setColor(Color.BLACK);
            Rectangle2D.Float currentStar = stars.get(i);
            g.fillRect((int) currentStar.getX(), (int) currentStar.getY(), (int) currentStar.getWidth(), (int) currentStar.getHeight());
        }
    }

    /**
     * Disegna un cuore sullo schermo.
     *
     * @param g
     * @param width la larghezza del cuore
     * @param height l'altezza del cuore
     * @param x la coordinata x in cui disegnare il cuore
     * @param y la coordinata y in cui disegnare il cuore
     */
    private void drawHeart(Graphics g, int width, int height, int x, int y) {
        g.drawImage(heartSprites[0][aniIndexHeart], x, y, width, height, null);
    }

    /**
     * Disegna i genitori che si abbracciano sullo schermo.
     *
     * @param g
     */
    private void drawParents(Graphics g) {
        g.drawImage(parentsHuggingSprites, GAME_WIDTH / 2 - (int) (35 * SCALE), GAME_HEIGHT / 2 + (int) (45 * SCALE), (int) (70 * SCALE), (int) (32 * SCALE), null);
    }

    /**
     * Disegna il messaggio di "Happy Ending" sullo schermo.
     *
     * @param g
     * @param x la coordinata x in cui disegnare il messaggio
     * @param y la coordinata y in cui disegnare il messaggio
     */
    private void drawHappyEnd(Graphics g, int x, int y) {
        g.drawImage(happyEndWritingSprite, x, y, (int) (69 * SCALE), (int) (36 * SCALE), null);
    }

    /**
     * Disegna l'animazione del bacio
     *
     * @param g
     * @param x la coordinata x in cui disegnare il bacio
     * @param y la coordinata y in cui disegnare il bacio
     * @param spriteIndex l'indice dello sprite del bacio
     */
    private void drawKiss(Graphics g, int x, int y, int spriteIndex) {
        g.drawImage(characterKissingSprites[spriteIndex][aniIndexKiss], x, y, (int) (55 * SCALE), (int) (24 * SCALE), null);
    }

    /**
     * Disegna il punteggio sullo schermo.
     *
     * @param g
     */
    private void drawPoints(Graphics g) {
        g.setColor(Color.GREEN);
        Font font = LoadSave.NES_FONT.deriveFont(25f * SCALE);
        g.setFont(font);
        FontMetrics measure = g.getFontMetrics(font);
        g.drawString("1000000PTS!!", GAME_WIDTH / 2 - measure.stringWidth("1000000PTS!!") / 2, (int) (100 * SCALE));
    }

    public int getDurationTick() {
        return durationTick;
    }

}
