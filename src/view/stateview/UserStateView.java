package view.stateview;

import model.UserModel;
import model.gamestate.UserStateModel;
import model.ui.buttons.ChangePageButtonModel;
import model.ui.buttons.UserButtonModel;
import static model.utilz.Constants.Directions.*;
import view.ui.buttons.ChangePageButtonView;
import view.ui.buttons.UserButtonView;
import view.utilz.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.utilz.Constants.GameConstants.*;

public class UserStateView {

    private static UserStateView instance;

    private UserButtonView[] userButtons;
    private UserStateModel userStateModel;

    private ChangePageButtonView nextPageButton;
    private ChangePageButtonView prevPageButton;
    private int userIndex = 0;

    private UserModel currentUser;
    private BufferedImage currentAvatar;
    private int firstWidth;
    private int firstHeight = GAME_HEIGHT / 2 - (int)(69 * SCALE);


    public static UserStateView getInstance() {
        if (instance == null) {
            instance = new UserStateView();
        }
        return instance;
    }

    private UserStateView() {
        userStateModel = UserStateModel.getInstance();
        userButtons = new UserButtonView[userStateModel.getUserModels().size()];
        currentUser = userStateModel.getUserModels().get(userIndex);
        currentAvatar = currentUser.getAvatar();
        firstWidth = currentAvatar.getWidth() + (int) (69 * SCALE);
        initUserButtons();
    }

    public void update() {

    }

    public void draw(Graphics g) {
        drawUserStats(g);
        drawButtons(g);
    }

    private void drawButtons(Graphics g) {
        nextPageButton.draw(g);
        prevPageButton.draw(g);
    }

    private void drawUserStats(Graphics g) {
        currentUser = userStateModel.getUserModels().get(userIndex);
        currentAvatar = currentUser.getAvatar();

        int startWidth = firstWidth;
        int startHeight = firstHeight;
        Font nicknameFont = (LoadSave.JEQO_FONT).deriveFont(15 * SCALE);
        FontMetrics nicknameMeasures = g.getFontMetrics(nicknameFont);
        FontMetrics measures = g.getFontMetrics(LoadSave.JEQO_FONT);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        g.drawImage(currentAvatar, startWidth, startHeight, (int) (32 * SCALE), (int)(32 * SCALE),  null);
        g.setFont(nicknameFont);
        g.setColor(new Color(242, 70, 152));
        startWidth += currentAvatar.getWidth() + (nicknameMeasures.stringWidth(currentUser.getNickname())/2) - (int) (5*SCALE);
        startHeight += nicknameMeasures.getHeight();
        g.drawString(currentUser.getNickname(), startWidth, startHeight);

        g.setColor(Color.WHITE);
        g.setFont(LoadSave.JEQO_FONT);
        startHeight += measures.getHeight() + (int) (10* SCALE);
        g.drawString("Livello attuale: " + currentUser.getLevel(), startWidth, startHeight);
        startHeight += measures.getHeight() + (int) (10* SCALE);
        g.drawString("Partite vinte: " + currentUser.getWins(), startWidth, startHeight);
        startHeight += measures.getHeight() + (int) (10* SCALE);
        g.drawString("Punteggio massimo: " + currentUser.getScore(), startWidth, startHeight);
        startHeight += measures.getHeight() + (int) (10* SCALE);
        g.drawString("Partite giocate: " + currentUser.getMatches(), startWidth, startHeight);
        startHeight += measures.getHeight() + (int) (10* SCALE);
        g.drawString("Partite perse: " + currentUser.getLosses(), startWidth, startHeight);

    }

    private void initUserButtons() {
        nextPageButton = new ChangePageButtonView(new ChangePageButtonModel(
                firstWidth + (int) (25 * SCALE),
                GAME_HEIGHT / 2,
                (int) (16 * SCALE),
                (int) (16 * SCALE),
                RIGHT));
        prevPageButton = new ChangePageButtonView(new ChangePageButtonModel(
                firstWidth - (int) (69 * SCALE),
                GAME_HEIGHT / 2,
                (int) (16 * SCALE),
                (int) (16 * SCALE),
                LEFT));
    }

    public UserButtonView[] getUserButtons() {
        return userButtons;
    }

    public ChangePageButtonView getNextPageButton() {
        return nextPageButton;
    }

    public ChangePageButtonView getPrevPageButton() {
        return prevPageButton;
    }
}
