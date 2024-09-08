package view.entities.enemies;

import model.LevelManagerModel;
import model.entities.enemies.*;
import model.objects.items.FoodModel;
import view.objects.items.FoodView;

import java.awt.*;
import java.util.ArrayList;

public class EnemiesManagerView {

    private static EnemiesManagerView instance;

    private EnemyManagerModel enemyManagerModel;
    private ArrayList<EnemyView> enemyViews = new ArrayList<>();
    private ArrayList<EnemyModel> enemyModels;
    private int currentLevel;
    private boolean restart = false;

    private ArrayList<FoodView> foodViews;
    private ArrayList<FoodModel> foodModels;

    public static EnemiesManagerView getInstance() {
        if (instance == null) {
            instance = new EnemiesManagerView();
        }
        return instance;
    }

    private EnemiesManagerView() {
        enemyManagerModel = EnemyManagerModel.getInstance();
        initEnemyViewsArrays();
        foodViews = new ArrayList<>();
    }

    public void update() {
        checkIfLevelChanged();
        updateEnemies();
        updateFood();
    }

    private void updateFood() {
        for (FoodView foodView : foodViews) {
            foodView.update();
        }
    }

    private void updateEnemies(){
        for (EnemyView enemyView : enemyViews) {
            if (!enemyView.getEnemy().isDeathMovement()){
                enemyView.update();
            }
        }
    }

    private void checkIfLevelChanged() {
        if(LevelManagerModel.getInstance().getLvlIndex() != currentLevel || restart) {
            restart = false;
            initEnemyViewsArrays();
            currentLevel = LevelManagerModel.getInstance().getLvlIndex();
            foodViews.clear();
        }
    }

    public void render(Graphics g) {
        renderEnemies(g);
        renderFoods(g);
    }

    private void renderEnemies(Graphics g) {
        for (EnemyView enemyView : enemyViews) {
            if(!enemyView.getEnemy().isDeathMovement()) {
                enemyView.render(g);
            }
        }
    }

    private void renderFoods(Graphics g) {
        getFoodViewsArrays();
        for (FoodView foodView : foodViews) {
            if (foodView.getScoreTick() <= foodView.getScoreDuration()) {
                foodView.draw(g);
            }
        }
    }

    private void initEnemyViewsArrays() {
        enemyViews.clear();
        enemyModels = enemyManagerModel.getEnemies();
        for (EnemyModel enemyModel : enemyModels)
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
