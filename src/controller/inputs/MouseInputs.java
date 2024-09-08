package controller.inputs;

import model.LevelManagerModel;
import model.entities.PlayerModel;
import model.gamestate.Gamestate;
import model.gamestate.PlayingModel;
import model.ui.buttons.*;
import view.entities.enemies.EnemiesManagerView;
import view.stateview.*;
import view.ui.DeathScreenView;
import view.ui.GamePausedScreenView;
import view.ui.buttons.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.stream.Stream;

import static model.utilz.Constants.GameConstants.TILES_IN_HEIGHT;
import static model.utilz.Constants.GameConstants.TILES_SIZE;

/**
 * La classe MouseInputs gestisce gli eventi del mouse, come click, movimenti e
 * trascinamenti, per diverse schermate e stati del gioco. Implementa
 * MouseMotionListener e MouseListener per rispondere agli input dell'utente.
 */
public class MouseInputs implements MouseMotionListener, MouseListener {

    private LevelEditorView levelEditorView;
    private LevelManagerModel levelManagerModel;
    private LevelSelectorView levelSelectorView;
    private DeathScreenView deathScreenView;
    private MenuView menuView;
    private UserStateView userStateView;
    private GamePausedScreenView gamePausedScreenView;
    private boolean justChangedScreen;

    /**
     * Costruttore della classe MouseInputs.
     * Inizializza le varie viste e il modello utilizzati durante il gioco.
     * Non accetta parametri e non restituisce valori.
     */
    public MouseInputs(){
        this.levelEditorView = LevelEditorView.getInstance();
        this.levelManagerModel = LevelManagerModel.getInstance();
        this.levelSelectorView = LevelSelectorView.getInstance();
        this.deathScreenView = DeathScreenView.getInstance();
        this.menuView = MenuView.getInstance();
        this.userStateView = UserStateView.getInstance();
        this.gamePausedScreenView = GamePausedScreenView.getInstance();
    }

    /**
     * Controlla se il mouse si trova all'interno dei limiti di un pulsante.
     *
     * @param <T> Il tipo del pulsante personalizzato che estende CustomButtonView.
     * @param button Il pulsante da controllare.
     * @param e L'evento del mouse che contiene la posizione corrente del mouse.
     * @return true se il mouse si trova all'interno dei confini del pulsante, altrimenti false.
     */
    private <T extends CustomButtonView> boolean isIn(T button, MouseEvent e) {
        if(button.getButtonModel().getBounds().contains(e.getX(), e.getY()))
            return true;
        else
            return false;
    }

