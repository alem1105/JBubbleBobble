package model.utilz;

public interface Fallable {

    void isInAirCheck();

    void fallingChecks(float xSpeed);

    void checkOutOfMap();

    void resetInAir();

    void updateXPos(float xSpeed);
}
