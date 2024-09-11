package view.objects.projectiles;

import model.level.LevelManagerModel;
import model.objects.projectiles.*;
import view.utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import java.awt.Graphics;
/**
 * Rappresenta la gestione e la visualizzazione dei proiettili nel gioco.
 * Questa classe è responsabile dell'aggiornamento e del disegno dei proiettili.
 */
public class ProjectileManagerView {

    private static ProjectileManagerView instance;

    private ProjectileManagerModel projectileManagerModel;

    private ArrayList<ProjectileView> projectileViews;

    protected int currentLevel;

    private BufferedImage[][] maitaFireballSprite;
    private BufferedImage[][] drunkBottleSprite;
    private BufferedImage[][] hidegonsFireballSprite;
    private BufferedImage[][] invaderLaserSprite;

    /**
     * Restituisce l'istanza singleton di ProjectileManagerView.
     *
     * @return L'istanza singleton di ProjectileManagerView.
     */
    public static ProjectileManagerView getInstance() {
        if(instance == null) {
            instance = new ProjectileManagerView();
        }
        return instance;
    }

    /**
     * Costruttore privato per inizializzare ProjectileManagerView.
     * Carica le sprite per i diversi tipi di proiettili e controlla eventuali cambiamenti di livello.
     */
    private ProjectileManagerView() {
        projectileManagerModel = ProjectileManagerModel.getInstance();
        projectileViews = new ArrayList<>();
        maitaFireballSprite = LoadSave.loadAnimations(LoadSave.MAITA_FIREBALL, 2, 3, 18, 18);
        drunkBottleSprite = LoadSave.loadAnimations(LoadSave.DRUNK_BOTTLE, 1, 4, 18, 18);
        hidegonsFireballSprite = LoadSave.loadAnimations(LoadSave.HIDEGONS_FIREBALL, 1, 1, 18, 13);
        invaderLaserSprite = LoadSave.loadAnimations(LoadSave.INVADER_LASER, 2, 2, 18, 18);
        checkIfLevelChanged();
    }

    /**
     * Aggiorna e disegna i proiettili
     *
     * @param g
     */
    public void updateAndDraw(Graphics g) {
        getProjectiles();
        for(ProjectileView projectileView : projectileViews)
            if (projectileView.conditionToDraw())
                projectileView.updateAndDraw(g);
    }

    /**
     * Ottiene i proiettili dal modello e aggiorna la lista delle visualizzazioni dei proiettili.
     */
    private void getProjectiles() {
        int modelLength = projectileManagerModel.getProjectiles().size();
        int i = projectileViews.size();
        if (i > modelLength) {
            projectileViews.clear();
        }
        while (modelLength > projectileViews.size()) {
            ProjectileModel proiettile = projectileManagerModel.getProjectiles().get(i);
            switch (proiettile) {
                case MaitaFireballModel maitaFireballModel -> projectileViews.add(new MaitaFireballView(maitaFireballModel, maitaFireballSprite));
                case DrunkBottleModel drunkBottleModel -> projectileViews.add(new DrunkBottleView(drunkBottleModel, drunkBottleSprite));
                case InvaderLaserModel invaderLaserModel -> projectileViews.add(new InvaderLaserView(invaderLaserModel, invaderLaserSprite));
                case HidegonsFireballModel hidegonsFireballModel -> projectileViews.add(new HidegonsFireballView(hidegonsFireballModel, hidegonsFireballSprite));
                default -> throw new IllegalStateException("Unexpected value: " + proiettile);
            }
            i++;
        }
    }

    /**
     * Controlla se il livello è cambiato e aggiorna la lista dei proiettili di conseguenza.
     */
    private void checkIfLevelChanged() {
        if(LevelManagerModel.getInstance().getLvlIndex() != currentLevel) {
            projectileViews.clear();
            currentLevel = LevelManagerModel.getInstance().getLvlIndex();
        }
    }

    public BufferedImage[][] getDrunkBottleSprite() {
        return drunkBottleSprite;
    }
}