    /**
     * Gestisce gli eventi di click del mouse.
     * Se lo stato di gioco è in modalità PLAYING, imposta lo stato di attacco
     * del giocatore su vero quando viene cliccato il pulsante sinistro del mouse.
     *
     * @param e L'evento del mouse che contiene le informazioni sul click.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (Gamestate.state) {
            case PLAYING -> {
                if (justChangedScreen){
                    justChangedScreen = false;
                } else {
                    switch(e.getButton()) {
                        case MouseEvent.BUTTON1 -> {
                            PlayerModel.getInstance().setAttack(true);
                        }
                    }
                }
            }

        }
    }

    /**
     * Gestisce gli eventi di pressione del pulsante del mouse.
     * A seconda dello stato del gioco, verifica quale pulsante è stato premuto e imposta lo stato appropriato.
     *
     * @param e L'evento del mouse che contiene le informazioni sul pulsante premuto.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        switch (Gamestate.state) {
            case USER -> {
                checkPressedUser(e);
            }
            case MENU -> {
                if (isIn(menuView.getStartButton(), e) && !isGameWon()){
                    getStartButton().setPressed(true);
                }
                if (isIn(menuView.getEditorButton(), e) && !isGameWon()){
                    getEditorButton().setPressed(true);
                }
            }
            case PLAYING -> {
                if(PlayerModel.getInstance().isGameOver()){
                    if(isIn(deathScreenView.getQuitButtonView(), e))
                        getQuitButton().setPressed(true);
                    if(isIn(deathScreenView.getRestartButtonView(), e))
                        getRestartButton().setPressed(true);
                    return;
                }
                if (PlayingModel.getInstance().isPaused()){
                    if (isIn(gamePausedScreenView.getStartButton(), e))
                        gamePausedScreenView.getStartButton().getButtonModel().setPressed(true);
                    else if (isIn(gamePausedScreenView.getQuitButton(), e))
                        gamePausedScreenView.getQuitButton().getButtonModel().setPressed(true);
                }

            }
            case LEVEL_EDITOR -> {
                editorCheckClicks(e);
                editorCheckEditedTiles(e);
                editorCheckPressed(e);
            }

            case LEVEL_SELECTOR -> {
                if (isIn(levelSelectorView.getNextLevelButtonView(), e)){
                    setNextLvlButtonPressed(true);
                    if (levelEditorView.getLevelIndex() == levelManagerModel.getLevels().size() - 1)
                        levelEditorView.setLevelIndex(0);
                    else{
                        levelEditorView.setLevelIndex(levelEditorView.getLevelIndex() + 1);
                    }
                    levelEditorView.getXButtonView().getButtonModel().updateData(levelEditorView.getLevelIndex());

                }

                if (isIn(levelSelectorView.getPrevLevelButtonView(), e)){
                    setPrevLvlButtonPressed(true);
                    if (levelEditorView.getLevelIndex() == 0)
                        levelEditorView.setLevelIndex(levelManagerModel.getLevels().size() - 1);
                    else{
                        levelEditorView.setLevelIndex(levelEditorView.getLevelIndex() - 1);
                    }
                    levelEditorView.getXButtonView().getButtonModel().updateData(levelEditorView.getLevelIndex());

                }

                if(isIn(levelSelectorView.getEditButtonView(), e)) {
                    setEditButtonPressed(true);
                    setEditButtonHover(false);
                }
            }

        }
    }

    /**
     * Gestisce gli eventi di rilascio del pulsante del mouse.
     * Quando l'utente rilascia un pulsante del mouse, vengono eseguite azioni come
     * il cambiamento di stato del gioco o la modifica dei dati dell'utente.
     *
     * @param e L'evento del mouse che contiene le informazioni sul rilascio.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state) {
            case USER -> {
                if (isIn(userStateView.getPrevPageButton(), e)){
                    if (getPrevPageButton().isPressed())
                        userStateView.changeIndex(-1);
                }
                if (isIn(userStateView.getNextPageButton(), e)){
                    if (getNextPageButton().isPressed())
                        userStateView.changeIndex(1);
                }
                getNextPageButton().setPressed(false);
                getPrevPageButton().setPressed(false);
                if (userStateView.isCreateUser()) {
                    if (isIn(userStateView.getPrevAvatarButton(), e)){
                        if (userStateView.getPrevAvatarButton().getButtonModel().isPressed())
                            userStateView.changeAvatarIndex(-1);
                    }
                    if (isIn(userStateView.getNextAvatarButton(), e)){
                        if (userStateView.getNextAvatarButton().getButtonModel().isPressed())
                            userStateView.changeAvatarIndex(1);
                    }
                    if (isIn(userStateView.getCreateButton(), e)) {
                        if (userStateView.getCreateButton().getButtonModel().isPressed()) {
                            userStateView.getCreateButton().getButtonModel().saveUser(userStateView.getCurrentUser());
                            userStateView.reloadUsers();
                        }
                    }
                    userStateView.getCreateButton().getButtonModel().setPressed(false);
                    userStateView.getPrevAvatarButton().getButtonModel().setPressed(false);
                    userStateView.getNextAvatarButton().getButtonModel().setPressed(false);
                }
            }
            case MENU -> {
                if (isIn(menuView.getStartButton(), e) && !isGameWon()){
                    Gamestate.state = Gamestate.PLAYING;
                    justChangedScreen = true;
                }
                if (isIn(menuView.getEditorButton(), e) && !isGameWon()){
                    Gamestate.state = Gamestate.LEVEL_SELECTOR;
                }
                getEditorButton().setPressed(false);
                getStartButton().setPressed(false);
            }
            case PLAYING -> {
                if(PlayerModel.getInstance().isGameOver()){
                    if(isIn(deathScreenView.getQuitButtonView(), e)){
                        if(getQuitButton().isPressed()){
                            getQuitButton().quit();
                        }
                    }
                    else if(isIn(deathScreenView.getRestartButtonView(), e)){
                        if(getRestartButton().isPressed()){
                            getRestartButton().restart();
                            EnemiesManagerView.getInstance().setRestart(true);
                        }
                    }
                    return;
                }

                if (PlayingModel.getInstance().isPaused()){
                    if (isIn(gamePausedScreenView.getStartButton(), e))
                        if (gamePausedScreenView.getStartButton().getButtonModel().isPressed())
                            PlayingModel.getInstance().setPaused(false);
                    if (isIn(gamePausedScreenView.getQuitButton(), e))
                        if (gamePausedScreenView.getQuitButton().getButtonModel().isPressed())
                            getQuitButton().quit();

                    gamePausedScreenView.getStartButton().getButtonModel().setPressed(false);
                    gamePausedScreenView.getQuitButton().getButtonModel().setPressed(false);
                    return;
                }

                PlayerModel.getInstance().setAttack(false);


            }
            case LEVEL_EDITOR -> {
                if(isIn(levelEditorView.getXButtonView(), e)){
                    if(getXButton().isPressed()){
                        setXButtonPressed(false);
                        xButtonClick(levelEditorView.getXButtonView().getButtonModel());
                        Gamestate.state = Gamestate.MENU;
                    }
                }
                if(isIn(levelEditorView.getSaveButtonView(), e)){
                    if(getSaveButton().isPressed()){
                        getSaveButtonModel().saveNewLevelImage(getLevelData(), getEnemiesData(), getPlayerSpawn(), levelEditorView.getLevelIndex());
                        Gamestate.state = Gamestate.MENU;
                    }
                }
            }

            case LEVEL_SELECTOR -> {
                setNextLvlButtonPressed(false);
                setPrevLvlButtonPressed(false);
                if(isIn(levelSelectorView.getEditButtonView(), e)){
                    if(getEditButton().isPressed()){
                        Gamestate.state = Gamestate.LEVEL_EDITOR;
                    }
                }
                setEditButtonPressed(false);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePressed(e);
    }

    /**
     * Gestisce il movimento del mouse per aggiornare lo stato di hover (passaggio del mouse) sui pulsanti.
     * Verifica quali pulsanti vengono "sorvolati" dal puntatore e imposta lo stato di hover.
     *
     * @param e L'evento del mouse che contiene le informazioni sul movimento.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU -> {
                if (isIn(menuView.getStartButton(), e) && !isGameWon()){
                    getStartButton().setHover(true);
                } else {
                    getStartButton().setHover(false);
                }
                if (isIn(menuView.getEditorButton(), e) && !isGameWon()){
                    getEditorButton().setHover(true);
                } else {
                    getEditorButton().setHover(false);
                }
            }

            case PLAYING -> {
                if(PlayerModel.getInstance().isGameOver()){
                    if(isIn(deathScreenView.getQuitButtonView(), e))
                        getQuitButton().setHover(true);
                    else {
                        getQuitButton().setHover(false);
                    }
                    if(isIn(deathScreenView.getRestartButtonView(), e))
                        deathScreenView.getRestartButtonView().getButtonModel().setHover(true);
                    else {
                        deathScreenView.getRestartButtonView().getButtonModel().setHover(false);
                    }
                    return;
                }

                if (PlayingModel.getInstance().isPaused()){
                    if (isIn(gamePausedScreenView.getStartButton(), e))
                        gamePausedScreenView.getStartButton().getButtonModel().setHover(true);
                    else
                        gamePausedScreenView.getStartButton().getButtonModel().setHover(false);

                    if (isIn(gamePausedScreenView.getQuitButton(), e))
                        gamePausedScreenView.getQuitButton().getButtonModel().setHover(true);
                    else
                        gamePausedScreenView.getQuitButton().getButtonModel().setHover(false);
                }
            }

            case LEVEL_EDITOR -> {
                if (isIn(levelEditorView.getSaveButtonView(), e)) {
                    setSaveButtonHover(true);
                }
                else {
                    setSaveButtonHover(false);
                }
                if (isIn(levelEditorView.getXButtonView(), e)) {
                    setXButtonHover(true);
                } else {
                    setXButtonHover(false);
                }
            }

            case LEVEL_SELECTOR -> {
                if (isIn(levelSelectorView.getNextLevelButtonView(), e)){
                    setNextLvlButtonHover(true);
                } else{
                    setNextLvlButtonHover(false);
                }

                if (isIn(levelSelectorView.getPrevLevelButtonView(), e)){
                    setPrevLvlButtonHover(true);
                } else{
                    setPrevLvlButtonHover(false);
                }

                if (isIn(levelSelectorView.getEditButtonView(), e)){
                    setEditButtonHover(true);
                } else {
                    setEditButtonHover(false);
                }
            }
        }
    }
    
    /**
     * Ripristina i dati del livello corrente utilizzando le informazioni memorizzate
     * nel modello del pulsante X.
     *
     * @param xButtonModel Il modello del pulsante X che contiene i dati del livello, i nemici e la posizione del giocatore.
     */
    private void xButtonClick(XButtonModel xButtonModel){
        LevelManagerModel.getInstance()
                .getLevels()
                .get(LevelEditorView.getInstance()
                        .getLevelIndex())
                .setLvlData(xButtonModel.getOldLvlData());

        LevelManagerModel.getInstance()
                .getLevels()
                .get(LevelEditorView
                        .getInstance()
                        .getLevelIndex())
                .setEnemiesData(xButtonModel.getOldEnemiesData());

        LevelManagerModel.getInstance()
                .getLevels()
                .get(LevelEditorView
                        .getInstance()
                        .getLevelIndex())
                .setPlayerSpawn(xButtonModel.getOldPlayerSpawn());

        LevelEditorView.getInstance().setLevelIndex(0);
    }

