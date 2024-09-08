package view.ui;

import model.ui.buttons.QuitButtonModel;
import model.ui.buttons.RestartButtonModel;
import view.ui.buttons.QuitButtonView;
import view.ui.buttons.RestartButtonView;
import view.utilz.LoadSave;

import java.awt.*;

import static java.awt.Color.*;
import static model.utilz.Constants.GameConstants.*;

/**
 * La classe DeathScreenView gestisce la visualizzazione della schermata di "Game Over".
 * Include i pulsanti per uscire dal gioco e per riavviare la partita.
 */
public class DeathScreenView {

    /**
     * Il pulsante per uscire dal gioco.
     */
    private QuitButtonView quitButton;

    /**
     * Il pulsante per riavviare il gioco.
     */
    private RestartButtonView restartButton;

    /**
     * Istanza singleton della DeathScreenView.
     */
    private static DeathScreenView instance;

    /**
     * Ritorna l'istanza singleton della DeathScreenView.
     * Se non è stata creata, ne crea una nuova.
     *
     * @return L'istanza della DeathScreenView.
     */
    public static DeathScreenView getInstance() {
        if (instance == null) {
            instance = new DeathScreenView();
        }
        return instance;
    }

    /**
     * Costruttore privato della DeathScreenView.
     * Inizializza i pulsanti della schermata.
     */
    private DeathScreenView() {
        initButtons();
    }

    /**
     * Inizializza i pulsanti della schermata Game Over.
     */
    private void initButtons() {
        quitButton = new QuitButtonView(new QuitButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE), (int) (147 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)), false);
        restartButton = new RestartButtonView(new RestartButtonModel(GAME_WIDTH / 2 - (int) (47 * SCALE), (int) (185 * SCALE), (int) (94 * SCALE), (int) (28 * SCALE)));
    }

    /**
     * Aggiorna lo stato dei pulsanti della schermata Game Over.
     */
    public void update() {
        quitButton.update();
        restartButton.update();
    }

    /**
     * Disegna la schermata di "Game Over" con i pulsanti.
     *
     * @param g
     */
    public void render(Graphics g) {
        Color myColour = new Color(0, 0, 0, 230);
        g.setColor(myColour);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        drawString(g);
        quitButton.draw(g);
        restartButton.draw(g);
    }

    /**
     * Disegna la stringa "GAME OVER" al centro della schermata.
     *
     * @param g
     */
    private void drawString(Graphics g) {
        // Imposta il font per il testo "GAME OVER".
        Font font = (LoadSave.NES_FONT).deriveFont(30 * SCALE);
        g.setColor(GREEN);
        g.setFont(font);

        // Misura il testo per centrarlo orizzontalmente.
        FontMetrics misure = g.getFontMetrics(font);
        g.drawString("GAME OVER", GAME_WIDTH / 2 - (misure.stringWidth("GAME OVER") / 2), (int) (130 * SCALE));
    }

    public QuitButtonView getQuitButtonView() {
        return quitButton;
    }

    public RestartButtonView getRestartButtonView() {
        return restartButton;
    }
}
