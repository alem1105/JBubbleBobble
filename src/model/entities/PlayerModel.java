package model.entities;

import model.UserModel;
import model.gamestate.UserStateModel;
import model.objects.bobbles.BobBubbleModel;
import model.objects.bobbles.BubbleManagerModel;
import model.utilz.Constants;

import java.awt.*;

import static model.utilz.Constants.CustomObjects.BUBBLE_SIZE;
import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;
import static model.utilz.Constants.GameConstants.SCALE;
import static model.utilz.Constants.PlayerConstants.*;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

/**
 * Classe che rappresenta il modello del giocatore nel gioco.
 * Estende la classe {@link EntityModel} e gestisce le azioni e le interazioni del giocatore.
 */
public class PlayerModel extends EntityModel {

    /**
     * Istanza statica del modello del giocatore.
     */
    private static PlayerModel instance;

    /**
     * Azione attuale del giocatore.
     * Rappresenta lo stato di animazione attuale del giocatore.
     */
    private int playerAction = IDLE;

    /**
     * Stati dei comandi del giocatore.
     * Indica se il giocatore sta premendo i tasti per andare a sinistra, a destra, saltare o resettare il tick dell'animazione.
     */
    private boolean left, right, jump, resetAniTick;

    /**
     * Indica se il giocatore si sta muovendo.
     */
    private boolean moving = false;

    /**
     * Velocità del giocatore.
     * Rappresenta la velocità di movimento del giocatore, scalata per la dimensione del gioco.
     */
    private float playerSpeed = 1.0f * SCALE;

    /**
     * Velocità di salto.
     * Rappresenta la velocità con cui il giocatore salta, scalata per la dimensione del gioco.
     */
    private float jumpSpeed = -2.25f * SCALE;

    /**
     * Numero di vite del giocatore.
     * Indica quante vite ha il giocatore attualmente.
     */
    private int lives = 3;

    /**
     * Indica se il gioco è finito.
     * Segnala che il giocatore ha perso tutte le vite.
     */
    private boolean gameOver = false;

    /**
     * Stati dell'attacco del giocatore.
     * Indica se il giocatore sta attaccando e se ha effettuato un clic per attaccare.
     */
    private boolean attack, attackingClick;

    /**
     * Direzione in cui il giocatore sta affrontando.
     * Rappresenta la direzione attuale del giocatore (sinistra o destra).
     */
    private int facing = RIGHT;

    /**
     * Indica se il giocatore è invincibile.
     * Il giocatore non può subire danni quando è in stato di invincibilità.
     */
    private boolean invincible = true;

    /**
     * Durata dell'invincibilità.
     * Indica per quanto tempo il giocatore rimane invincibile dopo essere stato colpito.
     */
    private int invincibleDuration = 600;

    /**
     * Tick per la gestione dell'invincibilità.
     * Contatore utilizzato per monitorare la durata dell'invincibilità.
     */
    private int invincibleTick = 0;

    /**
     * Variabili per i power-up.
     * Contatori per vari eventi, come bolle soffiate, bolle esplose, e oggetti raccolti.
     */
    private int blowedBubbles, poppedBubbles, poppedLightingBubbles, poppedFireBubbles = 1, poppedWaterBubbles = 1;
    private int jumpedTimes, eatenPinkCandies, eatenYellowCandies, runDistanceAmount;

    /**
     * Punti guadagnati per i salti.
     * Rappresenta il punteggio guadagnato dal giocatore per ogni salto effettuato.
     */
    private int scoreForJump = 0;

    /**
     * Indica se il giocatore sta sparando una bolla di fulmine.
     */
    private boolean shootingLightningBubble;

    /**
     * Gestore delle bolle.
     * Rappresenta l'istanza del gestore delle bolle, responsabile della gestione delle bolle nel gioco.
     */
    private BubbleManagerModel bubbleManagerModel = BubbleManagerModel.getInstance();

    /**
     * Restituisce l'istanza singleton di PlayerModel.
     *
     * @return L'istanza di PlayerModel.
     */
    public static PlayerModel getInstance() {
        if (instance == null) {
            instance = new PlayerModel(100, 100, (int) (18 * SCALE), (int) (18 * SCALE));
        }
        return instance;
    }

    /**
     * Costruttore privato per inizializzare il modello del giocatore.
     *
     * @param x La coordinata X iniziale del giocatore.
     * @param y La coordinata Y iniziale del giocatore.
     * @param width Larghezza della hitbox del giocatore.
     * @param height Altezza della hitbox del giocatore.
     */
    private PlayerModel(float x, float y, int width, int height) {
        super(x, y, width, height);
        this.x = getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getPlayerSpawn().x;
        this.y = getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getPlayerSpawn().y;
        initHitbox(13, 14); // Inizializza la hitbox del giocatore
    }

