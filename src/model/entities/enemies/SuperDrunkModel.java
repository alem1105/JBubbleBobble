package model.entities.enemies;

import model.objects.projectiles.DrunkBottleModel;

import java.util.ArrayList;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.GameConstants.*;

public class SuperDrunkModel extends EnemyModel{

    private int lives = 2;
    private boolean hasBeenHit;

    private boolean shot;
    private ArrayList<DrunkBottleModel> drunkBottles;
    private float startingBottleX;
    private float startingBottleY;
    private float[][] bottlesPositions = {
            {18 * SCALE, -18 * SCALE, UP_RIGHT},
            {18 * SCALE / 2, - 18 * SCALE / 2, UP_RIGHT},
            {0, 0, UP_RIGHT},
            {0, 0, RIGHT},
            {0, 0, DOWN_RIGHT},
            {18 * SCALE / 2, 18 * SCALE / 2, DOWN_RIGHT},
            {18 * SCALE, 18 * SCALE, DOWN_RIGHT},
            {18 * SCALE * 1.5f, 18 * SCALE * 1.5f, DOWN_RIGHT}
    };

    public SuperDrunkModel(float x, float y, int width, int height) {
        super(x, y, width, height);
        initHitbox(width, height);
        drunkBottles = new ArrayList<>();
        this.walkSpeed = 0.55f * SCALE;
        walkDir = UP_RIGHT;
    }

    public void update() {
        super.update();
        checkShotCondition();
        checkShoot();
        updateDrunkBottles();
    }

    private void checkShotCondition() {
        if(drunkBottles.isEmpty()) {
            shot = true;
        }

        if(areAllBottlesInactive()) {
            drunkBottles.clear();
        }
    }

    private boolean areAllBottlesInactive() {
        for(DrunkBottleModel drunkBottleModel : drunkBottles)
            if(drunkBottleModel.isActive())
                return false;
        return true;
    }

    private void checkShoot() {
        if(!shot || inBubble)
            return;

        for(int i = 0; i < 8; i++) {
            // se è uno lascio la direzione scritta, altrimenti mettendogli il meno altero il verso, ad esempio:
            // -RIGHT = LEFT, -UP_RIGHT = UP_LEFT, -DOWN_RIGHT = DOWN_LEFT
            int directionChanger = (walkDir == UP_RIGHT || walkDir == DOWN_RIGHT) ? 1 : -1;

            startingBottleX = (walkDir == UP_RIGHT || walkDir == DOWN_RIGHT) ? hitbox.x + hitbox.width : hitbox.x;
            startingBottleY = hitbox.y + hitbox.height / 2;

            float offsetX = bottlesPositions[i][0] * directionChanger * -1;
            float offsetY = bottlesPositions[i][1];
            int direction = (int) (bottlesPositions[i][2] * directionChanger);

            drunkBottles.add(new DrunkBottleModel(startingBottleX + offsetX, startingBottleY + offsetY, direction));
            drunkBottles.get(i).setSuperDrunk(true);
        }
        shot = false;
    }

    private void updateDrunkBottles() {
        for(DrunkBottleModel drunkBottleModel : drunkBottles)
            drunkBottleModel.update();
    }

    protected void updatePos() {
        switch (walkDir) {
            case UP_RIGHT -> {
                if (canSuperDrunkMoveOnThisX(hitbox.x + walkSpeed))
                    if (canSuperDrunkMoveOnThisY(hitbox.y - walkSpeed))
                        move(walkSpeed, -walkSpeed);
                    else
                        walkDir = DOWN_RIGHT;
                else
                    walkDir = UP_LEFT;
            }
            case UP_LEFT -> {
                if (canSuperDrunkMoveOnThisX(hitbox.x - walkSpeed))
                    if (canSuperDrunkMoveOnThisY(hitbox.y - walkSpeed))
                        move(-walkSpeed, -walkSpeed);
                    else
                        walkDir = DOWN_LEFT;
                else
                    walkDir = UP_RIGHT;
            }
            case DOWN_RIGHT -> {
                if (canSuperDrunkMoveOnThisX(hitbox.x + walkSpeed))
                    if (canSuperDrunkMoveOnThisY(hitbox.y + walkSpeed))
                        move(walkSpeed, walkSpeed);
                    else
                        walkDir = UP_RIGHT;
                else
                    walkDir = DOWN_LEFT;
            }
            case DOWN_LEFT -> {
                if (canSuperDrunkMoveOnThisX(hitbox.x - walkSpeed))
                    if (canSuperDrunkMoveOnThisY(hitbox.y + walkSpeed))
                        move(-walkSpeed, walkSpeed);
                    else
                        walkDir = UP_LEFT;
                else
                    walkDir = DOWN_RIGHT;
            }
        }
    }

    private void move(float xMovement, float yMovement) {
        hitbox.x += xMovement;
        hitbox.y += yMovement;
    }

    private boolean canSuperDrunkMoveOnThisY(float nextY) {
        return !(nextY + hitbox.height >= GAME_HEIGHT - (2 * TILES_SIZE) || nextY <= TILES_SIZE);
    }

    private boolean canSuperDrunkMoveOnThisX(float nextX) {
        return !(nextX + hitbox.width  >= GAME_WIDTH - TILES_SIZE || nextX <= TILES_SIZE);
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public boolean hasBeenHit() {
        return hasBeenHit;
    }

    public void setHasBeenHit(boolean hasBeenHit) {
        this.hasBeenHit = hasBeenHit;
    }

    public void decreaseLives() {
        this.lives--;
    }

    public ArrayList<DrunkBottleModel> getDrunkBottles() {
        return drunkBottles;
    }

}
