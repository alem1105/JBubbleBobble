package view.objects.bobbles;

import model.objects.bobbles.*;
import view.utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import static model.utilz.Constants.GameConstants.*;

import java.awt.Graphics;

/**
 * Gestisce la visualizzazione delle bolle nel gioco.
 * Questa classe tiene traccia di vari tipi di bolle e li disegna sullo schermo.
 */
public class BubbleManagerView {

    private static BubbleManagerView instance;

    private BubbleManagerModel bubbleManagerModel;

    /** Sprite della bolla EXTEND */
    private BufferedImage[][] extendSprite;
    /** Sprite bolla di fulmine */
    private BufferedImage[][] lightningBobBubble;

    /** ArrayList contenente le views delle bobBubble */
    private ArrayList<BobBubbleView> bobBubbleViews;
    /** ArrayList contenente le views delle bubble */
    private ArrayList<BubbleView> bubbleViews;
    /** ArrayList contenente le views del fuoco */
    private ArrayList<FireView> fireViews;
    /** ArrayList contenente le views dei fulmini */
    private ArrayList<LightningView> lightningViews;

    private final String EXTEND = "Extend";

    /**
     * Restituisce l'istanza singleton di BubbleManagerView.
     *
     * @return L'istanza di BubbleManagerView.
     */
    public static BubbleManagerView getInstance() {
        if (instance == null) {
            instance = new BubbleManagerView();
        }
        return instance;
    }

    /**
     * Costruttore privato per la classe BubbleManagerView.
     * Inizializza le collezioni delle bolle e carica le animazioni necessarie.
     */
    private BubbleManagerView() {
        bobBubbleViews = new ArrayList<>();
        bubbleViews = new ArrayList<>();
        fireViews = new ArrayList<>();
        lightningViews = new ArrayList<>();
        bubbleManagerModel = BubbleManagerModel.getInstance();
        extendSprite = LoadSave.loadAnimations(LoadSave.EXTEND_SPRITE, 6, 1, 16, 16);
        lightningBobBubble = LoadSave.loadAnimations(LoadSave.SPECIAL_BUBBLE_SPRITE, 4, 2, 16, 16);
    }

    /**
     * Aggiorna lo stato delle bolle e le loro animazioni.
     */
    public void update() {
        getBubblesFromModel();
        getBobBubblesFromModel();
        getExplodedBubblesFromModel();
        updateBubbles();
        updateFireBubbles();
    }

    /**
     * Disegna le bolle e gli effetti visivi.
     *
     * @param g Il contesto grafico su cui disegnare le bolle.
     */
    public void draw(Graphics g) {
        drawBobBubbles(g);
        drawBubblesAndWater(g);
        drawExplodedBubbles(g);
        drawExtendLetterOnTheWall(g);
    }

    /**
     * Aggiorna lo stato delle bolle di fuoco.
     */
    private void updateFireBubbles() {
        for (FireView fireView : fireViews) {
            fireView.update();
        }
    }

    /**
     * Aggiorna lo stato delle bolle normali e delle bolle del Player.
     */
    private void updateBubbles() {
        for (BobBubbleView bubbleView : bobBubbleViews) {
            if ((bubbleView.getModel().isActive() || (bubbleView.getModel().isTimeOut() && bubbleView.getAniIndex() < 2))) // ha finito animazione exploding
                bubbleView.update();
        }
        for (BubbleView bubbleView : bubbleViews) {
            if ((bubbleView.getModel().isActive() || (bubbleView.getModel().isTimeOut() && bubbleView.getAniIndex() < 2)))
                bubbleView.update();
        }
    }

    /**
     * Disegna le bolle del Player.
     *
     * @param g
     */
    private void drawBobBubbles(Graphics g) {
        for (BobBubbleView bubbleView : bobBubbleViews) {
            if ((bubbleView.getModel().isActive() || (bubbleView.getModel().isTimeOut() && bubbleView.getAniIndex() < 2)))
                bubbleView.draw(g);
        }
    }

