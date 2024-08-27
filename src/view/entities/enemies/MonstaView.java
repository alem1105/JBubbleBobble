package view.entities.enemies;

import model.entities.enemies.EnemyModel;
import model.entities.enemies.ZenChanModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import static view.utilz.LoadSave.loadAnimations;

public class MonstaView extends EnemyView {


    public MonstaView(EnemyModel enemy) {
        super(enemy);
    }
}
