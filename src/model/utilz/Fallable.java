package model.utilz;

/**
 * Interfaccia utilizzata per gestire gli oggetti che subiscono l'effetto della gravita'
 */
public interface Fallable {

    void isInAirCheck();

    void fallingChecks(float xSpeed);

    void checkOutOfMap();

    void resetInAir();

    void updateXPos(float xSpeed);
}
