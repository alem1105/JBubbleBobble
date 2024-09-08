package view.stateview;

import model.UserModel;
import model.gamestate.UserStateModel;
import model.ui.buttons.ChangePageButtonModel;
import model.ui.buttons.CreateButtonModel;

import static model.utilz.Constants.Directions.*;
import view.ui.buttons.ChangePageButtonView;
import view.ui.buttons.CreateButtonView;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

import static model.utilz.Constants.GameConstants.*;
import static view.utilz.LoadSave.NES_FONT;

/**
 * Gestisce la vista della selezione degli utenti
 */
public class UserStateView {

    private static UserStateView instance;

    private UserStateModel userStateModel;
    private ArrayList<UserModel> users;

    private ChangePageButtonView nextPageButton;
    private ChangePageButtonView prevPageButton;
    private CreateButtonView createButton;
    private int userIndex = 0;

    private UserModel currentUser;
    private int firstWidth;
    private int firstHeight = GAME_HEIGHT / 2 - (int)(69 * SCALE);

    private boolean createUser;

    private String[] avatars = {LoadSave.AVATAR_1, LoadSave.AVATAR_2, LoadSave.AVATAR_3, LoadSave.AVATAR_4, LoadSave.AVATAR_5, LoadSave.AVATAR_6, LoadSave.AVATAR_7, LoadSave.AVATAR_8, LoadSave.AVATAR_9, LoadSave.AVATAR_10, LoadSave.AVATAR_11};
    private int avatarIndex = 0;
    private ChangePageButtonView nextAvatarButton;
    private ChangePageButtonView prevAvatarButton;

    private String inputNickname ;
    private Rectangle2D.Float nicknameField;
    private boolean writingNickname;

    private boolean showingLeaderboard;

    public static UserStateView getInstance() {
        if (instance == null) {
            instance = new UserStateView();
        }
        return instance;
    }

    private UserStateView() {
        userStateModel = UserStateModel.getInstance();
        users = userStateModel.getUserModels();
        Collections.sort(users);
        inputNickname = "Write Nick";
        checkCreateUser();
        firstWidth = GAME_WIDTH / 2 - (int) (110 * SCALE);
        initButtons();
        nicknameField = new Rectangle2D.Float(firstWidth + (int) (60 * SCALE), firstHeight, (int) (180 * SCALE), (int) (26 * SCALE));
    }

    /**
     * Controlla se siamo nella schermata di creazione utente
     */
    private void checkCreateUser() {
        if (userIndex == users.size())
            createUser = true;

        if (createUser)
            currentUser = new UserModel(inputNickname, 0, 0, 0, 0, 0, 0, avatars[avatarIndex]);
        else
            currentUser = userStateModel.getUserModels().get(userIndex);
    }

    /**
     * Aggiorna i bottoni della schermata
     */
    public void update() {
        nextPageButton.update();
        prevPageButton.update();
        prevAvatarButton.update();
        nextAvatarButton.update();
        createButton.update();
    }

    /**
     * Disegna tutti gli elementi della schermata
     * @param g
     */
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        Font nicknameFont = (LoadSave.JEQO_FONT).deriveFont(15 * SCALE);
        FontMetrics nicknameMeasures = g.getFontMetrics(nicknameFont);
        FontMetrics measures = g.getFontMetrics(LoadSave.JEQO_FONT);

        if (showingLeaderboard){
            drawLeaderboard(g);
            return;
        }

