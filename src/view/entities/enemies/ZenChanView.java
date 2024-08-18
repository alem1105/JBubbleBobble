package view.entities.enemies;

import model.entities.enemies.ZenChanModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.Enemies.*;

public class ZenChanView extends EnemyView {
    
    public ZenChanView(ZenChanModel zenChan) {
        super(zenChan);
        loadAnimations(LoadSave.ZEN_CHAN_SPRITE);
        xDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
        yDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
    }

    @Override
    public void loadAnimations(String path) {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.ZEN_CHAN_SPRITE);
        animations = new BufferedImage[5][4];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                animations[i][j] = img.getSubimage(j * 18, i * 18, 18, 18);
            }
        }
    }

    @Override
    public int getSpriteAmount() {
        return switch (enemy.getEnemyState()) {
            case DEAD -> 4;
            case RUNNING, RUNNING_ANGRY, CAPTURED, CAPTURED_ANGRY -> 2;
            default -> 1; // comprende ATTACK
        };
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(animations[enemy.getEnemyState()][aniIndex],
                (int) (enemy.getHitbox().x - xDrawOffset) + flipX, (int) (enemy.getHitbox().y - yDrawOffset),
                enemy.getWidth() * flipW, enemy.getHeight(), null);
    }

}