    /**
     * Verifica quali pulsanti dell'interfaccia utente sono stati premuti e imposta
     * lo stato dei pulsanti corrispondenti. Gestisce l'interazione con la pagina
     * successiva, precedente e la creazione di un nuovo utente.
     *
     * @param e L'evento del mouse contenente le coordinate e le informazioni sul click.
     */
    private void checkPressedUser(MouseEvent e){
        if (isIn(userStateView.getNextPageButton(), e)){
            if (!userStateView.isCreateUser())
                getNextPageButton().setPressed(true);
        }
        if (isIn(userStateView.getPrevPageButton(), e)){
            if (userStateView.getUserIndex() != 0)
                getPrevPageButton().setPressed(true);
        }
        if(userStateView.isCreateUser()) {
            if (isIn(userStateView.getCreateButton(), e)){
                if (userStateView.isCreateUser())
                    userStateView.getCreateButton().getButtonModel().setPressed(true);
            }
            if (userStateView.getNicknameField().contains(e.getX(), e.getY())) {
                userStateView.setWritingNickname(true);
                userStateView.getCurrentUser().setNickname("");
            } else
                userStateView.setWritingNickname(false);

            if (isIn(userStateView.getPrevAvatarButton(), e)) {
                userStateView.getPrevAvatarButton().getButtonModel().setPressed(true);
            }
            if (isIn(userStateView.getNextAvatarButton(), e)) {
                userStateView.getNextAvatarButton().getButtonModel().setPressed(true);
            }
        }
    }

