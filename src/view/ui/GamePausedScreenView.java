package view.ui;

import java.awt.*;

import static model.utilz.Constants.GameConstants.GAME_HEIGHT;
import static model.utilz.Constants.GameConstants.GAME_WIDTH;

public class GamePausedScreenView {

    private static GamePausedScreenView instance;

    public static GamePausedScreenView getInstance() {
        if (instance == null) {
            instance = new GamePausedScreenView();
        }
        return instance;
    }

    private GamePausedScreenView() {

    }

    public void render(Graphics g) {
        Color myColour = new Color(0, 0, 0, 230);
        g.setColor(myColour);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

}
