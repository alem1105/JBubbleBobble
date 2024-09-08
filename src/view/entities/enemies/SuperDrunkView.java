package view.entities.enemies;

import model.entities.enemies.SuperDrunkModel;
import model.objects.projectiles.DrunkBottleModel;
import view.objects.projectiles.DrunkBottleView;
import view.objects.projectiles.ProjectileManagerView;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.utilz.Constants.Directions.DOWN_RIGHT;
import static model.utilz.Constants.Directions.UP_RIGHT;
import static view.utilz.LoadSave.loadAnimations;

/**
 * La classe {@code SuperDrunkView} rappresenta la visualizzazione del nemico Super Drunk nel gioco.
 * Estende la classe generica {@link EnemyView} per gestire il rendering, le animazioni specifiche
 * e i proiettili (bottiglie) lanciati dal nemico Super Drunk.
 */
public class SuperDrunkView extends EnemyView<SuperDrunkModel> {

    private int spriteIndex;

    /**
     * Indica la durata dell'animazione del nemico colpito
     */
    private int hitTimer = 180;

    /**
     * Il tick utilizzato per calcolare quando si arriva alla durata
     * dell'animazione del nemico colpito
     */
    private int hitTick;
    private ArrayList<DrunkBottleView> drunkBottlesView;
    private BufferedImage[][] drunkBottleSprites;

    /**
     * Costruisce un oggetto {@code SuperDrunkView} associato al modello {@link SuperDrunkModel}.
     * Inizializza le animazioni e dei proiettili lanciati dal nemico.
     *
     * @param superDrunkModel Il modello del nemico {@code SuperDrunkModel}.
     */
    public SuperDrunkView(SuperDrunkModel superDrunkModel) {
        super(superDrunkModel);
        aniIndex = 0;
        animations = loadAnimations(LoadSave.SUPERDRUNK_SPRITE, 5, 4, 48, 51);
        drunkBottlesView = new ArrayList<>();
        drunkBottleSprites = ProjectileManagerView.getInstance().getDrunkBottleSprite();
    }

    /**
     * Recupera i modelli delle bottiglie lanciate dal nemico e li converte in viste.
     */
    private void getDrunkBottlesFromModel() {
        int modelLength = enemy.getDrunkBottles().size();
        int i = drunkBottlesView.size();
        if (i > modelLength) {
            i = 0;
            drunkBottlesView.clear();
        }
        while (modelLength > drunkBottlesView.size()) {
            DrunkBottleModel drunkBottleModel = enemy.getDrunkBottles().get(i);
            drunkBottlesView.add(new DrunkBottleView(drunkBottleModel, drunkBottleSprites));
            i++;
        }
    }

    /**
     * Imposta l'indice dello sprite in base allo stato del nemico (normale, colpito, in una bolla o morto).
     */
    public void setSpriteIndex() {
        int tempSpriteIndex = spriteIndex;
        spriteIndex = 1;

        if (enemy.hasBeenHit())
            spriteIndex = 2;

        if (enemy.isInBubble())
            spriteIndex = 3;

        if (!enemy.isActive())
            spriteIndex = 4;

        if (tempSpriteIndex != spriteIndex)
            resetAniTick();
    }

    /**
     * Il metodo si occupa di aggiornare la visualizzazzione del nemico, il frame dell'animazione corrente
     * indice dello sprite da disegnare e di recuperare le bottiglie
     */
    @Override
    public void update() {
        checkHitAnimationOver();
        setSpriteIndex();
        updateAnimationTick();
        flipW();
        flipX();
        getDrunkBottlesFromModel();
    }

    /**
     * Utilizzati per specchiare il nemico se necessario
     */
    @Override
    public void flipX() {
        if (enemy.getWalkDir() == UP_RIGHT || enemy.getWalkDir() == DOWN_RIGHT)
            flipX = (int) enemy.getHitbox().getWidth();
        else
            flipX = 0;
    }

    @Override
    public void flipW() {
        if (enemy.getWalkDir() == UP_RIGHT || enemy.getWalkDir() == DOWN_RIGHT)
            flipW = -1;
        else
            flipW = 1;
    }

    /**
     * Aggiorna e disegna le bottiglie lanciate dal nemico sullo schermo.
     *
     * @param g Il contesto per disegnare le bottiglie.
     */
    private void updateAndDrawDrunkBottles(Graphics g) {
        for (DrunkBottleView drunkBottle : drunkBottlesView) {
            if (drunkBottle.conditionToDraw())
                drunkBottle.updateAndDraw(g);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(animations[spriteIndex][aniIndex],
                (int) (enemy.getHitbox().x) + flipX, (int) (enemy.getHitbox().y),
                (int) enemy.getHitbox().getWidth() * flipW, (int) enemy.getHitbox().getHeight(), null);
        updateAndDrawDrunkBottles(g);
    }

    @Override
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount()) {
                aniIndex = 0;
            }
        }
    }

    /**
     * Reimposta i tick dell'animazione quando lo stato dello sprite cambia.
     */
    private void resetAniTick() {
        aniIndex = 0;
        aniTick = 0;
        enemy.setResetAniTick(false);
    }

    /**
     * Controlla se l'animazione di colpo è terminata, e reimposta lo stato "colpito" del nemico se necessario.
     */
    private void checkHitAnimationOver() {
        if (enemy.hasBeenHit())
            hitTick++;
        if (hitTick >= hitTimer) {
            hitTick = 0;
            enemy.setHasBeenHit(false);
        }
    }
}

