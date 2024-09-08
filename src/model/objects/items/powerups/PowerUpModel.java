package model.objects.items.powerups;

import model.objects.CustomObjectModel;

/**
 * La classe astratta {@code PowerUpModel} rappresenta un potenziamento nel gioco.
 * Estende {@code CustomObjectModel} e fornisce funzionalità comuni per tutti i power-up,
 * inclusi il conteggio del tempo di durata e il comportamento di attivazione/disattivazione.
 */
public abstract class PowerUpModel extends CustomObjectModel {

    /** Il punteggio assegnato quando il potenziamento viene raccolto. */
    protected int score;

    /** Il tipo di power-up, utilizzato per identificare i diversi effetti. */
    protected int type;

    /** Indica se il power-up è stato raccolto. */
    protected boolean pickedUp;

    /** Conteggio dei tick per il timer di despawn del potenziamento. */
    protected int despawnTick;

    /** Durata massima del power-up in termini di tick prima di sparire automaticamente. */
    protected int despawnTimer = 1200;

    /**
     * Costruttore della classe {@code PowerUpModel}.
     *
     * @param x la coordinata X del potenziamento.
     * @param y la coordinata Y del potenziamento.
     * @param width la larghezza del potenziamento.
     * @param height l'altezza del potenziamento.
     * @param type il tipo di potenziamento.
     * @param score il punteggio assegnato quando il potenziamento viene raccolto.
     */
    public PowerUpModel(float x, float y, int width, int height, int type, int score) {
        super(x, y, width, height);
        this.type = type;
        this.score = score;
    }

    /**
     * Aggiorna lo stato del potenziamento ad ogni tick. Se il numero di tick supera
     * il valore di {@code despawnTimer}, il pwer-up sparisce automaticamente dalla schermata.
     */
    @Override
    public void update() {
        despawnTick++;
        if (despawnTick >= despawnTimer) {
            despawnTick = 0;
            active = false;
        }
    }

    /**
     * Applica l'effetto specifico del potenziamento al giocatore o all'entità.
     * Deve essere implementato dalle sottoclassi per definire l'effetto del potenziamento.
     */
    public abstract void applyEffect();

    /**
     * Rimuove l'effetto del potenziamento dal giocatore o dall'entità.
     * Deve essere implementato dalle sottoclassi per definire il comportamento
     * quando l'effetto del potenziamento termina.
     */
    public abstract void unapplyEffect();

    public int getType() {
        return type;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public int getScore() {
        return score;
    }
}
