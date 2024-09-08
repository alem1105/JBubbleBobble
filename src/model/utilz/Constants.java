package model.utilz;

/**
 * Constanti utilizzate per gestire vari elementi all'interno del gioco,
 * tra le quali alcuni costanti della dimensione della schermata di gioco.
 */
public class Constants {

    public static class GameConstants {

        public static final int ANI_SPEED = 25;
        public static final float SCALE = 2f;
        public final static int TILES_DEFAULT_SIZE = 18;
        public final static int TILES_IN_WIDTH = 20, TILES_IN_HEIGHT = 18;
        public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
        public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
        public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    }

    public static class PlayerConstants {

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALL = 3;
        public static final int ATTACK = 4;
        public static final int DEATH = 5;

    }

    public static class Directions {

        public static final int LEFT = 2;
        public static final int UP = 1;
        public static final int RIGHT = -2;

        public static final int UP_RIGHT = 4;
        public static final int UP_LEFT = -4;
        public static final int DOWN_RIGHT = 6;
        public static final int DOWN_LEFT = -6;

    }
    
    public static class Enemies {

        public static final int RUNNING = 0;
        public static final int RUNNING_ANGRY = 1;
        public static final int CAPTURED = 2;
        public static final int CAPTURED_ANGRY = 3;
        public static final int DEAD = 4;


        public static final int ZEN_CHAN_DEFAULT_WIDTH = 16;
        public static final int ZEN_CHAN_DEFAULT_HEIGHT = 16;

        public static final int ZEN_CHAN_WIDTH = (int) (ZEN_CHAN_DEFAULT_WIDTH * GameConstants.SCALE);
        public static final int ZEN_CHAN_HEIGHT = (int) (ZEN_CHAN_DEFAULT_HEIGHT * GameConstants.SCALE);

    }

    public static class CustomObjects {

        public static final int BUBBLE_SPAWNING = 0;
        public static final int BUBBLE_SPAWNED = 1;
        public static final int BUBBLE_EXPLODING = 2;

        public static final int BUBBLE_SIZE = (int) (16 * GameConstants.SCALE);

        public static final int FIREBALL_SPAWNED = 0;
        public static final int FIREBALL_EXPLODING = 1;

        public static final int WATER_WALKING = 0;
        public static final int WATER_FALLING = 1;

    }

    public static class SpecialBubbles {

        public static final int WATER_BUBBLE = 0;
        public static final int LIGHTNING_BUBBLE = 1;
        public static final int FIRE_BUBBLE = 2;
        public static final int EXTEND_BUBBLE = 3;
        public static final int BOB_BUBBLE = 4;

    }

    public static class Fruit {

        public static final int ORANGE = 0;
        public static final int PEPPER = 1;
        public static final int GRAPE = 2;
        public static final int TOMATO = 3;
        public static final int CHERRY = 4;

    }

    public static class PowerUps {

        public static final int CANDY_PINK = 0;
        public static final int CANDY_BLUE = 1;
        public static final int CANDY_YELLOW = 2;

        public static final int UMBRELLA_ORANGE = 3;
        public static final int UMBRELLA_RED = 4;
        public static final int UMBRELLA_PINK = 5;

        public static final int RING_PINK = 6;
        public static final int RING_RED = 7;

        public static final int SNEAKER = 8;
        public static final int CLOCK = 9;
        public static final int BOMB = 10;
        public static final int POTION_LIGHTNING = 11;
    }
}
