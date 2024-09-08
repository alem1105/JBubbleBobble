package view.objects.bobbles;

import model.objects.bobbles.FireModel;
import view.objects.CustomObjectView;
import view.utilz.LoadSave;

import static model.utilz.Constants.GameConstants.SCALE;
import static view.utilz.LoadSave.loadAnimations;

import java.awt.Graphics;

/**
 * Rappresenta la visualizzazione del fuoco nel gioco.
 * Questa classe gestisce l'animazione e la logica di visualizzazione del fuoco.
 */
public class FireView extends CustomObjectView<FireModel> {

    private int xDrawOffset = 4, yDrawOffset = 2;
    private int disappearingTimer = 120;  // Timer per la scomparsa del fuoco
    private int disappearingTick = 0;      // Contatore per il tempo di scomparsa

    /**
     * Costruttore per la classe FireView.
     *
     * @param objectModel Il modello del fuoco associato a questa vista.
     */
    public FireView(FireModel objectModel) {
        super(objectModel);
        sprites = loadAnimations(LoadSave.FIRE_SPRITE, 2, 3, 16, 16);
        spriteIndex = 0;
    }

    /**
     * Aggiorna lo stato e l'animazione del fuoco.
     * Incrementa il contatore per il timer di scomparsa se il fuoco non è attivo.
     */
    public void update() {
        setSpriteIndex();
        updateAnimationTick();
        if (!objectModel.isActive())
            disappearingTick++;
    }

    /**
     * Verifica se il fuoco può essere disegnato.
     *
     * @return true se il timer di scomparsa non è scaduto, false altrimenti.
     */
    public boolean canDrawFire() {
        return disappearingTick <= disappearingTimer;
    }

    /**
     * Imposta l'indice della sprite da visualizzare in base allo stato del fuoco.
     */
    private void setSpriteIndex() {
        if (!objectModel.isActive())
            spriteIndex = 1;
    }

    /**
     * Restituisce il numero di sprite in base allo stato attivo
     * @return 3 se non attivo, 2 altrimenti
     */
    @Override
    protected int getSpriteAmount() {
        return !objectModel.isActive() ? 3 : 2; // Restituisce il numero di sprite in base allo stato attivo
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(sprites[spriteIndex][aniIndex],
                (int) objectModel.getX() - xDrawOffset,
                (int) objectModel.getY() - yDrawOffset,
                (int) (16 * SCALE),
                (int) (16 * SCALE),
                null);
    }
}
