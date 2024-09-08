package view.objects.bobbles;

import model.objects.bobbles.*;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import static model.utilz.Constants.GameConstants.*;

public class BubbleManagerView {

    private static BubbleManagerView instance;

    private BubbleManagerModel bubbleManagerModel;

    private BufferedImage[][] extendSprite;

    private ArrayList<BobBubbleView> bobBubbleViews;
    private ArrayList<BubbleView> bubbleViews;
    private ArrayList<WaterView> waterViews;
    private ArrayList<FireView> fireViews;
    private ArrayList<LightningView> lightningViews;

    private String extend = "Extend";

    public static BubbleManagerView getInstance() {
        if (instance == null) {
            instance = new BubbleManagerView();
        }
        return instance;
    }

    private BubbleManagerView() {
        bobBubbleViews = new ArrayList<>();
        bubbleViews = new ArrayList<>();
        waterViews = new ArrayList<>();
        fireViews = new ArrayList<>();
        lightningViews = new ArrayList<>();
        bubbleManagerModel = BubbleManagerModel.getInstance();
        extendSprite = LoadSave.loadAnimations(LoadSave.EXTEND_SPRITE,6, 1, 16, 16);
    }

    public void update() {
        getBubblesFromModel();
        getBobBubblesFromModel();
        getExplodedBubblesFromModel();
        updateBubbles();
        updateFireBubbles();
    }

    public void draw(Graphics g) {
        drawBobBubbles(g);
        drawBubblesAndWater(g);
        drawExplodedBubbles(g);
        drawExtendLetterOnTheWall(g);
    }

    private void updateFireBubbles() {
        for (FireView fireView : fireViews){
            fireView.update();
        }
    }

    private void updateBubbles() {
        for (BobBubbleView bubbleView : bobBubbleViews) {
            if ((bubbleView.getModel().isActive() || (bubbleView.getModel().isTimeOut() && bubbleView.getAniIndex() < 2 ))) //ha finito animazione exploding
                bubbleView.update();
        }
        for (BubbleView bubbleView : bubbleViews) {
            if ((bubbleView.getModel().isActive() || (bubbleView.getModel().isTimeOut() && bubbleView.getAniIndex() < 2)))
                bubbleView.update();
        }
    }

    private void drawBobBubbles(Graphics g) {
        for (BobBubbleView bubbleView : bobBubbleViews) {
            if ((bubbleView.getModel().isActive() || (bubbleView.getModel().isTimeOut() && bubbleView.getAniIndex() < 2)))
                bubbleView.draw(g);
        }
    }

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
                    currentWaterView.draw(g);
                    currentWaterView.update();
                }
            }
        }
    }

    private void setWaterViewIndex(WaterView currentWaterView, ArrayList<WaterView> bubbleViewArray, int i) {
        if(currentWaterView.getIndex() != 0)
            return;

        if(i == 0)
            currentWaterView.setIndex(1);

        if(i == bubbleViewArray.size() - 1)
            currentWaterView.setIndex(bubbleViewArray.size());
    }


    private void drawExplodedBubbles(Graphics g) {
        for (WaterView waterView : waterViews) {
            waterView.draw(g);
        }
        for (LightningView lightningView : lightningViews) {
            if (lightningView.getObjectModel().isActive())
                lightningView.draw(g);
        }
        for (FireView fireView : fireViews){
            if (fireView.canDrawFire())
                fireView.draw(g);
        }
    }

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

    private void drawExtendLetterOnTheWall(Graphics g){
        HashMap<Character, Boolean> extendMap = BubbleManagerModel.getInstance().getExtend();
        for(int i = 0; i < extend.length(); i++){
            if (extendMap.get(extend.charAt(i))){
                g.drawImage(extendSprite[i][0],0, (TILES_IN_HEIGHT / 2 - 3 + i) * TILES_SIZE, (int)(16 * SCALE) ,(int)(16 * SCALE), null);
            }
        }
    }

}
