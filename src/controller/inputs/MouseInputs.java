package controller.inputs;

import model.LevelManager;
import model.gamestate.Gamestate;
import model.ui.PlayerButtonModel;
import view.stateview.LevelEditorView;
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

    public MouseInputs(){
        this.levelEditorView = LevelEditorView.getInstance();
        this.levelManager = LevelManager.getInstance();
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
        }
    }

    private void checkEditedTiles(MouseEvent e) {
        int currentTileX = (e.getX()) / (TILES_SIZE - levelEditorView.getDrawOffset());
        int currentTileY = (e.getY()) / (TILES_SIZE - levelEditorView.getDrawOffset());
        if (currentTileX < 20 && currentTileY < 18) {
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
        if (isIn(levelEditorView.getSaveButtonView(), e))
            setSaveButtonPressed(true);

        if (isIn(levelEditorView.getXButtonView(), e))
            setXButtonPressed(true);

        if (isIn(levelEditorView.getEraserButtonView(), e)) {
            eraserButtonClick();
        }

        if(isIn(levelEditorView.getPlayerButtonView(), e)) {
            playerButtonClick();
        }
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

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state) {
            case LEVEL_EDITOR -> {
                setXButtonPressed(false);
                setSaveButtonPressed(false);
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
                if (levelEditorView.getSaveButtonView().getButtonModel().getBounds().contains(e.getX(), e.getY())) {
                    setSaveButtonHover(true);
                }
                else {
                    setSaveButtonHover(false);
                }
                if (levelEditorView.getXButtonView().getButtonModel().getBounds().contains(e.getX(), e.getY())) {
                    setXButtonHover(true);
                } else {
                    setXButtonHover(false);
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

    private void setXButtonHover(boolean hover) {
        levelEditorView
                .getXButtonView()
                .getButtonModel()
                .setHover(hover);
    }

}
