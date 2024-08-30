package view.objects;

import model.LevelManagerModel;
import model.entities.enemies.EnemyManagerModel;
import model.objects.DrunkBottleModel;
import model.objects.MaitaFireballModel;
import model.objects.ProjectileManagerModel;
import model.objects.ProjectileModel;
import model.objects.bobbles.BubbleManagerModel;
import view.objects.bobbles.BobBubbleView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ProjectileManagerView {

    private static ProjectileManagerView instance;

    private ProjectileManagerModel projectileManagerModel;

    private ArrayList<ProjectileModel> projectileModels;
    private ArrayList<ProjectileView> projectileViews;

    protected int currentLevel;

    public static ProjectileManagerView getInstance() {
        if(instance == null) {
            instance = new ProjectileManagerView();
        }
        return instance;
    }

    private ProjectileManagerView() {
        projectileManagerModel = ProjectileManagerModel.getInstance();
        projectileViews = new ArrayList<>();
        checkIfLevelChanged();
    }

    public void updateAndDraw(Graphics g) {
        getProjectiles();
        projectileViews.stream()
                .filter(ProjectileView::conditionToDraw)
                .forEach(x -> x.updateAndDraw(g));
    }

    private void getProjectiles() {
        int modelLength = projectileManagerModel.getProjectiles().size();
        int i = projectileViews.size();
        if (i > modelLength) {
            projectileViews.clear();
        }
        while (modelLength > projectileViews.size()) {
            ProjectileModel proiettile = projectileManagerModel.getProjectiles().get(i);
            switch (proiettile) {
                case MaitaFireballModel maitaFireballModel -> projectileViews.add(new MaitaFireballView(maitaFireballModel));
                case DrunkBottleModel drunkBottleModel -> projectileViews.add(new DrunkBottleView(drunkBottleModel));
                default -> throw new IllegalStateException("Unexpected value: " + proiettile);
            }
            i++;
        }
    }

    private void checkIfLevelChanged() {
        if(LevelManagerModel.getInstance().getLvlIndex() != currentLevel) {
            projectileViews.clear();
            currentLevel = LevelManagerModel.getInstance().getLvlIndex();
        }
    }
}