    /**
     * Muove il giocatore alla posizione di spawn.
     */
    public void moveToSpawn() {
        hitbox.x = getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getPlayerSpawn().x;
        hitbox.y = getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getPlayerSpawn().y;
        inAir = true; // Imposta il giocatore in aria
    }

    /**
     * Gestisce la collisione del giocatore.
     * Riduce le vite e riposiziona il giocatore allo spawn se colpito.
     */
    public void playerHasBeenHit() {
        lives--;
        invincible = true; // Il giocatore diventa invincibile temporaneamente
        playerAction = DEATH; // Imposta l'azione del giocatore a morte
    }

    /**
     * Controlla se il giocatore sta attaccando e gestisce l'attacco.
     */
    private void checkAttack() {
        if (attack && !attackingClick) {
            blowedBubbles++; // Incrementa il conteggio delle bolle soffiato
            attackingClick = true; // Segna che il giocatore ha effettuato un attacco
            float bubbleX = hitbox.x;
            if (right) {
                bubbleX += hitbox.width; // Posizione della bolla
            }
            bubbleManagerModel.addBobBubbles(new BobBubbleModel(bubbleX, hitbox.y - (BUBBLE_SIZE - hitbox.height), BUBBLE_SIZE, BUBBLE_SIZE, facing));
        }
    }

    /**
     * Aggiorna lo stato del giocatore ad ogni frame.
     */
    public void update() {
        updatePos();
        checkRidingABubble();
        checkAttack();
        setPlayerAction();
        updateInvincibleStatus();
        if (lives <= 0) {
            gameOver = true;
            UserModel user = UserStateModel.getInstance().getCurrentUserModel();
            user.updateLevelScore();
            user.incrementLosses();
            user.incrementMatches();
            user.setMaxScore();
            user.serialize("res/users/" + user.getNickname() + ".bb");
        }
    }

    /**
     * Aggiorna lo stato di invincibilità del giocatore.
     */
    private void updateInvincibleStatus() {
        if (invincible) {
            invincibleTick++;
            if (invincibleTick >= invincibleDuration) {
                invincible = false; // Termina l'invincibilità
                invincibleTick = 0; // Resetta il tick
            }
        }
    }

    /**
     * Imposta l'azione attuale del giocatore in base ai comandi.
     */
    private void setPlayerAction() {
        int startAni = playerAction;

        if (attack) {
            playerAction = ATTACK;
        } else {
            if (moving)
                playerAction = RUNNING;
            else if (playerAction != DEATH)
                playerAction = IDLE;

            if (inAir) {
                if (airSpeed < 0) playerAction = JUMP;
                else playerAction = FALL;
            }
        }

        resetAniTick = startAni != playerAction;
    }

