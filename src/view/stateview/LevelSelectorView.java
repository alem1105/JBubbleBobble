package view.stateview;

import model.ui.buttons.ChangePageButtonModel;
import model.ui.buttons.EditButtonModel;
import view.ui.buttons.ChangePageButtonView;
import view.ui.buttons.EditButtonView;
import view.utilz.AudioManager;
import view.utilz.LoadSave;

import static model.utilz.Constants.Directions.*;
import static model.utilz.Constants.GameConstants.*;
import static view.utilz.AudioManager.LEVEL_EDITOR_INDEX;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Rappresenta la vista del selettore di livelli nel Level Editor.
 * Questa classe gestisce la visualizzazione e l'interazione con i livelli,
 * inclusi i pulsanti per navigare tra i livelli e modificare il livello attuale.
 */
public class LevelSelectorView {
    private static LevelSelectorView instance;

    private LevelEditorView levelEditorView;
    private ChangePageButtonView nextLevelButtonView;
    private ChangePageButtonView prevLevelButtonView;
    private EditButtonView editButtonView;

    /**
     * Restituisce l'istanza singleton di LevelSelectorView.
     *
     * @return L'istanza singleton di LevelSelectorView.
     */
    public static LevelSelectorView getInstance() {
        if (instance == null) {
            instance = new LevelSelectorView();
        }
        return instance;
    }

    /**
     * Costruttore privato per inizializzare il selettore di livelli.
     */
    private LevelSelectorView() {
        levelEditorView = LevelEditorView.getInstance();
        initButtons();
    }

    /**
     * Inizializza i pulsanti per navigare tra i livelli e modificare il livello attuale.
     */
    private void initButtons() {
        nextLevelButtonView = new ChangePageButtonView(new ChangePageButtonModel(
                levelEditorView.getLevelWidth() + ((GAME_WIDTH - levelEditorView.getLevelWidth()) / 2) + (int) (2 * SCALE),
                (int) (85 * SCALE),
                (int) (18 * SCALE),
                (int) (18 * SCALE),
                RIGHT));

        prevLevelButtonView = new ChangePageButtonView(new ChangePageButtonModel(
                levelEditorView.getLevelWidth() + ((GAME_WIDTH - levelEditorView.getLevelWidth()) / 2) - (int) (20 * SCALE),
                (int) (85 * SCALE),
                (int) (18 * SCALE),
                (int) (18 * SCALE),
                LEFT));

        editButtonView = new EditButtonView(new EditButtonModel(
                (levelEditorView.getLevelWidth() / 2) - (int) (52.5 * SCALE),
                levelEditorView.getLevelHeight() + ((GAME_HEIGHT - levelEditorView.getLevelHeight()) / 2) - (int) (30 * SCALE),
                (int) (105 * SCALE),
                (int) (42 * SCALE)
        ));
    }

    /**
     * Disegna il selettore di livelli e il livello attuale.
     *
     * @param g
     */
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        levelEditorView.drawTilesAndEnemies(g);
        levelEditorView.drawPlayer(g);
        nextLevelButtonView.draw(g);
        prevLevelButtonView.draw(g);
        editButtonView.draw(g);
        drawStrings(g);
    }

    /**
     * Disegna le stringhe che indicano il titolo del livello e l'indice del livello attuale.
     *
     * @param g
     */
    private void drawStrings(Graphics g) {
        Font font = (LoadSave.NES_FONT).deriveFont(12 * SCALE);
        g.setFont(font);

        // Scritta "LEVEL:"
        g.setColor(new Color(242, 70, 152));
        FontMetrics metrics = g.getFontMetrics(font); // Ottengo le misure della scritta per centrarla
        int offset = levelEditorView.getLevelWidth();
        int distance = (GAME_WIDTH - offset) / 2;
        int yLevel = (int) (40 * SCALE);
        g.drawString("LEVEL:", offset + distance - (metrics.stringWidth("LEVEL:") / 2), yLevel);

        // Scritta dell'indice del livello
        g.setColor(new Color(254, 238, 31));
        String lvl = "" + (levelEditorView.getLevelIndex() + 1);
        g.drawString(lvl, offset + distance - (metrics.stringWidth(lvl) / 2), yLevel + (int) (30 * SCALE));
    }

    /**
     * Aggiorna lo stato dei pulsanti nel selettore di livelli.
     */
    public void update() {
        AudioManager.getInstance().continuousSoundPlay(LEVEL_EDITOR_INDEX);
        nextLevelButtonView.update();
        prevLevelButtonView.update();
        editButtonView.update();
    }

    public ChangePageButtonView getNextLevelButtonView() {
        return nextLevelButtonView;
    }

    public ChangePageButtonView getPrevLevelButtonView() {
        return prevLevelButtonView;
    }

    public EditButtonView getEditButtonView() {
        return editButtonView;
    }
}
