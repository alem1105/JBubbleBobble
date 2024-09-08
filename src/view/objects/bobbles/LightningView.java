package view.objects.bobbles;

import model.objects.bobbles.LightningModel;
import view.objects.CustomObjectView;
import view.utilz.LoadSave;

/**
 * Rappresenta la visualizzazione del fulmine nel gioco.
 * Questa classe gestisce l'animazione e la logica di visualizzazione del fulmine.
 */
public class LightningView extends CustomObjectView<LightningModel> {

    /**
     * Costruttore per la classe LightningView.
     *
     * @param objectModel Il modello del fulmine associato a questa vista.
     */
    public LightningView(LightningModel objectModel) {
        super(objectModel);
        sprites = LoadSave.loadAnimations(LoadSave.LIGHTNING_SPRITE, 1, 1, 16, 16);
        aniIndex = 0;
    }

    /**
     * Restituisce il numero di sprite da visualizzare.
     *
     * @return sempre 1, poiché il fulmine ha solo una sprite.
     */
    @Override
    protected int getSpriteAmount() {
        return 1;
    }

    public LightningModel getObjectModel() {
        return objectModel;
    }
}