    /**
     * Verifica e modifica i blocchi e i nemici del livello corrente a seconda delle
     * interazioni dell'utente con il mouse. Gestisce il click su piastrelle del livello,
     * aggiornando i dati del livello e dei nemici.
     *
     * @param e L'evento del mouse contenente le informazioni sul click.
     */
    private void editorCheckEditedTiles(MouseEvent e) {
        int currentTileX = (e.getX()) / (TILES_SIZE - levelEditorView.getDrawOffset());
        int currentTileY = (e.getY()) / (TILES_SIZE - levelEditorView.getDrawOffset());
        if (currentTileX < 20 && currentTileX >= 0 && currentTileY < 18 && currentTileY >= 0) {
            int[][] lvlData = getLevelData();
            int[][] enemiesData = getEnemiesData();

            int playerTileX = (int) (getPlayerSpawn().getX() / TILES_SIZE);
            int playerTileY = (int) (getPlayerSpawn().getY() / TILES_SIZE);

            if ((currentTileX != playerTileX) || (currentTileY != playerTileY)) {
                if((checkRoofAndBottomTile(currentTileY))) {
                    lvlData[0][currentTileX] = 0;
                    lvlData[TILES_IN_HEIGHT - 2][currentTileX] = 0;
                }
                else {
                    lvlData[currentTileY][currentTileX] = levelEditorView.getBlockIndex();
                }
            }

            checkPlayerButtonClick(currentTileX, currentTileY);

            if(levelEditorView.getEnemyIndex() > 0)
                lvlData[currentTileY][currentTileX] = 0;
            enemiesData[currentTileY][currentTileX] = levelEditorView.getEnemyIndex();
        }
    }

