package controller.inputs;

import model.LevelManagerModel;
import model.entities.PlayerModel;
import model.gamestate.Gamestate;
import model.gamestate.UserStateModel;
import view.stateview.UserStateView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static model.gamestate.Gamestate.*;

public class KeyboardInputs implements KeyListener {

    public KeyboardInputs() {}

    @Override
    public void keyTyped(KeyEvent e) {
        switch (state) {
            case USER -> {
                if(UserStateView.getInstance().isWritingNickname()) {
                    String inputNickname = UserStateView.getInstance().getCurrentUser().getNickname();
                    if(Character.isLetter(e.getKeyChar()) && inputNickname.length() <= 8) {
                        UserStateView.getInstance().getCurrentUser().setNickname(inputNickname + e.getKeyChar());
                    } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && !inputNickname.isEmpty()) {
                        UserStateView.getInstance().getCurrentUser().setNickname(inputNickname.substring(0, inputNickname.length() - 1));
                    }
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER ->  {
                        Gamestate.state = PLAYING;
                    }
                }
            }
            case USER -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER -> {
                        UserStateModel.getInstance().setCurrentUserModel(UserStateView.getInstance().getCurrentUser());
                        Gamestate.state = MENU;
                    }
                }
            }
            case PLAYING -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE -> Gamestate.state = MENU;
                    case KeyEvent.VK_D -> PlayerModel.getInstance().setRight(true);
                    case KeyEvent.VK_A -> PlayerModel.getInstance().setLeft(true);
                    case KeyEvent.VK_SPACE -> PlayerModel.getInstance().setJump(true);
                    case KeyEvent.VK_N -> LevelManagerModel.getInstance().loadNextLevel();
                }
            }
            case LEVEL_EDITOR -> {
                switch (e.getKeyCode()) {
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
