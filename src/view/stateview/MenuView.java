package view.stateview;

import model.utilz.Constants;

import java.awt.*;

public class MenuView {

    private int aniIndex, aniTick;

    public MenuView() {
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constants.GameConstants.GAME_WIDTH, Constants.GameConstants.GAME_HEIGHT);
    }

}
