package view.ui;

import model.LevelManagerModel;
import model.utilz.UtilityMethods;
import org.w3c.dom.css.Rect;
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

public class GameWonScreenView {

    private static GameWonScreenView instance;

    private BufferedImage[][] heartSprites;
    private BufferedImage parentsHuggingSprites;
    private BufferedImage[][] characterKissingSprites;
    private BufferedImage happyEndWritingSprite;
    private BufferedImage[] lvlSprites;
    private int [][] lastLevelData;
    private int aniTick, aniIndexHeart, aniIndexKiss, aniSpeed = 25;
    private float blackScreenY = - GAME_HEIGHT;
    private boolean blackScreenFallingOver = false;

    // campi per le stelle
    private Random random;
    private Color[] starColors;
    private ArrayList<Rectangle2D.Float> stars;

    private int durationTick, durationTimer = 10920;

    public static GameWonScreenView getInstance() {
        if (instance == null) {
            instance = new GameWonScreenView();
        }
        return instance;
    }

    private GameWonScreenView() {
        heartSprites = LoadSave.loadAnimations(HEART_SPRITE, 1, 3, 50, 50);
        parentsHuggingSprites = LoadSave.GetSpriteAtlas(PARENTS_HUGGING_SPRITE);
        happyEndWritingSprite = LoadSave.GetSpriteAtlas(HAPPY_END);
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

    public void update(){
        checkDuration();
        updateBlackScreen();
        updateAnimationTickHeart();
        if (blackScreenFallingOver)
            updateAnimationTickKiss();
        createStars();
    }

    private void checkDuration() {
        durationTick++;
        if (durationTick >= durationTimer) {
            durationTick = 0;
            LevelManagerModel.getInstance().setGameWon(false);
            UtilityMethods.resetAll();
        }
    }

    private void updateBlackScreen() {
        if (blackScreenY < 0) {
            blackScreenY+=0.5f;
            return;
        }
        blackScreenFallingOver = true;
    }

    private void updateAnimationTickHeart(){
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndexHeart++;
            if (aniIndexHeart >= 3) {
                aniIndexHeart = 0;
            }
        }
    }

    private void updateAnimationTickKiss(){
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndexKiss++;
            if (aniIndexKiss >= 5) {
                aniIndexKiss = 4;
            }
        }
    }

    public void draw(Graphics g) {
        if (!blackScreenFallingOver)
            drawLevelBehind(g);
        drawBlackScreen(g);
        if (blackScreenFallingOver)
            drawStars(g);

        drawHeart(g, (int) (70 * SCALE), (int) (70 * SCALE), GAME_WIDTH / 2 - (int) (35 * SCALE), GAME_HEIGHT / 2 - (int) (35 * SCALE));
        drawHeart(g, (int) (25 * SCALE), (int) (25 * SCALE), (int) (63 * SCALE) , GAME_HEIGHT - TILES_SIZE * 2 - (int) (59 * SCALE));
        drawHeart(g, (int) (25 * SCALE), (int) (25 * SCALE), GAME_WIDTH - (int) (82 * SCALE), GAME_HEIGHT - TILES_SIZE * 2 - (int) (59 * SCALE));
        drawParents(g);
        drawHappyEnd(g, GAME_WIDTH - (int) (102 * SCALE), GAME_HEIGHT - (int) (150 * SCALE));
        drawHappyEnd(g, (int) (41 * SCALE), GAME_HEIGHT - (int) (150 * SCALE));
        drawKiss(g, (int) (52 * SCALE), GAME_HEIGHT - TILES_SIZE * 2 - (int) (24 * SCALE), 0);
        drawKiss(g, GAME_WIDTH - (int) (97 * SCALE), GAME_HEIGHT - TILES_SIZE * 2 - (int) (24 * SCALE), 1);
        drawPoints(g);
    }

    private void drawBlackScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, (int)blackScreenY, GAME_WIDTH, GAME_HEIGHT);
    }

    private void drawLevelBehind(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        for (int y = 0; y < TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < lastLevelData[0].length; x++) {
                int index = lastLevelData[y][x];
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

    private void createStars() {
        if(stars.size() != GAME_HEIGHT * GAME_WIDTH) {
            Rectangle2D.Float pixel = new Rectangle2D.Float(random.nextFloat(GAME_WIDTH), random.nextFloat(GAME_HEIGHT), (int) (1 * SCALE), (int) (1 * SCALE));
            stars.add(pixel);
        }
    }

    private void drawStars(Graphics g) {

        for(int i = 0; i < stars.size(); i++) {
            int randomColorIndex = random.nextInt(starColors.length);
            Color randomColor = starColors[randomColorIndex];
            if(i % 6 == 0)
                g.setColor(randomColor);
            else
                g.setColor(Color.BLACK);
            Rectangle2D.Float currentStar = stars.get(i);
            g.fillRect((int) currentStar.getX(), (int) currentStar.getY(), (int) currentStar.getWidth(), (int) currentStar.getHeight());
        }
    }

    private void drawHeart(Graphics g, int width, int height, int x, int y) {
        g.drawImage(heartSprites[0][aniIndexHeart], x, y, width, height, null);
    }

    private void drawParents(Graphics g) {
        g.drawImage(parentsHuggingSprites, GAME_WIDTH / 2 - (int) (35 * SCALE), GAME_HEIGHT / 2 + (int) (45 * SCALE) , (int) (70 * SCALE), (int) (32 * SCALE), null);
    }

    private void drawHappyEnd(Graphics g, int x, int y) {
        g.drawImage(happyEndWritingSprite, x, y, (int) (69 * SCALE), (int) (36 * SCALE), null);
    }

    private void drawKiss(Graphics g, int x, int y, int spriteIndex) {
        g.drawImage(characterKissingSprites[spriteIndex][aniIndexKiss], x, y, (int) (55 * SCALE), (int) (24 * SCALE), null);
    }

    private void drawPoints(Graphics g){
        g.setColor(Color.GREEN);
        Font font = LoadSave.BUBBLE_BOBBLE_FONT.deriveFont(43f * SCALE);
        g.setFont(font);
        FontMetrics measure = g.getFontMetrics(font);
        g.drawString("1000000PTS!!", GAME_WIDTH/ 2 - measure.stringWidth("1000000PTS!!")/2, (int) (100 * SCALE));
    }

    public int getDurationTick() {
        return durationTick;
    }

}
