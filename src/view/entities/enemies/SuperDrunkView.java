package view.entities.enemies;

import model.entities.enemies.DrunkModel;
import model.entities.PlayerModel;
import model.entities.enemies.SuperDrunkModel;
import model.objects.bobbles.BubbleModel;
import model.objects.projectiles.DrunkBottleModel;
import view.objects.bobbles.BubbleView;
import view.objects.projectiles.DrunkBottleView;
import view.objects.projectiles.ProjectileManagerView;
import view.objects.projectiles.ProjectileView;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.utilz.Constants.Directions.DOWN_RIGHT;
import static model.utilz.Constants.Directions.UP_RIGHT;
import static model.utilz.Constants.Enemies.DEAD;
import static model.utilz.Constants.Enemies.ZEN_CHAN_WIDTH;
import static model.utilz.Constants.GameConstants.SCALE;
import static view.utilz.LoadSave.loadAnimations;

public class SuperDrunkView extends EnemyView<SuperDrunkModel>{

    private int spriteIndex;
    private int hitTimer = 180, hitTick;
    private ArrayList<DrunkBottleView> drunkBottlesView;
    private BufferedImage[][] drunkBottleSprites;

    public SuperDrunkView(SuperDrunkModel superDrunkModel){
        super(superDrunkModel);
        aniIndex = 0;
        animations = loadAnimations(LoadSave.SUPERDRUNK_SPRITE, 5, 4, 48, 51);
        drunkBottlesView = new ArrayList<>();
        drunkBottleSprites = ProjectileManagerView.getInstance().getDrunkBottleSprite();
    }

    private void getDrunkBottlesFromModel(){
        int modelLength = enemy.getDrunkBottles().size();
        int i = drunkBottlesView.size();
        if (i > modelLength) {
            i = 0;
            drunkBottlesView.clear();
        }
        while (modelLength > drunkBottlesView.size()) {
            DrunkBottleModel drunkBottleModel = enemy.getDrunkBottles().get(i);
            drunkBottlesView.add(new DrunkBottleView(drunkBottleModel, drunkBottleSprites));
            i++;
        }
    }

    @Override
    public int getSpriteAmount() {
        int spriteAmount = 2;
        if (enemy.hasBeenHit())
            spriteAmount = 4;
        if (enemy.isInBubble() || !enemy.isActive())
            spriteAmount = 1;
        return spriteAmount;
    }

    public void setSpriteIndex(){
        int tempSpriteIndex = spriteIndex;
        spriteIndex = 1;

        if (enemy.hasBeenHit())
            spriteIndex = 2;

        if (enemy.isInBubble())
            spriteIndex = 3;

        if (!enemy.isActive())
            spriteIndex = 4;

        if (tempSpriteIndex != spriteIndex)
            resetAniTick();
    }

    @Override
    public void update(){
        checkHitAnimationOver();
        setSpriteIndex();
        updateAnimationTick();
        flipW();
        flipX();
        getDrunkBottlesFromModel();
    }

    @Override
    public void flipX() {
        if (enemy.getWalkDir() == UP_RIGHT || enemy.getWalkDir() == DOWN_RIGHT)
            flipX = (int) enemy.getHitbox().getWidth();
        else
            flipX = 0;
    }

    @Override
    public void flipW() {
        if (enemy.getWalkDir() == UP_RIGHT || enemy.getWalkDir() == DOWN_RIGHT)
            flipW = -1;
        else
            flipW = 1;
    }

    private void updateAndDrawDrunkBottles(Graphics g ) {
        for (DrunkBottleView drunkBottle : drunkBottlesView){
            if(drunkBottle.conditionToDraw())
                drunkBottle.updateAndDraw(g);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(animations[spriteIndex][aniIndex],
                (int) (enemy.getHitbox().x) + flipX, (int) (enemy.getHitbox().y),
                (int)enemy.getHitbox().getWidth() * flipW, (int)enemy.getHitbox().getHeight(), null);
        drawHitbox(g);
        updateAndDrawDrunkBottles(g);
    }

    @Override
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount()) {
                aniIndex = 0;
            }
        }
    }

    private void resetAniTick() {
        aniIndex = 0;
        aniTick = 0;
        enemy.setResetAniTick(false);
    }

    private void checkHitAnimationOver(){
        if (enemy.hasBeenHit())
            hitTick ++;
        if (hitTick >= hitTimer) {
            hitTick = 0;
            enemy.setHasBeenHit(false);
        }
    }
}
