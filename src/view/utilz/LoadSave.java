package view.utilz;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {

    public static final String PLAYER_SPRITE = "player_sprite.png";
    public static final String LEVEL_SPRITE = "level_sprite.png";
    public static final String ICON = "icon.png";
    public static final String ZEN_CHAN_SPRITE = "enemies/zen_chan_sprite.png";
    public static final String SAVE_BUTTON = "ui/save_button.png";
    public static final String X_BUTTON = "ui/x_button.png";

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

    public static BufferedImage[][] loadAnimations(String path, int rowIndex, int colIndex) {
        BufferedImage img = LoadSave.GetSpriteAtlas(path);
        BufferedImage[][] animations = new BufferedImage[rowIndex][colIndex];
        for (int i = 0; i < rowIndex; i++) {
            for (int j = 0; j < colIndex; j++) {
                animations[i][j] = img.getSubimage(j * 18, i * 18, 18, 18);
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
}
