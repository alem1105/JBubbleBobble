package view.stateview;

import model.gamestate.UserStateModel;
import model.ui.buttons.UserButtonModel;
import view.ui.buttons.UserButtonView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static model.utilz.Constants.GameConstants.*;

public class UserStateView {

    private static UserStateView instance;

    private UserButtonView[] userButtons;
    private UserStateModel userStateModel;

    public static UserStateView getInstance() {
        if (instance == null) {
            instance = new UserStateView();
        }
        return instance;
    }

    private UserStateView() {
        userStateModel = UserStateModel.getInstance();
        userButtons = new UserButtonView[userStateModel.getUserModels().size()];
        initUserButtons();
    }

    public void update() {

    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        for(UserButtonView userButton : userButtons) {
            userButton.draw(g);
        }
    }

    private void initUserButtons() {
        for (int i = 0; i < userStateModel.getUserModels().size(); i++) {
            userButtons[i] = new UserButtonView(new UserButtonModel(GAME_WIDTH / 2 - (int) (100 * SCALE / 2) ,
                    (int) ((i * 200 * SCALE)),
                    (int) (100 * SCALE),
                    (int) (15 * SCALE),
                    userStateModel.getUserModels().get(i)));
        }
    }

    public UserButtonView[] getUserButtons() {
        return userButtons;
    }

}