    /**
     * Controlla se il blocco o la piastrella si trova in corrispondenza del tetto
     * o del pavimento del livello e se il pulsante gomma o giocatore è attivo.
     *
     * @param currentTileY La posizione Y della piastrella corrente.
     * @return true se è un blocco di tetto o pavimento, altrimenti false.
     */
    private boolean checkRoofAndBottomTile(int currentTileY) {
        return (currentTileY == TILES_IN_HEIGHT - 2 || currentTileY == 0) && (isEraserButtonPressed() || isPlayerButtonPressed());
    }

    /**
     * Gestisce il click del pulsante giocatore per impostare la posizione di spawn
     * del giocatore nel livello corrente.
     *
     * @param currentTileX La posizione X della piastrella corrente.
     * @param currentTileY La posizione Y della piastrella corrente.
     */
    private void checkPlayerButtonClick(int currentTileX, int currentTileY) {
        if(isPlayerButtonPressed()) {
            setEraserButtonPressed(false);
            LevelManagerModel.getInstance()
                    .getLevels()
                    .get(levelEditorView.getLevelIndex())
                    .setPlayerSpawn(
                            new Point(currentTileX * TILES_SIZE, currentTileY * TILES_SIZE));
        }
    }

    /**
     * Gestisce il click sui pulsanti dell'editor, controllando se l'utente ha premuto
     * su un pulsante di blocco o nemico e attivando l'azione corrispondente.
     *
     * @param e L'evento del mouse contenente le informazioni sul click.
     */
    private void editorCheckClicks(MouseEvent e) {

        CustomButtonView[] allButtons = Stream
                .concat(Arrays.stream(levelEditorView.getButtons()),
                        Arrays.stream(levelEditorView.getEnemies()))
                .toArray(CustomButtonView[]::new);

        for(CustomButtonView button : allButtons){
            if(isIn(button, e)) {
                if(button instanceof BlockButtonView)
                    blockButtonClick((BlockButtonView) button);
                else if(button instanceof EnemyButtonView)
                    enemyButtonClick((EnemyButtonView) button);
            }
        }
    }

    /**
     * Verifica quali pulsanti dell'editor sono stati premuti e imposta lo stato dei pulsanti
     * corrispondenti, come il pulsante di salvataggio, il pulsante gomma o il pulsante giocatore.
     *
     * @param e L'evento del mouse contenente le informazioni sul click.
     */
    private void editorCheckPressed(MouseEvent e) {
        setEraserButtonPressed(false);
        if (isIn(levelEditorView.getSaveButtonView(), e)) {
            setSaveButtonPressed(true);
        }

        if (isIn(levelEditorView.getXButtonView(), e)) {
            setXButtonPressed(true);
        }

        if (isIn(levelEditorView.getEraserButtonView(), e)) {
            eraserButtonClick();
            setEraserButtonPressed(true);
        }

        if(isIn(levelEditorView.getPlayerButtonView(), e)) {
            playerButtonClick();
        }
    }

    // METODI HELPER

    private void enemyButtonClick(EnemyButtonView button) {
        setLevelEditorEnemyIndex(button.getButtonModel().getIndex());
        setEraserButtonPressed(false);
        setPlayerButtonPressed(false);
        setLevelEditorBlockIndex(0);
    }

    private void blockButtonClick(BlockButtonView button) {
        setLevelEditorBlockIndex(button.getButtonModel().getIndex());
        setEraserButtonPressed(false);
        setPlayerButtonPressed(false);
        setLevelEditorEnemyIndex(0);
    }

    private StartButtonModel getStartButton(){
        return menuView.getStartButton().getButtonModel();
    }

    private EditorButtonModel getEditorButton(){
        return menuView.getEditorButton().getButtonModel();
    }

    private RestartButtonModel getRestartButton(){
        return deathScreenView.getRestartButtonView().getButtonModel();
    }

