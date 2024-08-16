package model.gamestate;

import model.entities.PlayerModel;

public class Playing {

    private PlayerModel player;
    private static Playing instance;

    public static Playing getInstance() {
        if (instance == null) {
            instance = new Playing();
        }
        return instance;
    }

    private Playing() {
        player = PlayerModel.getInstance();
    }

    public void update() {
        player.update();
    }

    public PlayerModel getPlayer() {
        return player;
    }

}
