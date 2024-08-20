package controller.inputs;

import model.LevelManager;
import model.gamestate.Gamestate;
import view.LevelEditorView;
import view.ui.BlockButtonView;
import view.ui.EnemyButtonView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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
        // if (bounds.contains(e.getX(), e.getY())
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
            if(levelEditorView.getEnemyIndex() > 0)
                lvlData[currentTileY][currentTileX] = 0;
            enemiesData[currentTileY][currentTileX] = levelEditorView.getEnemyIndex();
        }
    }

    private void checkClicks(MouseEvent e) {
        for (BlockButtonView button : levelEditorView.getButtons()) {
            if (button.getButtonModel().getBounds().contains(e.getX(), e.getY())) {
                levelEditorView.setBlockIndex(button.getButtonModel().getIndex());
                levelEditorView.getEraserButtonView().getButtonModel().setPressed(false);
                levelEditorView.setEnemyIndex(0);
            }
        }
        for (EnemyButtonView button : levelEditorView.getEnemies()) {
            if (button.getButtonModel().getBounds().contains(e.getX(), e.getY())) {
                levelEditorView.setEnemyIndex(button.getButtonModel().getIndex());
                levelEditorView.getEraserButtonView().getButtonModel().setPressed(false);
                levelEditorView.setBlockIndex(0);
            }
        }
    }

    private void checkPressed(MouseEvent e) {
        if (levelEditorView.getSaveButtonView().getButtonModel().getBounds().contains(e.getX(), e.getY()))
            levelEditorView.getSaveButtonView().getButtonModel().setPressed(true);
        if (levelEditorView.getXButtonView().getButtonModel().getBounds().contains(e.getX(), e.getY()))
            levelEditorView.getXButtonView().getButtonModel().setPressed(true);
        if (levelEditorView.getEraserButtonView().getButtonModel().getBounds().contains(e.getX(), e.getY())) {
            levelEditorView.getEraserButtonView().getButtonModel().setPressed(true);
            levelEditorView.setBlockIndex(0);
            levelEditorView.setEnemyIndex(0);
        }
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
