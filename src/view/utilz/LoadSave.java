package view.utilz;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static model.utilz.Constants.GameConstants.SCALE;

public class LoadSave {

    public static final String PLAYER_SPRITE = "player_sprites.png";
    public static final String LEVEL_SPRITE = "level_sprite.png";
    public static final String ICON = "icon.png";
    public static final String ZEN_CHAN_SPRITE = "enemies/zen_chan_sprite.png";
    public static final String SAVE_BUTTON = "ui/save_button.png";
    public static final String X_BUTTON = "ui/x_button.png";
    public static final String ERASER_BUTTON = "ui/eraser_button.png";
    public static final String ENEMIES_BUTTON = "ui/enemies_button.png";
    public static final String PLAYER_BUTTON = "ui/player_button.png";
    public static final String CHANGE_LVL_BUTTON = "ui/change_lvl_button.png";
    public static final String EDIT_BUTTON = "ui/edit_button.png";
    public static final String HEART_LIFE_BUTTON = "ui/life.png";
    public static final String QUIT_BUTTON = "ui/quit_button.png";
    public static final String RESTART_BUTTON = "ui/restart_button.png";
    public static final String BOB_BUBBLE_SPRITE = "objects/bubbles/bubble_sprites.png";


    public static Font CUSTOM_FONT;

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);

        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[][] loadAnimations(String path, int rowIndex, int colIndex, int width, int height) {
        BufferedImage img = LoadSave.GetSpriteAtlas(path);
        BufferedImage[][] animations = new BufferedImage[rowIndex][colIndex];
        for (int i = 0; i < rowIndex; i++) {
            for (int j = 0; j < colIndex; j++) {
                animations[i][j] = img.getSubimage(j * width, i * height, width, height);
            }
        }
        return animations;
    }

    public static BufferedImage[] importSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_SPRITE);
        int index = 0;
        BufferedImage[] lvlSprites = new BufferedImage[30];
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 5; i++) {
                lvlSprites[index] = img.getSubimage(i * 16, j * 16, 16, 16);
                index++;
            }
        }
        return lvlSprites;
    }

    public static void loadCustomFont() {
        try {
            CUSTOM_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("./res/BubbleFont.ttf")).deriveFont(16f * SCALE);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(CUSTOM_FONT);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // Se il font non viene caricato, usa un font di fallback
            CUSTOM_FONT = new Font("Serif", Font.PLAIN, 24);
        }
    }
}
