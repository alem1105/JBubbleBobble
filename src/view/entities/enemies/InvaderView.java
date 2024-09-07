package view.entities.enemies;

import model.entities.enemies.InvaderModel;
import model.entities.enemies.ZenChanModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import static view.utilz.LoadSave.loadAnimations;

public class InvaderView extends EnemyView<InvaderModel> {
    public InvaderView(InvaderModel enemy) {
        super(enemy);
        animations = loadAnimations(LoadSave.INVADER_SPRITE, ROW_INDEX, COL_INDEX, 18, 18);
        xDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
        yDrawOffset = (int) (1 * Constants.GameConstants.SCALE);
    }
}
