package view.entities.enemies;

import model.entities.enemies.SuperDrunkModel;
import view.utilz.LoadSave;

import java.awt.*;

import static model.utilz.Constants.Enemies.DEAD;
import static model.utilz.Constants.GameConstants.SCALE;
import static view.utilz.LoadSave.loadAnimations;

public class SuperDrunkView extends EnemyView<SuperDrunkModel>{

    private int spriteIndex;

    public SuperDrunkView(SuperDrunkModel superDrunkModel){
        super(superDrunkModel);
        aniIndex = 0;
        animations = loadAnimations(LoadSave.SUPERDRUNK_SPRITE, 5, 4, 48, 51);
    }

    public int getSpriteAmount() {
        if (enemy.hasBeenHit())
            return 4;
        if (!enemy.isInBubble() && enemy.isActive())
            return 2;

        return 1 ;
    }

    public void setSpriteIndex(){
        if (enemy.hasBeenHit())
            spriteIndex = 2;
        else if (enemy.isInBubble() )
            spriteIndex = 3;
        else if (!enemy.isActive())
            spriteIndex = 4;
        else
            spriteIndex = 1;
    }


    @Override
    public void render(Graphics g) {
        g.drawImage(animations[spriteIndex][aniIndex],
                (int) (enemy.getHitbox().x) + flipX, (int) (enemy.getHitbox().y),
                enemy.getWidth() * flipW, enemy.getHeight(), null);
        drawHitbox(g);
    }

    public void update(){
        setSpriteIndex();
        updateAnimationTick();
    }

}
