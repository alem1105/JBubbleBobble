package model.entities.enemies;

import model.entities.EntityModel;
import model.entities.PlayerModel;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.Enemies.*;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.TILES_IN_WIDTH;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;
import static model.utilz.UtilityMethods.getPlayer;

/**
 * Classe astratta che rappresenta il modello di un nemico nel gioco.
 * Estende la classe {@link EntityModel} e gestisce le funzionalità e gli stati dei nemici.
 */
public abstract class EnemyModel extends EntityModel {
    /**
     * Indica se il nemico è attivo.
     */
    protected boolean active = true;

    /**
     * Indica se il nemico è intrappolato in una bolla.
     */
    protected boolean inBubble = false;

    /**
     * Indica se il nemico è arrabbiato.
     */
    protected boolean angry = false;

    /**
     * Indica se il contatore dell'animazione deve essere azzerato.
     */
    protected boolean resetAniTick = false;

    /**
     * Tempo in cui il nemico rimane intrappolato nella bolla.
     */
    protected int inBubbleTime;

    /**
     * Stato attuale del nemico.
     */
    protected int enemyState = RUNNING;

    /**
     * Direzione di camminata del nemico. Può assumere valori come {@code RIGHT} o {@code LEFT}.
     */
    protected int walkDir = RIGHT;

    /**
     * Velocità di camminata del nemico.
     */
    protected float walkSpeed;

    /**
     * Indica se il nemico sta salendo.
     * Valore predefinito è {@code false}.
     */
    protected boolean goingUp = false;

    /**
     * Obiettivo verticale del nemico, rappresentato come indice di tile.
     */
    protected int targetYTile;

    /**
     * Indica se il nemico sta facendo l'animazione della morte.
     * Valore predefinito è {@code false}.
     */
    private boolean deathMovement = false;

    /**
     * Indica se il movimento di morte deve essere invertito.
     * Valore predefinito è {@code false}.
     */
    private boolean invertDeathMovement = false;

    /**
     * Indica se il nemico ha già eseguito il movimento parabolico.
     * Valore predefinito è {@code false}.
     */
    private boolean alreadyDidParable = false;

    /**
     * Tick per il movimento parabolico.
     */
    private int parableTick = 0;

    /**
     * Indice del movimento parabolico, che determina quale movimento parabolico eseguire.
     */
    private int parableMoveIndex = 0;

    /**
     * Array bidimensionale che rappresenta i movimenti parabolici del nemico.
     * Ogni riga contiene tre valori: il numero di tick per il movimento,
     * il valore di spostamento orizzontale e il valore di spostamento verticale.
     */
    private float[][] parableMoves = {
            {20, 1, -4.5f},
            {20, 2, -0.7f},
            {20, 0.8f, 4},
            {20, 0.8f, 2}
    };

    /**
     * Timer per il nemico che rimane fermo, utilizzato per gestire le azioni di attacco.
     */
    protected int stillTimer = 30;

    /**
     * Tick per il timer fermo, utilizzato per tenere traccia del tempo in cui il nemico rimane fermo.
     */
    protected int stillTick = 0;

    /**
     * Timer per il tiro del nemico, utilizzato per controllare la frequenza di sparo.
     */
    protected int shootingTimer = 120;

    /**
     * Tick per il timer di tiro, utilizzato per monitorare il tempo trascorso tra i tiri.
     */
    protected int shootingTick = 0;

    /**
     * Indica se il nemico è fermo.
     * Valore predefinito è {@code false}.
     */
    protected boolean still = false;

    /**
     * Indica se il nemico ha sparato.
     * Valore predefinito è {@code false}.
     */
    protected boolean shot = false;

    /**
     * Indica se il cibo è stato generato.
     */
    protected boolean foodSpawned;

    /**
     * Indica se la bolla è nell'area in cui può iniziare a fluttuare.
     */
    protected boolean stuck;

    /**
     * Velocità del nemico quando è in bolla.
     */
    protected float bubbleSpeed = 0.3F * SCALE;

    /** Durata del percorso del nemico in bolla. */
    protected int pathDuration = 240;

    /** Tick del percorso del nemico in bolla. */
    protected int pathTick = 0;

    /**
     * Costruttore della classe EnemyModel.
     *
     * @param x      La posizione x del nemico.
     * @param y      La posizione y del nemico.
     * @param width  La larghezza del nemico.
     * @param height L'altezza del nemico.
     */
    public EnemyModel(float x, float y, int width, int height) {
        super(x, y - 1, width, height);
    }

