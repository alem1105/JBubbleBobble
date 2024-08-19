package view.entities.enemies;

import model.entities.enemies.ZenChanModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.Enemies.*;
import static view.utilz.LoadSave.*;

public class ZenChanView extends EnemyView {

    private static final int ROW_INDEX = 5;
    private static final int COL_INDEX = 4;

    public ZenChanView(ZenChanModel zenChan) {
        super(zenChan);
        animations = loadAnimations(LoadSave.ZEN_CHAN_SPRITE, ROW_INDEX, COL_INDEX);
        xDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
        yDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
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
