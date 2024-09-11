package model.ui.buttons;

import model.level.LevelManagerModel;

import java.util.Arrays;

import static model.utilz.Constants.GameConstants.*;

import java.awt.Point;

/**
 * Classe che rappresenta un modello di pulsante per gestire i dati di un livello.
 * Questa classe estende {@link CustomButtonModel} e serve a reimpostare il livello allo stato in cui si trovava prima delle modifiche.
 */
public class XButtonModel extends CustomButtonModel {

    /** Dati del livello prima delle modifiche*/
    private int[][] oldLvlData;
    /** Dati dei nemici prima delle modifiche*/
    private int[][] oldEnemiesData;
    /** Spawn del player prima delle modifiche*/
    private Point oldPlayerSpawn;

    /**
     * Costruttore per inizializzare un pulsante con le coordinate e le dimensioni specificate.
     *
     * @param x      Coordinata X iniziale del pulsante.
     * @param y      Coordinata Y iniziale del pulsante.
     * @param width  Larghezza del pulsante.
     * @param height Altezza del pulsante.
     */
    public XButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Aggiorna i dati del livello, dei nemici e la posizione di spawn del giocatore per l'indice specificato.
     *
     * @param index L'indice del livello da cui aggiornare i dati.
     */
    public void updateData(int index){
        updateLvlData(index);
        updateEnemiesData(index);
        updatePlayerSpawn(index);
    }

    /**
     * Aggiorna i dati del livello per l'indice specificato.
     *
     * @param index L'indice del livello da cui ottenere i dati.
     */
    private void updateLvlData(int index) {
        int[][] lvlData = LevelManagerModel.getInstance().getLevels().get(index).getLvlData();
        oldLvlData = new int[TILES_IN_HEIGHT][TILES_IN_WIDTH];
        for (int i = 0; i < lvlData.length; i++) {
            oldLvlData[i] = Arrays.copyOf(lvlData[i], TILES_IN_WIDTH);
        }
    }

    /**
     * Aggiorna i dati dei nemici per l'indice specificato.
     *
     * @param index L'indice del livello da cui ottenere i dati dei nemici.
     */
    private void updateEnemiesData(int index) {
        int[][] enemiesData = LevelManagerModel.getInstance().getLevels().get(index).getEnemiesData();
        oldEnemiesData = new int[TILES_IN_HEIGHT][TILES_IN_WIDTH];
        for (int i = 0; i < enemiesData.length; i++) {
            oldEnemiesData[i] = Arrays.copyOf(enemiesData[i], TILES_IN_WIDTH);
        }
    }

    /**
     * Aggiorna la posizione di spawn del giocatore per l'indice specificato.
     *
     * @param index L'indice del livello da cui ottenere la posizione di spawn del giocatore.
     */
    private void updatePlayerSpawn(int index){
        oldPlayerSpawn = LevelManagerModel.getInstance().getLevels().get(index).getPlayerSpawn();
    }

    public int[][] getOldEnemiesData() {
        return oldEnemiesData;
    }

    public int[][] getOldLvlData() {
        return oldLvlData;
    }

    public Point getOldPlayerSpawn() {
        return oldPlayerSpawn;
    }
}
