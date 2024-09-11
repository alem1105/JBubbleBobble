package controller.inputs;

import model.level.LevelManagerModel;
import model.entities.PlayerModel;
import model.gamestate.*;
import model.utilz.UtilityMethods;
import view.entities.enemies.EnemiesManagerView;
import view.stateview.UserStateView;
import view.ui.GameWonScreenView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static model.gamestate.Gamestate.*;

/**
 * La classe {@code KeyboardInputs} implementa l'interfaccia {@code KeyListener} per gestire
 * l'input della tastiera nei vari stati di gioco. Definisce il comportamento per la digitazione,
 * pressione e rilascio dei tasti in base allo stato attuale del gioco.
 */
public class KeyboardInputs implements KeyListener {

    /**
     * Costruisce un nuovo oggetto {@code KeyboardInputs}.
     * Questo costruttore non inizializza alcun campo.
     */
    public KeyboardInputs() {}

    /**
     * Gestisce l'evento di digitazione di un tasto (premuto e rilasciato).
     *
     * @param e il {@code KeyEvent} che rappresenta il tasto digitato
     *
     * <ul>
     *   <li>Se lo stato del gioco è {@code USER}, gestisce l'input per inserire il nickname dell'utente.
     *   Il nickname può essere composto solo da lettere e ha un limite di 8 caratteri.</li>
     *   <li>Se lo stato del gioco è {@code MENU} e il gioco è stato vinto, reimposta il gioco
     *   dopo che è trascorso un certo periodo di tempo.</li>
     * </ul>
     */
    @Override
    public void keyTyped(KeyEvent e) {
        switch (state) {
            case USER -> {
                if (UserStateView.getInstance().isWritingNickname()) {
                    String inputNickname = UserStateView.getInstance().getCurrentUser().getNickname();
                    if (Character.isLetter(e.getKeyChar()) && inputNickname.length() <= 8) {
                        UserStateView.getInstance().getCurrentUser().setNickname(inputNickname + e.getKeyChar());
                    } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && !inputNickname.isEmpty()) {
                        UserStateView.getInstance().getCurrentUser().setNickname(inputNickname.substring(0, inputNickname.length() - 1));
                    }
                }
            }
            case MENU -> {
                if (LevelManagerModel.getInstance().isGameWon() && GameWonScreenView.getInstance().getDurationTick() >= 2000) {
                    LevelManagerModel.getInstance().setGameWon(false);
                    UtilityMethods.resetAll();
                    EnemiesManagerView.getInstance().setRestart(true);
                }
            }
        }
    }

    /**
     * Gestisce l'evento di pressione di un tasto.
     *
     * @param e il {@code KeyEvent} che rappresenta il tasto premuto
     *
     * <ul>
     *   <li>Se lo stato del gioco è {@code MENU}, gestisce la pressione del tasto INVIO per iniziare a giocare
     *   se si sta inserendo il nickname dell'utente.</li>
     *   <li>Se lo stato del gioco è {@code USER}, gestisce il salvataggio del nickname o l'uscita dalla leaderboard.</li>
     *   <li>Se lo stato del gioco è {@code PLAYING}, gestisce i movimenti del giocatore e altre azioni come il salto
     *   e il caricamento del prossimo livello.</li>
     * </ul>
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER -> {
                        if (UserStateView.getInstance().isWritingNickname()) {
                            Gamestate.state = PLAYING;
                            UserStateModel.getInstance().getCurrentUserModel().setTempScore(0);
                        }
                    }
                }
            }
            case USER -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER -> {
                        UserStateModel.getInstance().setCurrentUserModel(UserStateView.getInstance().getCurrentUser());
                        Gamestate.state = MENU;
                    }
                    case KeyEvent.VK_L -> {
                        if (!UserStateView.getInstance().isCreateUser())
                            UserStateView.getInstance().setShowingLeaderboard(true);
                    }
                    case KeyEvent.VK_ESCAPE -> UserStateView.getInstance().setShowingLeaderboard(false);
                }
            }
            case PLAYING -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE -> PlayingModel.getInstance().invertPaused();
                    case KeyEvent.VK_D -> PlayerModel.getInstance().setRight(true);
                    case KeyEvent.VK_A -> PlayerModel.getInstance().setLeft(true);
                    case KeyEvent.VK_SPACE -> PlayerModel.getInstance().setJump(true);
                    case KeyEvent.VK_N -> LevelManagerModel.getInstance().loadNextLevel();
                }
            }
            case LEVEL_EDITOR -> {
                // Azioni per lo stato "LEVEL_EDITOR" possono essere aggiunte qui
            }
        }
    }

    /**
     * Gestisce l'evento di rilascio di un tasto.
     *
     * @param e il {@code KeyEvent} che rappresenta il tasto rilasciato
     *
     * <ul>
     *   <li>Se lo stato del gioco è {@code PLAYING}, interrompe i movimenti del giocatore quando i tasti corrispondenti vengono rilasciati.</li>
     * </ul>
     */
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

