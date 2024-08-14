package model.gamestate;

import model.entities.PlayerModel;

public class Playing {

    private final PlayerModel player;

    public Playing() {
        player = PlayerModel.getInstance();
    }

    public void update() {
        player.update();
    }

    public PlayerModel getPlayer() {
        return player;
    }

}
