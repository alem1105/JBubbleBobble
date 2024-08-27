package view.objects;

import model.objects.BobBubbleModel;
import model.objects.BubbleManagerModel;
import model.objects.BubbleModel;

import java.awt.*;
import java.util.ArrayList;

public class BubbleManagerView {

    private static BubbleManagerView instance;

    private ArrayList<BobBubbleView> bobBubbleViews;

    public static BubbleManagerView getInstance() {
        if (instance == null) {
            instance = new BubbleManagerView();
        }
        return instance;
    }

    private BubbleManagerView() {
        bobBubbleViews = new ArrayList<>();
    }

    public void update() {
        getBubbles();
        updateBubbles();
    }

    private void updateBubbles() {
        for (BobBubbleView bubbleView : bobBubbleViews) {
            if ((bubbleView.getBubbleModel().isActive() || (bubbleView.getBubbleModel().isTimeOut() && bubbleView.getAniIndex() <= 2)))
                bubbleView.update();
        }
    }

    public void draw(Graphics g) {
        for (BobBubbleView bubbleView : bobBubbleViews) {
            if ((bubbleView.getBubbleModel().isActive() || (bubbleView.getBubbleModel().isTimeOut() && bubbleView.getAniIndex() <= 2)))
                bubbleView.draw(g);

            // For debugging
//            g.setColor(Color.PINK);
//            g.drawRect((int) (bubbleView.bubbleModel.getHitbox().x), (int) (bubbleView.bubbleModel.getHitbox().y),
//                     bubbleView.bubbleModel.getWidth(),
//                     bubbleView.bubbleModel.getHeight());
            }
        }


    private void getBubbles() {
        int modelLength = BubbleManagerModel.getInstance().getBobBubbles().size();
        int i = bobBubbleViews.size();
        while (modelLength > bobBubbleViews.size()) {
            bobBubbleViews.add(new BobBubbleView(BubbleManagerModel.getInstance().getBobBubbles().get(i)));
            i++;
        }
    }

}
