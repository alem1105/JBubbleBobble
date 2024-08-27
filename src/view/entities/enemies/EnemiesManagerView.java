package view.entities.enemies;

import model.LevelManagerModel;
import model.entities.enemies.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnemiesManagerView {

    private EnemyManagerModel enemyManagerModel;
    private ArrayList<EnemyView> enemyViews;
    private ArrayList<EnemyModel> enemyModels;
    private int currentLevel;

    public EnemiesManagerView() {
        enemyManagerModel = EnemyManagerModel.getInstance();
        enemyModels = enemyManagerModel.getEnemies();
        initViews();
    }

    public void update() {
        checkIfLevelChanged();
        updateEnemies();
    }

    private void updateEnemies(){
        for (EnemyView enemyView : enemyViews) {
//            if (condizione che stoppa l'update del model) {
//                continue;
//            }

            enemyView.update();
        }
    }

    private void checkIfLevelChanged() {
        if(LevelManagerModel.getInstance().getLvlIndex() != currentLevel) {

            initViews();
            currentLevel = LevelManagerModel.getInstance().getLvlIndex();
        }
    }

    public void render(Graphics g) {
        for (EnemyView enemyView : enemyViews) {
            if(true ) { //condizione con cui si stoppa l'update
                enemyView.render(g);
            }
            //zenChanView.drawHitbox(g);
        }
    }

    private void initViews() {
        initEnemyViewsArrays();
    }

    private void initEnemyViewsArrays() {
        enemyViews = new ArrayList<>();
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

}