    /**
     * Aggiorna lo stato del nemico. Se il nemico è in uno stato di corsa,
     * aggiorna la posizione e lo stato del nemico.
     */
    public void update() {
        updatePos();
        updateEnemyState();
    }

    /**
     * Metodo per aggiornare la posizione del nemico.
     * Controlla se il nemico è in aria o nella bolla e aggiorna la posizione di conseguenza.
     */
    protected void updatePos() {
        if(inBubble) {
            inBubbleMovement();
            return;
        }

        isInAirCheck();

        if (!goingUp) {
            if (inAir)
                fallingChecks(walkSpeed);
            else
                updateXPos(walkSpeed);
        } else {
            if ((int) (hitbox.y / TILES_SIZE) != targetYTile) {
                hitbox.y -= 1.5F;
            } else {
                goingUp = false;
            }
        }
    }

    /**
     * Gestisce il movimento del nemico intrappolato in una bolla.
     * Se il nemico non è bloccato, lo sposta verticalmente fino a quando non raggiunge una certa altezza o posizione orizzontale.
     * Una volta bloccato, attiva il movimento fluttuante.
     */
    protected void inBubbleMovement() {
            if (!stuck) {
                if (getEnemyTileY() > 2) {
                    hitbox.y -= bubbleSpeed;
                } else {
                    if (isBubbleInXRange()) {
                        stuck = true;
                    } else {
                        checkBubbleDirection();
                    }
                }
            } else {
                startFloating();
            }
    }

    /**
     * Inizia il movimento fluttuante del nemico intrappolato in una bolla.
     */
    protected void startFloating() {
        if (pathTick <= pathDuration / 2) {
            hitbox.y -= bubbleSpeed;
        } else if (pathTick > pathDuration / 2 && pathTick <= pathDuration) {
            hitbox.y += bubbleSpeed;
        } else {
            pathTick = 0;
        }
        pathTick++;
    }

    /**
     * Controlla la direzione di movimento del nemico nella bolla.
     * Sposta il nemico orizzontalmente a destra o a sinistra a seconda della posizione rispetto all'intervallo centrale.
     */
    protected void checkBubbleDirection() {
        if (getEnemyTileX() < TILES_IN_WIDTH / 2 - 2) {
            hitbox.x += bubbleSpeed;
        } else {
            hitbox.x -= bubbleSpeed;
        }
    }

    /**
     * Verifica se la bolla si trova nell'intervallo orizzontale corretto per iniziare a fluttuare.
     *
     * @return true se la bolla si trova nell'intervallo, false altrimenti.
     */
    protected boolean isBubbleInXRange() {
        return TILES_IN_WIDTH / 2 - 2 <= getEnemyTileX() && getEnemyTileX() <= TILES_IN_WIDTH / 2 + 1;
    }

    /**
     * Aggiorna la posizione orizzontale del nemico in base alla velocità di camminata.
     *
     * @param walkSpeed La velocità di camminata del nemico.
     */
    @Override
    public void updateXPos(float walkSpeed) {
        if (playerAndEnemyAreOnTheSameRow() && !getPlayer().isInvincible()) {
            walkwithSameY();
        } else {
            if(isPlayerOnTopOfTheEnemy() && !(getPlayer().isInAir())) {
                if(checkUpSolid(getLvlData()) && !inAir) {
                    goingUp = true;
                } else {
                    walkWithDifferentY();
                }
            } else {
                walkWithDifferentY();
            }
        }
    }

    /**
     * Gestisce il movimento del nemico quando si trova sulla stessa riga del giocatore.
     */
    protected void walkwithSameY() {
        if(isPlayerToLeftOfEnemy()) {
            walkDir = LEFT;
            walkSpeed = -Math.abs(walkSpeed);
            checkEnemyMovement();
        } else if(isPlayerToRightOfEnemy()) {
            walkDir = RIGHT;
            walkSpeed = Math.abs(walkSpeed);
            checkEnemyMovement();
        } else {
            walkWithDifferentY();
        }
    }

    /**
     * Gestisce il movimento del nemico quando si trova su una riga diversa rispetto al giocatore.
     */
    protected void walkWithDifferentY() {
        if (walkDir == RIGHT) {
            walkSpeed = Math.abs(walkSpeed);
            if (canEnemyMoveHere()) {
                hitbox.x += walkSpeed;
            } else {
                hitbox.x = getEntityXPosNextToWall(hitbox, walkSpeed);
                walkDir = LEFT;
            }
        } else {
            walkSpeed = -Math.abs(walkSpeed);
            if (canEnemyMoveHere()) {
                hitbox.x += walkSpeed;
            } else {
                hitbox.x = getEntityXPosNextToWall(hitbox, walkSpeed);
                walkDir = RIGHT;
            }
        }
    }

