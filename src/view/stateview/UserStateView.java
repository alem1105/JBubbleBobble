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

    private UserStateModel userStateModel;
    ArrayList<UserModel> users;

    private ChangePageButtonView nextPageButton;
    private ChangePageButtonView prevPageButton;
    private int userIndex = 0;

    private UserModel currentUser;
    private BufferedImage currentAvatar;
    private int firstWidth;
    private int firstHeight = GAME_HEIGHT / 2 - (int)(69 * SCALE);

    private boolean createUser;
    private BufferedImage[] allAvatars;

    public static UserStateView getInstance() {
        if (instance == null) {
            instance = new UserStateView();
        }
        return instance;
    }

    private UserStateView() {
        userStateModel = UserStateModel.getInstance();
        users = userStateModel.getUserModels();
        checkCreateUser();
        firstWidth = (int) (77 * SCALE);
        initUserButtons();
    }

    private void checkCreateUser() {
        if (userIndex == users.size()) {
            createUser = true;
        }
        if (createUser) {
            currentAvatar = LoadSave.GetSpriteAtlas(LoadSave.MAITA_FIREBALL); // TODO METTERE UN AVATAR
            currentUser = new UserModel("Nickname", 0, 0, 0, 0, 0, LoadSave.MAITA_FIREBALL);
        } else {
            currentUser = userStateModel.getUserModels().get(userIndex);
            currentAvatar = currentUser.getAvatar();
        }
    }

    public void update() {
        if (userIndex < users.size())
            nextPageButton.update();
        if (userIndex != 0)
            prevPageButton.update();
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        Font nicknameFont = (LoadSave.JEQO_FONT).deriveFont(15 * SCALE);
        FontMetrics nicknameMeasures = g.getFontMetrics(nicknameFont);
        FontMetrics measures = g.getFontMetrics(LoadSave.JEQO_FONT);

        drawUserStats(g, measures, nicknameMeasures, nicknameFont);
        drawButtons(g);

    }

    private void drawButtons(Graphics g) {
        if (userIndex < users.size())
            nextPageButton.draw(g);
        if (userIndex != 0)
            prevPageButton.draw(g);
    }

    private void drawUserStats(Graphics g, FontMetrics measures, FontMetrics nicknameMeasures, Font nicknameFont) {
        //currentAvatar = currentUser.getAvatar();

        int startWidth = firstWidth;
        int startHeight = firstHeight;


        g.drawImage(currentAvatar, startWidth, startHeight, (int) (32 * SCALE), (int)(32 * SCALE),  null);
        g.setFont(nicknameFont);
        g.setColor(new Color(242, 70, 152));
        startWidth += currentAvatar.getWidth() + (nicknameMeasures.stringWidth(currentUser.getNickname())/2) - (int) (5*SCALE);
        startHeight += nicknameMeasures.getHeight();
        g.drawString(currentUser.getNickname(), startWidth, startHeight);

        g.setColor(Color.WHITE);
        g.setFont(LoadSave.JEQO_FONT);

        String[] texts = {
                "Livello attuale: " + currentUser.getLevel(),
                "Partite vinte: " + currentUser.getWins(),
                "Punteggio massimo: " + currentUser.getScore(),
                "Partite giocate: " + currentUser.getMatches(),
                "Partite perse: " + currentUser.getLosses()
        };

        for(String text : texts) {
            startHeight += measures.getHeight() + (int) (10* SCALE);
            g.drawString(text, startWidth, startHeight);
        }
    }

    private void initUserButtons() {
        nextPageButton = new ChangePageButtonView(new ChangePageButtonModel(
                GAME_WIDTH - (int)(10 * SCALE),
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

    public ChangePageButtonView getNextPageButton() {
        return nextPageButton;
    }

    public ChangePageButtonView getPrevPageButton() {
        return prevPageButton;
    }

    public void changeIndex(int i) {
        this.userIndex += i;
        createUser = false;
        checkCreateUser();
    }
}
