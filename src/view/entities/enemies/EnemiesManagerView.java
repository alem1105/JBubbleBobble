package view.entities.enemies;

import model.LevelManagerModel;
import model.entities.enemies.*;
import model.objects.items.FoodModel;
import view.objects.items.FoodView;

import java.awt.*;
import java.util.ArrayList;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * La classe {@code EnemiesManagerView} gestisce la visualizzazione di nemici e del food
 * che viene generato alla loro morte nel gioco e ne esegue la creazione, aggiornamento e renderizzazione.
 */
public class EnemiesManagerView {

    /** L'istanza singleton della classe {@code EnemiesManagerView}. */
    private static EnemiesManagerView instance;

    /** Il modello di gestione nemici. */
    private EnemyManagerModel enemyManagerModel;

    /** Lista delle views dei nemici. */
    private ArrayList<EnemyView> enemyViews = new ArrayList<>();

    /** Lista di nemici presenti nel model. */
    private ArrayList<EnemyModel> enemyModels;

    /** Il livello corrente del gioco. */
    private int currentLevel;

    /** Flag per il riavvio. */
    private boolean restart = false;

    /** Lista delle visualizzazioni del cibo. */
    private ArrayList<FoodView> foodViews;

    /** Lista dei cibi nel model. */
    private ArrayList<FoodModel> foodModels;

    /**
     * Restituisce l'istanza singleton della classe {@code EnemiesManagerView}.
     *
     * @return L'istanza singleton di {@code EnemiesManagerView}.
     */
    public static EnemiesManagerView getInstance() {
        if (instance == null) {
            instance = new EnemiesManagerView();
        }
        return instance;
    }

    /**
     * Costruisce una nuova istanza di {@code EnemiesManagerView}.
     * Inizializza i modelli e le views dei nemici e del cibo.
     */
    private EnemiesManagerView() {
        enemyManagerModel = EnemyManagerModel.getInstance();
        initEnemyViewsArrays();
        foodViews = new ArrayList<>();
    }

    /**
     * Aggiorna le visualizzazioni dei nemici e del cibo.
     */
    public void update() {
        checkIfLevelChanged();
        updateEnemies();
        updateFood();
    }

    /**
     * Aggiorna le visualizzazioni del cibo.
     */
    private void updateFood() {
        for (FoodView foodView : foodViews) {
            foodView.update();
        }
    }

    /**
     * Aggiorna le visualizzazioni dei nemici.
     */
    private void updateEnemies() {
        for (EnemyView enemyView : enemyViews) {
            if (!enemyView.getEnemy().isDeathMovement()) {
                enemyView.update();
            }
        }
    }

    /**
     * Controlla se il livello è cambiato e riinizializza le visualizzazioni dei nemici e del cibo.
     */
    private void checkIfLevelChanged() {
        if (LevelManagerModel.getInstance().getLvlIndex() != currentLevel || restart) {
            restart = false;
            initEnemyViewsArrays();
            currentLevel = LevelManagerModel.getInstance().getLvlIndex();
            foodViews.clear();
        }
    }

    /**
     * Renderizza le visualizzazioni dei nemici e del cibo.
     *
     * @param g L'oggetto {@code Graphics} su cui disegnare le visualizzazioni.
     */
    public void render(Graphics g) {
        renderEnemies(g);
        renderFoods(g);
    }

    /**
     * Renderizza le visualizzazioni dei nemici.
     *
     * @param g L'oggetto {@code Graphics} su cui disegnare le visualizzazioni dei nemici.
     */
    private void renderEnemies(Graphics g) {
        for (EnemyView enemyView : enemyViews) {
            if (!enemyView.getEnemy().isDeathMovement()) {
                enemyView.render(g);
            }
        }
    }

    /**
     * Renderizza le visualizzazioni del cibo.
     *
     * @param g L'oggetto {@code Graphics} su cui disegnare le visualizzazioni del cibo.
     */
    private void renderFoods(Graphics g) {
        getFoodViewsArrays();
        for (FoodView foodView : foodViews) {
            if (foodView.getScoreTick() <= foodView.getScoreDuration()) {
                foodView.draw(g);
            }
        }
    }

    /**
     * Inizializza l'array delle visualizzazioni dei nemici, prendendo i modelli dei nemici presenti
     * nell'array de nemici del model.
     */
    private void initEnemyViewsArrays() {
        enemyViews.clear();
        enemyModels = enemyManagerModel.getEnemies();
        for (EnemyModel enemyModel : enemyModels) {
            switch (enemyModel) {
                case ZenChanModel zenChanModel -> enemyViews.add(new ZenChanView(zenChanModel));
                case MaitaModel maitaModel -> enemyViews.add(new MaitaView(maitaModel));
                case InvaderModel invaderModel -> enemyViews.add(new InvaderView(invaderModel));
                case MonstaModel monstaModel -> enemyViews.add(new MonstaView(monstaModel));
                case DrunkModel drunkModel -> enemyViews.add(new DrunkView(drunkModel));
                case SuperDrunkModel superDrunkModel -> enemyViews.add(new SuperDrunkView(superDrunkModel));
                case HidegonsModel hidegonsModel -> enemyViews.add(new HidegonsView(hidegonsModel));
                default -> System.out.println("Errore caricamento nemici");
            }
        }
    }

    /**
     * Inizializza l'array delle visualizzazioni del cibo in base ai modelli del cibo.
     */
    private void getFoodViewsArrays() {
        foodModels = enemyManagerModel.getFoods();
        int modelLength = foodModels.size();
        int i = foodViews.size();
        if (i > modelLength) {
            foodViews.clear();
        }
        while (modelLength > foodViews.size()) {
            FoodModel food = foodModels.get(i);
            foodViews.add(new FoodView(food));
            i++;
        }
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }

}
