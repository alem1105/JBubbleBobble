package view.entities.enemies;

import model.LevelManagerModel;
import model.entities.enemies.EnemyManagerModel;
import model.entities.enemies.ZenChanModel;

import java.awt.*;
import java.util.ArrayList;

public class EnemiesManagerView {

    private EnemyManagerModel enemyManagerModel;

    private ArrayList<ZenChanView> zenChanViews;
    private int currentLevel;

    public EnemiesManagerView() {
        enemyManagerModel = EnemyManagerModel.getInstance();
        initViews();
    }

    public void update() {
        checkIfLevelChanged();
        updateEnemies();
    }

    private void updateEnemies(){
        for (ZenChanView zenChanView : zenChanViews) {
//            if (condizione che stoppa l'update del model) {
//                continue;
//            }

            zenChanView.update();
        }
    }

    private void checkIfLevelChanged() {
        if(LevelManagerModel.getInstance().getLvlIndex() != currentLevel) {
            initViews();
            currentLevel = LevelManagerModel.getInstance().getLvlIndex();
        }
    }

    public void render(Graphics g) {
        for (ZenChanView zenChanView : zenChanViews) {
            if(true ) { //condizione con cui si stoppa l'update
                zenChanView.render(g);
            }
            //zenChanView.drawHitbox(g);
        }
    }

    private void initViews() {
        zenChanViews = new ArrayList<>();
        ArrayList<ZenChanModel> zenChans = enemyManagerModel.getZenChans();
        for (ZenChanModel zenChan : zenChans) {
            zenChanViews.add(new ZenChanView(zenChan));
        }
    }

}