    /**
     * Controlla e aggiorna la posizione del nemico in base alla possibilità di movimento.
     */
    private void checkEnemyMovement() {
        if(canEnemyMoveHere()) {
            hitbox.x += walkSpeed;
        } else {
            hitbox.x = getEntityXPosNextToWall(hitbox, walkSpeed);
        }
    }

    /**
     * Controlla se il nemico può muoversi nella posizione attuale.
     *
     * @return true se il nemico può muoversi, false altrimenti.
     */
    private boolean canEnemyMoveHere() {
        return canMoveHere(hitbox.x + walkSpeed, hitbox.y, hitbox.width, hitbox.height, getLvlData());
    }

    /**
     * Aggiorna lo stato del nemico in base alla sua condizione attuale (es. intrappolato nella bolla, arrabbiato).
     */
    public void updateEnemyState() {
        int startAni = enemyState;

        if (inBubble) {
            inBubbleTime++;
            if (inBubbleTime >= 600) {
                angry = true;
                stuck = false;
                inBubble = false;
                inBubbleTime = 0;
            }
            if (angry)
                enemyState = CAPTURED_ANGRY;
            else
                enemyState = CAPTURED;
        }
        else if (angry) {
            enemyState = RUNNING_ANGRY;
            walkSpeed = 0.80f * SCALE;
        }

        if (startAni != enemyState)
            resetAniTick = true;
        else
            resetAniTick = false;
    }

    /**
     * Gestisce il movimento del nemico alla morte.
     *
     * @param enemyModel Il modello del nemico.
     * @param direction   La direzione del movimento.
     */
    public void doDeathMovement(EnemyModel enemyModel, int direction) {
        if(!(deathMovement))
            parableMovement(enemyModel, direction);
    }

    /**
     * Esegue il movimento parabolico alla morte del nemico.
     *
     * @param enemyModel Il modello del nemico.
     * @param direction   La direzione del movimento.
     */
    private void parableMovement(EnemyModel enemyModel, int direction) {
        if (invertDeathMovement) {
            if(direction == RIGHT) {
                hitbox.y += 2;
                hitbox.x -= 0.8F;
            } else {
                hitbox.y += 2;
                hitbox.x += 0.8F;
            }
            if(checkIfEnemyIsOnTheFloorBorder(enemyModel))
                deathMovement = true;
        } else {
            checkIfEnemyHitASideBorder(enemyModel);
            if (alreadyDidParable)
                deathMovement = true;
            else
                doParableMovement(enemyModel, direction);
        }
    }

    /**
     * Controlla se il nemico ha colpito il bordo laterale.
     *
     * @param enemyModel Il modello del nemico.
     */
    private void checkIfEnemyHitASideBorder(EnemyModel enemyModel) {
        if((enemyModel.getEnemyTileX() >= TILES_IN_WIDTH - 1 || enemyModel.getEnemyTileX() <= 0))
            invertDeathMovement = true;
    }

    /**
     * Esegue il movimento parabolico.
     *
     * @param enemyModel Il modello del nemico.
     * @param direction   La direzione del movimento.
     */
    private void doParableMovement(EnemyModel enemyModel, int direction) {
        if(parableMoveIndex > 3) {
            if(checkIfEnemyIsOnTheFloorBorder(enemyModel)){
                alreadyDidParable = true;
            } else {
                float xToAdd = (direction == RIGHT) ? parableMoves[parableMoves.length - 1][1] : -parableMoves[parableMoves.length - 1][1];
                float yToAdd = parableMoves[parableMoveIndex - 1][2];

                hitbox.x += xToAdd;
                hitbox.y += yToAdd;
            }
        } else {
            if (parableTick <= parableMoves[parableMoveIndex][0]) {
                float xToAdd = (direction == RIGHT) ? parableMoves[parableMoveIndex][1] : -parableMoves[parableMoveIndex][1];
                float yToAdd = parableMoves[parableMoveIndex][2];

                hitbox.x += xToAdd;
                hitbox.y += yToAdd;
            } else {
                parableTick = 0;
                parableMoveIndex++;
            }
        }
        parableTick++;
    }

