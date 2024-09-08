package model.ui.buttons;

import model.UserModel;
import model.gamestate.UserStateModel;

/**
 * Bottone usato per la creazione di un utente
 */
public class CreateButtonModel extends CustomButtonModel{

    public CreateButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void saveUser(UserModel user) {
        user.serialize("res/users/" + user.getNickname() + ".bb");
        UserStateModel.getInstance().getAllUsers();
    }
}
