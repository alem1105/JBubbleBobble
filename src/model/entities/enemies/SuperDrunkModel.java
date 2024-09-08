package model.entities.enemies;

import model.objects.projectiles.DrunkBottleModel;

import java.util.ArrayList;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.GameConstants.*;

/**
 * Rappresenta un nemico chiamato SuperDrunk nel gioco.
 * Estende il modello di nemico base e gestisce la logica di movimento,
 * attacco e gestione delle bottiglie.
 */
public class SuperDrunkModel extends EnemyModel {

    /**
     * Numero di vite del nemico.
     */
    private int lives = 60;

    /**
     * Indica se il nemico è stato colpito.
     */
    private boolean hasBeenHit;

    /**
     * Indica se il nemico ha già sparato.
     */
    private boolean shot;

    /**
     * Lista di bottiglie lanciate dal nemico.
     */
    private ArrayList<DrunkBottleModel> drunkBottles;

    /**
     * Coordinate iniziali della bottiglia da lanciare.
     */
    private float startingBottleX;
    private float startingBottleY;

    /**
     * Posizioni delle bottiglie in relazione alla posizione del nemico.
     * Ogni bottiglia ha una posizione (x, y) e una direzione.
     */
    private float[][] bottlesPositions = {
            {18 * SCALE, -18 * SCALE, UP_RIGHT},
            {18 * SCALE / 2, -18 * SCALE / 2, UP_RIGHT},
            {0, 0, UP_RIGHT},
            {0, 0, RIGHT},
            {0, 0, DOWN_RIGHT},
            {18 * SCALE / 2, 18 * SCALE / 2, DOWN_RIGHT},
            {18 * SCALE, 18 * SCALE, DOWN_RIGHT},
            {18 * SCALE * 1.5f, 18 * SCALE * 1.5f, DOWN_RIGHT}
    };

    /**
     * Costruttore della classe SuperDrunkModel.
     *
     * @param x La coordinata x iniziale del nemico.
     * @param y La coordinata y iniziale del nemico.
     * @param width Larghezza del nemico.
     * @param height Altezza del nemico.
     */
    public SuperDrunkModel(float x, float y, int width, int height) {
        super(x, y, width, height);
        initHitbox(width, height);
        drunkBottles = new ArrayList<>();
        this.walkSpeed = 0.55f * SCALE;
        walkDir = UP_RIGHT;
    }

    /**
     * Aggiorna lo stato del nemico in ogni frame.
     * Chiama i metodi per controllare le condizioni di attacco
     * e aggiorna le bottiglie lanciate.
     */
    @Override
    public void update() {
        super.update();
        checkShotCondition();
        checkShoot();
        updateDrunkBottles();
    }

    /**
     * Controlla se il nemico ha già sparato le bottiglie.
     * Se non ci sono bottiglie attive, resetta lo stato di sparo.
     */
    private void checkShotCondition() {
        if (drunkBottles.isEmpty()) {
            shot = true;
        }

        if (areAllBottlesInactive()) {
            drunkBottles.clear();
        }
    }

    /**
     * Verifica se tutte le bottiglie lanciate sono inattive.
     *
     * @return true se tutte le bottiglie sono inattive, false altrimenti.
     */
    private boolean areAllBottlesInactive() {
        return drunkBottles.stream().noneMatch(DrunkBottleModel::isActive);
    }

    /**
     * Controlla se il nemico può sparare bottiglie.
     * Se ha già sparato, lancia otto bottiglie nella direzione corrente.
     */
    private void checkShoot() {
        if (!shot || inBubble) {
            return;
        }

        for (int i = 0; i < 8; i++) {
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

    /**
     * Aggiorna lo stato delle bottiglie lanciate dal nemico.
     * Se una bottiglia è attiva, chiama il suo metodo di aggiornamento.
     */
    private void updateDrunkBottles() {
        for (DrunkBottleModel drunkBottleModel : drunkBottles) {
            if (drunkBottleModel.isActive()) {
                drunkBottleModel.update();
            }
        }
    }

    /**
     * Aggiorna la posizione del nemico in base alla direzione di movimento.
     * Cambia la direzione se il nemico non può muoversi nella direzione attuale.
     */
    @Override
    protected void updatePos() {
        switch (walkDir) {
            case UP_RIGHT -> {
                if (canSuperDrunkMoveOnThisX(hitbox.x + walkSpeed)) {
                    if (canSuperDrunkMoveOnThisY(hitbox.y - walkSpeed)) {
                        move(walkSpeed, -walkSpeed);
                    } else {
                        walkDir = DOWN_RIGHT;
                    }
                } else {
                    walkDir = UP_LEFT;
                }
            }
            case UP_LEFT -> {
                if (canSuperDrunkMoveOnThisX(hitbox.x - walkSpeed)) {
                    if (canSuperDrunkMoveOnThisY(hitbox.y - walkSpeed)) {
                        move(-walkSpeed, -walkSpeed);
                    } else {
                        walkDir = DOWN_LEFT;
                    }
                } else {
                    walkDir = UP_RIGHT;
                }
            }
            case DOWN_RIGHT -> {
                if (canSuperDrunkMoveOnThisX(hitbox.x + walkSpeed)) {
                    if (canSuperDrunkMoveOnThisY(hitbox.y + walkSpeed)) {
                        move(walkSpeed, walkSpeed);
                    } else {
                        walkDir = UP_RIGHT;
                    }
                } else {
                    walkDir = DOWN_LEFT;
                }
            }
            case DOWN_LEFT -> {
                if (canSuperDrunkMoveOnThisX(hitbox.x - walkSpeed)) {
                    if (canSuperDrunkMoveOnThisY(hitbox.y + walkSpeed)) {
                        move(-walkSpeed, walkSpeed);
                    } else {
                        walkDir = UP_LEFT;
                    }
                } else {
                    walkDir = DOWN_RIGHT;
                }
            }
        }
    }

    /**
     * Muove il nemico di una certa quantità nelle direzioni x e y.
     *
     * @param xMovement Spostamento lungo l'asse x.
     * @param yMovement Spostamento lungo l'asse y.
     */
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
