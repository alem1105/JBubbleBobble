package view.objects.bobbles;

import model.objects.bobbles.WaterModel;
import view.objects.CustomObjectView;
import view.utilz.LoadSave;

import java.awt.*;

import static model.utilz.Constants.CustomObjects.WATER_FALLING;
import static model.utilz.Constants.CustomObjects.WATER_WALKING;
import static model.utilz.Constants.Directions.LEFT;
import static model.utilz.Constants.Directions.RIGHT;

/**
 * La classe WaterView rappresenta la vista dell'acqua nel gioco.
 * Estende la classe generica CustomObjectView e si occupa di gestire l'aspetto e l'animazione dell'acqua.
 */
public class WaterView extends CustomObjectView<WaterModel> {

    private int waterState;
    private int flipW = 1, flipX = 0;
    private int index;

    /**
     * Costruttore della classe WaterView.
     * Carica le animazioni e imposta l'indice dell'animazione.
     *
     * @param objectModel il modello dell'oggetto acqua.
     */
    public WaterView(WaterModel objectModel) {
        super(objectModel);
        sprites = LoadSave.loadAnimations(LoadSave.WATER_SPRITE, 5, 1, 8, 8);
        aniIndex = 0;
    }

    /**
     * Aggiorna lo stato dell'acqua e l'animazione corrente.
     */
    public void update() {
        updateWaterState();
        setSpriteIndex();
        updateSpriteBasedOnDirection();
    }

    /**
     * Aggiorna lo stato dell'acqua in base alla sua condizione (in aria o a terra).
     * Se l'acqua è in aria, lo stato sarà WATER_FALLING, altrimenti sarà WATER_WALKING.
     */
    private void updateWaterState() {
        waterState = (objectModel.isInAir()) ? WATER_FALLING : WATER_WALKING;
    }

    /**
     * Imposta l'indice dello sprite da utilizzare in base allo stato dell'acqua e alla sua posizione nell'array della "cascata".
     * Viene gestito un diverso sprite per il primo e l'ultimo "cubetto" dell'acqua.
     */
    private void setSpriteIndex() {
        spriteIndex = (waterState == WATER_FALLING) ? 1 : 0;
        if(index == 1 || index == 10) {
            if(waterState == WATER_FALLING) {
                if(index == 1)
                    spriteIndex = 4;
                else
                    spriteIndex = 3;
            } else {
                spriteIndex = 2;
            }
        }
    }

    /**
     * Restituisce il numero di sprite disponibili per l'oggetto acqua.
     *
     * @return il numero di sprite disponibili.
     */
    @Override
    protected int getSpriteAmount() {
        return 1;
    }

    /**
     * Aggiorna lo sprite in base alla direzione dell'acqua.
     * Se l'acqua si sta muovendo a sinistra o destra, l'immagine viene capovolta.
     */
    private void updateSpriteBasedOnDirection() {
        if(index == 0)
            return;

        if ((objectModel.getDirection() == RIGHT && index == 10) || (objectModel.getDirection() == LEFT && index == 1)) {
            flipX = objectModel.getWidth();
            flipW = -1;
        } else {
            flipX = 0;
            flipW = 1;
        }
    }

    /**
     * Disegna l'acqua sullo schermo.
     *
     * @param g l'oggetto Graphics utilizzato per disegnare l'acqua.
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(sprites[spriteIndex][0],
                (int) (objectModel.getHitbox().x) + flipX, (int) (objectModel.getHitbox().y),
                objectModel.getWidth() * flipW, objectModel.getHeight(), null);
    }


    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
