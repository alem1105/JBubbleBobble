package view.entities.enemies;

import model.entities.enemies.ZenChanModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import java.awt.*;

import static model.utilz.Constants.Enemies.*;
import static view.utilz.LoadSave.*;

public class ZenChanView extends EnemyView {

    private static final int ROW_INDEX = 5;
    private static final int COL_INDEX = 4;

    public ZenChanView(ZenChanModel zenChan) {
        super(zenChan);
        animations = loadAnimations(LoadSave.ZEN_CHAN_SPRITE, ROW_INDEX, COL_INDEX, 18, 18);
        xDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
        yDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
    }

    @Override
    public int getSpriteAmount() {
        return switch (enemy.getEnemyState()) {
            case DEAD -> 4;
            case RUNNING, RUNNING_ANGRY, CAPTURED, CAPTURED_ANGRY -> 2;
            case ATTACK -> 1;
            default -> 2; // exploding
        };
    }

    @Override
    public void render(Graphics g) {
        if (!enemy.isActive()){
            aniIndex = 0;
            aniTick = 0;
            g.drawImage(explodingImage[aniIndex],
                    (int) enemy.getX(),
                    (int) enemy.getY(),
                    enemy.getWidth(),
                    enemy.getHeight(),
                    null);
        }
        else {g.drawImage(animations[enemy.getEnemyState()][aniIndex],
                (int) (enemy.getHitbox().x - xDrawOffset) + flipX, (int) (enemy.getHitbox().y - yDrawOffset),
                enemy.getWidth() * flipW, enemy.getHeight(), null);
        }
    }



}
