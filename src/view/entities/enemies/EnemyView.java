package view.entities.enemies;

import model.entities.enemies.EnemyModel;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.*;

public abstract class EnemyView<T extends EnemyModel> {

    protected static final int ROW_INDEX = 5;
    protected static final int COL_INDEX = 6;

    protected int xDrawOffset;
    protected int yDrawOffset;
    protected int aniIndex;
    protected int aniTick, aniSpeed = 25;
    protected BufferedImage[][] animations;
    protected T enemy;
    protected int flipW = 1, flipX, exploding = 0;

    public EnemyView(T enemy) {
        this.enemy = enemy;
    }

    public void update() {
        updateAnimationTick();
        flipX();
        flipW();
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            if (!enemy.isActive())
                exploding ++;
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount()) {
                aniIndex = 0;
                if (enemy.getEnemyState() == DEAD && exploding > 2)
                    aniIndex = 2;
            }
        }

        if (enemy.isResetAniTick()){
            aniIndex = 0;
            aniTick = 0;
            enemy.setResetAniTick(false);
        }
    }

    public void flipX() {
        if (enemy.getWalkDir() == RIGHT)
            flipX = ZEN_CHAN_WIDTH;
        else
            flipX = 0;
    }

    public void flipW() {
        if (enemy.getWalkDir() == RIGHT)
            flipW = -1;
        else
            flipW = 1;
    }

// For Debugging
//    protected void drawHitbox(Graphics g) {
//        g.setColor(Color.PINK);
//        g.drawRect((int) (enemy.getHitbox().x), (int) (enemy.getHitbox().y),
//                (int) enemy.getHitbox().width,
//                (int) enemy.getHitbox().height);
//    }

    public int getSpriteAmount() {
        return switch (enemy.getEnemyState()) {
            case DEAD -> 6;
            default -> 2;
        };
    }

    public void render(Graphics g) {
        g.drawImage(animations[enemy.getEnemyState()][aniIndex],
                (int) (enemy.getHitbox().x - xDrawOffset) + flipX, (int) (enemy.getHitbox().y - yDrawOffset),
                enemy.getWidth() * flipW, enemy.getHeight(), null);
    }

    public T getEnemy(){
        return enemy;
    }
}
