package controller.inputs;

import model.ModelManager;
import model.entities.PlayerModel;
import model.gamestate.Gamestate;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static model.gamestate.Gamestate.*;

public class KeyboardInputs implements KeyListener {

    public KeyboardInputs() {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER -> Gamestate.state = PLAYING;
                    case KeyEvent.VK_L -> Gamestate.state = LEVEL_EDITOR;
                }
            }
            case PLAYING -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE -> Gamestate.state = MENU;
                    case KeyEvent.VK_D -> PlayerModel.getInstance().setRight(true);
                    case KeyEvent.VK_A -> PlayerModel.getInstance().setLeft(true);
                    case KeyEvent.VK_SPACE -> PlayerModel.getInstance().setJump(true);
                }
            }
            case LEVEL_EDITOR -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER -> Gamestate.state = MENU;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (Gamestate.state) {
            case PLAYING -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_D -> PlayerModel.getInstance().setRight(false);
                    case KeyEvent.VK_A -> PlayerModel.getInstance().setLeft(false);
                    case KeyEvent.VK_SPACE -> PlayerModel.getInstance().setJump(false);
                }
            }
        }
    }
}
