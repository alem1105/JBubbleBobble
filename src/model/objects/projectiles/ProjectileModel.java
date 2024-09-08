package model.objects.projectiles;

import model.objects.CustomObjectModel;

/**
 * Classe astratta che rappresenta un proiettile nel gioco.
 * Questa classe estende CustomObjectModel e fornisce funzionalità comuni per tutti i proiettili.
 */
public abstract class ProjectileModel extends CustomObjectModel {

    /**
     * Direzione del proiettile.
     */
    protected int direction;

    /**
     * Costruttore per inizializzare un proiettile con le coordinate e le dimensioni specificate.
     *
     * @param x         Coordinata X iniziale del proiettile.
     * @param y         Coordinata Y iniziale del proiettile.
     * @param width     Larghezza del proiettile.
     * @param height    Altezza del proiettile.
     * @param direction Direzione in cui il proiettile si muove.
     */
    public ProjectileModel(float x, float y, int width, int height, int direction) {
        super(x, y, width, height);
        this.direction = direction;
    }

    /**
     * Metodo astratto che deve essere implementato dalle sottoclassi per aggiornare lo stato del proiettile.
     */
    @Override
    public abstract void update();

    public int getDirection() {
        return direction;
    }
}

