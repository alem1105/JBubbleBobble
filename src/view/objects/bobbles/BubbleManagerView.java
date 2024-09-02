package view.objects.bobbles;

import model.objects.bobbles.BubbleManagerModel;
import model.objects.bobbles.BubbleModel;
import model.objects.bobbles.WaterModel;

import java.awt.*;
import java.util.ArrayList;

public class BubbleManagerView {

    private static BubbleManagerView instance;

    private BubbleManagerModel bubbleManagerModel;

    private ArrayList<BobBubbleView> bobBubbleViews;
    private ArrayList<BubbleView> bubbleViews;
    private ArrayList<WaterView> waterViews;

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
        bubbleManagerModel = BubbleManagerModel.getInstance();
    }

    public void update() {
        getBubblesFromModel();
        getBobBubblesFromModel();
        getExplodedBubblesFromModel();
        updateBubbles();
        updateExplodedBubbles();
    }

    private void updateExplodedBubbles() {
        for(WaterView waterView : waterViews) {
            waterView.update();
        }
    }

    private void updateBubbles() {
        for (BobBubbleView bubbleView : bobBubbleViews) {
            if ((bubbleView.getBubbleModel().isActive() || (bubbleView.getBubbleModel().isTimeOut() && bubbleView.getAniIndex() <= 2 ))) //ha finito animazione exploding
                bubbleView.update();
        }
    }

    public void draw(Graphics g) {
        drawBobBubbles(g);
        drawBubbles(g);
        drawExplodedBubbles(g);
    }

    private void drawBobBubbles(Graphics g) {
        for (BobBubbleView bubbleView : bobBubbleViews) {
            if ((bubbleView.getBubbleModel().isActive() || (bubbleView.getBubbleModel().isTimeOut() && bubbleView.getAniIndex() <= 2)))
                bubbleView.draw(g);
        }
    }

    private void drawBubbles(Graphics g) {
        for (BubbleView bubbleView : bubbleViews) {
            bubbleView.draw(g);
        }
    }

    private void drawExplodedBubbles(Graphics g) {
        for (WaterView waterView : waterViews) {
            waterView.draw(g);
        }
    }

    private void getExplodedBubblesFromModel() {
        int modelLength = bubbleManagerModel.getWaters().size();
        int i = waterViews.size();
        if (i > modelLength) {
            i = 0;
            waterViews.clear();
        }
        while (modelLength > waterViews.size()) {
            WaterModel water = bubbleManagerModel.getWaters().get(i);
            waterViews.add(new WaterView(water));
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

}
