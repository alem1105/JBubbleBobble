package view.entities.enemies;

import model.entities.enemies.EnemyManagerModel;
import model.entities.enemies.ZenChanModel;

import java.awt.*;
import java.util.ArrayList;

public class EnemiesManagerView {

    private EnemyManagerModel enemyManagerModel;

    private ArrayList<ZenChanView> zenChanViews;

    public EnemiesManagerView() {
        enemyManagerModel = EnemyManagerModel.getInstance();
        initViews();
    }

    public void update() {
        for (ZenChanView zenChanView : zenChanViews) {
            zenChanView.update();
        }
    }

    public void render(Graphics g) {
        for (ZenChanView zenChanView : zenChanViews) {
            zenChanView.render(g);
            zenChanView.drawHitbox(g);
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
