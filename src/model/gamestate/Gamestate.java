package model.gamestate;

/**
 * L'enum {@code Gamestate} rappresenta i diversi stati in cui il gioco può trovarsi.
 * Ogni stato corrisponde a una fase specifica dell'applicazione, come il gioco attivo,
 * il menu principale o l'editor di livelli.
 */
public enum Gamestate {

    PLAYING, MENU, LEVEL_EDITOR, LEVEL_SELECTOR, USER;

    /**
     * Lo stato attuale del gioco. Di default, è impostato su {@code USER}.
     */
    public static Gamestate state = USER;
}

