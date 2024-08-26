package view.entities;

import model.LevelManager;
import model.entities.PlayerModel;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PlayerConstants.*;
import static model.utilz.Constants.GameConstants.ANI_SPEED;

public class PlayerView {

    private static final int ROW_INDEX = 6;
    private static final int COL_INDEX = 6;

    private int aniTick, aniIndex;
    private PlayerModel playerModel;

    private BufferedImage[][] animations;

    private float xDrawOffset = 3 * SCALE;
    private float yDrawOffset = 3 * SCALE;

    private int flipW = 1, flipX = 0;

    public PlayerView(PlayerModel playerModel) {
        this.playerModel = playerModel;
        animations = LoadSave.loadAnimations(LoadSave.PLAYER_SPRITE, ROW_INDEX, COL_INDEX, 18, 18);
    }

    public void update() {
        updateAnimationTick();
        updateDirections();
        checkAniTick();
    }

    private void checkAniTick() {
        if (playerModel.isResetAniTick()) {
            aniTick = 0;
            aniIndex = 0;
        }
    }

    private void updateDirections() {
        if (playerModel.isLeft()) {
            flipX = playerModel.getWidth();
            flipW = -1;
        }
        if (playerModel.isRight()) {
            flipX = 0;
            flipW = 1;
        }
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerModel.getPlayerAction()][aniIndex],
                (int) (playerModel.getHitbox().x - xDrawOffset) + flipX, (int) (playerModel.getHitbox().y - yDrawOffset),
                playerModel.getWidth() * flipW, playerModel.getHeight(), null);
        drawHitbox(g);
    }

    public void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(playerModel.getPlayerAction())) {
                if (playerModel.getPlayerAction() == DEATH) {
                    playerModel.setPlayerAction(IDLE);
                    playerModel.getHitbox().x = getPlayerSpawn().x;
                    playerModel.getHitbox().y = getPlayerSpawn().y;
                    playerModel.setInAir(true);
                    playerModel.setInvincible(true);
                }
                if (playerModel.isAttack()) {
                    playerModel.setAttack(false);
                    playerModel.setAttackingClick(false);
                }
                aniIndex = 0;
            }
        }
    }

    public int getSpriteAmount(int player_action) {
        return switch (player_action) {
            case DEATH -> 6;
            case JUMP, FALL, IDLE, RUNNING -> 2;
            default -> 1; // Comprende anche Attack
        };
    }

    private void drawHitbox(Graphics g) {
        // For debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int) (playerModel.getHitbox().x), (int) (playerModel.getHitbox().y),
                (int) playerModel.getHitbox().width,
                (int) playerModel.getHitbox().height);
    }

    public PlayerModel getPlayerModel() {
        return playerModel;
    }

    public Point getPlayerSpawn() {
        return playerModel.getLevelManager().getLevels().get(playerModel.getLevelManager().getLvlIndex()).getPlayerSpawn();
    }
}
