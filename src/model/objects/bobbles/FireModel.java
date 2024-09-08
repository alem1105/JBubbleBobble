package model.objects.bobbles;

import model.objects.CustomObjectModel;
import model.utilz.Fallable;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.TILES_SIZE;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

/**
 * La classe {@code FireModel} rappresenta un oggetto di fuoco,
 * creato a seguito dell'esplosione di una bolla di fuoco nel gioco, che può cadere
 * e comportarsi come parte di un "tappeto" di fuoco. Implementa l'interfaccia {@code Fallable},
 * consentendo un comportamento di caduta, e estende {@code CustomObjectModel}.
 */
public class FireModel extends CustomObjectModel implements Fallable {

    /** Indica se il fuoco è in aria. */
    private boolean inAir = true;

    /** Velocità di caduta del fuoco. */
    private float airSpeed = 0.65f * SCALE;

    /** Indica se il fuoco è parte di un tappeto di fuoco e se deve creare un tappeto di fuoco. */
    private boolean partOfTheCarpet, creatingCarpet = false;

    /** Durata del tappeto di fuoco. */
    private int carpetDuration = 300;

    /** Conteggio dei tick durante la durata del tappeto di fuoco. */
    private int carpetTick;

    /**
     * Costruttore della classe {@code FireModel}.
     *
     * @param x coordinata X iniziale del fuoco.
     * @param y coordinata Y iniziale del fuoco.
     * @param partOfTheCarpet indica se il fuoco fa parte di un tappeto di fuoco.
     */
    public FireModel(float x, float y, boolean partOfTheCarpet) {
        super(x, y, (int) (8 * SCALE), (int) (12 * SCALE));
        this.partOfTheCarpet = partOfTheCarpet;
    }

    /**
     * Verifica se il fuoco è ancora in aria controllando se l'entità è sul pavimento.
     * Se non è a terra, il fuoco viene considerato in aria.
     */
    @Override
    public void isInAirCheck() {
        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, getLvlData())) {
                inAir = true;
            }
        }
    }

    /**
     * Gestisce il comportamento del fuoco mentre cade. Se può continuare a cadere,
     * aggiorna la posizione Y. Se la caduta è terminata, imposta a true il booleano
     * per la creazione del tappeto di fuoco e lui stesso ne diventa parte.
     */
    public void fallingChecks() {
        // Controllo collisioni e movimento in aria
        if (!CanMoveHere(hitbox.x, hitbox.y, hitbox.width, hitbox.height, getLvlData())) {
            hitbox.y += airSpeed;
        }

        // Caduta continua
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, getLvlData())) {
            hitbox.y += airSpeed;
            checkOutOfMap();
        }
        // Fine caduta
        else {
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            resetInAir();
            partOfTheCarpet = true;
            creatingCarpet = true;
        }
    }

    /**
     * Controlla se l'entità è uscita dallo schermo. Se esce dalla mappa,
     * viene riposizionata.
     */
    @Override
    public void checkOutOfMap() {
        int currentTileY = (int) (hitbox.y / TILES_SIZE);
        if (currentTileY == TILES_IN_HEIGHT - 1)
            hitbox.y = -TILES_SIZE;
    }

    /**
     * Reimposta lo stato dell'entità, indicando che non è più in aria.
     */
    @Override
    public void resetInAir() {
        inAir = false;
    }

    /**
     * Metodo non implementato per l'aggiornamento della posizione X.
     * Poiché il fuoco non si muove lungo l'asse X, il metodo è vuoto.
     *
     * @param xSpeed velocità lungo l'asse X (non utilizzata).
     */
    @Override
    public void updateXPos(float xSpeed) {
    }

    /**
     * Aggiorna lo stato del fuoco. Se è in aria, esegue i controlli di caduta.
     * Se fa parte di un tappeto, ne aggiorna il timer.
     */
    @Override
    public void update() {
        if (inAir && !partOfTheCarpet)
            fallingChecks();
        else if (partOfTheCarpet)
            carpetTimer();
    }

    /**
     * Esegue il conteggio del tempo in cui il fuoco rimane attivo come parte del tappeto di fuoco.
     * Dopo la scadenza del timer, l'oggetto viene disattivato.
     */
    private void carpetTimer() {
        if (carpetTick <= carpetDuration)
            carpetTick++;
        else
            active = false;
    }

    public boolean isPartOfTheCarpet() {
        return partOfTheCarpet;
    }

    public void setCreatingCarpet(boolean creatingCarpet) {
        this.creatingCarpet = creatingCarpet;
    }

    public boolean isCreatingCarpet() {
        return creatingCarpet;
    }
}
