package view.objects.bobbles;

import model.objects.MaitaFireballModel;
import model.objects.ProjectileModel;
import model.objects.bobbles.BobBubbleModel;
import model.objects.bobbles.BubbleManagerModel;
import model.objects.bobbles.BubbleModel;
import view.objects.MaitaFireballView;

import java.awt.*;
import java.util.ArrayList;

public class BubbleManagerView {

    private static BubbleManagerView instance;

    private BubbleManagerModel bubbleManagerModel;

    private ArrayList<BobBubbleView> bobBubbleViews;
    private ArrayList<BubbleView> bubbleViews;

    public static BubbleManagerView getInstance() {
        if (instance == null) {
            instance = new BubbleManagerView();
        }
        return instance;
    }

    private BubbleManagerView() {
        bobBubbleViews = new ArrayList<>();
        bubbleViews = new ArrayList<>();
        bubbleManagerModel = BubbleManagerModel.getInstance();
    }

    public void update() {
        getBubblesFromModel();
        getBobBubblesFromModel();
        updateBubbles();
    }

    private void updateBubbles() {
        for (BobBubbleView bubbleView : bobBubbleViews) {
            if ((bubbleView.getBubbleModel().isActive() || (bubbleView.getBubbleModel().isTimeOut() && bubbleView.getAniIndex() <= 2 ))) //ha finito animazione exploding
                bubbleView.update();
        }
//        for (BubbleView bubbleView : bubbleViews) {
//            bubbleView.update();
//        }
    }

    public void draw(Graphics g) {
        for (BobBubbleView bubbleView : bobBubbleViews) {
            if ((bubbleView.getBubbleModel().isActive() || (bubbleView.getBubbleModel().isTimeOut() && bubbleView.getAniIndex() <= 2)))
                bubbleView.draw(g);
            }

        for (BubbleView bubbleView : bubbleViews) {
            bubbleView.draw(g);
        }
    }


    private void getBubblesFromModel() {
        int modelLength = BubbleManagerModel.getInstance().getBubbles().size();
        int i = bubbleViews.size();
        if (i > modelLength) {
            bubbleViews.clear();
        }
        while (modelLength > bubbleViews.size()) {
            BubbleModel bubble = BubbleManagerModel.getInstance().getBubbles().get(i);
            bubbleViews.add(new BubbleView(bubble));
            i++;
        }
    }

    private void getBobBubblesFromModel() {
        int modelLength = BubbleManagerModel.getInstance().getBobBubbles().size();
        int i = bobBubbleViews.size();
        if (i > modelLength) {
            bobBubbleViews.clear();
        }
        while (modelLength > bobBubbleViews.size()) {
            bobBubbleViews.add(new BobBubbleView(BubbleManagerModel.getInstance().getBobBubbles().get(i)));
            i++;
        }
    }

}
