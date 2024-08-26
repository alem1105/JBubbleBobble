package model.entities.enemies;

import model.LevelManager;
import model.entities.PlayerModel;
import view.ui.buttons.BlockButtonView;
import view.ui.buttons.CustomButtonView;
import view.ui.buttons.EnemyButtonView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static model.utilz.Constants.PlayerConstants.DEATH;

public class EnemyManagerModel {

    private static EnemyManagerModel instance;
    private LevelManager levelManager;
    private ArrayList<MaitaModel> maitas;
    private ArrayList<ZenChanModel> zenChans;
    private ArrayList<EnemyModel> enemies;

    public static EnemyManagerModel getInstance() {
        if (instance == null) {
            instance = new EnemyManagerModel();
        }
        return instance;
    }

    private EnemyManagerModel() {
        levelManager = LevelManager.getInstance();
        initEnemies();
    }

    private void initEnemies(){
        zenChans = levelManager.getLevels().get(levelManager.getLvlIndex()).getZenChans();
        maitas = new ArrayList<>();
        createGeneralEnemiesArray();
    }

    private void createGeneralEnemiesArray() {
        enemies = Stream
                .concat(zenChans.stream(), maitas.stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void update() {
        for (ZenChanModel zenChan : zenChans) {
            zenChan.update();
            if (zenChan.getHitbox().intersects(getPlayerModel().getHitbox())) {
                if (zenChan.isInBubble()) {
                    zenChan.setActive(false);
                }
                else if (getPlayerModel().getPlayerAction() != DEATH) {
                    getPlayerModel().playerHasBeenHit();
                }
            }
        }
    }

    public ArrayList<ZenChanModel> getZenChans() {
        return zenChans;
    }

    private PlayerModel getPlayerModel() {
        return PlayerModel.getInstance();
    }

    public ArrayList<EnemyModel> getEnemies() {
        return enemies;
    }
}
