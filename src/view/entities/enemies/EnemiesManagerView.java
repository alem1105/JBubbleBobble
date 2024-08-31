package view.entities.enemies;

import model.LevelManagerModel;
import model.entities.enemies.*;

import java.awt.*;
import java.util.ArrayList;

public class EnemiesManagerView {

    private EnemyManagerModel enemyManagerModel;
    private ArrayList<EnemyView> enemyViews;
    private ArrayList<EnemyModel> enemyModels;
    private int currentLevel;

    public EnemiesManagerView() {
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
        if(LevelManagerModel.getInstance().getLvlIndex() != currentLevel) {

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
        enemyViews = new ArrayList<>();
        enemyModels = enemyManagerModel.getEnemies();
        for (EnemyModel enemyModel : enemyModels)
            switch (enemyModel) {
                case ZenChanModel zenChanModel -> enemyViews.add(new ZenChanView(zenChanModel));
                case MaitaModel maitaModel -> enemyViews.add(new MaitaView(maitaModel));
                case InvaderModel invaderModel -> enemyViews.add(new InvaderView(invaderModel));
                case MonstaModel monstaModel -> enemyViews.add(new MonstaView(monstaModel));
                case DrunkModel drunkModel -> enemyViews.add(new DrunkView(drunkModel));
                case null, default -> enemyViews.add(new HidegonsView((HidegonsModel) enemyModel));
            }
    }

}
