package model.utilz;

import java.awt.geom.Rectangle2D;

import static model.utilz.Constants.GameConstants.GAME_HEIGHT;
import static model.utilz.Constants.GameConstants.TILES_SIZE;

/**
 * Classe che gestisce la gravità e le collisioni con il livello.
 * Fornisce metodi per determinare se un'entità può muoversi in una posizione,
 * controllare la solidità dei blocchi e calcolare le posizioni delle entità
 * in relazione a muri e pavimenti.
 */
public class Gravity {

    /**
     * Determina se un'entità può muoversi in una posizione specifica.
     *
     * @param x      Coordinata x dell'entità.
     * @param y      Coordinata y dell'entità.
     * @param width  Larghezza dell'entità.
     * @param height Altezza dell'entità.
     * @param lvlData Dati del livello, rappresentati come una matrice di interi.
     * @return true se l'entità può muoversi, false altrimenti.
     */
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!isSolid(x, y, lvlData))
            if (!isSolid(x + width, y + height, lvlData))
                if (!isSolid(x + width, y, lvlData))
                    if (!isSolid(x, y + height, lvlData))
                        return true;
        return false;
    }

    /**
     * Controlla se una posizione specifica è solida nel livello.
     *
     * @param x      Coordinata x della posizione da controllare.
     * @param y      Coordinata y della posizione da controllare.
     * @param lvlData Dati del livello, rappresentati come una matrice di interi.
     * @return true se la posizione è solida, false altrimenti.
     */
    private static boolean isSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= GAME_HEIGHT)
            return true;
        float xIndex = x / TILES_SIZE;
        float yIndex = y / TILES_SIZE;

        return isTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    /**
     * Controlla se un blocco specifico è solido.
     *
     * @param xTile  Indice x del blocco.
     * @param yTile  Indice y del blocco.
     * @param lvlData Dati del livello, rappresentati come una matrice di interi.
     * @return true se il blocco è solido, false altrimenti.
     */
    public static boolean isTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];

        if (value == 0 || value == 255)
            return false;
        return true;
    }

    /**
     * Calcola la posizione x di un'entità accanto a un muro.
     *
     * @param hitbox  Hitbox dell'entità come rettangolo.
     * @param xSpeed  Velocità in direzione x dell'entità.
     * @return La nuova posizione x dell'entità accanto al muro.
     */
    public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / TILES_SIZE);
        if (xSpeed > 0) {
            // A destra
            int tileXPos = currentTile * TILES_SIZE;
            int xOffset = (int) (TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else
            // A sinistra
            return currentTile * TILES_SIZE;
    }

    /**
     * Calcola la posizione y di un'entità sotto un tetto o sopra un pavimento.
     *
     * @param hitbox  Hitbox dell'entità come rettangolo.
     * @param airSpeed Velocità in direzione y dell'entità.
     * @return La nuova posizione y dell'entità sotto un tetto o sopra un pavimento.
     */
    public static float getEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / TILES_SIZE);
        if (airSpeed > 0) {
            // Cadendo - toccando il pavimento
            int tileYPos = currentTile * TILES_SIZE;
            int yOffset = (int) (TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else
            // Saltando
            return currentTile * TILES_SIZE;
    }

    /**
     * Controlla se un'entità si trova sul pavimento.
     *
     * @param hitbox  Hitbox dell'entità come rettangolo.
     * @param lvlData Dati del livello, rappresentati come una matrice di interi.
     * @return true se l'entità si trova sul pavimento, false altrimenti.
     */
    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Controlla il pixel sotto il bordo inferiore sinistro e destro
        if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;

        return true;
    }

}

