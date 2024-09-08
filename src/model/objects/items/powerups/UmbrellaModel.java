package model.objects.items.powerups;

import model.LevelManagerModel;

import static model.utilz.Constants.PowerUps.*;

/**
 * La classe {@code UmbrellaModel} estende {@code PowerUpModel} e rappresenta un potenziamento di tipo ombrello che consente di saltare
 * un certo numero di livelli nel gioco. Estende  e modifica l'indice del livello
 * corrente in base al tipo di ombrello.
 */
public class UmbrellaModel extends PowerUpModel {

    /** Gestore dei livelli. */
    private LevelManagerModel levelManager;

    /** Numero di livelli da saltare. */
    private int levelsSkipped;

    /**
     * Costruttore della classe {@code UmbrellaModel}.
     *
     * @param x la coordinata X del potenziamento.
     * @param y la coordinata Y del potenziamento.
     * @param width la larghezza del potenziamento.
     * @param height l'altezza del potenziamento.
     * @param umbrellaType il tipo di ombrello, che determina il numero di livelli da saltare.
     */
    public UmbrellaModel(float x, float y, int width, int height, int umbrellaType) {
        super(x, y, width, height, umbrellaType, 200);
        levelManager = LevelManagerModel.getInstance();
        setLevelSkipped();
    }

    /**
     * Imposta il numero di livelli da saltare in base al tipo di ombrello.
     */
    private void setLevelSkipped() {
        switch (type) {
            case UMBRELLA_ORANGE -> levelsSkipped = 3;
            case UMBRELLA_RED -> levelsSkipped = 5;
            case UMBRELLA_PINK -> levelsSkipped = 7;
        }
    }

    /**
     * Applica l'effetto del potenziamento. Salta un certo numero di livelli e carica il livello successivo.
     */
    @Override
    public void applyEffect() {
        active = false;
        pickedUp = false;
        levelManager.setLvlIndex(levelManager.getLvlIndex() + levelsSkipped - 1);
        LevelManagerModel.getInstance().setLevelSkipped(levelsSkipped);
        LevelManagerModel.getInstance().loadNextLevel();
    }

    /**
     * Annulla l'effetto del potenziamento. Questo metodo non esegue alcuna azione poiché l'effetto non lo necessita.
     */
    @Override
    public void unapplyEffect() {
    }
}
