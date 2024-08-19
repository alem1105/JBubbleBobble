package controller.inputs;

import model.LevelManager;
import model.gamestate.Gamestate;
import view.LevelEditorView;
import view.ui.BlockButtonView;

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
                for(BlockButtonView button: levelEditorView.getButtons()) {
                    if (button.getButtonModel().getBounds().contains(e.getX(), e.getY())) {
                        levelEditorView.setBlockIndex(button.getButtonModel().getIndex());
                    }
                }
                // clicca nella griglia del livello
                int currentTileX = (e.getX()) / (TILES_SIZE - levelEditorView.getDrawOffset());
                int currentTileY = (e.getY()) / (TILES_SIZE - levelEditorView.getDrawOffset());
                if (currentTileX < 20 && currentTileY < 18) {
                    int[][] lvlData = levelManager.getLevels().get(levelManager.getLvlIndex()).getLvlData();
                    lvlData[currentTileY][currentTileX] = levelEditorView.getBlockIndex() + 1;
                }

            }
        }
        if (levelEditorView.getSaveButtonView().getButtonModel().getBounds().contains(e.getX(), e.getY()))
            levelEditorView.getSaveButtonView().getButtonModel().setPressed(true);
        if (levelEditorView.getXButtonView().getButtonModel().getBounds().contains(e.getX(), e.getY()))
            levelEditorView.getXButtonView().getButtonModel().setPressed(true);
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