    /**
     * Disegna le bolle normali e l'acqua.
     *
     * @param g
     */
    private void drawBubblesAndWater(Graphics g) {
        for (BubbleView bubbleView : bubbleViews) {
            if ((bubbleView.getModel().isActive() || (bubbleView.getModel().isTimeOut() && bubbleView.getAniIndex() < 2)))
                bubbleView.draw(g);
            else {
                bubbleView.getWaterfallModelArray();
                ArrayList<WaterView> bubbleViewArray = bubbleView.getWaterfallView();
                for (int i = 0; i < bubbleViewArray.size(); i++) {
                    WaterView currentWaterView = bubbleViewArray.get(i);
                    setWaterViewIndex(currentWaterView, bubbleViewArray, i);
                    if (currentWaterView.getObjectModel().isActive()) {
                        currentWaterView.draw(g);
                        currentWaterView.update();
                    }
                }
            }
        }
    }

    /**
     * Imposta l'indice della view dell'acqua in modo che il primo e l'ultimo elemento
     * della "cascata" abbiano sprite diversi dal resto degli elementi della "cascata".
     *
     * @param currentWaterView La vista dell'acqua corrente da aggiornare.
     * @param bubbleViewArray  L'array delle viste delle bolle.
     * @param i               L'indice della vista corrente.
     */
    private void setWaterViewIndex(WaterView currentWaterView, ArrayList<WaterView> bubbleViewArray, int i) {
        if (currentWaterView.getIndex() != 0)
            return;

        if (i == 0)
            currentWaterView.setIndex(1);

        if (i == bubbleViewArray.size() - 1)
            currentWaterView.setIndex(bubbleViewArray.size());
    }

    /**
     * Disegna i potenziamenti delle bolle speciali (acqua, fuoco, fulmine).
     *
     * @param g
     */
    private void drawExplodedBubbles(Graphics g) {
        for (LightningView lightningView : lightningViews) {
            if (lightningView.getObjectModel().isActive())
                lightningView.draw(g);
        }
        for (FireView fireView : fireViews) {
            if (fireView.canDrawFire())
                fireView.draw(g);
        }
    }

    /**
     * Recupera gli effetti delle bolle dal modello e li aggiunge alla lista di visualizzazione.
     */
    private void getExplodedBubblesFromModel() {
        int modelLength = bubbleManagerModel.getLightnings().size();
        int i = lightningViews.size();
        if (i > modelLength) {
            i = 0;
            lightningViews.clear();
        }
        while (modelLength > lightningViews.size()) {
            LightningModel lightning = bubbleManagerModel.getLightnings().get(i);
            lightningViews.add(new LightningView(lightning));
            i++;
        }

        modelLength = bubbleManagerModel.getFires().size();
        i = fireViews.size();
        if (i > modelLength) {
            fireViews.clear();
        }
        while (modelLength > fireViews.size()) {
            FireModel fire = bubbleManagerModel.getFires().get(i);
            fireViews.add(new FireView(fire));
            i++;
        }
    }

    /**
     * Recupera le bolle dal modello e le aggiunge alla lista di visualizzazione.
     */
    private void getBubblesFromModel() {
        int modelLength = bubbleManagerModel.getBubbles().size();
        int i = bubbleViews.size();
        if (i > modelLength) {
            i = 0;
            bubbleViews.clear();
        }
        while (modelLength > bubbleViews.size()) {
            BubbleModel bubble = bubbleManagerModel.getBubbles().get(i);
            bubbleViews.add(new BubbleView(bubble));
            i++;
        }
    }

    /**
     * Recupera le bolle del Player dal modello e le aggiunge alla lista di visualizzazione.
     */
    private void getBobBubblesFromModel() {
        int modelLength = bubbleManagerModel.getBobBubbles().size();
        int i = bobBubbleViews.size();
        if (i > modelLength) {
            bobBubbleViews.clear();
            i = 0;
        }
        while (modelLength > bobBubbleViews.size()) {
            bobBubbleViews.add(new BobBubbleView(bubbleManagerModel.getBobBubbles().get(i)));
            i++;
        }
    }

    /**
     * Disegna la lettera "Extend" sul muro del gioco.
     *
     * @param g
     */
    private void drawExtendLetterOnTheWall(Graphics g) {
        HashMap<Character, Boolean> extendMap = BubbleManagerModel.getInstance().getExtend();
        for (int i = 0; i < EXTEND.length(); i++) {
            if (extendMap.get(EXTEND.charAt(i))) {
                g.drawImage(extendSprite[i][0], 0, (TILES_IN_HEIGHT / 2 - 3 + i) * TILES_SIZE, (int) (16 * SCALE), (int) (16 * SCALE), null);
            }
        }
    }

    public BufferedImage[][] getLightningBobBubble() {
        return lightningBobBubble;
    }
}
