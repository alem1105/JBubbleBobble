package view.entities.enemies;

import model.entities.enemies.EnemyModel;
import static view.utilz.LoadSave.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.Enemies.*;


public abstract class EnemyView {
    protected int xDrawOffset;
    protected int yDrawOffset;
    protected int aniIndex;
    protected int aniTick, aniSpeed = 25;
    protected BufferedImage[][] animations;
    protected EnemyModel enemy;
    protected int flipW, flipX;

    public EnemyView(EnemyModel enemy) {
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
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount()) {
                if (enemy.getEnemyState() == DEAD){
                    aniIndex = 6;
                } else {
                    aniIndex = 0;
                }
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

    // TODO RIMUOVERE
    protected void drawHitbox(Graphics g) {
        // For debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int) (enemy.getHitbox().x), (int) (enemy.getHitbox().y),
                (int) enemy.getHitbox().width,
                (int) enemy.getHitbox().height);
    }

    protected abstract int getSpriteAmount();

    public void render(Graphics g) {
        g.drawImage(animations[enemy.getEnemyState()][aniIndex],
                (int) (enemy.getHitbox().x - xDrawOffset) + flipX, (int) (enemy.getHitbox().y - yDrawOffset),
                enemy.getWidth() * flipW, enemy.getHeight(), null);
    }

    public EnemyModel getEnemy(){
        return enemy;
    }
}
