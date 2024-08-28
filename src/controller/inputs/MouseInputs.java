package controller.inputs;

import model.LevelManagerModel;
import model.entities.PlayerModel;
import model.gamestate.Gamestate;
import model.gamestate.UserStateModel;
import model.ui.buttons.*;
import view.stateview.*;
import view.ui.DeathScreenView;
import view.ui.buttons.BlockButtonView;
import view.ui.buttons.CustomButtonView;
import view.ui.buttons.EnemyButtonView;
import view.ui.buttons.UserButtonView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static model.utilz.Constants.GameConstants.TILES_IN_HEIGHT;
import static model.utilz.Constants.GameConstants.TILES_SIZE;


public class MouseInputs implements MouseMotionListener, MouseListener {

    private LevelEditorView levelEditorView;
    private LevelManagerModel levelManagerModel;
    private LevelSelectorView levelSelectorView;
    private PlayingView playingView;
    private DeathScreenView deathScreenView;
    private MenuView menuView;
    private boolean justChangedScreen;

    public MouseInputs(){
        this.levelEditorView = LevelEditorView.getInstance();
        this.levelManagerModel = LevelManagerModel.getInstance();
        this.levelSelectorView = LevelSelectorView.getInstance();
        this.playingView = new PlayingView();
        this.deathScreenView = DeathScreenView.getInstance();
        this.menuView = MenuView.getInstance();
    }

    private <T extends CustomButtonView> boolean isIn(T button, MouseEvent e) {
        if(button.getButtonModel().getBounds().contains(e.getX(), e.getY()))
            return true;
        else
            return false;
    }

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

    @Override
    public void mousePressed(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU -> {
                if (isIn(menuView.getStartButton(), e)){
                    getStartButton().setPressed(true);
                }
                if (isIn(menuView.getEditorButton(), e)){
                    getEditorButton().setPressed(true);
                }
            }
            case PLAYING -> {
                if(PlayerModel.getInstance().isGameOver()){
                    if(isIn(deathScreenView.getQuitButtonView(), e))
                        getQuitButton().setPressed(true);
                    if(isIn(deathScreenView.getRestartButtonView(), e))
                            getRestartButton().setPressed(true);
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

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU -> {
                if (isIn(menuView.getStartButton(), e)){
                    Gamestate.state = Gamestate.PLAYING;
                    justChangedScreen = true;
                }
                if (isIn(menuView.getEditorButton(), e)){
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
                        }
                    }
                }
                else {
                    PlayerModel.getInstance().setAttack(false);
                }

            }
            case LEVEL_EDITOR -> {
                setXButtonPressed(false);
                setSaveButtonPressed(false);
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

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU -> {
                if (isIn(menuView.getStartButton(), e)){
                    getStartButton().setHover(true);
                } else {
                    getStartButton().setHover(false);
                }
                if (isIn(menuView.getEditorButton(), e)){
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

            if(levelEditorView.getEnemyIndex() > 0 || isPlayerButtonSelected())
                lvlData[currentTileY][currentTileX] = 0;
            enemiesData[currentTileY][currentTileX] = levelEditorView.getEnemyIndex();
        }
    }

    private boolean checkRoofAndBottomTile(int currentTileY) {
        return (currentTileY == TILES_IN_HEIGHT - 2 || currentTileY == 0) && (isEraserButtonPressed() || isPlayerButtonPressed());
    }

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

    private void editorCheckClicks(MouseEvent e) {
        // TODO ABBIAMO USATO GLI STREAM QUI
        CustomButtonView[] allButtons = Stream
                .concat(Arrays.stream(UserStateView.getInstance().getUserButtons()),
                        Stream.concat(Arrays.stream(levelEditorView.getButtons()),
                        Arrays.stream(levelEditorView.getEnemies())))
                .toArray(CustomButtonView[]::new);

        for(CustomButtonView button : allButtons){
            if(isIn(button, e)) {
                if(button instanceof BlockButtonView)
                    blockButtonClick((BlockButtonView) button);
                else if(button instanceof EnemyButtonView)
                    enemyButtonClick((EnemyButtonView) button);
                else if(button instanceof UserButtonView)
                    userButtonClick((UserButtonView) button);
            }
        }
    }

    private void editorCheckPressed(MouseEvent e) {
        if (isIn(levelEditorView.getSaveButtonView(), e)) {
            setSaveButtonPressed(true);
            getSaveButtonModel().saveNewLevelImage(getLevelData(), getEnemiesData(), getPlayerSpawn(), levelEditorView.getLevelIndex());
            Gamestate.state = Gamestate.MENU;
        }

        if (isIn(levelEditorView.getXButtonView(), e)) {
            setXButtonPressed(true);
            levelEditorView.getXButtonView().getButtonModel().isClicked();
            Gamestate.state = Gamestate.MENU;
            setXButtonPressed(false);
        }

        if (isIn(levelEditorView.getEraserButtonView(), e)) {
            eraserButtonClick();
        }

        if(isIn(levelEditorView.getPlayerButtonView(), e)) {
            playerButtonClick();
        }

    }

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

    private void userButtonClick(UserButtonView button) {
        UserStateModel.getInstance().setCurrentUserModel(button.getButtonModel().getUserModel());
        button.getButtonModel().setPressed(true);
    }

    // METODI HELPER

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

    private boolean isPlayerButtonSelected() {
        return levelEditorView
                .getPlayerButtonView()
                .getButtonModel()
                .isSelected();
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

    private PlayerButtonModel getPlayerButtonModel() {
        return levelEditorView
                .getPlayerButtonView()
                .getButtonModel();
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
}