    private QuitButtonModel getQuitButton(){
        return deathScreenView.getQuitButtonView().getButtonModel();
    }

    private XButtonModel getXButton(){
        return levelEditorView.getXButtonView().getButtonModel();
    }

    private SaveButtonModel getSaveButton(){
        return levelEditorView.getSaveButtonView().getButtonModel();
    }

    private EditButtonModel getEditButton(){
        return levelSelectorView.getEditButtonView().getButtonModel();
    }

    private int[][] getLevelData() {
        return levelManagerModel
                .getLevels()
                .get(levelEditorView.getLevelIndex())
                .getLvlData();
    }

    private int[][] getEnemiesData() {
        return levelManagerModel
                .getLevels()
                .get(levelEditorView.getLevelIndex())
                .getEnemiesData();
    }

    private void setPlayerButtonPressed(boolean pressed) {
        levelEditorView
                .getPlayerButtonView()
                .getButtonModel()
                .setPressed(pressed);
    }

    private void setEraserButtonPressed(boolean pressed) {
        levelEditorView
                .getEraserButtonView()
                .getButtonModel()
                .setPressed(pressed);
    }

    private void setNextLvlButtonPressed(boolean pressed) {
        levelSelectorView.getNextLevelButtonView().getButtonModel().setPressed(pressed);
    }

    private void setPrevLvlButtonPressed(boolean pressed) {
        levelSelectorView.getPrevLevelButtonView().getButtonModel().setPressed(pressed);
    }

    private boolean isPlayerButtonPressed() {
        return levelEditorView
                .getPlayerButtonView()
                .getButtonModel()
                .isPressed();
    }

    private void setLevelEditorBlockIndex(int index) {
        levelEditorView.setBlockIndex(index);
    }

    private void setLevelEditorEnemyIndex(int index) {
        levelEditorView.setEnemyIndex(index);
    }

    private boolean isEraserButtonPressed() {
        return levelEditorView
                .getEraserButtonView()
                .getButtonModel()
                .isPressed();
    }

    private void setXButtonPressed(boolean pressed) {
        levelEditorView
                .getXButtonView()
                .getButtonModel()
                .setPressed(pressed);
    }

    private void setSaveButtonPressed(boolean pressed) {
        levelEditorView
                .getSaveButtonView()
                .getButtonModel()
                .setPressed(pressed);
    }

    private void setEditButtonPressed(boolean pressed) {
        levelSelectorView.getEditButtonView().getButtonModel().setPressed(pressed);
    }

    private void setSaveButtonHover(boolean hover) {
        levelEditorView
                .getSaveButtonView()
                .getButtonModel()
                .setHover(hover);
    }

    private void setNextLvlButtonHover(boolean hover) {
        levelSelectorView.getNextLevelButtonView().getButtonModel().setHover(hover);
    }

    private void setPrevLvlButtonHover(boolean hover) {
        levelSelectorView.getPrevLevelButtonView().getButtonModel().setHover(hover);
    }

    private void setEditButtonHover(boolean hover) {
        levelSelectorView.getEditButtonView().getButtonModel().setHover(hover);
    }

    private void setXButtonHover(boolean hover) {
        levelEditorView
                .getXButtonView()
                .getButtonModel()
                .setHover(hover);
    }

    private SaveButtonModel getSaveButtonModel() {
        return levelEditorView.getSaveButtonView().getButtonModel();
    }

    private void eraserButtonClick() {
        setEraserButtonPressed(true);
        setLevelEditorBlockIndex(0);
        setLevelEditorEnemyIndex(0);
        setPlayerButtonPressed(false);
    }

    private void playerButtonClick() {
        setLevelEditorBlockIndex(0);
        setLevelEditorEnemyIndex(0);
        setPlayerButtonPressed(true);
    }

    private Point getPlayerSpawn() {
        return LevelManagerModel
                .getInstance()
                .getLevels()
                .get(levelEditorView.getLevelIndex())
                .getPlayerSpawn();
    }

    private ChangePageButtonModel getNextPageButton() {
        return userStateView.getNextPageButton().getButtonModel();
    }

    private ChangePageButtonModel getPrevPageButton() {
        return userStateView.getPrevPageButton().getButtonModel();
    }

    private boolean isGameWon() {
        return LevelManagerModel.getInstance().isGameWon();
    }

}
