package controller;

import controller.inputs.MouseInputs;
import model.ModelManager;
import view.GamePanel;
import view.GameWindow;
import controller.inputs.KeyboardInputs;

/**
 * La classe GameController gestisce il ciclo principale del gioco e coordina
 * il modello, la vista e la gestione degli input. È responsabile
 * dell'inizializzazione dei componenti chiave del gioco, come il model manager,
 * il pannello di gioco e i listener per gli input, e avvia il ciclo del gioco
 * che ne aggiorna continuamente lo stato.
 */
public class GameController implements Runnable {

    /** Il numero di aggiornamenti per secondo (UPS). */
    private final int UPS_SET = 120;

    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private Thread gameThread;

    /** Il manager del modello che gestisce lo stato e i dati del gioco. */
    private ModelManager modelManager;

    /** Il gestore degli input per gli eventi del mouse. */
    private MouseInputs mouseInputs;

    /**
     * Costruttore della classe GameController. Inizializza i componenti chiave
     * del gioco e avvia il ciclo del gioco.
     * Non prende parametri e non restituisce alcun valore.
     */
    public GameController() {
        initClasses();
        startGameLoop();
    }

    /**
     * Inizializza i componenti chiave del gioco come il model manager,
     * il pannello di gioco, la finestra e i listener per gli input.
     * Inoltre, registra il pannello di gioco come osservatore del model manager,
     * assicurando che la vista venga aggiornata ogni volta che il modello cambia.
     *
     * Non prende parametri e non restituisce alcun valore.
     */
    private void initClasses() {
        modelManager = ModelManager.getInstance();
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.addKeyListener(new KeyboardInputs());
        mouseInputs = new MouseInputs();
        gamePanel.addMouseListener(mouseInputs);
        gamePanel.addMouseMotionListener(mouseInputs);
        modelManager.addObserver(gamePanel);
    }

    /**
     * Avvia il ciclo di gioco creando un nuovo thread e avviandolo. Il ciclo
     * di gioco aggiornerà lo stato del gioco a una frequenza fissa definita da
     * {@code UPS_SET}.
     *
     * Non prende parametri e non restituisce alcun valore.
     */
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Aggiorna il modello del gioco invocando il metodo {@code update()}
     * sul model manager. Questo metodo viene chiamato continuamente dal ciclo di gioco.
     *
     * Non prende parametri e non restituisce alcun valore.
     */
    private void update() {
        modelManager.update();
    }

    /**
     * Regola la frequenza degli aggiornamenti dello stato del gioco in base
     * al valore target di UPS (aggiornamenti per secondo) definito in {@code UPS_SET}.
     * Il ciclo calcola il tempo trascorso tra ogni aggiornamento e chiama il metodo
     * {@code update()} quando necessario.
     *
     * Implementa l'interfaccia {@code Runnable} e viene eseguito in un thread separato.
     */
    @Override
    public void run() {

        double timePerUpdate = 1000000000.0 / UPS_SET;
        long previousTime = System.nanoTime();

        double deltaU = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                deltaU--;
            }
        }
    }
}
