package controller;

import controller.inputs.MouseInputs;
import model.ModelManager;
import view.GamePanel;
import view.GameWindow;
import controller.inputs.KeyboardInputs;

public class GameController implements Runnable {
    private final int UPS_SET = 120;

    private GamePanel gamePanel;
    private GameWindow gameWindow;

    private Thread gameThread;

    private ModelManager modelManager;

    private MouseInputs mouseInputs;

    public GameController() {
        initClasses();
        startGameLoop();
    }

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

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void update() {
        modelManager.update();
    }

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
