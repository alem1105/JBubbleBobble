package view.utilz;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static model.utilz.Constants.GameConstants.SCALE;

/**
 * Classe utilizzata per caricare i file immagine e i font personalizzati
 */
public class LoadSave {

    public static final String MENU_LOGO = "ui/menu_sprite.png";
    public static final String STARS_SPRITE = "ui/twinkle_sprite.png";
    public static final String PLAYER_SPRITE = "player_sprites.png";
    public static final String LEVEL_SPRITE = "level_sprite.png";
    public static final String ICON = "icon.png";
    public static final String BUB_LEVEL_TRANSITION = "bubble_level_transition.png";
    public static final String PAUSE_BACKGROUND = "Pause_background.png";

    public static final String BOB_BUBBLE_SPRITE = "objects/bubbles/bubble_sprites.png";
    public static final String EXTEND_SPRITE = "objects/bubbles/extend_sprite.png";
    public static final String SPECIAL_BUBBLE_SPRITE = "objects/bubbles/special_bubbles.png";
    public static final String WATER_SPRITE = "objects/bubbles/water_sprites.png";
    public static final String FIRE_SPRITE = "objects/bubbles/fire.png";
    public static final String LIGHTNING_SPRITE = "objects/bubbles/lightning.png";

    // Enemies
    public static final String ZEN_CHAN_SPRITE = "enemies/zen_chan_sprite.png";
    public static final String MAITA_SPRITE = "enemies/maita_sprite.png";
    public static final String INVADER_SPRITE = "enemies/invader_sprite.png";
    public static final String MONSTA_SPRITE = "enemies/monsta_sprite.png";
    public static final String DRUNK_SPRITE = "enemies/drunk_sprite.png";
    public static final String HIDEGONS_SPRITE = "enemies/hidegons_sprite.png";
    public static final String SUPERDRUNK_SPRITE = "enemies/superdrunk_sprite.png";

    // Avatar
    public static final String AVATAR_1 = "avatars/avatar1.png";
    public static final String AVATAR_2 = "avatars/avatar2.png";
    public static final String AVATAR_3 = "avatars/avatar3.png";
    public static final String AVATAR_4 = "avatars/avatar4.png";
    public static final String AVATAR_5 = "avatars/avatar5.png";
    public static final String AVATAR_6 = "avatars/avatar6.png";
    public static final String AVATAR_7 = "avatars/avatar7.png";
    public static final String AVATAR_8 = "avatars/avatar8.png";
    public static final String AVATAR_9 = "avatars/avatar9.png";
    public static final String AVATAR_10 = "avatars/avatar10.png";
    public static final String AVATAR_11 = "avatars/avatar11.png";


    // Buttons
    public static final String SAVE_BUTTON = "ui/save_button.png";
    public static final String X_BUTTON = "ui/x_button.png";
    public static final String ERASER_BUTTON = "ui/eraser_button.png";
    public static final String ENEMIES_BUTTON = "ui/enemies_button.png";
    public static final String PLAYER_BUTTON = "ui/player_button.png";
    public static final String CHANGE_LVL_BUTTON = "ui/change_lvl_button.png";
    public static final String EDIT_BUTTON = "ui/edit_button.png";
    public static final String HEART_LIFE_BUTTON = "ui/life.png";
    public static final String QUIT_BUTTON = "ui/quit_button.png";
    public static final String QUIT_BUTTON2 = "ui/quit_button2.png";
    public static final String RESTART_BUTTON = "ui/restart_button.png";
    public static final String START_BUTTON = "ui/start_button.png";
    public static final String START_BUTTON2 = "ui/start_button2.png";
    public static final String EDITOR_BUTTON = "ui/editor_button.png";
    public static final String CREATE_BUTTON = "ui/create_button.png";


    // Projectiles
    public static final String MAITA_FIREBALL = "enemies/maita_fireball_sprite.png";
    public static final String HIDEGONS_FIREBALL = "enemies/hidegons_fireball_sprite.png";
    public static final String INVADER_LASER = "enemies/invader_laser_sprite.png";
    public static final String DRUNK_BOTTLE = "enemies/drunk_projectile_sprite.png";

    // Items
    public static final String FOOD_SPRITE = "objects/items/items.png";
    public static final String POWERUP_SPRITE = "objects/items/powerup_sprite.png";

    // Game Won
    public static final String HAPPY_END = "ui/winoverlay/happy_end_sprite.png";
    public static final String HEART_SPRITE = "ui/winoverlay/heart_sprite.png";
    public static final String PARENTS_HUGGING_SPRITE = "ui/winoverlay/parents_hugging_sprite.png";
    public static final String CHARACTER_KISSING = "ui/winoverlay/character_kissing_sprite.png";

    public static Font JEQO_FONT, NES_FONT;

    /**
     * @param fileName Percorso del file immagine
     * @return l'immagine come tipo {@link BufferedImage}
     */
    public static BufferedImage getSpriteAtlas(String fileName) {
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

    /**
     * Lo utilizziamo per caricare le sprites
     * @param path percorso del file
     * @param rowIndex quante righe sono presenti
     * @param colIndex quante colonne sono presenti
     * @param width larghezza di un frame
     * @param height altezza di un frame
     * @return tutti i frame dell'animazione in una matrice bidimensionale di tipo {@link BufferedImage}
     */
    public static BufferedImage[][] loadAnimations(String path, int rowIndex, int colIndex, int width, int height) {
        BufferedImage img = LoadSave.getSpriteAtlas(path);
        BufferedImage[][] animations = new BufferedImage[rowIndex][colIndex];
        for (int i = 0; i < rowIndex; i++) {
            for (int j = 0; j < colIndex; j++) {
                animations[i][j] = img.getSubimage(j * width, i * height, width, height);
            }
        }
        return animations;
    }

    /**
     * @return Un array do tipo {@code BufferedImage} contenente le sprites delle tiles dei livelli
     */
    public static BufferedImage[] importSprites() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_SPRITE);
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

    /**
     * Carica i vari font personalizzati nelle rispettive variabili
     */
    public static void loadCustomFont() {
        try {
            JEQO_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("./res/Jeqo-5-Bit.ttf")).deriveFont(6f * SCALE);
            NES_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("./res/nintendo-nes-font.ttf")).deriveFont(10f * SCALE);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(JEQO_FONT);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // Se il font non viene caricato, usa un font di fallback
            JEQO_FONT = new Font("Serif", Font.PLAIN, 24);
            NES_FONT = new Font("Serif", Font.PLAIN, 24);
        }
    }

    /**
     * Metodo utilizzato per disegnare le ombre sui livelli, scurisce un colore
     * @param rgb Colore in input da scurire
     * @return {@link Color} piu' scuro da inserire dietro le tiles
     */
    public static Color getDarkenedColor(int rgb) {
        Color originalColor = new Color(rgb); // Colore RGB originale
        float darkeningFactor = 0.4f; // Riduce il colore a metà della sua intensità
        int red = (int) (originalColor.getRed() * darkeningFactor);
        int green = (int) (originalColor.getGreen() * darkeningFactor);
        int blue = (int) (originalColor.getBlue() * darkeningFactor);
        Color darkColor = new Color(red, green, blue);
        return darkColor;
    }
}
