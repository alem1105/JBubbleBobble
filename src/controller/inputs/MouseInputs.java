package controller.inputs;

import model.LevelManager;
import model.gamestate.Gamestate;
import view.LevelEditorView;
import view.ui.BlockButtonView;
import view.ui.CustomButtonView;
import view.ui.EnemyButtonView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.stream.Stream;

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
            int[][] lvlData = levelManager.getLevels().get(levelManager.getLvlIndex()).getLvlData();
            int[][] enemiesData = levelManager.getLevels().get(levelManager.getLvlIndex()).getEnemiesData();
            lvlData[currentTileY][currentTileX] = levelEditorView.getBlockIndex();

            checkPlayerButtonClick(currentTileX, currentTileY);

            if(levelEditorView.getEnemyIndex() > 0 || levelEditorView.getPlayerButtonView().getButtonModel().isSelected())
                lvlData[currentTileY][currentTileX] = 0;
            enemiesData[currentTileY][currentTileX] = levelEditorView.getEnemyIndex();
        }
    }

    private void checkPlayerButtonClick(int currentTileX, int currentTileY) {
        if(levelEditorView.getPlayerButtonView().getButtonModel().isPressed())
            LevelManager.getInstance()
                    .getLevels()
                    .get(LevelManager.getInstance().getLvlIndex())
                    .setPlayerSpawn(
                            new Point(currentTileX * TILES_SIZE, currentTileY * TILES_SIZE));
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
        levelEditorView.setEnemyIndex(button.getButtonModel().getIndex());
        levelEditorView.getEraserButtonView().getButtonModel().setPressed(false);
        levelEditorView.getPlayerButtonView().getButtonModel().setPressed(false);
        levelEditorView.setBlockIndex(0);
    }

    private void blockButtonClick(BlockButtonView button) {
        levelEditorView.setBlockIndex(button.getButtonModel().getIndex());
        levelEditorView.getEraserButtonView().getButtonModel().setPressed(false);
        levelEditorView.getPlayerButtonView().getButtonModel().setPressed(false);
        levelEditorView.setEnemyIndex(0);
    }

    private <T extends CustomButtonView> boolean isIn(T button, MouseEvent e) {
        if(button.getButtonModel().getBounds().contains(e.getX(), e.getY()))
            return true;
        else
            return false;
    }

    private void checkPressed(MouseEvent e) {
        if (isIn(levelEditorView.getSaveButtonView(), e))
            levelEditorView.getSaveButtonView().getButtonModel().setPressed(true);

        if (isIn(levelEditorView.getXButtonView(), e))
            levelEditorView.getXButtonView().getButtonModel().setPressed(true);

        if (isIn(levelEditorView.getEraserButtonView(), e)) {
            eraserButtonClick();
        }

        if(isIn(levelEditorView.getPlayerButtonView(), e)) {
            playerButtonClick();
        }
    }

    private void eraserButtonClick() {
        levelEditorView.getEraserButtonView().getButtonModel().setPressed(true);
        levelEditorView.setBlockIndex(0);
        levelEditorView.setEnemyIndex(0);
        levelEditorView.getPlayerButtonView().getButtonModel().setPressed(false);
    }

    private void playerButtonClick() {
        levelEditorView.setBlockIndex(0);
        levelEditorView.setEnemyIndex(0);
        levelEditorView.getPlayerButtonView().getButtonModel().setPressed(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state) {
            case LEVEL_EDITOR -> {
                levelEditorView.getXButtonView().getButtonModel().setPressed(false);
                levelEditorView.getSaveButtonView().getButtonModel().setPressed(false);
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
                    levelEditorView.getSaveButtonView().getButtonModel().setHover(true);
                }
                else {
                    levelEditorView.getSaveButtonView().getButtonModel().setHover(false);
                }
                if (levelEditorView.getXButtonView().getButtonModel().getBounds().contains(e.getX(), e.getY())) {
                    levelEditorView.getXButtonView().getButtonModel().setHover(true);
                } else {
                    levelEditorView.getXButtonView().getButtonModel().setHover(false);
                }
            }
        }
    }
}
