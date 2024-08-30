package view.entities.enemies;

import model.LevelManagerModel;
import model.entities.enemies.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnemiesManagerView {

    private static EnemiesManagerView instance;

    private EnemyManagerModel enemyManagerModel;
    private ArrayList<EnemyView> enemyViews = new ArrayList<>();
    private ArrayList<EnemyModel> enemyModels;
    private int currentLevel;
    private boolean restart = false;

    public static EnemiesManagerView getInstance() {
        if (instance == null) {
            instance = new EnemiesManagerView();
        }
        return instance;
    }

    private EnemiesManagerView() {
        enemyManagerModel = EnemyManagerModel.getInstance();
        initEnemyViewsArrays();
    }

    public void update() {
        checkIfLevelChanged();
        updateEnemies();
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
        }
    }

    public void render(Graphics g) {
        for (EnemyView enemyView : enemyViews) {
            if(!enemyView.getEnemy().isDeathMovement()) {
                enemyView.render(g);
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
                case PulpulModel pulpulModel -> enemyViews.add(new PulpulView(pulpulModel));
                case MonstaModel monstaModel -> enemyViews.add(new MonstaView(monstaModel));
                case DrunkModel drunkModel -> enemyViews.add(new DrunkView(drunkModel));
                case null, default -> enemyViews.add(new HidegonsView((HidegonsModel) enemyModel));
            }
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }

}
