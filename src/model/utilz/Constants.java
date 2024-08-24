package model.utilz;

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

        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;

    }
    
    public static class Enemies {

        public static final int ZEN_CHAN = 0;
        public static final int MAITA = 1;

        public static final int RUNNING = 0;
        public static final int RUNNING_ANGRY = 1;
        public static final int CAPTURED = 2;
        public static final int CAPTURED_ANGRY = 3;
        public static final int DEAD = 4;
        public static final int ATTACK = 5;


        public static final int ZEN_CHAN_DEFAULT_WIDTH = 16;
        public static final int ZEN_CHAN_DEFAULT_HEIGHT = 16;

        public static final int ZEN_CHAN_WIDTH = (int) (ZEN_CHAN_DEFAULT_WIDTH * GameConstants.SCALE);
        public static final int ZEN_CHAN_HEIGHT = (int) (ZEN_CHAN_DEFAULT_HEIGHT * GameConstants.SCALE);

    }
}
