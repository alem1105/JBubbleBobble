package model.objects.projectiles;

import model.entities.PlayerModel;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.GAME_WIDTH;
import static model.utilz.Gravity.CanMoveHere;
import static model.utilz.UtilityMethods.getLvlData;

public class DrunkBottleModel extends ProjectileModel {

    private float xBottleSpeed = 0.8f * SCALE, yBottleSpeed = 0.8f * SCALE;
    private float startX;
    private int dirChanged;

    private boolean superDrunk;

    public DrunkBottleModel(float x, float y,int direction) {
        super(x, y, (int) (18 * SCALE), (int)(18* SCALE), direction);
        startX = x;
        dirChanged = 0;
        checkDirection();
    }

    private void checkDirection() {
        switch(direction) {
            case RIGHT -> yBottleSpeed = 0;
            case LEFT -> {
                xBottleSpeed = -xBottleSpeed;
                yBottleSpeed = 0;
            }
            case UP_RIGHT -> yBottleSpeed = -yBottleSpeed;
            case UP_LEFT -> {
                xBottleSpeed = -xBottleSpeed;
                yBottleSpeed = -yBottleSpeed;
            }
            case DOWN_LEFT -> xBottleSpeed = -xBottleSpeed;
        }
    }

    @Override
    public void update(){
        updatePos();
    }

    private void updatePos() {
        if(superDrunk)
            updateSuperDrunkPos();
        else
            updateDrunkPos();
    }

    private void updateSuperDrunkPos() {
        if(hitbox.intersects(PlayerModel.getInstance().getHitbox())) {
            PlayerModel.getInstance().playerHasBeenHit();
        }

        if(canDrunkBottleMoveOnThisX(hitbox.x + xBottleSpeed))
            if (canDrunkBottleMoveOnThisY(hitbox.y + yBottleSpeed)) {
                hitbox.x += xBottleSpeed;
                hitbox.y += yBottleSpeed;
                return;
            }

        active = false;
    }

    private boolean canDrunkBottleMoveOnThisY(float nextY) {
        return !(nextY + hitbox.height >= GAME_HEIGHT - (2 * TILES_SIZE) || nextY <= TILES_SIZE);
    }

    private boolean canDrunkBottleMoveOnThisX(float nextX) {
        return !(nextX + hitbox.width  >= GAME_WIDTH - TILES_SIZE || nextX <= TILES_SIZE);
    }

    private void updateDrunkPos() {
        // Controlla se scoppiare, un po' di scarto per sicurezza
        if (hitbox.x >= startX - (2 * SCALE) && hitbox.x <= startX + (2 * SCALE) && dirChanged > 0) {
            active = false;
        }

        // Controlla se invertire direzione
        if (Math.abs(hitbox.x - startX) >= TILES_SIZE * 4) {
            dirChanged++;
            xBottleSpeed = -xBottleSpeed;
        }

        // Controlla se puoi muoverti (passa attraverso i muri?)
        if(CanMoveHere(hitbox.x + xBottleSpeed, hitbox.y, hitbox.width, hitbox.height, getLvlData())) {
            hitbox.x += xBottleSpeed;
        } else {
            dirChanged++;
            xBottleSpeed = -xBottleSpeed;
        }
    }

    public void setSuperDrunk(boolean superDrunk) {
        this.superDrunk = superDrunk;
    }
}