    /**
     * Controlla se il nemico si trova al bordo del pavimento.
     *
     * @param enemyModel Il modello del nemico.
     * @return true se il nemico è al bordo del pavimento, false altrimenti.
     */
    private boolean checkIfEnemyIsOnTheFloorBorder(EnemyModel enemyModel) {
        return enemyModel.getEnemyTileY() >= TILES_IN_HEIGHT - 3 || enemyModel.getEnemyTileY() <= 0;
    }

    /**
     * Controlla se sopra il nemico ci sono tre tile solidi.
     *
     * @param lvlData Dati di livello per controllare la solidità delle tile.
     * @return true se ci sono tre tile solidi sopra il nemico, false altrimenti.
     */
    protected boolean checkUpSolid(int[][] lvlData) {
        int currentTileY = (int) (hitbox.y / TILES_SIZE);
        int currentTileX = (int) (hitbox.x / TILES_SIZE);
        if(checkThreeYTilesSolid(currentTileY - 2, currentTileX, lvlData)) {
            targetYTile = currentTileY - 4;
            return true;
        } else if(checkThreeYTilesSolid(currentTileY - 1, currentTileX, lvlData)) {
            targetYTile = currentTileY - 3;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Controlla se ci sono tre tile solide in una certa posizione verticale.
     *
     * @param yTile  La coordinata Y della tile.
     * @param xTile  La coordinata X della tile.
     * @param lvldata Dati di livello per controllare la solidità delle tile.
     * @return true se ci sono tre tile solide, false altrimenti.
     */
    protected boolean checkThreeYTilesSolid(int yTile, int xTile, int[][] lvldata) {
        if (xTile - 2 >= 0 && yTile - 2 >= 0 && xTile + 1 >= 0)
            return isTileSolid(xTile - 2, yTile, lvldata) && isTileSolid(xTile, yTile, lvldata) && isTileSolid(xTile + 1, yTile, lvldata);
        return false;
    }

    // Metodi per la gestione dell'attacco

    /**
     * Avvia il timer di tiro del nemico.
     */
    protected void startShootingTimer() {
        if(shootingTick >= shootingTimer && !inAir) {
            still = true;
            shootingTick = 0;
            shot = false;
        }
        shootingTick++;
    }

    /**
     * Avvia il timer per rimanere fermo.
     */
    protected void startStillTimer() {
        if (stillTick >= stillTimer)  {
            stillTick = 0;
            still = false;
            shootingTick = 0;
        }
        stillTick++;
    }

    /**
     * @return true se il nemico è nella stessa y del player
     */
    protected boolean isEnemyOnPlayerY() {
        return (int) (hitbox.y / TILES_SIZE) == (int) (PlayerModel.getInstance().getHitbox().y / TILES_SIZE);
    }

    protected boolean isPlayerToRightOfEnemy() {
        return getPlayerTileX() > getEnemyTileX();
    }

    protected boolean isPlayerToLeftOfEnemy() {
        return getPlayerTileX() < getEnemyTileX();
    }

    protected boolean playerAndEnemyAreOnTheSameRow() {
        return getPlayerTileY() == getEnemyTileY();
    }

    protected boolean isPlayerOnTopOfTheEnemy() {
        return getPlayerTileY() < getEnemyTileY();
    }

    protected int getPlayerTileY() {
        return (int) (getPlayer().getHitbox().y / TILES_SIZE);
    }

    protected int getEnemyTileY() {
        return (int) (hitbox.y / TILES_SIZE);
    }

    protected int getPlayerTileX() {
        return (int) (getPlayer().getHitbox().x / TILES_SIZE);
    }

    protected int getEnemyTileX() {
        return (int) (hitbox.x / TILES_SIZE);
    }

    public int getEnemyState() {
        return enemyState;
    }

    public void setEnemyState(int enemyState) {
        this.enemyState = enemyState;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setInBubble(boolean inBubble) {this.inBubble = inBubble;}

    public int getWalkDir() {
        return walkDir;
    }

    public boolean isInBubble() {
        return inBubble;
    }

    public boolean isActive() {
        return active;
    }

    public void setResetAniTick(boolean resetAniTick) {
        this.resetAniTick = resetAniTick;
    }

    public boolean isResetAniTick() {
        return resetAniTick;
    }

    public boolean isDeathMovement() {
        return deathMovement;
    }

    public boolean isFoodSpawned() {
        return foodSpawned;
    }

    public void setFoodSpawned(boolean foodSpawned) {
        this.foodSpawned = foodSpawned;
    }
}