        drawUserStats(g, measures, nicknameMeasures, nicknameFont);
        drawButtons(g);
        drawHintForLeaderboard(g);
    }

    /**
     * Disegna la classifica
     * @param g
     */
    private void drawLeaderboard(Graphics g) {
        Color[] colors = new Color[]{new Color(142, 253, 153), new Color(255, 178, 244), new Color(175, 255, 255)};
        Collections.sort(users);
        g.setFont(NES_FONT);
        FontMetrics fontMetrics = g.getFontMetrics();
        int x = GAME_WIDTH / 2 - fontMetrics.stringWidth("00    AAAAAAAAA     000000") / 2;
        int scoreX = GAME_WIDTH / 2 + fontMetrics.stringWidth("00    AAAAAAAAA     000000") / 2 - fontMetrics.stringWidth(" 000000");
        for(int y = 0; y < users.size(); y++) {
            UserModel user = users.get(y);

            if (y >= 3)
                g.setColor(Color.WHITE);
            else
                g.setColor(colors[y]);

            g.drawString((y+1) + "    " + user.getNickname().toUpperCase(), x, y * (fontMetrics.getHeight() + (int) (10 * SCALE)) + (int)(100 * SCALE));
            g.drawString(String.valueOf(user.getMaxScore()).toUpperCase(), scoreX, y * (fontMetrics.getHeight() + (int) (10 * SCALE)) + (int)(100 * SCALE));
        }
        Font font = NES_FONT.deriveFont(20f * SCALE);
        g.setFont(font);
        g.setColor(new Color(255, 81, 81));
        fontMetrics = g.getFontMetrics();
        g.drawString("LEADERBOARD", GAME_WIDTH / 2 - fontMetrics.stringWidth("LEADERBOARD") / 2, (int) (50 * SCALE));
    }

    /**
     * Disegna la scritta per spiegare al giocatore come mostrare la classifica
     * @param g
     */
    private void drawHintForLeaderboard(Graphics g) {
        if(createUser)
            return;

        Color lightRed = new Color(255, 81, 81);
        g.setColor(lightRed);
        g.setFont(NES_FONT);
        FontMetrics fontMetrics = g.getFontMetrics(NES_FONT);
        g.drawString("PRESS L TO SEE LEADERBOARD", GAME_WIDTH / 2 - fontMetrics.stringWidth("PRESS L TO SEE LEADERBOARD") / 2, GAME_HEIGHT - (int) (70 * SCALE));
    }

    private void drawButtons(Graphics g) {
        if (userIndex < users.size())
            nextPageButton.draw(g);
        if (userIndex != 0)
            prevPageButton.draw(g);

        if (createUser) {
            createButton.draw(g);
            if (avatarIndex != avatars.length - 1)
                nextAvatarButton.draw(g);
            if (avatarIndex != 0)
                prevAvatarButton.draw(g);
        }
    }

    /**
     * Disegna le statistiche del giocatore
     * @param g
     * @param measures
     * @param nicknameMeasures
     * @param nicknameFont
     */
    private void drawUserStats(Graphics g, FontMetrics measures, FontMetrics nicknameMeasures, Font nicknameFont) {

        int startWidth = firstWidth;
        int startHeight = firstHeight;

        g.drawImage(currentUser.getAvatar(), startWidth, startHeight  - (int) (2 * SCALE), (int) (50 * SCALE), (int)(50 * SCALE),  null);
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
                "Max score: " + currentUser.getMaxScore(),
                "Matches played: " + currentUser.getMatches(),
                "Matches lost: " + currentUser.getLosses()
        };

        for(String text : texts) {
            startHeight += measures.getHeight() + (int) (10* SCALE);
            g.drawString(text, startWidth, startHeight);
        }
    }

    /**
     * Inizializza i bottoni della schermata
     */
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
        prevAvatarButton = new ChangePageButtonView(new ChangePageButtonModel(
                firstWidth - (int)(6 * SCALE),
                firstHeight + (int)(15 * SCALE),
                (int) (10 * SCALE),
                (int) (10 * SCALE),
                LEFT));
        nextAvatarButton = new ChangePageButtonView(new ChangePageButtonModel(
                firstWidth + (int)(46 * SCALE),
                firstHeight + (int)(15 * SCALE),
                (int) (10 * SCALE),
                (int) (10 * SCALE),
                RIGHT));
    }

    /**
     * Cambia indice dell'utente selezionato
     * @param i di quanto aumentare l'indice
     */
    public void changeIndex(int i) {
        this.userIndex += i;
        avatarIndex = 0;
        createUser = false;
        inputNickname = "Write Nick";
        checkCreateUser();
    }

    /**
     * Cambia l'indice per selezionare un nuovo avatar
     * @param i
     */
    public void changeAvatarIndex(int i) {
        if (0 <= avatarIndex + i && avatarIndex + i < avatars.length ) {
            this.avatarIndex += i;
            currentUser.setAvatarPath(avatars[avatarIndex]);
        }
    }

    /**
     * Ricarica tutti gli utenti
     */
    public void reloadUsers(){
        users = userStateModel.getUserModels();
        createUser = false;
        userIndex = 0;
        currentUser = users.get(userIndex);
        avatarIndex = 0;
    }

    public ChangePageButtonView getNextPageButton() {
        return nextPageButton;
    }

    public ChangePageButtonView getPrevPageButton() {
        return prevPageButton;
    }

    public ChangePageButtonView getNextAvatarButton() {
        return nextAvatarButton;
    }

    public ChangePageButtonView getPrevAvatarButton() {
        return prevAvatarButton;
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

    public UserModel getCurrentUser() {
        return currentUser;
    }

    public int getUserIndex() {
        return userIndex;
    }

    public CreateButtonView getCreateButton() {
        return createButton;
    }

    public void setShowingLeaderboard(boolean showingLeaderboard) {
        this.showingLeaderboard = showingLeaderboard;
    }
}