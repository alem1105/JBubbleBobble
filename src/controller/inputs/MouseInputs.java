package controller.inputs;

import model.LevelManager;
import model.gamestate.Gamestate;
import model.ui.PlayerButtonModel;
import model.ui.SaveButtonModel;
import model.ui.XButtonModel;
import view.stateview.LevelEditorView;
import view.stateview.LevelSelectorView;
import view.ui.BlockButtonView;
import view.ui.CustomButtonView;
import view.ui.EnemyButtonView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.stream.Stream;

import static model.utilz.Constants.GameConstants.TILES_IN_HEIGHT;
import static model.utilz.Constants.GameConstants.TILES_SIZE;


public class MouseInputs implements MouseMotionListener, MouseListener {

    private LevelEditorView levelEditorView;
    private LevelManager levelManager;
    private LevelSelectorView levelSelectorView;

    public MouseInputs(){
        this.levelEditorView = LevelEditorView.getInstance();
        this.levelManager = LevelManager.getInstance();
        this.levelSelectorView = LevelSelectorView.getInstance();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (Gamestate.state) {
            case LEVEL_EDITOR -> {
                checkClicks(e);
                checkEditedTiles(e);
                checkPressed(e);
            }

            case LEVEL_SELECTOR -> {
                if (isIn(levelSelectorView.getNextLevelButtonView(), e)){
                    setNextLvlButtonPressed(true);
                    if (levelEditorView.getLevelIndex() == levelManager.getLevels().size() - 1)
                        levelEditorView.setLevelIndex(0);
                    else{
                        levelEditorView.setLevelIndex(levelEditorView.getLevelIndex() + 1);
                    }

                }

                if (isIn(levelSelectorView.getPrevLevelButtonView(), e)){
                    setPrevLvlButtonPressed(true);
                    if (levelEditorView.getLevelIndex() == 0)
                        levelEditorView.setLevelIndex(levelManager.getLevels().size() - 1);
                    else{
                        levelEditorView.setLevelIndex(levelEditorView.getLevelIndex() - 1);
                    }

                }

                if(isIn(levelSelectorView.getEditButtonView(), e)) {
                    Gamestate.state = Gamestate.LEVEL_EDITOR;
                }
            }
        }
    }

    private void checkEditedTiles(MouseEvent e) {
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
            LevelManager.getInstance()
                    .getLevels()
                    .get(LevelManager.getInstance().getLvlIndex())
                    .setPlayerSpawn(
                            new Point(currentTileX * TILES_SIZE, currentTileY * TILES_SIZE));
        }
    }

    private Point getPlayerSpawn() {
        return LevelManager
                .getInstance()
                .getLevels()
                .get(LevelManager.getInstance().getLvlIndex())
                .getPlayerSpawn();
    }

    private void checkClicks(MouseEvent e) {
        // TODO ABBIAMO USATO GLI STREAM QUI
        CustomButtonView[] allButtons = Stream.concat(
                        Arrays.stream(levelEditorView.getButtons()),
                        Arrays.stream(levelEditorView.getEnemies()))
                .toArray(CustomButtonView[]::new);

        for(CustomButtonView button : allButtons){
            if(isIn(button, e)) {
                if(button.getClass().equals(BlockButtonView.class))
                    blockButtonClick((BlockButtonView) button);
                else if(button.getClass().equals(EnemyButtonView.class))
                    enemyButtonClick((EnemyButtonView) button);
            }
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

    private <T extends CustomButtonView> boolean isIn(T button, MouseEvent e) {
        if(button.getButtonModel().getBounds().contains(e.getX(), e.getY()))
            return true;
        else
            return false;
    }

    private void checkPressed(MouseEvent e) {
        if (isIn(levelEditorView.getSaveButtonView(), e)) {
            setSaveButtonPressed(true);
            getSaveButtonModel().saveNewLevelImage(getLevelData(), getEnemiesData(), getPlayerSpawn(), 1);
            Gamestate.state = Gamestate.MENU;
        }

        if (isIn(levelEditorView.getXButtonView(), e)) {
            setXButtonPressed(true);
            levelEditorView.getXButtonView().getButtonModel().isClicked();
            Gamestate.state = Gamestate.MENU;
        }

        if (isIn(levelEditorView.getEraserButtonView(), e)) {
            eraserButtonClick();
        }

        if(isIn(levelEditorView.getPlayerButtonView(), e)) {
            playerButtonClick();
        }

    }



    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state) {
            case LEVEL_EDITOR -> {
                setXButtonPressed(false);
                setSaveButtonPressed(false);
            }

            case LEVEL_SELECTOR -> {
                setNextLvlButtonPressed(false);
                setPrevLvlButtonPressed(false);
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
                }
                else{
                    setNextLvlButtonHover(false);
                }

                if (isIn(levelSelectorView.getPrevLevelButtonView(), e)){
                    setPrevLvlButtonHover(true);
                }
                else{
                    setPrevLvlButtonHover(false);
                }
            }
        }
    }

    // METODI HELPER

    private int[][] getLevelData() {
        return levelManager
                .getLevels()
                .get(levelManager.getLvlIndex())
                .getLvlData();
    }

    private int[][] getEnemiesData() {
        return levelManager
                .getLevels()
                .get(levelManager.getLvlIndex())
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
}