    /**
     * Aggiorna la posizione del giocatore in base ai comandi.
     */
    private void updatePos() {
        moving = false;

        if (jump) {
            jump();
        }

        if (!inAir) {
            if ((!left && !right) || (right && left)) {
                return;
            }
        }

        float xSpeed = 0;

        if (left) {
            facing = LEFT;
            xSpeed -= playerSpeed;
        }
        if (right) {
            facing = RIGHT;
            xSpeed += playerSpeed;
        }

        isInAirCheck();

        if (inAir) {
            if (airSpeed < 0) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                if (canJumpHere(xSpeed)) hitbox.x += xSpeed;
            } else {
                fallingChecks(xSpeed);
            }
        } else {
            updateXPos(xSpeed);
        }
        moving = true;
    }

    /**
     * Controlla se il giocatore sta cavalcando una bolla.
     */
    private void checkRidingABubble() {
        if (airSpeed > 0) {
            bubbleManagerModel.getBobBubbles().stream()
                    .filter(BobBubbleModel::isActive)
                    .filter(BobBubbleModel::isCollision)
                    .filter(bobBubble -> hitbox.getMaxY() >= bobBubble.getHitbox().getY() - (5 * SCALE) // Controlla se il giocatore è sopra la bolla
                            && hitbox.getMaxY() <= bobBubble.getHitbox().getY() + (5 * SCALE)
                            && hitbox.x >= bobBubble.getHitbox().getX()
                            && hitbox.x <= bobBubble.getHitbox().getMaxX()
                            && jump) // Verifica se sta saltando
                    .findFirst() // Si ferma alla prima bolla trovata
                    .ifPresent(bobBubble -> {
                        inAir = true; // Imposta il giocatore in aria
                        airSpeed = jumpSpeed; // Imposta la velocità di salto
                    });
        }
    }

    /**
     * Aggiorna la posizione orizzontale del giocatore.
     *
     * @param xSpeed La velocità orizzontale da applicare.
     */
    @Override
    public void updateXPos(float xSpeed) {
        if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height,
                getLvlData())) {
            hitbox.x += xSpeed;
            runDistanceAmount += (int) Math.abs(xSpeed);
        } else {
            hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    /**
     * Controlla se il giocatore può saltare nella posizione attuale.
     *
     * @param xSpeed La velocità orizzontale da considerare.
     * @return True se il giocatore può saltare, false altrimenti.
     */
    private boolean canJumpHere(float xSpeed) {
        return hitbox.x + xSpeed > Constants.GameConstants.TILES_SIZE &&
                hitbox.x + xSpeed + hitbox.width < Constants.GameConstants.GAME_WIDTH - Constants.GameConstants.TILES_SIZE;
    }

    /**
     * Esegue il salto del giocatore.
     */
    private void jump() {
        if (inAir)
            return;
        UserStateModel.getInstance().getCurrentUserModel().incrementTempScore(scoreForJump);
        jumpedTimes++;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    public int getPlayerAction() {
        return playerAction;
    }

    public void setRight(boolean b) {
        this.right = b;
    }

    public void setLeft(boolean b) {
        this.left = b;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isResetAniTick() {
        return resetAniTick;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setPlayerAction(int playerAction) {
        this.playerAction = playerAction;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public void setAttack(boolean attack) {
        this.attack = attack;
    }

    public boolean isAttack() {
        return attack;
    }

    public void setAttackingClick(boolean attackingClick) {
        this.attackingClick = attackingClick;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public float getAirSpeed() {
        return airSpeed;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean getJump() {
        return jump;
    }

    public int getJumpedTimes() {
        return jumpedTimes;
    }

    public void setScoreForJump(int value) {
        scoreForJump = value;
    }

    public int getPoppedWaterBubbles() {
        return poppedWaterBubbles;
    }

    public int getBlowedBubbles() {
        return blowedBubbles;
    }

    public void setBlowedBubbles(int blowedBubbles) {
        this.blowedBubbles = blowedBubbles;
    }

    public int getPoppedBubbles() {
        return poppedBubbles;
    }

    public void setPoppedBubbles(int poppedBubbles) {
        this.poppedBubbles = poppedBubbles;
    }

    public int incrementPoppedBubbles() {
        return poppedBubbles++;
    }

    public int getPoppedLightingBubbles() {
        return poppedLightingBubbles;
    }

    public void incrementPoppedLightingBubbles() {
        poppedLightingBubbles++;
    }

    public int getPoppedFireBubbles() {
        return poppedFireBubbles;
    }

    public void incrementPoppedFireBubbles() {
        poppedFireBubbles++;
    }

    public void incrementPoppedWaterBubbles() {
        poppedWaterBubbles ++;
    }

    public void setJumpedTimes(int jumpedTimes) {
        this.jumpedTimes = jumpedTimes;
    }

    public int getEatenPinkCandies() {
        return eatenPinkCandies;
    }

    public void setEatenPinkCandies(int eatenPinkCandies) {
        this.eatenPinkCandies = eatenPinkCandies;
    }

    public int getEatenYellowCandies() {
        return eatenYellowCandies;
    }

    public void setEatenYellowCandies(int eatenYellowCandies) {
        this.eatenYellowCandies = eatenYellowCandies;
    }

    public int getRunDistanceAmount() {
        return runDistanceAmount;
    }

    public void setRunDistanceAmount(int runDistanceAmount) {
        this.runDistanceAmount = runDistanceAmount;
    }

    public void setPlayerSpeed(float value) {
        this.playerSpeed = value;
    }

    public void setJumpSpeed(float value) {
        this.jumpSpeed = value;
    }

    public void setGravity(float value) {
        this.gravity = value;
    }

    public void setPoppedLightingBubbles(int poppedLightingBubbles) {
        this.poppedLightingBubbles = poppedLightingBubbles;
    }

    public void incrementLives() {
        this.lives++;
    }

    public int getFacing() {
        return facing;
    }

    public boolean isShootingLightningBubble() {
        return shootingLightningBubble;
    }

    public void setShootingLightningBubble(boolean shootingLightningBubble) {
        this.shootingLightningBubble = shootingLightningBubble;
    }

    public Point getPlayerSpawn() {
        return getLevelManager().getLevels().get(getLevelManager().getLvlIndex()).getPlayerSpawn();
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }
}
