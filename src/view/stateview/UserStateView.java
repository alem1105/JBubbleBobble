package view.stateview;

import model.UserModel;
import model.gamestate.UserStateModel;
import model.ui.buttons.ChangePageButtonModel;
import model.ui.buttons.CreateButtonModel;
import model.ui.buttons.UserButtonModel;
import static model.utilz.Constants.Directions.*;
import view.ui.buttons.ChangePageButtonView;
import view.ui.buttons.CreateButtonView;
import view.ui.buttons.UserButtonView;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.utilz.Constants.GameConstants.*;

public class UserStateView {

    private static UserStateView instance;

    private UserStateModel userStateModel;
    ArrayList<UserModel> users;

    private ChangePageButtonView nextPageButton;
    private ChangePageButtonView prevPageButton;
    private CreateButtonView createButton;
    private int userIndex = 0;

    private UserModel currentUser;
    private BufferedImage currentAvatar;
    private int firstWidth;
    private int firstHeight = GAME_HEIGHT / 2 - (int)(69 * SCALE);

    private boolean createUser;

    private String inputNickname ;
    private Rectangle2D.Float nicknameField;
    private boolean writingNickname;

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
        firstWidth = GAME_WIDTH / 2 - (int) (110 * SCALE);
        initButtons();
        nicknameField = new Rectangle2D.Float(firstWidth + (int) (60 * SCALE), firstHeight, (int) (180 * SCALE), (int) (26 * SCALE));
        inputNickname = "Write Nick";
    }

    private void checkCreateUser() {
        if (userIndex == users.size())
            createUser = true;

        if (createUser)
            currentUser = new UserModel(inputNickname, 0, 0, 0, 0, 0, LoadSave.AVATAR_2);
        else
            currentUser = userStateModel.getUserModels().get(userIndex);

        currentAvatar = currentUser.getAvatar();
    }

    public void update() {
        nextPageButton.update();
        prevPageButton.update();
        createButton.update();
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
        if (createUser)
            createButton.draw(g);
    }

    private void drawUserStats(Graphics g, FontMetrics measures, FontMetrics nicknameMeasures, Font nicknameFont) {

        int startWidth = firstWidth;
        int startHeight = firstHeight;

        g.drawImage(currentAvatar, startWidth, startHeight  - (int) (2 * SCALE), (int) (50 * SCALE), (int)(50 * SCALE),  null);
        g.setFont(nicknameFont);
        g.setColor(new Color(242, 70, 152));
        startWidth = (int) (nicknameField.x + 5 * SCALE);
        startHeight = (int) (nicknameField.y + nicknameMeasures.getHeight() - 2 * SCALE)  ;

        if (createUser) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.draw(nicknameField);
        }

        g.drawString(currentUser.getNickname(), startWidth, startHeight);

        g.setColor(Color.WHITE);
        g.setFont(LoadSave.JEQO_FONT);

        String[] texts = {
                "Level: " + currentUser.getLevel(),
                "Matches won: " + currentUser.getWins(),
                "Max score: " + currentUser.getScore(),
                "Matches played: " + currentUser.getMatches(),
                "Matches lost: " + currentUser.getLosses()
        };

        for(String text : texts) {
            startHeight += measures.getHeight() + (int) (10* SCALE);
            g.drawString(text, startWidth, startHeight);
        }
    }

    private void initButtons() {
        nextPageButton = new ChangePageButtonView(new ChangePageButtonModel(
                GAME_WIDTH - (int)(30 * SCALE),
                GAME_HEIGHT / 2 - (int)(5 * SCALE),
                (int) (20 * SCALE),
                (int) (20 * SCALE),
                RIGHT));
        prevPageButton = new ChangePageButtonView(new ChangePageButtonModel(
                (int)(12 * SCALE),
                GAME_HEIGHT / 2 - (int)(5 * SCALE),
                (int) (20 * SCALE),
                (int) (20 * SCALE),
                LEFT));
        createButton = new CreateButtonView(new CreateButtonModel(
                GAME_WIDTH/2 - (int)(47 * SCALE) ,
                (int)(230 * SCALE),
                (int) (94 * SCALE),
                (int) (28 * SCALE)));
    }

    public void changeIndex(int i) {
        this.userIndex += i;
        createUser = false;
        checkCreateUser();
    }

    public ChangePageButtonView getNextPageButton() {
        return nextPageButton;
    }

    public ChangePageButtonView getPrevPageButton() {
        return prevPageButton;
    }

    public String getInputNickname() {
        return inputNickname;
    }

    public void setWritingNickname(boolean writingNickname) {
        this.writingNickname = writingNickname;
    }

    public boolean isWritingNickname() {
        return writingNickname;
    }

    public boolean isCreateUser() {
        return createUser;
    }

    public Rectangle2D.Float getNicknameField() {
        return nicknameField;
    }

    public void setCurrentUser(UserModel currentUser) {
        this.currentUser = currentUser;
    }

    public UserModel getCurrentUser() {
        return currentUser;
    }

    public int getUserIndex() {
        return userIndex;
    }

    public CreateButtonView getCreateButton() {
        return createButton;
    }
}
